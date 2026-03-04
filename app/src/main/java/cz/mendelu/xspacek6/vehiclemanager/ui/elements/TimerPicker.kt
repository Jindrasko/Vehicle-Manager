package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.duration.DurationDialog
import com.maxkeppeler.sheets.duration.models.DurationConfig
import com.maxkeppeler.sheets.duration.models.DurationFormat
import com.maxkeppeler.sheets.duration.models.DurationSelection
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import cz.mendelu.xspacek6.vehiclemanager.utils.TrackingUtility
import java.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDurationPicker(
    label: String,
    time: Long?,
    onTimeChanged: (Long?) -> Unit,
    canBeNull: Boolean = false,
    outlined: Boolean = true,
    onTimeClear: () -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var pickedTime by remember { mutableStateOf(if (canBeNull) (time?.div(1000)) else (time?.div(1000)) ?: 0L)   }

    val durationState = rememberUseCaseState()
    DurationDialog(
        state = durationState,
        selection = DurationSelection {
            pickedTime = it
            onTimeChanged(it * 1000)
        },
        config = DurationConfig(
            timeFormat = DurationFormat.HH_MM_SS,
            currentTime = pickedTime
        )

    )


    if (outlined) {
        OutlinedTextField(
            value = if(pickedTime != null) TrackingUtility.getFormattedStopWatchTime(pickedTime!! * 1000) else "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { durationState.show() },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = {
                if (canBeNull) {
                    IconButton(
                        onClick = {
                            pickedTime = null
                            onTimeClear()
                        },
                        content = {
                            Icon(Icons.Default.Clear, contentDescription = "")
                        }
                    )
                }
            },
            enabled = false,

            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                //For Icons
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    } else {
        TextField(
            value = if(pickedTime != null) TrackingUtility.getFormattedStopWatchTime(pickedTime!! * 1000) else "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { durationState.show() },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = {
                if (canBeNull) {
                    IconButton(
                        onClick = {
                            pickedTime = null
                            onTimeClear()
                        },
                        content = {
                            Icon(Icons.Default.Clear, contentDescription = "")
                        }
                    )
                }
            },
            enabled = false,

            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                //For Icons
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }


}



@Composable
fun TimerPicker(
    label: String,
    time: Long?,
    onTimeChanged: (Long?) -> Unit
) {
    var timerText by remember { mutableStateOf(
        if (time != null) TrackingUtility.getFormattedStopWatchTime(time)
        else "") }
    var isDialogVisible by remember { mutableStateOf(false) }
    var selectedDuration by remember { mutableStateOf(Duration.ZERO) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        OutlinedTextField(
            value = timerText,
            onValueChange = {
                timerText = it
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isDialogVisible = true
                },
            enabled = false,

            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                //For Icons
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )


        if (isDialogVisible) {
            TimePickerDialog(
                label = label,
                time = time,
                onDismissRequest = {
                    isDialogVisible = false
                },
                onTimeSelected = {
                    timerText = TrackingUtility.getFormattedStopWatchTime(it.toMillis())
                    selectedDuration = it
                    isDialogVisible = false
                    onTimeChanged(
                        if (it != Duration.ZERO) it.toMillis()
                        else null
                    )
                }
            )
        }
    }
}


@Composable
fun TimePickerDialog(
    label: String,
    time: Long?,
    onDismissRequest: () -> Unit,
    onTimeSelected: (Duration) -> Unit
) {

    var selectedDuration by remember { mutableStateOf(
        if (time != null) time.toDuration(DurationUnit.MILLISECONDS).toJavaDuration()
        else Duration.ZERO
    ) }
    val currentDuration = selectedDuration

    Dialog(
        onDismissRequest = {
            onDismissRequest()
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .heightIn(200.dp, 400.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(text = label,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = MaterialTheme.spacing.small, top = MaterialTheme.spacing.small)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    TimePickerView(
                        initialDuration = currentDuration,
                        onTimeSelected = onTimeSelected,
                        onDismissRequest = onDismissRequest
                    )
                }

            }
        }
    }
}


@Composable
fun TimePickerView(
    initialDuration: Duration,
    onDismissRequest: () -> Unit,
    onTimeSelected: (Duration) -> Unit
) {
    // Convert milliseconds to hours, minutes, and seconds
    var hours by remember { mutableStateOf(initialDuration.toHours().toInt()) }
    var minutes by remember { mutableStateOf((initialDuration.toMinutes() % 60).toInt()) }
    var seconds by remember { mutableStateOf((initialDuration.seconds % 60).toInt()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "hh",
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Text(
                text = "mm",
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            Text(
                text = "ss",
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier
                .weight(1f)
            ) {
                NumberPicker(
                    value = hours,
                    onValueChange = { newValue ->
                        hours = newValue
                    },
                    minValue = 0,
                    maxValue = 99 // Allow up to 99 hours
                )
            }

            Column(modifier = Modifier
                .weight(1f)
            ) {
                NumberPicker(
                    value = minutes,
                    onValueChange = { newValue ->
                        minutes = newValue
                    },
                    minValue = 0,
                    maxValue = 59
                )
            }

            Column(modifier = Modifier
                .weight(1f)
            ) {
                NumberPicker(
                    value = seconds,
                    onValueChange = { newValue ->
                        seconds = newValue
                    },
                    minValue = 0,
                    maxValue = 59
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        // Set the selected duration when the "Set Timer" button is clicked
        Button(
            onClick = {
                val newDuration = Duration.ofHours(hours.toLong()) +
                        Duration.ofMinutes(minutes.toLong()) +
                        Duration.ofSeconds(seconds.toLong())
                onTimeSelected(newDuration)
                onDismissRequest()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small)
        ) {
            Text(stringResource(id = R.string.set_time))
        }
    }
}

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    minValue: Int,
    maxValue: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        IconButton(
            onClick = {
                onValueChange((value + 1).coerceIn(minValue, maxValue))
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
        }

        OutlinedTextField(
            value = value.toString(),
            onValueChange = {
                val newValue = it.toIntOrNull() ?: value
                if (newValue in minValue..maxValue) {
                    onValueChange(newValue)
                }
            },
            label = { },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier
//                .fillMaxWidth()
                .padding(4.dp)
        )

        IconButton(
            onClick = {
                onValueChange((value - 1).coerceIn(minValue, maxValue))
            },
            modifier = Modifier.size(24.dp)
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
    }
}

