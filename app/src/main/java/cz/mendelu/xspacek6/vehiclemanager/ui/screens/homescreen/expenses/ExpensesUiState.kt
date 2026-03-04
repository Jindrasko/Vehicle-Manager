package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.expenses

sealed class ExpensesUiState<out T> {

    class Default(): ExpensesUiState<Nothing>()
    class Loaded<T>(var data: T): ExpensesUiState<T>()
    class Error(var error: Int): ExpensesUiState<Nothing>()

}