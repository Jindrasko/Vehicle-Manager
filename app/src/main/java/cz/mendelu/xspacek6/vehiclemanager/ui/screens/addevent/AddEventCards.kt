package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addevent

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CategoryMenu
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomDatePicker
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomOutlinedTextField
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.IconRow
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.SegmentedControl
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@Composable
fun EventTitleCard(
    event: Event,
    eventTitleErrorMessage: String,

    lastMileage: Int?,
    doneChecked: (Boolean) -> Unit
) {
    var title by rememberSaveable { mutableStateOf(event.title) }
    val eventCategory = rememberSaveable { mutableStateOf(event.eventCategory) }
    var done by rememberSaveable{ mutableStateOf(event.done) }
    var odometer by rememberSaveable { mutableStateOf(if (event.finalKm != null) event.finalKm.toString() else if(lastMileage != null) lastMileage.toString() else "0") }

    CustomBasicCard {
        IconRow(icon = R.drawable.title_24) {
            CustomOutlinedTextField(
                value = title,
                errorMessage = eventTitleErrorMessage,
                label = stringResource(id = R.string.title),
                onValueChange = {
                    title = it
                    event.title = it
                })
        }

        IconRow(icon = R.drawable.label_24, modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)) {
            CategoryMenu(
                categoryValue = eventCategory,
                onCategoryChanged = {
                    event.eventCategory = it
                }
            )
        }

        IconRow(icon = R.drawable.check_24) {
            Text(text = stringResource(id = R.string.done))
            Switch(
                checked = done,
                onCheckedChange = {
                    event.done = it
                    done = it
                    doneChecked(done)
                }
            )
        }

        if(done && event.onceAtKm != null) {
            IconRow(icon = R.drawable.speed_24, modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)) {
                CustomOutlinedTextField(
                    value = odometer,
                    label = stringResource(id = R.string.finished_at_km),
                    underMessage = if (lastMileage != null) stringResource(id = R.string.last_mileage) + ": $lastMileage" else "",
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        if (it.isNullOrEmpty()) {
                            odometer = ""
                            event.finalKm = null
                        } else if (it.toIntOrNull() != null) {
                            odometer = it.filter { it.isDigit() }
                            event.finalKm = it.filter { it.isDigit() }.toInt()
                        }
                    }
                )
            }
        }

        if (done && event.onceDate != null) {
            IconRow(icon = R.drawable.today_24) {
                CustomDatePicker(
                    label = stringResource(id = R.string.finished_at_date),
                    date = event.finalDate,
                    onDateChange = {
                        event.finalDate = it
                    }
                )
            }
        }


    }
}



@Composable
fun EventRepeatCard(
    event: Event,
    lastMileage: Int?
) {
    var repeat by remember { mutableStateOf(if (event.everyKm != null || event.everyMonths != null) true else false) }
    var odometer by rememberSaveable { mutableStateOf(if (event.onceAtKm != null) event.onceAtKm.toString() else "") }
    var everyDays by rememberSaveable { mutableStateOf(if (event.everyMonths != null) event.everyMonths.toString() else "") }
    var everyKm by rememberSaveable { mutableStateOf(if (event.everyKm != null) event.everyKm.toString() else "") }

    CustomBasicCard {
        IconRow(icon = R.drawable.repeat_24, modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)) {
            SegmentedControl(
                items = listOf(
                    stringResource(id = R.string.one_time),
                    stringResource(id = R.string.repeat)
                ),
                cornerRadius = 50,
                defaultSelectedItemIndex = if (event.everyKm != null || event.everyMonths != null) 1 else 0
            ) {
                if (it == 0) {
                    repeat = false
                    everyDays = ""
                    event.everyMonths = null
                    everyKm = ""
                    event.everyKm = null
                } else if (it == 1) {
                    repeat = true
                } else {
                }
            }
        }

        IconRow(icon = R.drawable.today_24) {
            CustomDatePicker(
                label = stringResource(id = R.string.once_at_date),
                date = event.onceDate,
                onDateChange = {
                    event.onceDate = it
                },
                canBeNull = true,
                onDateClear = { event.onceDate = null }
            )
        }

        if (repeat) {
            IconRow(icon = R.drawable.event_note_24) {
                CustomOutlinedTextField(
                    value = everyDays,
                    label = stringResource(id = R.string.every_months),
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        if (it.isNullOrEmpty()) {
                            everyDays = ""
                            event.everyMonths = null
                        } else if (it.toIntOrNull() != null) {
                            everyDays = it.filter { it.isDigit() }
                            event.everyMonths = it.filter { it.isDigit() }.toInt()
                        }
                    })
            }
        }

        IconRow(icon = R.drawable.speed_24) {
            CustomOutlinedTextField(
                value = odometer,
                label = stringResource(id = R.string.once_at_km),
                underMessage = if (lastMileage != null) stringResource(id = R.string.last_mileage) + ": $lastMileage" else "",
                keyboardType = KeyboardType.Number,
                onValueChange = {
                    if (it.isNullOrEmpty()) {
                        odometer = ""
                        event.onceAtKm = null
                    } else if (it.toIntOrNull() != null) {
                        odometer = it.filter { it.isDigit() }
                        event.onceAtKm = it.filter { it.isDigit() }.toInt()
                    }
                }
            )
        }

        if (repeat) {
            IconRow(icon = R.drawable.repeat_24) {
                CustomOutlinedTextField(
                    value = everyKm,
                    label = stringResource(id = R.string.every_km),
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        if (it.isNullOrEmpty()) {
                            everyKm = ""
                            event.everyKm = null
                        } else if (it.toIntOrNull() != null) {
                            everyKm = it.filter { it.isDigit() }
                            event.everyKm = it.filter { it.isDigit() }.toInt()
                        }
                    })
            }
        }

    }
}



@Composable
fun EventNoteCard(
    event: Event
) {
    var note by rememberSaveable { mutableStateOf(event.note) }

    CustomBasicCard {
        IconRow(icon = R.drawable.sticky_note_2_24) {
            CustomOutlinedTextField(
                value = note,
                label = stringResource(id = R.string.note),
                maxLines = 10,
                onValueChange = {
                    note = it
                    event.note = it
                })
        }

    }
}