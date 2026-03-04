package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle

sealed class AddVehicleUiState {

    object Default : AddVehicleUiState()
    object LoadSpecifications : AddVehicleUiState()
    object VehicleLoaded : AddVehicleUiState()
    object VehicleSaved : AddVehicleUiState()
    class VehicleError(val error: Int) : AddVehicleUiState()
    object VehicleRemoved : AddVehicleUiState()

}