package cz.mendelu.xspacek6.vehiclemanager.ui.activities

import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository

class AppIntroViewModel(private val dataStoreRepository: IDataStoreRepository): BaseViewModel() {
    suspend fun setFirstRun(){
        dataStoreRepository.setFirstRun()
    }
}