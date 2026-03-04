package cz.mendelu.xspacek6.vehiclemanager.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {

    override suspend fun setFirstRun() {
        val preferencesKey = booleanPreferencesKey(DataStoreConstants.FIRST_RUN)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = false
        }
    }

    override suspend fun getFirstRun(): Boolean {
        return try {
            val preferencesKey = booleanPreferencesKey(DataStoreConstants.FIRST_RUN)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey))
                true
            else
                preferences[preferencesKey]!!
        }catch (e: Exception){
            e.printStackTrace()
            true
        }
    }

    override suspend fun setCurrency(currency: String) {
        val preferencesKey = stringPreferencesKey(DataStoreConstants.CURRENCY)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = currency
        }
    }

    override suspend fun getCurrency(): String {
        return try {
            val preferencesKey = stringPreferencesKey(DataStoreConstants.CURRENCY)
            val preferences = context.dataStore.data.first()
            if (!preferences.contains(preferencesKey))
                "€"
            else
                preferences[preferencesKey]!!
        }catch (e: Exception){
            e.printStackTrace()
            "€"
        }
    }

}