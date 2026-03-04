package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.addtrip

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import kotlinx.coroutines.launch
import java.util.Date

class AddTripViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    var vehicleId: Long? = null
    var tripId: Long? = null

    var trip: Trip = Trip(
        title = "",
        date = Date().time,
        vehicleId = -1L
    )

    val addTripUiState: MutableState<AddTripUiState<Trip>> = mutableStateOf(AddTripUiState.Default)

    fun loadTrip() {
        if (tripId != null) {
            launch {
                trip = databaseRepository.findTripById(tripId!!)
            }.invokeOnCompletion {
                addTripUiState.value = AddTripUiState.TripLoaded(trip)
            }
        } else {
            trip.vehicleId = vehicleId!!
            addTripUiState.value = AddTripUiState.TripLoaded(trip)
        }
    }

    fun saveTrip() {
        if (trip.title.isEmpty()){
            addTripUiState.value = AddTripUiState.TripError(R.string.required_field)
        } else {
            launch {
                if (tripId != null) {
                    databaseRepository.updateTrip(trip)
                } else {
                    databaseRepository.insertTrip(trip)
                }
            }
            addTripUiState.value = AddTripUiState.TripSaved
        }

    }

    fun deleteTrip() {
        launch {
            databaseRepository.deleteTrip(trip)
            addTripUiState.value = AddTripUiState.TripRemoved
        }
    }

}