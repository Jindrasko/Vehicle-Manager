package cz.mendelu.xspacek6.vehiclemanager.ui.screens.settings

sealed class SettingsUiState<out T> {
    object Default : SettingsUiState<Nothing>()
    class SettingsLoaded<T>(var data: T) : SettingsUiState<T>()
    object SettingsSaved : SettingsUiState<Nothing>()
}