package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.drive

import android.content.Intent
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.ACTION_STOP_SERVICE
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import cz.mendelu.xspacek6.vehiclemanager.services.TrackingService
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Date
import kotlin.math.round

data class DriveData(
    var pathPoints: List<List<LatLng>>
) : Serializable

class DriveViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    var vehicleId: Long? = null

    val driveUiState: MutableState<DriveUiState<DriveData>>
    = mutableStateOf(DriveUiState.Default)

    var pathPoints = mutableStateOf(listOf<List<LatLng>>(listOf()))

    fun updatePolylines(newPathPoints: MutableList<MutableList<LatLng>>) {
        pathPoints.value = newPathPoints.map {it.toList()}
    }

    fun stopDrive() {
        sendCommandToService(ACTION_STOP_SERVICE, vehicleId!!)
    }

    fun endAndSaveDrive(driveDuration: Long) {

        val finalPathPoints = removeEmptyLists(pathPoints.value)

        var finalDistance = 0

        for (polyline in finalPathPoints) {
            finalDistance += calculateDistance(polyline).toInt()
        }

        val averageSpeed = round((finalDistance / 1000) / (driveDuration / 1000f / 60 / 60) * 10) / 10f
        val endTimestamp = Date().time



        val finalTrip = Trip(
            title = appContext.getString(R.string.trip) + " " + DateUtils.getDateString(endTimestamp),
            date = endTimestamp,
            vehicleId = vehicleId!!
            )
        finalTrip.pathPoints = finalPathPoints
        finalTrip.averageSpeed = averageSpeed
        finalTrip.distance = finalDistance
        finalTrip.timeDriven = driveDuration

        saveTrip(finalTrip)
        stopDrive()
    }

    private fun saveTrip(trip: Trip) {
        launch {
            databaseRepository.insertTrip(trip)
        }
    }

    fun sendCommandToService(action: String, vehicleId: Long) =
        Intent(appContext, TrackingService::class.java).also {
            it.action = action
            it.putExtra("vehicleId", vehicleId)
            appContext.startService(it)
        }

    fun removeEmptyLists(originalList: List<List<LatLng>>): List<List<LatLng>> {
        return originalList.filter { it.isNotEmpty() }
    }

    fun calculateDistance(polyline: List<LatLng>): Float {
        var distance = 0f
        for (i in 0..polyline.size-2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i+1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result)
            distance += result[0]
        }
        return distance
    }


}