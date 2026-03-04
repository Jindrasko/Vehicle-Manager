package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(
    label: String,
    date: Long?,
    onDateChange: (Long?) -> Unit,
    canBeNull: Boolean = false,
    outlined: Boolean = true,
    onDateClear: () -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var pickedDate by remember { mutableStateOf(if(canBeNull) date else date ?: Date().time) }

    val calendarState = rememberUseCaseState()
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(monthSelection = true, yearSelection = true),
        selection = CalendarSelection.Date { localDate ->
            pickedDate = DateUtils.getUnixTime(localDate)
            onDateChange(pickedDate)
        }
    )

    if (outlined) {
        OutlinedTextField(
            value = if(pickedDate != null) DateUtils.getDateString(pickedDate!!) else "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { calendarState.show() },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = {
                if (canBeNull) {
                    IconButton(
                        onClick = {
                            pickedDate = null
                            onDateClear()
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
            value = if(pickedDate != null) DateUtils.getDateString(pickedDate!!) else "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { calendarState.show() },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            trailingIcon = {
                if (canBeNull) {
                    IconButton(
                        onClick = {
                            pickedDate = null
                            onDateClear()
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



/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficialCustomDatePicker(
    label: String,
    date: String,
    onValueChange: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var showDatePicker: Boolean by rememberSaveable { mutableStateOf(false) }
    val pickedDate by remember { mutableStateOf(Date().time) }


    Box(modifier = Modifier.clickable { showDatePicker = true }){
        OutlinedTextField(
            value = pickedDate.toString(),
            onValueChange = onValueChange,
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.clickable { showDatePicker = true },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
        )
    }


    if (showDatePicker) {
        DatePickerDialog(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            onDismissRequest = { showDatePicker = false },
            properties = DialogProperties(

            ),
            dismissButton = {
                OutlinedButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                            },
            confirmButton = {
                OutlinedButton(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            DatePicker(
                state = DatePickerState(
                    initialSelectedDateMillis = pickedDate,
                    initialDisplayedMonthMillis = null,
                    yearRange = IntRange(start = 1900, endInclusive = 2100),
                    initialDisplayMode = DisplayMode.Picker
                ),
                title = { Text(text = stringResource(id = R.string.select_date)) },
                showModeToggle = true,

//                headline = DatePickerDefaults.DatePickerHeadline(
//                    state = DatePickerState(initialSelectedDateMillis = null,
//                        initialDisplayedMonthMillis = null,
//                        yearRange = IntRange(start = 1900, endInclusive = 2100),
//                        initialDisplayMode = DisplayMode.Picker),
////                    dateFormatter =  DatePickerFormatter(
////                        yearSelectionSkeleton = ,
////                        selectedDateSkeleton = ,
////                        selectedDateDescriptionSkeleton =
////                    )
//                )

            )

        }
    }

}

 */

