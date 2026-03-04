package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.drive

sealed class DriveUiState<out T> {

    object Default: DriveUiState<Nothing>()
    class Loaded<T>(var data: T): DriveUiState<T>()
    class Error(var error: Int): DriveUiState<Nothing>()

}