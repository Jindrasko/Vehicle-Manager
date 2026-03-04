package cz.mendelu.xspacek6.vehiclemanager.ui.screens.vehicles

sealed class ListOfVehiclesUiState<out T> {
    class Default : ListOfVehiclesUiState<Nothing>()
    class Loaded<T>(var data: T): ListOfVehiclesUiState<T>()
    class Error(var error: Int): ListOfVehiclesUiState<Nothing>()
}