package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagDelete
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagSave
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Specification
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Vehicle
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomAlertDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import org.koin.androidx.compose.getViewModel


@Composable
fun AddVehicleScreen(
    navigation: INavigationRouter,
    viewModel: AddVehicleViewModel = getViewModel(),
    vehicleId: Long?
) {
    viewModel.vehicleId = vehicleId

    var vehicle: Vehicle by remember {  mutableStateOf(viewModel.vehicle)  }
    var specifications: MutableList<Specification> = remember { viewModel.specifications }
    val showDeleteAlertDialog = remember {  mutableStateOf(false)  }
    var isNotLoading: Boolean by rememberSaveable { mutableStateOf(false) }
    var vehicleNameErrorMessage: String by rememberSaveable { mutableStateOf("") }

    viewModel.addVehicleUiState.value.let {
        when(it) {
            is AddVehicleUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadVehicle()
                }
            }
            is AddVehicleUiState.LoadSpecifications -> {
                LaunchedEffect(it) {
                    viewModel.loadSpecifications()
                }
            }
            is AddVehicleUiState.VehicleLoaded -> {
                vehicle = viewModel.vehicle
                specifications = viewModel.specifications
                isNotLoading = true
            }
            is AddVehicleUiState.VehicleSaved -> {
                LaunchedEffect(it){
                    navigation.returnBack()
                }
            }
            is AddVehicleUiState.VehicleError -> {
                vehicleNameErrorMessage = stringResource(id = it.error)
            }
            is AddVehicleUiState.VehicleRemoved -> {
                LaunchedEffect(it){
                    navigation.returnBack()
                }
            }
        }
    }

    BackArrowScreen(
        topBarText = if (vehicleId != null) stringResource(id = R.string.edit_vehicle)
            else stringResource(id = R.string.new_vehicle),
        content = {
            if(showDeleteAlertDialog.value) {
                CustomAlertDialog(
                    show = showDeleteAlertDialog,
                    text = stringResource(id = R.string.vehicle_delete_alert_text)
                ) {
                    viewModel.deleteVehicle()
                }
            }

            AddVehicleScreenContent(
                viewModel = viewModel,
                vehicle = vehicle,
                vehicleNameErrorMessage = vehicleNameErrorMessage,
                isNotLoaded = isNotLoading,
                specifications = specifications
            )
        },
        actions = {
            if(viewModel.vehicleId != null){
                IconButton(onClick = { showDeleteAlertDialog.value = true }, modifier = Modifier.testTag(TestTagDelete)) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
            IconButton(onClick = { viewModel.saveVehicle() }, modifier = Modifier.testTag(TestTagSave) ) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Save")
            }
        },
        onBackClick = { navigation.returnBack() }
    )
}



@Composable
fun AddVehicleScreenContent(
    vehicle: Vehicle,
    specifications: MutableList<Specification>,
    isNotLoaded: Boolean,
    vehicleNameErrorMessage: String,
    viewModel: AddVehicleViewModel
) {
    val showSpecDialog = remember { mutableStateOf(false) }
    val selectedSpec: MutableState<Specification?> = remember { mutableStateOf(null) }

    if(showSpecDialog.value){
        SpecificationDialog(
            showDialog = showSpecDialog,
            viewModel = viewModel,
            specificationsList = specifications,
            specification = selectedSpec.value
        )
    }

    if(isNotLoaded) {
        val vehicleBitmap = remember { mutableStateOf(viewModel.vehicleImg) }
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri: Uri? ->
            imageUri = uri
        }

        imageUri?.let {
            if(Build.VERSION.SDK_INT < 28) {
                vehicleBitmap.value = MediaStore.Images
                    .Media.getBitmap(appContext.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(appContext.contentResolver, it)
                vehicleBitmap.value = ImageDecoder.decodeBitmap(source)
            }
//            vehicle.image = vehicleBitmap.value
            viewModel.vehicleImg = vehicleBitmap.value
        }

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = MaterialTheme.spacing.medium)
                .clickable {
                    launcher.launch("image/*")
                },
            contentScale = ContentScale.FillWidth,
            bitmap = vehicleBitmap.value!!.asImageBitmap(),
            contentDescription = "")


        // Card 1 - Name
        VehicleNameCard(vehicle = vehicle, vehicleNameErrorMessage = vehicleNameErrorMessage)

        // Card 2 - Fuel
        VehicleFuelCard(vehicle = vehicle)

        // Card 3 - Model
        VehicleModelCard(vehicle = vehicle)

        // Card 4 - Specs
        SpecificationsCard(
            specifications = specifications,
            showSpecDialog = showSpecDialog,
            selectedSpec = selectedSpec
        )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace))

}