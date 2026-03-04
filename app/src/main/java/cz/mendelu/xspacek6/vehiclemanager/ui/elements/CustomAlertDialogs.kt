package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagDeleteConfirm
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import java.util.Date

@Composable
fun CustomAlertDialog(
    show: MutableState<Boolean>,
    title: String = stringResource(id = R.string.delete_alert_title),
    text: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = text) },
        onDismissRequest = { show.value = false },
        dismissButton = { TextButton(onClick = { show.value = false }) {
            Text(text = stringResource( id = R.string.cancel ))
        } },
        confirmButton = {
            TextButton(
                modifier = Modifier.testTag(TestTagDeleteConfirm),
                onClick = {
                show.value = false
                onConfirm()
            }) {
            Text(text = stringResource( id = R.string.confirm ))
        } },
    )
}


@Composable
fun MileageDialog(
    show: MutableState<Boolean>,
    lastMileage: String,
    mileage: Mileage? = null,
    vehicleId: Long,
    onConfirm: (Mileage) -> Unit
) {
    var odometer: String by rememberSaveable { mutableStateOf(mileage?.mileage?.toString() ?: "") }
    val localMileage: Mileage by remember { mutableStateOf(
        mileage ?: Mileage(mileage = lastMileage.toInt(),
            date = Date().time,
            vehicleId = vehicleId)) }

    AlertDialog(
        title = {
            Text(text = stringResource(id = R.string.add_odometer_value))
        },
        text = {
            Column() {
                if (mileage != null) {
                    CustomDatePicker(label = stringResource(id = R.string.date), date = localMileage.date, onDateChange = { localMileage.date = it!! })
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }

                CustomOutlinedTextField(
                    value = odometer,
                    label = stringResource(id = R.string.mileage),
                    underMessage = stringResource(id = R.string.odometer) + ": " + lastMileage,
                    onValueChange = {
                        if(it.isNullOrEmpty()) {
                            odometer = ""
                            localMileage.mileage = 0
                        } else if(it.toIntOrNull() != null){
                            odometer = it.filter { it.isDigit() }
                            localMileage.mileage = it.filter { it.isDigit() }.toInt()
                        }
                    },
                    keyboardType = KeyboardType.Number
                )
            }
        },
        onDismissRequest = { show.value = false },
        dismissButton = { TextButton(onClick = { show.value = false }) {
            Text(text = stringResource( id = R.string.cancel ))
        } },
        confirmButton = { TextButton(onClick = {
            show.value = false
            onConfirm(localMileage)
        }) {
            Text(text = stringResource( id = R.string.confirm ))
        } }
    )
}


