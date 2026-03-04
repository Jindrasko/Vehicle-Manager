package cz.mendelu.xspacek6.vehiclemanager.ui.screens.charts

sealed class ChartsUiState<out T> {
    object Default: ChartsUiState<Nothing>()
    class Loaded<T>(var data: T): ChartsUiState<T>()
    class Error(var error: Int): ChartsUiState<Nothing>()
}