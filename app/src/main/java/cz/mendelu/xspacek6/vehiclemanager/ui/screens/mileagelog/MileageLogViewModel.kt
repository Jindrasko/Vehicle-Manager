package cz.mendelu.xspacek6.vehiclemanager.ui.screens.mileagelog

import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MileageLogViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    private val _mileageLogUiState = MutableStateFlow<MileageLogUiState<List<Mileage>>>(MileageLogUiState.Default)
    val mileageLogUiState: StateFlow<MileageLogUiState<List<Mileage>>> = _mileageLogUiState

    var vehicleId: Long? = null

    fun loadMileages() {
        launch {
            databaseRepository.getMileages(vehicleId!!).collect {
                _mileageLogUiState.value = MileageLogUiState.Loaded(it)
            }
        }
    }

    fun deleteMileage(mileage: Mileage){
        launch {
            databaseRepository.deleteMileage(mileage = mileage)
        }
    }

    fun saveMileage(mileage: Mileage){
        launch {
            databaseRepository.insertMileage(mileage = mileage)
        }
    }

}