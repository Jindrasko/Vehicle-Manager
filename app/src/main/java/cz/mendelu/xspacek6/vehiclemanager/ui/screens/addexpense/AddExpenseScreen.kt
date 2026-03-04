package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addexpense

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.*
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import org.koin.androidx.compose.getViewModel

@Composable
fun AddExpenseScreen(
    navigation: INavigationRouter,
    viewModel: AddExpenseViewModel = getViewModel(),
    vehicleId: Long,
    expenseId: Long?
) {

    viewModel.vehicleId = vehicleId
    viewModel.expenseId = expenseId

    val showDeleteAlertDialog = remember {  mutableStateOf(false)  }
    var expenseTitleErrorMessage: String by rememberSaveable { mutableStateOf("") }

    val screenState: MutableState<ScreenState<Expense>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    viewModel.addExpenseUiState.value.let {
        when(it){
            is AddExpenseUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadExpense()
                }
            }
            is AddExpenseUiState.ExpenseLoaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is AddExpenseUiState.ExpenseSaved -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                }
            }
            is AddExpenseUiState.ExpenseRemoved -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                }
            }
            is AddExpenseUiState.ExpenseError -> {
                expenseTitleErrorMessage = stringResource(id = it.error)
            }
        }
    }


    BackArrowScreen(
        topBarText = if (expenseId != null) stringResource(id = R.string.edit_expense)
        else stringResource(id = R.string.new_expense),
        content = {
            if(showDeleteAlertDialog.value) {
                CustomAlertDialog(
                    show = showDeleteAlertDialog,
                    text = stringResource(id = R.string.refill_delete_alert_text)
                ) {
                    viewModel.deleteExpense()
                }
            }

            AddExpenseScreenContent(
                screenState = screenState.value,
                expenseTitleErrorMessage = expenseTitleErrorMessage,
                viewModel = viewModel)

        },
        actions = {
            if(viewModel.vehicleId != null){
                IconButton(onClick = {  showDeleteAlertDialog.value = true  }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
            IconButton(onClick = { viewModel.saveExpense() }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Save")
            }
        },
        onBackClick = { navigation.returnBack() }
    )


}


@Composable
fun AddExpenseScreenContent(
    screenState: ScreenState<Expense>,
    expenseTitleErrorMessage: String,
    viewModel: AddExpenseViewModel
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> AddExpense(
                expense = it.data,
                expenseTitleErrorMessage = expenseTitleErrorMessage,
                viewModel = viewModel)
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}



@Composable
fun AddExpense(
    expense: Expense,
    expenseTitleErrorMessage: String,
    viewModel: AddExpenseViewModel
) {
    ExpenseDateCard(expense = expense, lastMileage = viewModel.lastMileage)
    ExpenseBaseCard(expense = expense, expenseTitleErrorMessage = expenseTitleErrorMessage)
    ExpenseNoteCard(expense = expense)

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace))
}