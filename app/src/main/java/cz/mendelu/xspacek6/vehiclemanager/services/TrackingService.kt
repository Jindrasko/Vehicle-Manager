package cz.mendelu.xspacek6.vehiclemanager.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.ACTION_PAUSE_SERVICE
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.ACTION_SHOW_DRIVE_SCREEN
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.ACTION_START_SERVICE
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.ACTION_STOP_SERVICE
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.FASTEST_LOCATION_UPDATE_INTERVAL
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.LOCATION_UPDATE_INTERVAL
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.NOTIFICATION_CHANNEL_ID
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.NOTIFICATION_CHANNEL_NAME
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.NOTIFICATION_ID
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.TIMER_UPDATE_INTERVAL
import cz.mendelu.xspacek6.vehiclemanager.ui.activities.MainActivity
import cz.mendelu.xspacek6.vehiclemanager.utils.TrackingUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TrackingService() : LifecycleService() {

    var vehicleId: Long? = null
    private var serviceKilled: Boolean = false

    private var isFirstTime = true

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var baseNotificationBuilder : NotificationCompat.Builder

    private lateinit var currentNotificationBuilder: NotificationCompat.Builder

    private val timeDriveInSeconds = MutableLiveData<Long>()

    companion object {
        val timeDriveInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<MutableList<MutableList<LatLng>>>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeDriveInSeconds.postValue(0L)
        timeDriveInMillis.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotificationTrackingState(it)
        })
    }

    private fun killService() {
        serviceKilled = true
        isFirstTime = true
        pauseService()
        postInitialValues()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action) {
                ACTION_START_SERVICE -> {
                    if (isFirstTime) {
                        vehicleId = intent.extras!!.getLong("vehicleId")
                        startForegroundService()
                        isFirstTime = false
                    } else {
                        startTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    killService()
                }
                else -> {
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnabled = false
    private var sectionTime = 0L
    private var driveTime = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                sectionTime = System.currentTimeMillis() - timeStarted
                timeDriveInMillis.postValue(driveTime + sectionTime)
                if (timeDriveInMillis.value!! >= lastSecondTimestamp + 1000L) {
                    timeDriveInSeconds.postValue(timeDriveInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000
                }
                delay(TIMER_UPDATE_INTERVAL)
            }

            driveTime += sectionTime
        }
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun updateNotificationTrackingState(isTracking: Boolean) {
        val notificationActionText = if (isTracking) appContext.getString(R.string.pause) else appContext.getString(R.string.resume)
        val pendingIntent = if (isTracking) {
            val pauseIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this, 1, pauseIntent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                } else {
                    FLAG_UPDATE_CURRENT
                })
        } else {
            val resumeIntent = Intent(this, TrackingService::class.java).apply {
                action = ACTION_START_SERVICE
            }
            PendingIntent.getService(
                this,
                2,
                resumeIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                } else {
                    FLAG_UPDATE_CURRENT
                })
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        if (!serviceKilled) {
            currentNotificationBuilder = baseNotificationBuilder
                .addAction(R.drawable.pause_black_24, notificationActionText, pendingIntent)

            notificationManager.notify(NOTIFICATION_ID, currentNotificationBuilder.build())
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            if (TrackingUtility.hasLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_UPDATE_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
//                val request = LocationRequest.Builder(LOCATION_UPDATE_INTERVAL)

                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
            }

        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val position = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(position)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))


    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        baseNotificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.directions_car_black_24)
            .setContentTitle(appContext.getString(R.string.drive))
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        currentNotificationBuilder = baseNotificationBuilder

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())

        timeDriveInSeconds.observe(this, Observer {
            if (!serviceKilled) {
                val notification = currentNotificationBuilder
                    .setContentText(TrackingUtility.getFormattedStopWatchTime(it * 1000, false))
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }
        })
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_DRIVE_SCREEN
            it.putExtra("vehicleId", vehicleId)
        },
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
        } else {
            FLAG_UPDATE_CURRENT
        }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


}