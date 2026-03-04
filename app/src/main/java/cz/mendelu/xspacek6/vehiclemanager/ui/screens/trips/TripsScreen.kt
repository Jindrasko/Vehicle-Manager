package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomRecordCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomRecordDescriptionText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.EmptyRecordsText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.MileageDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.HomeScreenBottomSheet
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.TripColor
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import cz.mendelu.xspacek6.vehiclemanager.utils.TrackingUtility
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    vehicleId: Long,
    navigation: INavigationRouter,
    viewModel: TripsViewModel = getViewModel()
) {

    viewModel.vehicleId = vehicleId

    val screenState: MutableState<ScreenState<List<Trip>>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    val showOdoDialog = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val lastMileage = rememberSaveable { mutableStateOf("") }

    val tripsUiState: TripsUiState<List<Trip>>? by viewModel.tripsUiState.collectAsState()

    tripsUiState?.let {
        when(it){
            is TripsUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadTrips()
                }
            }
            is TripsUiState.Loaded -> {
                lastMileage.value = viewModel.lastMileage.toString()
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is TripsUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
        }
    }


    BackArrowScreen(
        topBarText = stringResource(id = R.string.trips),
        content = {

            if(showOdoDialog.value){
                MileageDialog(
                    show = showOdoDialog,
                    lastMileage = lastMileage.value,
                    vehicleId = vehicleId
                ) {
                    viewModel.saveMileage(it)
                    lastMileage.value = it.mileage.toString()
                }
            }

            HomeScreenBottomSheet(
                sheetState = sheetState,
                coroutineScope = coroutineScope,
                showOdoDialog = showOdoDialog,
                vehicleId = vehicleId,
                navigation = navigation
            )

            TripsScreenContent(
                navigation = navigation,
                vehicleId = vehicleId,
                screenState = screenState.value
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        sheetState.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "plus"
                )
            }
        },
        actions = {
        },
        onBackClick = { navigation.returnBack() }
    )


}


@Composable
fun TripsScreenContent(
    navigation: INavigationRouter,
    vehicleId: Long,
    screenState: ScreenState<List<Trip>>
) {

    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> ListOfTrips(
                navigation = navigation,
                vehicleId = vehicleId,
                trips = it.data
            )
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }

}



@Composable
fun ListOfTrips(
    navigation: INavigationRouter,
    vehicleId: Long,
    trips: List<Trip>
) {

    if(trips.isEmpty()){
        EmptyRecordsText(
            text = stringResource(id = R.string.empty_trip_list),
            actionText = "",
            onClick = {})
    } else {
        trips.forEach {
            TripRow(
                trip = it
            ) {
                navigation.navigateToTripDetail(vehicleId, it.tripId!!)
            }

        }
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace))

}



@Composable
fun TripRow(
    trip: Trip,
    onRowClick: () -> Unit
) {

    CustomRecordCard(
        title = trip.title,
        date = DateUtils.getDateString(unixTime = trip.date),
        odometer = null,
        icon = R.drawable.route_24,
        color = TripColor,
        cost = null,
        currency = "",
        onClick = onRowClick
    ) {

        if (trip.startLocation != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.play_arrow_24,
                title = stringResource(id = R.string.start),
                value = trip.startLocation
            )
        }

        if (trip.finishLocation != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.finish_24,
                title = stringResource(id = R.string.finish),
                value = trip.finishLocation
            )
        }

        if (trip.distance != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.straighten_24,
                title = stringResource(id = R.string.distance),
                value = if (trip.distance!! >= 10000)
                    "${String.format("%.2f", trip.distance!!.toFloat() / 1000)  } km"
                else
                    "${trip.distance!!} m"
            )
        }

        if (trip.timeDriven != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.outline_timer_24,
                title = stringResource(id = R.string.duration),
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


}
