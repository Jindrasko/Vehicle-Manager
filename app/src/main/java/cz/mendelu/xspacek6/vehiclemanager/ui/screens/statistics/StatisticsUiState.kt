package cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics

sealed class StatisticsUiState<out T> {
    object Default : StatisticsUiState<Nothing>()
    class Loaded<T>(var data: T): StatisticsUiState<T>()
    class Error(var error: Int): StatisticsUiState<Nothing>()
}