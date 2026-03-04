package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.addtrip

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomAlertDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import org.koin.androidx.compose.getViewModel

@Composable
fun AddTripScreen(
    navigation: INavigationRouter,
    viewModel: AddTripViewModel = getViewModel(),
    vehicleId: Long,
    tripId: Long?
) {

    viewModel.vehicleId = vehicleId
    viewModel.tripId = tripId

    val showDeleteAlertDialog = remember {  mutableStateOf(false)  }
    var tripTitleErrorMessage: String by rememberSaveable { mutableStateOf("") }

    val screenState: MutableState<ScreenState<Trip>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    viewModel.addTripUiState.value.let {
        when(it){
            is AddTripUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadTrip()
                }
            }
            is AddTripUiState.TripLoaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is AddTripUiState.TripSaved -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                }
            }
            is AddTripUiState.TripRemoved -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                    navigation.returnBack()
                }
            }
            is AddTripUiState.TripError -> {
                tripTitleErrorMessage = stringResource(id = it.error)
            }
        }
    }

    BackArrowScreen(
        topBarText = if (tripId != null) stringResource(id = R.string.edit_trip)
        else stringResource(id = R.string.new_trip),
        content = {
            if(showDeleteAlertDialog.value) {
                CustomAlertDialog(
                    show = showDeleteAlertDialog,
                    text = stringResource(id = R.string.trip_delete_alert_text)
                ) {
                    viewModel.deleteTrip()
                }
            }

            AddTripScreenContent(
                screenState = screenState.value,
                tripTitleErrorMessage = tripTitleErrorMessage)

        },
        actions = {
            if(viewModel.tripId != null){
                IconButton(onClick = {  showDeleteAlertDialog.value = true  }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
            IconButton(onClick = { viewModel.saveTrip() }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Save")
            }
        },
        onBackClick = { navigation.returnBack() }
    )

}



@Composable
fun AddTripScreenContent(
    screenState: ScreenState<Trip>,
    tripTitleErrorMessage: String
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> AddTrip(
                trip = it.data,
                tripTitleErrorMessage = tripTitleErrorMessage)
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}



@Composable
fun AddTrip(
    trip: Trip,
    tripTitleErrorMessage: String
) {

    TripTitleCard(trip = trip, tripTitleErrorMessage = tripTitleErrorMessage)
    TripLocationCard(trip = trip)
    TripStatsCard(trip = trip)
    TripNoteCard(trip = trip)

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace))
}



