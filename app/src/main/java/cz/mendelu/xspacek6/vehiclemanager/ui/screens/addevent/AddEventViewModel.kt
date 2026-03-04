package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addevent

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.Date

class AddEventViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    var vehicleId: Long? = null
    var eventId: Long? = null

    val addEventUiState: MutableState<AddEventUiState<Event>> = mutableStateOf(AddEventUiState.Default)

    var doneChecked = false

    var event: Event = Event(
        title = "",
        vehicleId = -1L,
        initialDate = Date().time,
        initialKm = 0,
    )

    var lastMileage: Int? = null

    fun loadEvent() {
        if(eventId != null) {
            launch {
                lastMileage = databaseRepository.getLastMileage(vehicleId!!)?.mileage
                event = databaseRepository.findEventById(eventId = eventId!!)
            }.invokeOnCompletion {
                addEventUiState.value = AddEventUiState.EventLoaded(event)
            }
        } else {
            launch {
                lastMileage = databaseRepository.getLastMileage(vehicleId!!)?.mileage
            }.invokeOnCompletion {
                event.vehicleId = vehicleId!!
                event.initialKm = lastMileage ?: 0
                addEventUiState.value = AddEventUiState.EventLoaded(event)
            }
        }
    }

    fun saveEvent() {
        if (event.everyMonths != null) {
            if(event.onceDate == null) {
                event.onceDate = DateUtils.addMonthsToDate(Date().time, event.everyMonths!!)
            } else {
                event.initialDate = DateUtils.addMonthsToDate(Date().time, -event.everyMonths!!)
            }
        }
        if (event.everyKm != null) {
            if(event.onceAtKm == null) {
                event.onceAtKm = event.everyKm!! + (lastMileage ?: 0)
            } else {
                event.initialKm = (lastMileage ?: 0) - event.everyKm!!
            }
        }
        launch {
            if (eventId != null) {
                databaseRepository.updateEvent(event)
            } else {
                databaseRepository.insertEvent(event)
            }
            if(doneChecked && (event.everyMonths != null || event.everyKm!= null)) {
                databaseRepository.insertEvent(createNewEvent(event))
            }
        }
        addEventUiState.value = AddEventUiState.EventSaved
    }

    fun createNewEvent(event: Event): Event {
        val newEvent = Event(
            title = event.title,
            vehicleId = event.vehicleId,
            initialDate = event.finalDate ?: Date().time,
            initialKm = event.finalKm ?: lastMileage ?: 0
            )
        newEvent.note = event.note
        newEvent.eventCategory = event.eventCategory
        if (event.everyMonths != null) {
            newEvent.onceDate = DateUtils.addMonthsToDate(event.finalDate!!, event.everyMonths!!)
            newEvent.everyMonths = event.everyMonths
        }
        if(event.everyKm != null){
            newEvent.onceAtKm = event.finalKm!! + event.everyKm!!
            newEvent.everyKm = event.everyKm
        }
        return newEvent
    }


    fun deleteEvent() {
        launch {
            databaseRepository.deleteEvent(event)
            addEventUiState.value = AddEventUiState.EventRemoved
        }
    }


}