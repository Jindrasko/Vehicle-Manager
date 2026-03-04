package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.events


import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventsViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    private val _eventsUiState = MutableStateFlow<EventsUiState<List<Event>>>(EventsUiState.Default)
    val eventsUiState: StateFlow<EventsUiState<List<Event>>> = _eventsUiState

    var vehicleId: Long? = null
    var lastMileage: Int? = null
    var doneFilter = false

    val selectedCategories = mutableStateOf(listOf<Int?>())

    fun loadEvents() {
        launch {
            lastMileage = databaseRepository.getLastMileage(vehicleId!!)?.mileage
            databaseRepository.getEvents(vehicleId!!).collect() { list ->
                _eventsUiState.value = EventsUiState.Loaded(list.filter {
                    if (doneFilter) it.done else !it.done
                })
            }
        }
    }

    fun reload() {
        _eventsUiState.value = EventsUiState.Default
    }


    fun getRepeatText(everyKm: Int?, everyMonths: Int?): String {
        var text = ""
        if (everyKm != null) text += ("$everyKm " + appContext.getString(R.string.km))
        if (everyKm!=null && everyMonths!=null) text += ", "
        if (everyMonths!=null) text += ("$everyMonths " + appContext.getString(R.string.months))
        return text
    }

    fun updateSelectedCategories(category: Int?) {
        if (selectedCategories.value.contains(category)) {
            selectedCategories.value = selectedCategories.value.filter { it != category }
        } else {
            selectedCategories.value = selectedCategories.value + category
        }
    }


}