package cz.mendelu.xspacek6.vehiclemanager.di

import android.content.Context
import cz.mendelu.xspacek6.vehiclemanager.datastore.DataStoreRepositoryImpl
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single { provideDataStoreRepository(androidContext()) }
}

fun provideDataStoreRepository(context: Context): IDataStoreRepository
        = DataStoreRepositoryImpl(context)