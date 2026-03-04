package cz.mendelu.xspacek6.vehiclemanager.ui.screens.mileagelog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomAlertDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.MileageDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import org.koin.androidx.compose.getViewModel
import java.util.Date


@Composable
fun MileageLogScreen(
    navigation: INavigationRouter,
    vehicleId: Long,
    viewModel: MileageLogViewModel = getViewModel()
) {

    viewModel.vehicleId = vehicleId

    val screenState: MutableState<ScreenState<List<Mileage>>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    val showMileageDialog = rememberSaveable { mutableStateOf(false) }
    val editMileage: MutableState<Mileage?> = remember { mutableStateOf(null) }

    val mileageLogUiState: MileageLogUiState<List<Mileage>>? by viewModel.mileageLogUiState.collectAsState()

    mileageLogUiState?.let {
        when(it){
            is MileageLogUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadMileages()
                }
            }
            is MileageLogUiState.Loaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is MileageLogUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
        }
    }



    BackArrowScreen(
        topBarText = stringResource(id = R.string.mileage_log),
        content = {
            MileageLogScreenContent(
                screenState = screenState.value,
                showMileageDialog = showMileageDialog,
                editMileage = editMileage,
                vehicleId = vehicleId,
                viewModel = viewModel
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editMileage.value = null
                    showMileageDialog.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "plus"
                )
            }
        },
        actions = {

        },
        onBackClick = { navigation.returnBack() }
    )

}


@Composable
fun MileageLogScreenContent(
    screenState: ScreenState<List<Mileage>>,
    showMileageDialog: MutableState<Boolean>,
    editMileage: MutableState<Mileage?>,
    vehicleId: Long,
    viewModel: MileageLogViewModel
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> MileageLog(
                mileages = it.data,
                showMileageDialog = showMileageDialog,
                editMileage = editMileage,
                vehicleId = vehicleId,
                viewModel = viewModel
            )
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}


@Composable
fun MileageLog(
    mileages: List<Mileage>,
    showMileageDialog: MutableState<Boolean>,
    editMileage: MutableState<Mileage?>,
    vehicleId: Long,
    viewModel: MileageLogViewModel
) {
    val showDeleteAlertDialog = remember {  mutableStateOf(false)  }
    val deletedMileage = remember { mutableStateOf(Mileage(0, Date().time,-1L)) }

    if (showMileageDialog.value){
        MileageDialog(
            show = showMileageDialog,
            lastMileage = if(mileages.isEmpty()) "0" else mileages[0].mileage.toString(),
            mileage = editMileage.value,
            vehicleId = vehicleId
        ) {
            viewModel.saveMileage(it)
        }
    }

    if(showDeleteAlertDialog.value) {
        CustomAlertDialog(
            show = showDeleteAlertDialog,
            text = stringResource(id = R.string.mileage_delete_alert_text)
        ) {
            viewModel.deleteMileage(deletedMileage.value)
        }
    }

    mileages.forEach {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    editMileage.value = it
                    showMileageDialog.value = true
                },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                Text(text = DateUtils.getDateString(it.date) + ": " + it.mileage)

                IconButton(onClick = {
                    deletedMileage.value = it
                    showDeleteAlertDialog.value = true
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete))
                }
            }
            Divider(modifier = Modifier.fillMaxWidth())
        }
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace))

}