package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addexpense

import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addrefill.AddRefillUiState

sealed class AddExpenseUiState<out T> {

    object Default : AddExpenseUiState<Nothing>()
    class ExpenseLoaded<T>(var data: T) : AddExpenseUiState<T>()
    object ExpenseSaved : AddExpenseUiState<Nothing>()
    class ExpenseError(var error: Int) : AddExpenseUiState<Nothing>()
    object ExpenseRemoved : AddExpenseUiState<Nothing>()

}