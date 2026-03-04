package cz.mendelu.xspacek6.vehiclemanager.di

import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.LocalVehiclesRepositoryImpl
import cz.mendelu.xspacek6.vehiclemanager.database.VehiclesDao
import org.koin.dsl.module

val repositoryModule = module {
    fun provideLocalVehicleRepository(dao: VehiclesDao): ILocalVehiclesRepository {
        return LocalVehiclesRepositoryImpl(dao)
    }
    single {
        provideLocalVehicleRepository(get())
    }
}