package cz.mendelu.xspacek6.vehiclemanager.di

import cz.mendelu.xspacek6.vehiclemanager.database.VehiclesDao
import cz.mendelu.xspacek6.vehiclemanager.database.VehiclesDatabase
import org.koin.dsl.module

val daoModule = module {
    fun provideVehiclesDao(database: VehiclesDatabase): VehiclesDao = database.vehiclesDao()
    single {
        provideVehiclesDao(get())
    }
}