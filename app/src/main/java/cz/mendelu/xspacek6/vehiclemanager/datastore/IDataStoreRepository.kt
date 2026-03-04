package cz.mendelu.xspacek6.vehiclemanager.datastore

interface IDataStoreRepository {
    suspend fun setFirstRun()
    suspend fun getFirstRun(): Boolean

    suspend fun setCurrency(currency: String)

    suspend fun getCurrency(): String

}