package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addrefill

sealed class AddRefillUiState<out T> {
    object Default : AddRefillUiState<Nothing>()
    class RefillLoaded<T>(var data: T) : AddRefillUiState<T>()
    object RefillSaved : AddRefillUiState<Nothing>()
    object RefillError : AddRefillUiState<Nothing>()
    object RefillRemoved : AddRefillUiState<Nothing>()
}
