package cz.mendelu.xspacek6.vehiclemanager.ui.screens.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagCalculatorAvgButton
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagCalculatorAvgField
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagCalculatorDistanceButton
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagCalculatorDistanceField
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagCalculatorFuelButton
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagCalculatorFuelField
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagCalculatorPriceField
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomOutlinedTextField
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing


@Composable
fun CalculatorScreen(
    navigation: INavigationRouter
) {
    var distance by remember { mutableStateOf("") }
    var consumption by remember { mutableStateOf("") }
    var fuel by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedRow by remember { mutableStateOf(1) }

    val resultLabel: String = try {
        when(selectedRow) {
            0 -> appContext.getString(R.string.distance)
            1 -> appContext.getString(R.string.avg_fuel_consumption)
            2 -> appContext.getString(R.string.fuel_required)
            else -> "!#%$"
        }
    } catch (e: Exception){
        "0"
    }

    val result = try {
        when(selectedRow) {
            0 -> {
//                ((fuel.toFloat() * 100) / consumption.toFloat()).toString() + " km"
                String.format("%.2f", ((fuel.toFloat() * 100) / consumption.toFloat())) + " km"
            }
            1 -> {
//                ((fuel.toFloat() * 100) / distance.toFloat()).toString() + " l/100km"
                String.format("%.2f", ((fuel.toFloat() * 100) / distance.toFloat())) + " l/100km"
            }
            2 -> {
//                ((consumption.toFloat() * distance.toFloat()) / 100).toString() + " l"
                String.format("%.2f",  ((consumption.toFloat() * distance.toFloat()) / 100)) + " l"
            }
            else -> 0
        }
    } catch (e: Exception) {
        0
    }

    val totalCost = try {
        if(selectedRow == 2){
            ((consumption.toFloat() * distance.toFloat()) / 100) * price.toFloat()
        } else {
            fuel.toFloat() * price.toFloat()
        }
    } catch (e: Exception) {
        0
    }

    BackArrowScreen(
        topBarText = stringResource(id = R.string.calculator),
        actions = {},
        onBackClick = { navigation.returnBack() }
    ) {
        CustomBasicCard {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
            ) {
                RadioButton(
                    selected = selectedRow == 0,
                    onClick = {
                        selectedRow = 0
                        distance = ""
                    },
                    modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
                        .testTag(TestTagCalculatorDistanceButton)
                )
                CustomOutlinedTextField(
                    modifier = Modifier.testTag(TestTagCalculatorDistanceField),
                    value = distance,
                    label = stringResource(id = R.string.distance),
                    enabled = selectedRow != 0,
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = {
                        distance = if (it.isEmpty()){
                            it
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> distance
                                else -> it
                            }
                        }
                    }

                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
            ) {
                RadioButton(
                    selected = selectedRow == 1,
                    onClick = {
                        selectedRow = 1
                        consumption = ""
                    },
                    modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
                        .testTag(TestTagCalculatorAvgButton)
                )
                CustomOutlinedTextField(
                    modifier = Modifier.testTag(TestTagCalculatorAvgField),
                    value = consumption,
                    label = stringResource(id = R.string.avg_fuel_consumption),
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = {
                        consumption = if (it.isEmpty()){
                        it
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> consumption
                                else -> it
                            }
                        }
                    },
                    enabled = selectedRow != 1
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.small)
            ) {
                RadioButton(
                    selected = selectedRow == 2,
                    onClick = {
                        selectedRow = 2
                        fuel = ""
                    },
                    modifier = Modifier.padding(end = MaterialTheme.spacing.medium)
                        .testTag(TestTagCalculatorFuelButton)
                )
                CustomOutlinedTextField(
                    modifier = Modifier.testTag(TestTagCalculatorFuelField),
                    value = fuel,
                    label = stringResource(id = R.string.fuel_required),
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = {
                        fuel = if (it.isEmpty()){
                            it
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> fuel
                                else -> it
                            }
                        }
                    },
                    enabled = selectedRow != 2
                )
            }

            Box(modifier = Modifier
                .width(200.dp)
                .align(Alignment.End)
                .padding(vertical = MaterialTheme.spacing.small)) {
                CustomOutlinedTextField(
                    modifier = Modifier.testTag(TestTagCalculatorPriceField),
                    value = price,
                    label = stringResource(id = R.string.price),
                    keyboardType = KeyboardType.Decimal,
                    onValueChange = {
                        price = if (it.isEmpty()){
                            it
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> price
                                else -> it
                            }
                        }
                    }
                )

            }

            Text(
                text = "$resultLabel:",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = MaterialTheme.spacing.small)
            )

            Text(
                text = "$result",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.End)
            )

            Text(
                text = stringResource(id = R.string.total_cost) + ": ",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
            )

            Text(
//                text = "$totalCost",
                text = String.format("%.2f", totalCost.toFloat()),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.End)
            )

        }
    }
}

