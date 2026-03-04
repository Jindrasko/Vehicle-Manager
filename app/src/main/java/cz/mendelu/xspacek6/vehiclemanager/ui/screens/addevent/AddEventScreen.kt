package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addevent

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
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomAlertDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import org.koin.androidx.compose.getViewModel

@Composable
fun AddEventScreen(
    navigation: INavigationRouter,
    viewModel: AddEventViewModel = getViewModel(),
    vehicleId: Long,
    eventId: Long?
) {

    viewModel.vehicleId = vehicleId
    viewModel.eventId = eventId

    val showDeleteAlertDialog = remember {  mutableStateOf(false)  }
    var eventTitleErrorMessage: String by rememberSaveable { mutableStateOf("") }

    val screenState: MutableState<ScreenState<Event>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    viewModel.addEventUiState.value.let {
        when(it){
            is AddEventUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadEvent()
                }
            }
            is AddEventUiState.EventLoaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is AddEventUiState.EventSaved -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                }
            }
            is AddEventUiState.EventRemoved -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                }
            }
            is AddEventUiState.EventError -> {
                eventTitleErrorMessage = stringResource(id = it.error)
            }
        }
    }

    BackArrowScreen(
        topBarText = if (eventId != null) stringResource(id = R.string.edit_event)
        else stringResource(id = R.string.new_event),
        content = {
            if(showDeleteAlertDialog.value) {
                CustomAlertDialog(
                    show = showDeleteAlertDialog,
                    text = stringResource(id = R.string.event_delete_alert_text)
                ) {
                    viewModel.deleteEvent()
                }
            }

            AddEventScreenContent(
                screenState = screenState.value,
                eventTitleErrorMessage = eventTitleErrorMessage,
                viewModel = viewModel)

        },
        actions = {
            if(viewModel.vehicleId != null){
                IconButton(onClick = {  showDeleteAlertDialog.value = true  }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
            IconButton(onClick = { viewModel.saveEvent() }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Save")
            }
        },
        onBackClick = { navigation.returnBack() }
    )

}


@Composable
fun AddEventScreenContent(
    screenState: ScreenState<Event>,
    eventTitleErrorMessage: String,
    viewModel: AddEventViewModel
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> AddEvent(
                event = it.data,
                eventTitleErrorMessage = eventTitleErrorMessage,
                viewModel = viewModel)
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}


@Composable
fun AddEvent(
    event: Event,
    eventTitleErrorMessage: String,
    viewModel: AddEventViewModel
) {

    EventTitleCard(
        event = event,
        eventTitleErrorMessage = eventTitleErrorMessage,
        lastMileage = viewModel.lastMileage,
        doneChecked = {viewModel.doneChecked = it}
    )
    EventRepeatCard(
        event = event,
        lastMileage = viewModel.lastMileage
    )
    EventNoteCard(event = event)

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace))
}