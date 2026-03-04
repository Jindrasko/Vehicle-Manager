package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.GasType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GasTypeMenu(
    gasTypeValue: MutableState<GasType?>,
    outlined: Boolean = false,
    onTypeChanged: (GasType) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {
        if (outlined){
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = if(gasTypeValue.value != null) stringResource(id = gasTypeValue.value!!.title) else "",
                onValueChange = { },
                label = { Text(stringResource(id = R.string.gas_type)) },
//                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
        } else {
            TextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = if(gasTypeValue.value != null) stringResource(id = gasTypeValue.value!!.title) else "",
                onValueChange = { },
                label = { Text(stringResource(id = R.string.gas_type)) },
//                colors = ExposedDropdownMenuDefaults.textFieldColors()
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )
            )
        }

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            GasType.values().forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = selectionOption.title))
                    },
                    onClick = {
                        gasTypeValue.value = selectionOption
                        onTypeChanged(selectionOption)
                        expanded.value = false
                    }
                )
            }
        }


    }



}