package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagDeleteConfirm
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagSpecDelete
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagSpecTitle
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagSpecValue
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Specification
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomTextField
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@Composable
fun SpecificationDialog(
    showDialog: MutableState<Boolean>,
    specificationsList: MutableList<Specification>,
    specification: Specification? = null,
    viewModel: AddVehicleViewModel
) {

    val spec: Specification by remember {  mutableStateOf(specification ?: Specification("", "", -1L))  }

    var specTitle by rememberSaveable { mutableStateOf(spec.specTitle) }
    var specValue by rememberSaveable { mutableStateOf(spec.specValue) }

    AlertDialog(
        title = {
        Row(modifier = Modifier
            .fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(id = R.string.specification))
            IconButton(
                modifier = Modifier.testTag(TestTagSpecDelete),
                onClick = {
                specificationsList.remove(spec)
                viewModel.deleteSpecification(spec)
                viewModel.addVehicleUiState.value = AddVehicleUiState.VehicleLoaded
                showDialog.value = false
            }) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = "")
            }
        } },
        text = {
                Column() {
                    CustomTextField(
                        modifier = Modifier.testTag(TestTagSpecTitle),
                        value = specTitle,
                        label = stringResource(id = R.string.title),
                        maxLines = 5,
                        onValueChange = {
                            specTitle = it
                        })
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    CustomTextField(
                        modifier = Modifier.testTag(TestTagSpecValue),
                        value = specValue,
                        label = stringResource(id = R.string.value),
                        maxLines = 5,
                        onValueChange = {
                            specValue = it
                        })
                }
        },
        onDismissRequest = { showDialog.value = false },
        dismissButton = { TextButton(onClick = { showDialog.value = false }) {
            Text(text = stringResource( id = R.string.cancel ))
        } },
        confirmButton = {
            TextButton(
                modifier = Modifier.testTag(TestTagDeleteConfirm),
                onClick = {
            if (specification == null){
                specificationsList.add(spec)
            }
            spec.specTitle = specTitle
            spec.specValue = specValue

            viewModel.addVehicleUiState.value = AddVehicleUiState.VehicleLoaded

            showDialog.value = false
        }) {
            Text(text = stringResource( id = R.string.confirm ))
        } },
    )
}