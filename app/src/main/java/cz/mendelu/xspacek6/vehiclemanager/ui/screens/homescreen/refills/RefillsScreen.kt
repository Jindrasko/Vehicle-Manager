package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.refills

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagLazyList
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomRecordCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomRecordDescriptionText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.EmptyRecordsText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.RefillColor
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import org.koin.androidx.compose.getViewModel

@Composable
fun RefillsScreen(
    vehicleId: Long,
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    viewModel: RefillsViewModel = getViewModel()
) {

    viewModel.vehicleId = vehicleId

    val screenState: MutableState<ScreenState<List<Refill>>> = remember {
        mutableStateOf(ScreenState.Loading())
    }

    val refillsUiState: RefillsUiState<List<Refill>>? by viewModel.refillsUiState.collectAsState()

    refillsUiState?.let {
        when(it) {
            is RefillsUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadRefills()
                }
            }

            is RefillsUiState.Loaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }

            is RefillsUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }

        }
    }

    
    RefillsScreenContent(
        navigation = navigation,
        paddingValues = paddingValues,
        vehicleId = vehicleId,
        screenState = screenState.value,
        viewModel = viewModel
    )

}



@Composable
fun RefillsScreenContent(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    vehicleId: Long,
    screenState: ScreenState<List<Refill>>,
    viewModel: RefillsViewModel
) {
//    val state by screenState

    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> ListOfRefills(
                navigation = navigation,
                paddingValues = paddingValues,
                vehicleId = vehicleId,
                refills = it.data,
                currency = viewModel.currency
            )
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }


}


@Composable
fun ListOfRefills(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    vehicleId: Long,
    refills: List<Refill>,
    currency: String
) {

    LazyColumn(modifier = Modifier
        .padding(paddingValues)
        .testTag(TestTagLazyList)
    ) {

        if(refills.isEmpty()){
            item {
                EmptyRecordsText(
                    text = stringResource(id = R.string.empty_refuel_list),
                    actionText = stringResource(id = R.string.add_new_refuel)
                ) {
                    navigation.navigateToAddRefill(
                        vehicleId = vehicleId,
                        refillId = -1L
                    )
                }
            }
        }

        refills.forEach{
            item(key = it.refillId) {
                RefillRow(refill = it,
                    currency = currency) {
                        navigation.navigateToAddRefill(
                            vehicleId = it.vehicleId,
                            refillId = it.refillId
                        )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace)) }
    }

}


@Composable
fun RefillRow(
    refill: Refill,
    currency: String,
    onRowClick: () -> Unit
) {

    CustomRecordCard(
        title = stringResource(id = R.string.refueling),
        date = DateUtils.getDateString(unixTime = refill.date),
        odometer = refill.mileage,
        icon = R.drawable.local_gas_station_24,
        color = RefillColor,
        cost = refill.totalCost,
        currency = currency,
        onClick = onRowClick
    ) {
        if (refill.volume != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.opacity_24,
                title = stringResource(id = R.string.volume),
                value = String.format("%.2f", refill.volume) + " l"
            )
        }
        
        if (refill.fuelCost != null){
            CustomRecordDescriptionText(icon = R.drawable.coin_24,
                title = stringResource(id = R.string.cost),
                value = String.format("%.2f", refill.fuelCost) + " $currency")
        }

        if (refill.fuelType != null){
            CustomRecordDescriptionText(
                icon = R.drawable.local_gas_station_24,
                title = stringResource(id = R.string.gas_type),
                value = refill.fuelType!!.name)
        }

        if (refill.full){
            CustomRecordDescriptionText(
                icon = R.drawable.gas_meter_24,
                title = stringResource(id = R.string.full_tank),
                value = null)
        }

        if (refill.previousMissed) {
            CustomRecordDescriptionText(
                icon = R.drawable.format_color_reset_24,
                title = stringResource(id = R.string.missed_previous),
                value = null)
        }

        if (refill.note != null) {
            CustomRecordDescriptionText(
                icon = R.drawable.sticky_note_2_24,
                title = refill.note!!,
                value = null)
        }

    }


}

