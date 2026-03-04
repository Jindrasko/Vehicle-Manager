package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.tripdetail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import kotlinx.coroutines.launch

class TripDetailViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    var vehicleId: Long? = null
    var tripId: Long? = null

    lateinit var trip: Trip

    val tripDetailUiState: MutableState<TripDetailUiState<Trip>> = mutableStateOf(TripDetailUiState.Default)

    fun loadData() {
        launch {
            trip = databaseRepository.findTripById(tripId!!)
        }.invokeOnCompletion {
            tripDetailUiState.value = TripDetailUiState.Loaded(trip)
        }
    }


}