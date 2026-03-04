package cz.mendelu.xspacek6.vehiclemanager.di

import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication
import cz.mendelu.xspacek6.vehiclemanager.database.VehiclesDatabase
import org.koin.dsl.module

val databaseModule = module {
    fun provideDatabase(): VehiclesDatabase = VehiclesDatabase.getDatabase(VehicleManagerApplication.appContext)
    single {
        provideDatabase()
    }
}