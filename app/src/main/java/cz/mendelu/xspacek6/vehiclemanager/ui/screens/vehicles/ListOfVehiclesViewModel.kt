package cz.mendelu.xspacek6.vehiclemanager.ui.screens.vehicles

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Vehicle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class ListOfVehiclesViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    private val _listOfVehiclesUiState = MutableStateFlow<ListOfVehiclesUiState<List<Vehicle>>>(ListOfVehiclesUiState.Default())
    val listOfVehiclesUiState: StateFlow<ListOfVehiclesUiState<List<Vehicle>>> = _listOfVehiclesUiState

    fun loadVehicles() {
        launch {
            databaseRepository.getVehicles().collect {
                _listOfVehiclesUiState.value = ListOfVehiclesUiState.Loaded(it)
            }
        }
    }

    fun getImageBitmap(fullPath: String, fileName: String): Bitmap {
        try {
            val f = File(fullPath, fileName)
            return BitmapFactory.decodeStream(FileInputStream(f))
//            val img = findViewById(R.id.imgPicker) as ImageView
//            img.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return BitmapFactory.decodeResource(VehicleManagerApplication.appContext.resources, R.drawable.car_icon)
    }

}