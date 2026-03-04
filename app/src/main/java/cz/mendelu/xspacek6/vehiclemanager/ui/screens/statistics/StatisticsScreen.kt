package cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetGreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetOrange
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetRed
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import org.koin.androidx.compose.getViewModel

@Composable
fun StatisticsScreen(
    vehicleId: Long?,
    navigation: INavigationRouter,
    viewModel: StatisticsViewModel = getViewModel()
) {

    viewModel.vehicleId = vehicleId

    val screenState: MutableState<ScreenState<StatisticsStateData>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    viewModel.statisticsUiState.value.let {
        when(it){
            is StatisticsUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadData()
                }
            }
            is StatisticsUiState.Loaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is StatisticsUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
        }
    }

    BackArrowScreen(
        topBarText = stringResource(id = R.string.statistics),
        onBackClick = { navigation.returnBack() }
    ) {
        StatisticsScreenContent(
            screenState = screenState.value,
            viewModel = viewModel
        )
    }

}


@Composable
fun StatisticsScreenContent(
    screenState: ScreenState<StatisticsStateData>,
    viewModel: StatisticsViewModel
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> StatisticsCards(
                stateData = it.data,
                currency = viewModel.currency
            )
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}


@Composable
fun StatisticsCards(
    stateData: StatisticsStateData,
    currency: String
) {

    CustomBasicCard {
        StatisticsBox(
            icon = painterResource(id = R.drawable.local_gas_station_24),
            title = stringResource(id = R.string.total_gas),
            unit = "l",
            values = stateData.totalGas
        )
    }
    
    CustomBasicCard {
        StatisticsBox(
            icon = painterResource(id = R.drawable.receipt_24),
            title = stringResource(id = R.string.total_expenses),
            unit = currency,
            values = stateData.totalExpenses
        )
    }

    CustomBasicCard {
        StatisticsBox(
            icon = painterResource(id = R.drawable.straighten_24),
            title = stringResource(id = R.string.distance_traveled),
            unit = "km",
            values = stateData.totalDistance)
    }

    CustomBasicCard {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.opacity_24), contentDescription = "", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = MaterialTheme.spacing.extraSmall))
            Text(text = stringResource(id = R.string.average_fuel_consumption), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.Start) {
                Text(text = stringResource(id = R.string.this_year), color = MaterialTheme.colorScheme.secondary)
                Text(text = String.format("%.2f", stateData.thisYearAverageConsumption) + " l/100km", style = MaterialTheme.typography.bodyMedium)
            }
            Column(modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
                horizontalAlignment = Alignment.Start) {
                Text(text = stringResource(id = R.string.last_year), color = MaterialTheme.colorScheme.secondary)
                Text(text = String.format("%.2f", stateData.lastYearAverageConsumption) + " l/100km", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    CustomBasicCard {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.coin_24), contentDescription = "", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = MaterialTheme.spacing.extraSmall))
            Text(text = stringResource(id = R.string.gas_price), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = stringResource(id = R.string.this_year), color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Text(text = stringResource(id = R.string.best_price), color = SheetGreen)
                Text(text = String.format("%.2f", stateData.thisYearBestPrice) + " $currency", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Text(text = stringResource(id = R.string.worst_price), color = SheetRed)
                Text(text = String.format("%.2f", stateData.thisYearWorstPrice) + " $currency", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Text(text = stringResource(id = R.string.average_price), color = SheetOrange)
                Text(text = String.format("%.2f", stateData.thisYearAveragePrice) + " $currency", style = MaterialTheme.typography.bodyMedium)
            }
            VerticalDivider(thickness = 5.dp)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = stringResource(id = R.string.last_year), color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Text(text = stringResource(id = R.string.best_price), color = SheetGreen)
                Text(text = String.format("%.2f", stateData.lastYearBestPrice) + " $currency", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Text(text = stringResource(id = R.string.worst_price), color = SheetRed)
                Text(text = String.format("%.2f", stateData.lastYearWorstPrice) + " $currency", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Text(text = stringResource(id = R.string.average_price), color = SheetOrange)
                Text(text = String.format("%.2f", stateData.lastYearAveragePrice) + " $currency", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }


}


@Composable
fun StatisticsBox(
    icon: Painter,
    title: String,
    unit: String,
    values: ValuesAtTime
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = MaterialTheme.spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = icon, contentDescription = "", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(end = MaterialTheme.spacing.extraSmall))
        Text(text = title, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleMedium)
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = MaterialTheme.spacing.extraSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = stringResource(id = R.string.this_month), color = MaterialTheme.colorScheme.secondary)
            Text(text = String.format("%.2f", values.thisMonth) + " $unit", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
            Text(text = stringResource(id = R.string.this_year), color = MaterialTheme.colorScheme.secondary)
            Text(text = String.format("%.2f", values.thisYear) + " $unit", style = MaterialTheme.typography.bodyMedium)
        }
        VerticalDivider(thickness = 5.dp)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = stringResource(id = R.string.last_month), color = MaterialTheme.colorScheme.secondary)
            Text(text = String.format("%.2f", values.lastMonth) + " $unit", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
            Text(text = stringResource(id = R.string.last_year), color = MaterialTheme.colorScheme.secondary)
            Text(text = String.format("%.2f", values.lastYear) + " $unit", style = MaterialTheme.typography.bodyMedium)
        }
    }




}