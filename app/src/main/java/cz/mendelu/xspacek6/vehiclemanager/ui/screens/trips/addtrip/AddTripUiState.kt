package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.addtrip

sealed class AddTripUiState<out T> {
    object Default : AddTripUiState<Nothing>()
    class TripLoaded<T>(var data: T) : AddTripUiState<T>()
    object TripSaved : AddTripUiState<Nothing>()
    class TripError(var error: Int) : AddTripUiState<Nothing>()
    object TripRemoved : AddTripUiState<Nothing>()
}