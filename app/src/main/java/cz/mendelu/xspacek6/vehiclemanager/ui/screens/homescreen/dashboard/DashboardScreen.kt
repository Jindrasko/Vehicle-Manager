 package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.dashboard


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.DashboardCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.EmptyRecordsText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.events.EventProgress
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import org.koin.androidx.compose.getViewModel


@Composable
fun DashboardScreen(
    vehicleId: Long,
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    homeNavController: NavHostController,
    viewModel: DashboardViewModel = getViewModel()
) {

    viewModel.vehicleId = vehicleId

    val screenState: MutableState<ScreenState<DashboardStateData>> = remember {
        mutableStateOf(ScreenState.Loading())
    }

    val dashboardUiState: DashboardUiState<DashboardStateData>? by viewModel.dashboardState.collectAsState()

    val currentRoute = homeNavController.currentDestination?.route

    LaunchedEffect(currentRoute) {
        // Trigger a recomposition whenever the current route changes
        viewModel.setDefaultState()
    }

    dashboardUiState?.let {
        when(it){
            is DashboardUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadData()
                }
            }
            is DashboardUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
            is DashboardUiState.Loaded -> {
                // THIS SCREEN STATE recomposes
                screenState.value = ScreenState.DataLoaded(it.data)
            }
        }
    }


    DashboardScreenContent(
        navigation = navigation,
        paddingValues = paddingValues,
        screenState = screenState,
        vehicleId = vehicleId,
        viewModel = viewModel
    )

}



@Composable
fun DashboardScreenContent(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    screenState: MutableState<ScreenState<DashboardStateData>>,
    vehicleId: Long,
    viewModel: DashboardViewModel
) {
    val state by screenState

    when(state){
        is ScreenState.DataLoaded -> DashboardCards(
            navigation = navigation,
            paddingValues = paddingValues,
            stateData = (state as ScreenState.DataLoaded<DashboardStateData>).data,
            vehicleId = vehicleId,
            viewModel = viewModel
        )
        is ScreenState.Error -> ErrorScreen(text = stringResource(id = (state as ScreenState.Error).error))
        is ScreenState.Loading -> LoadingScreen()
    }

}



@Composable
fun DashboardCards(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    stateData: DashboardStateData,
    vehicleId: Long,
    viewModel: DashboardViewModel
) {

    LazyColumn(modifier = Modifier.padding(paddingValues)) {
        item {
            DashboardCard(icon = painterResource(id = R.drawable.notifications_active_24), title = stringResource(id = R.string.upcoming_events)
            ) {
                for (i in 0..2) {
                    if (stateData.events.getOrNull(i) != null) {
                        if (i == 1 || i == 2) {
                            HorizontalDivider(modifier = Modifier.padding(top = MaterialTheme.spacing.small))
                        }
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigation.navigateToAddEvent(
                                    vehicleId = stateData.events[i].vehicleId,
                                    stateData.events[i].eventId
                                )
                            }) {
                            Column {
                                Text(
                                    text = stateData.events[i].title,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
                                )
                                EventProgress(
                                    event = stateData.events[i],
                                    lastMileage = viewModel.lastMileage
                                )
                            }
                        }
                    }
                    else {
                        if(i == 0) {
                            EmptyRecordsText(
                                text = stringResource(id = R.string.no_upcoming_events),
                                actionText = stringResource(id = R.string.add_new_event)
                            ) {
                                navigation.navigateToAddEvent(
                                    vehicleId = vehicleId,
                                    eventId = -1L
                                )
                            }
                        }
                    }
                }
            }
        }
        item {
            DashboardCard(icon = painterResource(id = R.drawable.payments_24), title = stringResource(id = R.string.expenses)
            ) {
                Text(text = stringResource(id = R.string.this_month), color = MaterialTheme.colorScheme.primary)
                MonthExpensesStats(expenses = stateData.thisMonth, viewModel.currency)

                Text(text = stringResource(id = R.string.last_month), color = MaterialTheme.colorScheme.primary)
                MonthExpensesStats(expenses = stateData.lastMonth, viewModel.currency)
            }
        }
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace))
        }
    }
}



@Composable
fun MonthExpensesStats(
    expenses: MonthExpenses,
    currency: String
) {

    ExpenseStatisticsRow(
        icon = painterResource(id = R.drawable.local_gas_station_24),
        title = stringResource(id = R.string.fuel),
        value = expenses.refills.toString(),
        currency = currency)
    if(expenses.maintenance != 0.0) {
        ExpenseStatisticsRow(
            icon = painterResource(id = ExpenseCategory.MAINTENANCE.icon),
            title = stringResource(id = ExpenseCategory.MAINTENANCE.title),
            value = expenses.maintenance.toString(),
            currency = currency)}
    if(expenses.repair != 0.0) {
        ExpenseStatisticsRow(
            icon = painterResource(id = ExpenseCategory.REPAIR.icon),
            title = stringResource(id = ExpenseCategory.REPAIR.title),
            value = expenses.repair.toString(),
            currency = currency)}
    if(expenses.insurance != 0.0) {
        ExpenseStatisticsRow(
            icon = painterResource(id = ExpenseCategory.INSURANCE.icon),
            title = stringResource(id = ExpenseCategory.INSURANCE.title),
            value = expenses.insurance.toString(),
            currency = currency)}
    if(expenses.tuning != 0.0) {
        ExpenseStatisticsRow(
            icon = painterResource(id = ExpenseCategory.TUNING.icon),
            title = stringResource(id = ExpenseCategory.TUNING.title),
            value = expenses.tuning.toString(),
            currency = currency)}
    if(expenses.cleaning != 0.0) {
        ExpenseStatisticsRow(
            icon = painterResource(id = ExpenseCategory.CLEANING.icon),
            title = stringResource(id = ExpenseCategory.CLEANING.title),
            value = expenses.cleaning.toString(),
            currency = currency)}
    if(expenses.toll != 0.0) {
        ExpenseStatisticsRow(
            icon = painterResource(id = ExpenseCategory.TOLL.icon),
            title = stringResource(id = ExpenseCategory.TOLL.title),
            value = expenses.toll.toString(),
            currency = currency)}
    if(expenses.other != 0.0) {
        ExpenseStatisticsRow(
            icon = painterResource(id = R.drawable.question_mark_24),
            title = stringResource(id = R.string.other),
            value = expenses.other.toString(),
            currency = currency)}
}



@Composable
fun ExpenseStatisticsRow(
    icon: Painter,
    title: String,
    value: String,
    currency: String
) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = MaterialTheme.spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = icon, contentDescription = "")
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = MaterialTheme.spacing.small),horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title)
            Text(text = "$value $currency")
        }
    }
}