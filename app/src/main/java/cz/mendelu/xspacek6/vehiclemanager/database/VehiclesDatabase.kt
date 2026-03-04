package cz.mendelu.xspacek6.vehiclemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.mendelu.xspacek6.vehiclemanager.database.entities.*

@Database(entities = [Vehicle::class, Event::class, Specification::class, Expense::class, Mileage::class, Refill::class, Trip::class], version = 24, exportSchema = false)
@TypeConverters(Converters::class)
abstract class VehiclesDatabase : RoomDatabase() {

    abstract fun vehiclesDao(): VehiclesDao

    companion object {
        private var INSTANCE: VehiclesDatabase? = null

        fun getDatabase(context: Context): VehiclesDatabase {
            if (INSTANCE == null) {
                synchronized(VehiclesDatabase::class.java){
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            VehiclesDatabase::class.java,
                            "vehicles_database"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
    
}