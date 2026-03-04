package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.refills

sealed class RefillsUiState<out T> {

    class Default : RefillsUiState<Nothing>()
    class Loaded<T>(var data: T): RefillsUiState<T>()
    class Error(var error: Int): RefillsUiState<Nothing>()

}