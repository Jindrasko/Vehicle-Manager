package cz.mendelu.xspacek6.vehiclemanager.ui.screens.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository
import kotlinx.coroutines.launch

data class SettingsData(
    var currency: String
)

class SettingsViewModel(private val dataStoreRepository: IDataStoreRepository) : BaseViewModel() {

    val settingsUiState: MutableState<SettingsUiState<SettingsData>> = mutableStateOf(SettingsUiState.Default)

    val settingsData = SettingsData(currency = "")

    fun loadSettingsData() {
        launch {
            settingsData.currency = dataStoreRepository.getCurrency()
        }.invokeOnCompletion {
            settingsUiState.value = SettingsUiState.SettingsLoaded(settingsData)
        }
    }

    fun saveSettingsData() {
        launch {
            dataStoreRepository.setCurrency(settingsData.currency)
        }.invokeOnCompletion {
            settingsUiState.value = SettingsUiState.SettingsSaved
        }
    }


}