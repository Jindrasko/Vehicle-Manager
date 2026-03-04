package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen

sealed class HomeScreenUiState {

    object Default : HomeScreenUiState()
    object LastMileageLoaded : HomeScreenUiState()

}
