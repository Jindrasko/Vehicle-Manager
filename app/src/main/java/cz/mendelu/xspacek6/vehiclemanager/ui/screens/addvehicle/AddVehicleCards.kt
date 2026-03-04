package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagAddSpecButton
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagAddVehicleName
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagGasType
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagVolumeTextField
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Specification
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Vehicle
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomDatePicker
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomTextField
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing


@Composable
fun VehicleNameCard(
    vehicle: Vehicle,
    vehicleNameErrorMessage: String
) {
    var vehicleName by rememberSaveable { mutableStateOf(vehicle.name) }
    var vehicleDescription by rememberSaveable { mutableStateOf(vehicle.description) }

    CustomBasicCard(){

        CustomTextField(
            modifier = Modifier.testTag(TestTagAddVehicleName),
            value = vehicleName,
            label = stringResource(id = R.string.name),
            errorMessage = vehicleNameErrorMessage,
            onValueChange = {
                vehicleName = it
                vehicle.name = it
            })
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        CustomTextField(
            value = vehicleDescription,
            label = stringResource(id = R.string.description),
            onValueChange = {
                vehicleDescription = it
                vehicle.description = it
            })

    }
}



@Composable
fun VehicleModelCard(
    vehicle: Vehicle,
) {
    var vehicleManufacturer by rememberSaveable { mutableStateOf(vehicle.manufacturer) }
    var vehicleModel by rememberSaveable { mutableStateOf(vehicle.model) }
    var vehicleColor by rememberSaveable { mutableStateOf(vehicle.color) }
    var vehicleLicencePlate by rememberSaveable { mutableStateOf(vehicle.licencePlate) }
    var vehicleVIN by rememberSaveable { mutableStateOf(vehicle.vin) }

    CustomBasicCard(){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                CustomTextField(
                    value = vehicleManufacturer,
                    label = stringResource(id = R.string.manufacturer),
                    onValueChange = {
                        vehicleManufacturer = it
                        vehicle.manufacturer = it
                    })
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Column(modifier = Modifier.weight(1f)) {
                CustomTextField(
                    value = vehicleModel,
                    label = stringResource(id = R.string.model),
                    onValueChange = {
                        vehicleModel = it
                        vehicle.model = it
                    })
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically) {
            CustomDatePicker(
                label = stringResource(id = R.string.date_of_purchase),
                date = vehicle.dateOfPurchase,
                outlined = false,
                canBeNull = true,
                onDateClear = {
                    vehicle.dateOfPurchase = null
                },
                onDateChange = {
                    vehicle.dateOfPurchase = it
                }
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                CustomTextField(
                    value = vehicleColor,
                    label = stringResource(id = R.string.color),
                    onValueChange = {
                        vehicleColor = it
                        vehicle.color = it
                    })
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Column(modifier = Modifier.weight(1f)) {
                CustomTextField(
                    value = vehicleLicencePlate,
                    label = stringResource(id = R.string.licence_plate),
                    onValueChange = {
                        vehicleLicencePlate = it
                        vehicle.licencePlate = it
                    })

            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            CustomTextField(
                value = vehicleVIN,
                label = stringResource(id = R.string.vin),
                onValueChange = {
                    vehicleVIN = it
                    vehicle.vin = it
                })
        }


    }

}



@Composable
fun VehicleFuelCard(
    vehicle: Vehicle
) {
    var vehicleFirstTankVolume by rememberSaveable { mutableStateOf(if (vehicle.fuelTankVolume != null) vehicle.fuelTankVolume.toString() else "" ) }
    val vehicleFirstFuelType = rememberSaveable { mutableStateOf(vehicle.fuelType) }


    CustomBasicCard(){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(id = R.string.fuel_tank))

        }

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.tank),
                modifier = Modifier.width(80.dp),
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Column(modifier = Modifier.weight(1f)) {
                CustomTextField(
                    modifier = Modifier.testTag(TestTagVolumeTextField),
                    value = vehicleFirstTankVolume,
                    label = stringResource(id = R.string.volume),
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        if(it.isNullOrEmpty()) {
                            vehicleFirstTankVolume = ""
                            vehicle.fuelTankVolume = null
                        } else if(it.toIntOrNull() != null){
                            vehicleFirstTankVolume = it.filter { it.isDigit() }
                            vehicle.fuelTankVolume = it.filter { it.isDigit() }.toInt()
                        }
                    })
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier
                    .testTag(TestTagGasType)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.TopStart)) {
                    GasTypeMenu(
                        gasTypeValue = vehicleFirstFuelType,
                        onTypeChanged = {
                            vehicle.fuelType = it
                        }
                    )
                }
            }

        }

    }

}



@Composable
fun SpecificationsCard(
    specifications: List<Specification>,
    showSpecDialog: MutableState<Boolean>,
    selectedSpec: MutableState<Specification?>
) {
    CustomBasicCard(){

        Text(text = stringResource(id = R.string.specifications) + ":", style = MaterialTheme.typography.titleLarge)
        Divider()

        specifications.forEach { spec ->
            Row(modifier = Modifier
                .padding(
                    top = MaterialTheme.spacing.extraSmall,
                    bottom = MaterialTheme.spacing.extraSmall
                )
                .clickable {
                    selectedSpec.value = spec
                    showSpecDialog.value = true
                }) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = spec.specTitle, modifier = Modifier.fillMaxWidth())
                }
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = spec.specValue, modifier = Modifier.fillMaxWidth())
                }
            }
            Divider()
        }

        Button(onClick = {
            selectedSpec.value = null
            showSpecDialog.value = true
        }, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = MaterialTheme.spacing.small)
            .testTag(TestTagAddSpecButton)
        ) {
            Row() {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Save")
                Text(modifier = Modifier.align(Alignment.CenterVertically), text = stringResource(id = R.string.specification))
            }

        }

    }
}