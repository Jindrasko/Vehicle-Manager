package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.refills

import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RefillsViewModel(private val databaseRepository: ILocalVehiclesRepository, private val dataStoreRepository: IDataStoreRepository) : BaseViewModel() {

    private val _refillsUiState = MutableStateFlow<RefillsUiState<List<Refill>>>(RefillsUiState.Default())
    val refillsUiState: StateFlow<RefillsUiState<List<Refill>>> = _refillsUiState

    var currency = ""
    var vehicleId: Long? = null

    init {
        launch {
            currency = dataStoreRepository.getCurrency()
        }
    }

    fun loadRefills() {
        launch {
            databaseRepository.getRefills(vehicleId!!).collect {
                _refillsUiState.value = RefillsUiState.Loaded(it)
            }
        }
    }


}