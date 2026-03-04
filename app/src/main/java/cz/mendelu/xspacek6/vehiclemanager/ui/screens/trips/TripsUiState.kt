package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips

sealed class TripsUiState<out T> {

    object Default : TripsUiState<Nothing>()
    class Loaded<T>(var data: T): TripsUiState<T>()
    class Error(var error: Int): TripsUiState<Nothing>()

}