package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.dashboard

sealed class DashboardUiState<out T> {
    class Default : DashboardUiState<Nothing>()
    class Loaded<T>(var data: T): DashboardUiState<T>()
    class Error(var error: Int): DashboardUiState<Nothing>()
}