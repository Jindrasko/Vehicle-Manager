package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Specification
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Vehicle
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class AddVehicleViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    val addVehicleUiState: MutableState<AddVehicleUiState> = mutableStateOf(AddVehicleUiState.Default)

    var vehicleId: Long? = null

    var vehicleImg: Bitmap? = null
    var vehicle: Vehicle = Vehicle("")
    var specifications: MutableList<Specification> = mutableListOf()

    fun loadVehicle(){
        if(vehicleId != null) {

            launch {
                vehicle = databaseRepository.findVehicleById(vehicleId = vehicleId!!)

            }.invokeOnCompletion {
                vehicleImg = loadImageFromStorage(vehicle.imagePath!!)
                addVehicleUiState.value = AddVehicleUiState.LoadSpecifications
            }
        } else {
//            vehicle.image = BitmapFactory.decodeResource(appContext.resources, R.drawable.car_icon)
            vehicleImg = BitmapFactory.decodeResource(appContext.resources, R.drawable.car_icon)
            addVehicleUiState.value = AddVehicleUiState.VehicleLoaded
        }
    }

    fun loadSpecifications(){
        if(vehicleId != null) {
            launch {
                specifications = databaseRepository.getSpecifications(vehicleId = vehicleId!!).toMutableList()

            }.invokeOnCompletion {
                addVehicleUiState.value = AddVehicleUiState.VehicleLoaded
            }
        } else {
            addVehicleUiState.value = AddVehicleUiState.VehicleLoaded
        }
    }

    fun saveVehicle(){
        if(vehicle.name.isEmpty()) {
            addVehicleUiState.value = AddVehicleUiState.VehicleError(R.string.required_field)
        } else {
            if (vehicleImg!= null) {
                vehicle.imagePath = saveToInternalStorage(vehicleImg!!)
            }
            launch {
                if (vehicleId != null) {
                    databaseRepository.updateVehicle(vehicle)
                } else {
                    vehicleId = databaseRepository.insertVehicle(vehicle)
                }
                specifications.forEach {
                    if (it.vehicleId == -1L) {
                        it.vehicleId = vehicleId!!
                    }
                    saveSpecification(it)
                }


                addVehicleUiState.value = AddVehicleUiState.VehicleSaved
            }
        }
    }

    fun deleteVehicle(){
        launch {
            databaseRepository.deleteVehicle(vehicle)
            specifications.forEach {
                deleteSpecification(it)
            }
            addVehicleUiState.value = AddVehicleUiState.VehicleRemoved
        }
    }

    fun saveSpecification(specification: Specification){
        launch {
            databaseRepository.insertSpecification(specification)
            specifications.add(specification)
        }
    }


    fun deleteSpecification(specification: Specification){
        launch {
            databaseRepository.deleteSpecification(specification)
        }
    }


    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(appContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, vehicle.name + ".jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }


    private fun loadImageFromStorage(path: String): Bitmap? {
        try {
            val f = File(path, vehicle.name + ".jpg")
            return BitmapFactory.decodeStream(FileInputStream(f))
//            val img = findViewById(R.id.imgPicker) as ImageView
//            img.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }


}