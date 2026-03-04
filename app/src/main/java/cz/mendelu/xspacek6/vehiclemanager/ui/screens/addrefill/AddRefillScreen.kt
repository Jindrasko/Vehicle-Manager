package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addrefill

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagDelete
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagSave
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomAlertDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import org.koin.androidx.compose.getViewModel

@Composable
fun AddRefillScreen(
    navigation: INavigationRouter,
    viewModel: AddRefillViewModel = getViewModel(),
    vehicleId: Long,
    refillId: Long?
) {
    viewModel.vehicleId = vehicleId
    viewModel.refillId = refillId

    val showDeleteAlertDialog = remember {  mutableStateOf(false)  }

    val screenState: MutableState<ScreenState<Refill>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }


    
    viewModel.addRefillUiState.value.let {
        when(it){
            is AddRefillUiState.Default -> {
                LaunchedEffect(it){
                    viewModel.loadRefill()
                }
            }
            is AddRefillUiState.RefillLoaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is AddRefillUiState.RefillSaved -> {
                LaunchedEffect(it){
                    navigation.returnBack()
                }
            }
            is AddRefillUiState.RefillRemoved -> {
                LaunchedEffect(it){
                    navigation.returnBack()
                }
            }
            is AddRefillUiState.RefillError -> {

            }
        }
    }

    BackArrowScreen(
        topBarText = if (refillId != null) stringResource(id = R.string.edit_refill)
        else stringResource(id = R.string.new_refill),
        content = {
            if(showDeleteAlertDialog.value) {
                CustomAlertDialog(
                    show = showDeleteAlertDialog,
                    text = stringResource(id = R.string.refill_delete_alert_text)
                ) {
                    viewModel.deleteRefill()
                }
            }
            
            AddRefillScreenContent(screenState = screenState.value, viewModel = viewModel)
            
        },
        actions = {
            if(viewModel.vehicleId != null){
                IconButton(onClick = {  showDeleteAlertDialog.value = true  }, modifier = Modifier.testTag(TestTagDelete)) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
            IconButton(onClick = { viewModel.saveRefill() }, modifier = Modifier.testTag(TestTagSave)) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Save")
            }
        },
        onBackClick = { navigation.returnBack() }
    )
}



@Composable
fun AddRefillScreenContent(
    screenState: ScreenState<Refill>,
    viewModel: AddRefillViewModel
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> AddRefill(refill = it.data, viewModel = viewModel)
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}



@Composable
fun AddRefill(
    refill: Refill,
    viewModel: AddRefillViewModel
) {

    RefillDateCard(refill = refill, lastMileage = viewModel.lastMileage)
    RefillFuelCard(refill = refill)
    RefillNoteCard(refill = refill)

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace))

}