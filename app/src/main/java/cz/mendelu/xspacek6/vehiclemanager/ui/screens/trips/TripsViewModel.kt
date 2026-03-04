package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips

import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TripsViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    private val _tripsUiState = MutableStateFlow<TripsUiState<List<Trip>>>(TripsUiState.Default)
    val tripsUiState: StateFlow<TripsUiState<List<Trip>>> = _tripsUiState


    var vehicleId: Long? = null
    var lastMileage: Int = 0

    fun loadTrips() {
        launch {
            lastMileage = databaseRepository.getLastMileage(vehicleId!!)?.mileage ?: 0
            databaseRepository.getTrips(vehicleId!!).collect {
                _tripsUiState.value = TripsUiState.Loaded(it)
            }
        }
    }


    fun saveMileage(mileage: Mileage){
        launch {
            databaseRepository.insertMileage(mileage)
        }
    }


}