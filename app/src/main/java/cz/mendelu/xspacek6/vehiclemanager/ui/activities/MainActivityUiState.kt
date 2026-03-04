package cz.mendelu.xspacek6.vehiclemanager.ui.activities

sealed class MainActivityUiState {
    object Default : MainActivityUiState()
    object RunForAFirstTime : MainActivityUiState()
    object ContinueToApp : MainActivityUiState()
}