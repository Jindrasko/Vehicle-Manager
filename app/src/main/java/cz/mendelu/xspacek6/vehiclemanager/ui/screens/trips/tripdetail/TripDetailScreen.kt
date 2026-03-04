package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.tripdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomRecordDescriptionText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import cz.mendelu.xspacek6.vehiclemanager.utils.TrackingUtility
import org.koin.androidx.compose.getViewModel

@Composable
fun TripDetailScreen(
    vehicleId: Long,
    tripId: Long,
    navigation: INavigationRouter,
    viewModel: TripDetailViewModel = getViewModel()
) {

    viewModel.vehicleId = vehicleId
    viewModel.tripId = tripId

    val screenState: MutableState<ScreenState<Trip>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    viewModel.tripDetailUiState.value.let {
        when(it){
            is TripDetailUiState.Default -> {
                screenState.value = ScreenState.Loading()
                LaunchedEffect(it) {
                    viewModel.loadData()
                }
            }
            is TripDetailUiState.Loaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is TripDetailUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
        }
    }

    BackArrowScreen(
        topBarText = stringResource(id = R.string.trip_detail),
        onBackClick = { navigation.returnBack() },
        drawFullScreenContent = false,
        actions = {
            IconButton(onClick = { navigation.navigateToAddTrip(vehicleId, tripId) }) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_24),
                    contentDescription = "edit")
            }
        }
    ) {
        TripDetailScreenContent(screenState = screenState.value)
    }



}


@Composable
fun TripDetailScreenContent(
    screenState: ScreenState<Trip>
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> TripDetail(trip = it.data)
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}


@Composable
fun TripDetail(
    trip: Trip
) {

    
        if (!trip.pathPoints.isNullOrEmpty()) {

        val boundsBuilder = LatLngBounds.builder()
        for (polyline in trip.pathPoints!!) {
            for (latLng in polyline) {
                boundsBuilder.include(latLng)
            }
        }
        val bounds = boundsBuilder.build()

                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(trip.pathPoints!!.last().last(), 50f)
                }

                Box(modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.spacing.small)) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {

                        val cameraUpdateFactory = CameraUpdateFactory.newLatLngBounds(bounds,150)
                        cameraPositionState.move(cameraUpdateFactory)

                        trip.pathPoints!!.forEach {
                            Polyline(
                                points = it,
                                color = Color.Red
                            )
                        }

                        Marker(
                            state = MarkerState(position = trip.pathPoints!!.first().first()),
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                            title = stringResource(id = R.string.start_location)
                        )

                        Marker(
                            state = MarkerState(position = trip.pathPoints!!.last().last()),
                            title = stringResource(id = R.string.finish_location)
                        )

                    }


                }




        }

    

        Text(
            text = trip.title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(text = DateUtils.getDateString(trip.date))

        if (!trip.startLocation.isNullOrEmpty()) {
            CustomRecordDescriptionText(
                icon = R.drawable.play_arrow_24,
                title = stringResource(id = R.string.start) + ": ",
                value = trip.startLocation!!
            )
        }

        if (!trip.finishLocation.isNullOrEmpty()) {
            CustomRecordDescriptionText(
                icon = R.drawable.finish_24,
                title = stringResource(id = R.string.finish) + ": ",
                value = trip.finishLocation!!
            )
        }

        HorizontalDivider()

        if (trip.distance != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.straighten_24,
                title = stringResource(id = R.string.distance) + ": ",
                value = if (trip.distance!! >= 10000)
                    "${String.format("%.2f", trip.distance!!.toFloat() / 1000)} km"
                else
                    "${trip.distance!!} m"
            )
        }


        if (trip.timeDriven != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.outline_timer_24,
                title = stringResource(id = R.string.time) + ": ",
                value = TrackingUtility.getFormattedStopWatchTime(trip.timeDriven!!)
            )
        }

        if (trip.averageSpeed != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.speed_24,
                title = stringResource(id = R.string.average_speed),
                value = trip.averageSpeed.toString() + "km/h"
            )
        }

        if (!trip.note.isNullOrEmpty()) {
            CustomRecordDescriptionText(
                icon = R.drawable.sticky_note_2_24,
                title = stringResource(id = R.string.note),
                value = trip.note
            )
        }


    
}


