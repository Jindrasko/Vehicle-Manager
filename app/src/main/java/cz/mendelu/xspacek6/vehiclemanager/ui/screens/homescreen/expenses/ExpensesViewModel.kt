package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.expenses

import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpensesViewModel(private val databaseRepository: ILocalVehiclesRepository, private val dataStoreRepository: IDataStoreRepository) : BaseViewModel() {

    var vehicleId: Long? = null
    var currency = ""

    val selectedCategories = mutableStateOf(listOf<Int?>())

    init {
        launch {
            currency = dataStoreRepository.getCurrency()
        }
    }

    private val _expensesUiState = MutableStateFlow<ExpensesUiState<List<Expense>>>(ExpensesUiState.Default())
    val expensesUiState: StateFlow<ExpensesUiState<List<Expense>>> = _expensesUiState

    fun loadExpenses(){
        launch {
            databaseRepository.getExpenses(vehicleId!!).collect {
                _expensesUiState.value = ExpensesUiState.Loaded(it)
            }
        }
    }

    fun updateSelectedCategories(category: Int?) {
        if (selectedCategories.value.contains(category)) {
            selectedCategories.value = selectedCategories.value.filter { it != category }
        } else {
            selectedCategories.value = selectedCategories.value + category
        }
    }

}