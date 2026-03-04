package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagAddEvent
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagAddExpense
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagAddOdo
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagAddRefill
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagAddTrip
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagDrive
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomIconWithTextButton
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetBlue
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetGreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetLightBlue
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetOrange
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetPurple
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.SheetRed
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenBottomSheet(
    sheetState: SheetState,
    coroutineScope: CoroutineScope,
    showOdoDialog: MutableState<Boolean>,
    vehicleId: Long,
    navigation: INavigationRouter
) {

    if(sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                }
            }
        ) {

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CustomIconWithTextButton(
                            title = stringResource(id = R.string.odometer),
                            icon = R.drawable.speed_24,
                            bgColor = SheetPurple,
                            modifier = Modifier.testTag(TestTagAddOdo)
                        ) {
                            showOdoDialog.value = true
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }

                        CustomIconWithTextButton(
                            title = stringResource(id = R.string.drive),
                            icon = R.drawable.add_road_24,
                            bgColor = SheetGreen,
                            modifier = Modifier.testTag(TestTagDrive)
                        ) {
                            navigation.navigateToDrive(vehicleId)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CustomIconWithTextButton(
                            title = stringResource(id = R.string.refuel),
                            icon = R.drawable.local_gas_station_24,
                            bgColor = SheetOrange,
                            modifier = Modifier.testTag(TestTagAddRefill)
                        ) {
                            navigation.navigateToAddRefill(vehicleId = vehicleId, refillId = -1L)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }

                        CustomIconWithTextButton(
                            title = stringResource(id = R.string.trip),
                            icon = R.drawable.route_24,
                            bgColor = SheetBlue,
                            modifier = Modifier.testTag(TestTagAddTrip)
                        ) {
                            navigation.navigateToAddTrip(vehicleId, -1L)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CustomIconWithTextButton(
                            title = stringResource(id = R.string.expense),
                            icon = R.drawable.receipt_24,
                            bgColor = SheetRed,
                            modifier = Modifier.testTag(TestTagAddExpense)
                        ) {
                            navigation.navigateToAddExpense(vehicleId = vehicleId, expenseId = -1L)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }

                        CustomIconWithTextButton(
                            title = stringResource(id = R.string.event),
                            icon = R.drawable.event_note_24,
                            bgColor = SheetLightBlue,
                            modifier = Modifier.testTag(TestTagAddEvent)
                        ) {
                            navigation.navigateToAddEvent(vehicleId = vehicleId, eventId = -1L)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }
                    }

                }
                
                Spacer(modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium))

            }
        }
    }
}