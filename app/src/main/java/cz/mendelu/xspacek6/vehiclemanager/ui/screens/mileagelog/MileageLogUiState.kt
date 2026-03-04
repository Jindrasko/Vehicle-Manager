package cz.mendelu.xspacek6.vehiclemanager.ui.screens.mileagelog

sealed class MileageLogUiState<out T> {
    object Default : MileageLogUiState<Nothing>()
    class Loaded<T>(var data: T): MileageLogUiState<T>()
    class Error(var error: Int): MileageLogUiState<Nothing>()
}
