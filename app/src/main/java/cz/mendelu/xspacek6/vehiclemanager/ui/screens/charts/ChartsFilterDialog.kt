package cz.mendelu.xspacek6.vehiclemanager.ui.screens.charts


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsFilterDialog(
    showDialog: MutableState<Boolean>,
    viewModel: ChartsViewModel
) {

    val selectedOption = remember { mutableIntStateOf(viewModel.chartTimeInterval) }

    AlertDialog(
        onDismissRequest = { showDialog.value = false }
    ) {
        CustomBasicCard {
            Column {
                Text(
                    text = "Period",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                        selectedOption.intValue = 1
                        viewModel.chartTimeInterval = 1
                        showDialog.value = false
                        viewModel.chartsUiState.value = ChartsUiState.Default
                    }
                ) {
                    RadioButton(
                        selected = selectedOption.intValue == 1,
                        onClick = {
                            selectedOption.intValue = 1
                            viewModel.chartTimeInterval = 1
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        })
                    Text(
                        text = "1 month",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption.intValue = 6
                            viewModel.chartTimeInterval = 6
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        }
                ) {
                    RadioButton(
                        selected = selectedOption.intValue == 6,
                        onClick = {
                            selectedOption.intValue = 6
                            viewModel.chartTimeInterval = 6
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        })
                    Text(
                        text = "6 months",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption.intValue = 12
                            viewModel.chartTimeInterval = 12
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        }
                ) {
                    RadioButton(
                        selected = selectedOption.intValue == 12,
                        onClick = {
                            selectedOption.intValue = 12
                            viewModel.chartTimeInterval = 12
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        })
                    Text(
                        text = "1 year",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption.intValue = 24
                            viewModel.chartTimeInterval = 24
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        }
                ) {
                    RadioButton(
                        selected = selectedOption.intValue == 24,
                        onClick = {
                            selectedOption.intValue = 24
                            viewModel.chartTimeInterval = 24
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        })
                    Text(
                        text = "2 year",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption.intValue = 100
                            viewModel.chartTimeInterval = 100
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        }
                ) {
                    RadioButton(
                        selected = selectedOption.intValue == 100,
                        onClick = {
                            selectedOption.intValue = 100
                            viewModel.chartTimeInterval = 100
                            showDialog.value = false
                            viewModel.chartsUiState.value = ChartsUiState.Default
                        })
                    Text(
                        text = "All time",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

            }
        }
    }
    
}