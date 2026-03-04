package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addrefill

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagCostTextField
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagFullTankSwitch
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagNoteTextField
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagOdoTextField
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagPreviousMissedSwitch
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagVolumeTextField
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomDatePicker
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomOutlinedTextField
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.IconRow
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle.GasTypeMenu
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing


@Composable
fun RefillDateCard(
    refill: Refill,
    lastMileage: Int?
) {
    var odometer by rememberSaveable { mutableStateOf(if (refill.mileage != null) refill.mileage.toString() else "") }

    CustomBasicCard {
        IconRow(icon = R.drawable.today_24, modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)) {
            CustomDatePicker(
                label = stringResource(id = R.string.date),
                date = refill.date,
                onDateChange = {
                    refill.date = it!!
                }
            )
        }

        IconRow(icon = R.drawable.speed_24) {
            CustomOutlinedTextField(
                value = odometer,
                label = stringResource(id = R.string.mileage),
                underMessage = if (lastMileage != null) stringResource(id = R.string.odometer) + ": $lastMileage" else "",
                keyboardType = KeyboardType.Number,
                onValueChange = {
                    if(it.isNullOrEmpty()) {
                        odometer = ""
                        refill.mileage = null
                    } else if(it.toIntOrNull() != null){
                        odometer = it.filter { it.isDigit() }
                        refill.mileage = it.filter { it.isDigit() }.toInt()
                    }
                },
                modifier = Modifier.testTag(TestTagOdoTextField)
            )
        }
    }

}



@Composable
fun RefillFuelCard(
    refill: Refill
) {
    var volume by rememberSaveable { mutableStateOf(if (refill.volume != null) refill.volume.toString() else "" ) }
    val fuelType = rememberSaveable { mutableStateOf(refill.fuelType) }
    var fuelCost by rememberSaveable { mutableStateOf(if (refill.fuelCost != null) refill.fuelCost.toString() else "") }
    var totalCost by rememberSaveable { mutableStateOf(if (refill.totalCost != null) refill.totalCost.toString() else "") }
    var fullTank by rememberSaveable{ mutableStateOf(refill.full) }

    CustomBasicCard {
        IconRow(icon = R.drawable.opacity_24, modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)) {
            Box(modifier = Modifier.weight(1f)) {
                CustomOutlinedTextField(
                    value = volume,
                    label = stringResource(id = R.string.volume),
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        if (it.isEmpty()){
                            volume = it
                            refill.volume = null
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> {
                                    if(volume.isEmpty()){
                                        refill.volume = null
                                    } else {
                                        refill.volume = volume.toDouble()
                                    }
                                }
                                else -> {
                                    volume = it //new value
                                    refill.volume = it.toDouble()
                                }
                            }
                        }
                    },
                    modifier = Modifier.testTag(TestTagVolumeTextField)
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Box(modifier = Modifier.weight(1f)) {
                GasTypeMenu(
                    gasTypeValue = fuelType,
                    outlined = true,
                    onTypeChanged = {
                        refill.fuelType = it
                    })
            }
        }

        IconRow(icon = R.drawable.payments_24, modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)) {
            Box(modifier = Modifier.weight(1f)) {
                CustomOutlinedTextField(
                    value = fuelCost,
                    label = stringResource(id = R.string.price),
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        if (it.isEmpty()){
                            fuelCost = it
                            refill.fuelCost = null
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> {
                                    if(fuelCost.isEmpty()){
                                        refill.fuelCost = null
                                    } else {
                                        refill.fuelCost = fuelCost.toDouble()
                                    }
                                }
                                else -> {
                                    fuelCost = it //new value
                                    refill.fuelCost = it.toDouble()
                                }
                            }
                        }
                    },
                    modifier = Modifier.testTag(TestTagCostTextField)
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Box(modifier = Modifier.weight(1f)) {
                CustomOutlinedTextField(
                    value = totalCost,
                    label = stringResource(id = R.string.total_cost),
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        if (it.isEmpty()){
                            totalCost = it
                            refill.totalCost = null
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> {
                                    //old value
                                    if(totalCost.isEmpty()){
                                        refill.totalCost = null
                                    } else {
                                        refill.totalCost = totalCost.toDouble()
                                    }
                                }
                                else -> {
                                    totalCost = it //new value
                                    refill.totalCost = it.toDouble()
                                }
                            }
                        }
                    }
                )
            }
        }

        IconRow(icon = R.drawable.gas_meter_24) {
            Text(text = stringResource(id = R.string.full_tank))
            Switch(
                checked = fullTank,
                onCheckedChange = {
                    refill.full = it
                    fullTank = it
                },
                modifier = Modifier.testTag(TestTagFullTankSwitch)
            )
        }
    }

}



@Composable
fun RefillNoteCard(
    refill: Refill
) {
    var note by rememberSaveable { mutableStateOf(refill.note) }
    var missed by rememberSaveable { mutableStateOf(refill.previousMissed) }

    CustomBasicCard {
        IconRow(icon = R.drawable.sticky_note_2_24, modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)) {
            CustomOutlinedTextField(
                value = note,
                label = stringResource(id = R.string.note),
                maxLines = 10,
                onValueChange = {
                    note = it
                    refill.note = it
                },
                modifier = Modifier.testTag(TestTagNoteTextField)
            )
        }

        IconRow(icon = R.drawable.format_color_reset_24){
            Text(text = stringResource(id = R.string.missed_previous))
            Switch(
                checked = missed,
                onCheckedChange = {
                    missed = it
                    refill.previousMissed = it
                },
                modifier = Modifier.testTag(TestTagPreviousMissedSwitch)
            )
        }
    }

}