package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.expenses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomRecordCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomRecordDescriptionText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.EmptyRecordsText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.EmptyCategory
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import org.koin.androidx.compose.getViewModel


@Composable
fun ExpensesScreen(
    vehicleId: Long,
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    viewModel: ExpensesViewModel = getViewModel()
) {

    viewModel.vehicleId = vehicleId

    val screenState: MutableState<ScreenState<List<Expense>>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    val expensesUiState: ExpensesUiState<List<Expense>>? by viewModel.expensesUiState.collectAsState()

    expensesUiState?.let {
        when(it) {
            is ExpensesUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadExpenses()
                }
            }
            is ExpensesUiState.Loaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is ExpensesUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
        }
    }

    ExpensesScreenContent(
        navigation = navigation,
        paddingValues = paddingValues,
        vehicleId = vehicleId,
        screenState = screenState.value,
        viewModel = viewModel
    )

}


@Composable
fun ExpensesScreenContent(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    vehicleId: Long,
    screenState: ScreenState<List<Expense>>,
    viewModel: ExpensesViewModel
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> ListOfExpenses(
                navigation = navigation,
                paddingValues = paddingValues,
                vehicleId = vehicleId,
                expenses = it.data,
                currency = viewModel.currency,
                viewModel = viewModel
            )
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}



@Composable
fun ListOfExpenses(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    vehicleId: Long,
    expenses: List<Expense>,
    currency: String,
    viewModel: ExpensesViewModel
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        LazyRow {
            ExpenseCategory.entries.forEach {category ->
                item {
                    FilterChip(
                        selected = viewModel.selectedCategories.value.contains(category.ordinal),
                        onClick = { viewModel.updateSelectedCategories(category.ordinal) },
                        label = { 
                            Row(verticalAlignment = Alignment.CenterVertically){
                                Icon(painter = painterResource(id = category.icon), contentDescription = category.name)
                                Text(text = category.name, modifier = Modifier.padding(start = MaterialTheme.spacing.small))
                            }
                        },
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                    )
                }
            }
            item {
                FilterChip(
                    selected = viewModel.selectedCategories.value.contains(null),
                    onClick = { viewModel.updateSelectedCategories(null) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(id = R.drawable.question_mark_24), contentDescription = stringResource(id = R.string.other))
                            Text(text = stringResource(id = R.string.other), modifier = Modifier.padding(start = MaterialTheme.spacing.small))
                        }
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                )
            }
        }

        LazyColumn {

            if (expenses.isEmpty()) {
                item {
                    EmptyRecordsText(
                        text = stringResource(id = R.string.empty_expense_list),
                        actionText = stringResource(id = R.string.add_new_expense)
                    ) {
                        navigation.navigateToAddExpense(
                            vehicleId = vehicleId,
                            expenseId = -1L
                        )
                    }
                }
            }

            expenses.forEach {
                if (viewModel.selectedCategories.value.contains(it.expenseCategory?.ordinal) || viewModel.selectedCategories.value.isEmpty()) {
                    item(key = it.expenseId) {
                        ExpenseRow(
                            expense = it,
                            currency = currency
                        ) {
                            navigation.navigateToAddExpense(
                                vehicleId = it.vehicleId,
                                expenseId = it.expenseId
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace)) }
        }
    }
}


@Composable
fun ExpenseRow(
    expense: Expense,
    currency: String,
    onRowClick: () -> Unit
) {

    CustomRecordCard(
        title = expense.title,
        date = DateUtils.getDateString(unixTime = expense.date),
        odometer = expense.mileage,
        icon = expense.expenseCategory?.icon ?: R.drawable.question_mark_24,
        color = expense.expenseCategory?.color ?: EmptyCategory,
        cost = (expense.costParts ?: 0.0) + (expense.costServices ?: 0.0),
        currency = currency,
        onClick = onRowClick
    ) {
        if (expense.expenseCategory != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.label_24,
                title = stringResource(id = R.string.category),
                value = expense.expenseCategory!!.name)
        }

        if (expense.costParts != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.settings_24,
                title = stringResource(id = R.string.parts),
                value = expense.costParts!!.toString() + " $currency")
        }

        if (expense.costServices != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.build_24,
                title = stringResource(id = R.string.services),
                value = expense.costServices!!.toString() + " $currency")
        }

        if (expense.note != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.sticky_note_2_24,
                title = expense.note!!,
                value = null)
        }

    }


}