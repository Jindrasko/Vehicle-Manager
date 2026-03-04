package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.tripdetail

sealed class TripDetailUiState<out T> {
    object Default: TripDetailUiState<Nothing>()
    class Loaded<T>(var data: T): TripDetailUiState<T>()
    class Error(var error: Int): TripDetailUiState<Nothing>()
}