package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.drive

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.services.TrackingService
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomAlertDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import cz.mendelu.xspacek6.vehiclemanager.utils.TrackingUtility
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DriveScreen(
    navigation: INavigationRouter,
    viewModel: DriveViewModel = getViewModel(),
    vehicleId: Long
) {

    viewModel.vehicleId = vehicleId

    val showCancelAlertDialog = remember { mutableStateOf(false) }
    val driveDuration = remember { mutableLongStateOf(0L) }


    val snackbarHostState = remember { SnackbarHostState() }



    BackArrowScreen(
        topBarText = stringResource(id = R.string.drive),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = {

            if (showCancelAlertDialog.value) {
                CustomAlertDialog(
                    show = showCancelAlertDialog,
                    title = stringResource(id = R.string.cancel),
                    text = stringResource(id = R.string.drive_cancel_alert_text)
                ) {
                    viewModel.stopDrive()
                    navigation.returnBack()
                }
            }

            DriveScreenContent(
                viewModel = viewModel,
                navigation = navigation,
                vehicleId = vehicleId,
                driveDuration = driveDuration,
                snackbarHostState = snackbarHostState
            )

        },
        drawFullScreenContent = true,
        actions = {
            if (driveDuration.longValue > 0L) {
                IconButton(onClick = { showCancelAlertDialog.value = true }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Cancel")
                }
            }
        },
        onBackClick = { navigation.returnBack() }
    )

}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DriveScreenContent(
    viewModel: DriveViewModel,
    navigation: INavigationRouter,
    vehicleId: Long,
    driveDuration: MutableState<Long>,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    var isLocationEnabled by remember { mutableStateOf(false) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    )

    val backgroundLocationPermissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    val lifecycleOwner = LocalLifecycleOwner.current
    var isTracking by remember { mutableStateOf(false) }
    val pathPoints by rememberUpdatedState(newValue = viewModel.pathPoints)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            if(pathPoints.value.last().isNotEmpty()) {
                pathPoints.value.last()[pathPoints.value.last().size - 1]
            } else {
                LatLng(49.195061,16.606836)
            },
            1f
        )
    }

    TrackingService.pathPoints.observe(lifecycleOwner) {
        viewModel.updatePolylines(it)
        if (!pathPoints.value.isNullOrEmpty()) {
            if (!pathPoints.value?.last().isNullOrEmpty()) {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    it.last()[pathPoints.value.last().size - 1],
                    16f
                )
            }
        }
    }

    TrackingService.timeDriveInMillis.observe(lifecycleOwner) {
        driveDuration.value = it
    }

    TrackingService.isTracking.observe(lifecycleOwner) {
        isTracking = it
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Google Map View
        Box(
            Modifier
                .fillMaxSize()
                .weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = locationPermissionsState.allPermissionsGranted),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    mapToolbarEnabled = true
                )
            ) {

                pathPoints.value.forEach {
                    Polyline(
                        points = it,
                        color = Color.Red
                    )
                }

            }

        }

        Column(
            Modifier.wrapContentHeight()
        ) {

            // Timer
            Text(
                text = TrackingUtility.getFormattedStopWatchTime(driveDuration.value, true),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(MaterialTheme.spacing.medium),
                style = MaterialTheme.typography.titleLarge
            )

            val enableLocationLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult()
                ) { activityResult ->
                    if (activityResult.resultCode == RESULT_OK) {
                        viewModel.sendCommandToService(Constants.ACTION_START_SERVICE, vehicleId)
                    } else {
                    }
                }

            Row(modifier = Modifier.fillMaxWidth()) {
                // Start Tracking Button
                Button(
                    onClick = {
                        if (!isTracking) {
                            if (locationPermissionsState.allPermissionsGranted
//                                && if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) backgroundLocationPermissionState.status.isGranted else true
                                ) {

                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    if(!backgroundLocationPermissionState.status.isGranted) {
                                        scope.launch {
                                            val snackbarResult = snackbarHostState.showSnackbar(
                                                message = appContext.getString(R.string.snackbar_permissions_not_granted),
                                                actionLabel = appContext.getString(R.string.open_settings),
                                                duration = SnackbarDuration.Long
                                            )
                                            when (snackbarResult) {
                                                SnackbarResult.ActionPerformed -> {
                                                    backgroundLocationPermissionState.launchPermissionRequest()
                                                }
                                                SnackbarResult.Dismissed -> {}
                                            }
                                        }
                                    }
                                }

                                //Start tracking
                                val locationManager = appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

                                // If location is not enabled, prompt the user to enable it
                                if (!isLocationEnabled) {
                                    enableLocationLauncher.launch(createLocationSettingsIntentSenderRequest(appContext))
                                } else {
                                    viewModel.sendCommandToService(Constants.ACTION_START_SERVICE, vehicleId)
                                    scope.launch {
                                        val snackbarResult = snackbarHostState.showSnackbar(
                                            message = appContext.getString(R.string.snackbar_exclude_battery_optimization),
                                            actionLabel = appContext.getString(R.string.open_settings),
                                            duration = SnackbarDuration.Long,
                                        )
                                        when(snackbarResult) {
                                            SnackbarResult.ActionPerformed -> {
                                                val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                                appContext.startActivity(intent)
                                            }
                                            SnackbarResult.Dismissed -> {}
                                        }
                                    }
                                }
                            } else {
                                //Ask for permissions
                                locationPermissionsState.launchMultiplePermissionRequest()
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    scope.launch {
                                        val snackbarResult = snackbarHostState.showSnackbar(
                                            message = appContext.getString(R.string.snackbar_permissions_not_granted),
                                            actionLabel = appContext.getString(R.string.open_settings),
                                            duration = SnackbarDuration.Long
                                        )
                                        when (snackbarResult) {
                                            SnackbarResult.ActionPerformed -> {
                                                backgroundLocationPermissionState.launchPermissionRequest()
                                            }

                                            SnackbarResult.Dismissed -> {}
                                        }
                                    }
                                }

                            }
                        } else {
                            // Stop tracking
                            viewModel.sendCommandToService(Constants.ACTION_PAUSE_SERVICE, vehicleId)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = MaterialTheme.spacing.medium)
                ) {
                    Text(text = if (isTracking)
                        stringResource(id = R.string.stop_tracking)
                    else
                        stringResource(id = R.string.start_tracking)
                    )
                }


                // Finish Tracking Button
                Button(
                    onClick = {
                        viewModel.endAndSaveDrive(driveDuration.value)
                        navigation.returnBack()
                    },
                    enabled = !isTracking && driveDuration.value > 0L,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = MaterialTheme.spacing.medium)
                ) {
                    Text(text = stringResource(id = R.string.finish_trip))
                }
            }
            Spacer(modifier = Modifier.padding(vertical = MaterialTheme.spacing.large))
        }
    }

}




private fun createLocationSettingsIntentSenderRequest(context: Context): IntentSenderRequest {
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
        PendingIntent.FLAG_IMMUTABLE
    )

    return IntentSenderRequest.Builder(pendingIntent.intentSender)
        .build()
}
