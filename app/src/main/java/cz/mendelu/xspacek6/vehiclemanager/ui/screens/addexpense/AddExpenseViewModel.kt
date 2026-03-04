package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addexpense

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import kotlinx.coroutines.launch
import java.util.*

class AddExpenseViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    var vehicleId: Long? = null
    var expenseId: Long? = null

    val addExpenseUiState: MutableState<AddExpenseUiState<Expense>> = mutableStateOf(AddExpenseUiState.Default)

    var expense: Expense = Expense(
        title = "",
        vehicleId = -1L,
        date = Date().time
    )

    var lastMileage: Int? = null
    var mileage: Mileage? = null

    fun loadExpense() {
        if(expenseId != null){
            launch {
                expense = databaseRepository.findExpenseById(expenseId!!)
                mileage = databaseRepository.findMileageByExpenseId(expenseId!!)
            }.invokeOnCompletion {
                addExpenseUiState.value = AddExpenseUiState.ExpenseLoaded(expense)
            }
        } else {
            launch {
                lastMileage = databaseRepository.getLastMileage(vehicleId!!)?.mileage
            }.invokeOnCompletion {
                expense.vehicleId = vehicleId!!
                addExpenseUiState.value = AddExpenseUiState.ExpenseLoaded(expense)
            }
        }
    }

    fun saveExpense() {
        if (expense.title.isEmpty()){
            addExpenseUiState.value = AddExpenseUiState.ExpenseError(R.string.required_field)
        } else {
            launch {
                if (expenseId != null) {
                    databaseRepository.updateExpense(expense)
                } else {
                    expenseId = databaseRepository.insertExpense(expense)
                }

                if (expense.mileage != null) {
                    if (mileage != null) {
                        mileage!!.mileage = expense.mileage!!
                        mileage!!.date = expense.date
                        databaseRepository.insertMileage(mileage!!)
                    } else {
                        databaseRepository.insertMileage(
                            Mileage(
                                mileage = expense.mileage!!,
                                date = expense.date,
                                vehicleId = vehicleId!!,
                                expenseId = expenseId
                            )
                        )
                    }
                } else if (mileage != null) {
                    databaseRepository.deleteMileage(mileage!!)
                }

                addExpenseUiState.value = AddExpenseUiState.ExpenseSaved
            }
        }
    }

    fun deleteExpense() {
        launch {
            if (mileage != null) {
                databaseRepository.deleteMileage(mileage!!)
            }
            databaseRepository.deleteExpense(expense)
            addExpenseUiState.value = AddExpenseUiState.ExpenseRemoved
        }
    }

}