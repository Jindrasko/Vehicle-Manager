package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.events

import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.expenses.ExpensesUiState

sealed class EventsUiState<out T> {

    object Default: EventsUiState<Nothing>()
    class Loaded<T>(var data: T): EventsUiState<T>()
    class Error(var error: Int): EventsUiState<Nothing>()

}