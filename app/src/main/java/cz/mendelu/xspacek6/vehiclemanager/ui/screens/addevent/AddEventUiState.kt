package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addevent

import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addexpense.AddExpenseUiState

sealed class AddEventUiState<out T> {

    object Default : AddEventUiState<Nothing>()
    class EventLoaded<T>(var data: T) : AddEventUiState<T>()
    object EventSaved : AddEventUiState<Nothing>()
    class EventError(var error: Int) : AddEventUiState<Nothing>()
    object EventRemoved : AddEventUiState<Nothing>()

}