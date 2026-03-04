package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    val homeScreenUiState: MutableState<HomeScreenUiState> = mutableStateOf(HomeScreenUiState.Default)

    var vehicleId: Long? = null


    var lastMileage: Int = 0

    fun loadLastMileage() {
        launch {
            lastMileage = databaseRepository.getLastMileage(vehicleId!!)?.mileage ?: 0
        }.invokeOnCompletion {
            homeScreenUiState.value = HomeScreenUiState.LastMileageLoaded
        }
    }

    fun saveMileage(mileage: Mileage){
        launch {
            databaseRepository.insertMileage(mileage)
        }
    }

}
