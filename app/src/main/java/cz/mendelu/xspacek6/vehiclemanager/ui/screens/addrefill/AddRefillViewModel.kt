package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addrefill

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import kotlinx.coroutines.launch

class AddRefillViewModel(private val databaseRepository: ILocalVehiclesRepository) : BaseViewModel() {

    var vehicleId: Long? = null
    var refillId: Long? = null

    val addRefillUiState: MutableState<AddRefillUiState<Refill>> = mutableStateOf(AddRefillUiState.Default)

    var refill: Refill = Refill(
        vehicleId = -1L,
        date = System.currentTimeMillis()
    )

    var lastMileage: Int? = null
    var mileage: Mileage? = null

    fun loadRefill() {
        if (refillId != null) {
            launch {
                refill = databaseRepository.findRefillById(refillId!!)
                mileage = databaseRepository.findMileageByRefillId(refillId!!)
            }.invokeOnCompletion {
                refill.vehicleId = vehicleId!!
                addRefillUiState.value = AddRefillUiState.RefillLoaded(refill)
            }
        } else {
            launch {
                lastMileage = databaseRepository.getLastMileage(vehicleId!!)?.mileage
            }.invokeOnCompletion {
                refill.vehicleId = vehicleId!!
                addRefillUiState.value = AddRefillUiState.RefillLoaded(refill)
            }
        }
    }

    private fun calculatePrices() {
        if (refill.fuelCost != null && refill.volume != null && refill.totalCost == null) {
            refill.totalCost = refill.fuelCost!! * refill.volume!!
        } else if (refill.fuelCost != null && refill.volume == null && refill.totalCost != null){
            refill.volume = refill.totalCost!! / refill.fuelCost!!
        } else if (refill.fuelCost == null && refill.volume != null && refill.totalCost != null) {
            refill.fuelCost = refill.totalCost!! / refill.volume!!
        }
    }


    fun saveRefill() {
        launch {
            calculatePrices()
            if(refillId != null){
                databaseRepository.updateRefill(refill)
            } else {
                refillId = databaseRepository.insertRefill(refill)
            }

            if(refill.mileage != null) {
                if (mileage != null) {
                    mileage!!.mileage = refill.mileage!!
                    mileage!!.date = refill.date
                    databaseRepository.insertMileage(mileage!!)
                } else {
                    databaseRepository.insertMileage(Mileage(mileage = refill.mileage!!, date = refill.date, vehicleId = vehicleId!!, refillId = refillId))
                }
            } else if(mileage != null) {
                databaseRepository.deleteMileage(mileage!!)
            }

            addRefillUiState.value = AddRefillUiState.RefillSaved
        }
    }

    fun deleteRefill() {
        launch {
            if (mileage != null) {
                databaseRepository.deleteMileage(mileage!!)
            }
            databaseRepository.deleteRefill(refill = refill)
            addRefillUiState.value = AddRefillUiState.RefillRemoved
        }
    }

}