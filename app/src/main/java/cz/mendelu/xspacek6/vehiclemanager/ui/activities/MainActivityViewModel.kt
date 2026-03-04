package cz.mendelu.xspacek6.vehiclemanager.ui.activities

import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val dataStoreRepository: IDataStoreRepository) : BaseViewModel() {

    private val _mainActivityScreenState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Default)
    val mainActivityScreenState: StateFlow<MainActivityUiState> = _mainActivityScreenState

    fun checkAppState(){
        launch {
            if (dataStoreRepository.getFirstRun()){
                _mainActivityScreenState.value = MainActivityUiState.RunForAFirstTime
            } else {
                _mainActivityScreenState.value = MainActivityUiState.ContinueToApp
            }

        }
    }

    fun setToContinue(){
        _mainActivityScreenState.value = MainActivityUiState.ContinueToApp
    }



}