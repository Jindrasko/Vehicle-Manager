package cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.addtrip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Trip
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomDatePicker
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomDurationPicker
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomOutlinedTextField
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.IconRow


@Composable
fun TripTitleCard(
    trip: Trip,
    tripTitleErrorMessage: String
    ) {

    var title by rememberSaveable { mutableStateOf(trip.title) }

    CustomBasicCard {
        IconRow(icon = R.drawable.title_24) {
            CustomOutlinedTextField(
                value = title,
                errorMessage = tripTitleErrorMessage,
                label = stringResource(id = R.string.title),
                onValueChange = {
                    title = it
                    trip.title = it
                })
        }

        IconRow(icon = R.drawable.today_24) {
            CustomDatePicker(
                label = stringResource(id = R.string.date),
                date = trip.date,
                onDateChange = {
                    trip.date = it!!
                })
        }
    }

}


@Composable
fun TripLocationCard(
    trip: Trip
) {

    var startLocation by rememberSaveable { mutableStateOf(trip.startLocation) }
    var finishLocation by rememberSaveable { mutableStateOf(trip.finishLocation) }

    CustomBasicCard {
        IconRow(icon = R.drawable.play_arrow_24) {
            CustomOutlinedTextField(
                value = startLocation,
                label = stringResource(id = R.string.start_location),
                onValueChange = {
                    startLocation = it
                    trip.startLocation = it
                }
            )
        }

        IconRow(icon = R.drawable.finish_24) {
            CustomOutlinedTextField(
                value = finishLocation,
                label = stringResource(id = R.string.finish_location),
                onValueChange = {
                    finishLocation = it
                    trip.finishLocation = it
                })
        }
    }


}


@Composable
fun TripStatsCard(
    trip: Trip
) {

    var distance by rememberSaveable { mutableStateOf(if (trip.distance != null) trip.distance.toString() else "") }
    var avgSpeed by rememberSaveable { mutableStateOf(if (trip.averageSpeed != null) trip.averageSpeed.toString() else "") }

    CustomBasicCard {
        
        IconRow(icon = R.drawable.outline_timer_24) {
            CustomDurationPicker(
                label = stringResource(id = R.string.duration),
                time = trip.timeDriven,
                canBeNull = true,
                onTimeChanged = {
                    trip.timeDriven = it
                },
                onTimeClear = {
                    trip.timeDriven = null
                }
            )


        }
        
        
        IconRow(icon = R.drawable.straighten_24) {
            CustomOutlinedTextField(
                value = distance,
                label = stringResource(id = R.string.distance) + " (m)",
                keyboardType = KeyboardType.Number,
                onValueChange = {
                    if(it.isNullOrEmpty()) {
                        distance = ""
                        trip.distance = null
                    } else if(it.toIntOrNull() != null){
                        distance = it.filter { it.isDigit() }
                        trip.distance = it.filter { it.isDigit() }.toInt()
                    }
                })
        }

        IconRow(icon = R.drawable.speed_24) {
            CustomOutlinedTextField(
                value = avgSpeed,
                label = stringResource(id = R.string.average_speed) + " (km/h)",
                keyboardType = KeyboardType.Number,
                onValueChange = {
                    if (it.isEmpty()) {
                        avgSpeed = it
                        trip.averageSpeed = null
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> {
                                if (avgSpeed.isEmpty()) {
                                    trip.averageSpeed = null
                                } else {
                                    trip.averageSpeed = avgSpeed.toFloat()
                                }
                            }

                            else -> {
                                avgSpeed = it //new value
                                trip.averageSpeed = it.toFloat()
                            }
                        }
                    }
                })
        }
    }
}

@Composable
fun TripNoteCard(
    trip: Trip
) {
    var note by rememberSaveable { mutableStateOf(trip.note) }

    CustomBasicCard {
        IconRow(icon = R.drawable.sticky_note_2_24) {
            CustomOutlinedTextField(
                value = note,
                label = stringResource(id = R.string.note),
                maxLines = 10,
                onValueChange = {
                    note = it
                    trip.note = it
                })
        }
    }
}

