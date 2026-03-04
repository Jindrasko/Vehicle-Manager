package cz.mendelu.xspacek6.vehiclemanager

//import org.koin.android.BuildConfig
import android.app.Application
import android.content.Context
import cz.mendelu.xspacek6.vehiclemanager.di.daoModule
import cz.mendelu.xspacek6.vehiclemanager.di.dataStoreModule
import cz.mendelu.xspacek6.vehiclemanager.di.databaseModule
import cz.mendelu.xspacek6.vehiclemanager.di.repositoryModule
import cz.mendelu.xspacek6.vehiclemanager.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class VehicleManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@VehicleManagerApplication)
            modules(listOf(
                databaseModule,
                daoModule,
                repositoryModule,
                viewModelModule,
                dataStoreModule
            ))
        }
    }

    companion object {
        lateinit var appContext: Context
    }


}