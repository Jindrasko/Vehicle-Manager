package cz.mendelu.xspacek6.vehiclemanager.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.xspacek6.vehiclemanager.constants.GasType
import java.io.Serializable

@Entity(tableName = "refills")
data class Refill(
    @ColumnInfo(name = "vehicle_id") var vehicleId: Long,
    @ColumnInfo(name = "date") var date: Long,
): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "refill_id")
    var refillId: Long? = null

    @ColumnInfo(name = "mileage")
    var mileage: Int? = null

    @ColumnInfo(name = "volume")
    var volume: Double? = null

    @ColumnInfo(name = "fuel_type")
    var fuelType: GasType? = null

    @ColumnInfo(name = "fuel_cost")
    var fuelCost: Double? = null

    @ColumnInfo(name = "total_cost")
    var totalCost: Double? = null

    @ColumnInfo(name = "full")
    var full: Boolean = false

    @ColumnInfo(name = "previous_missed")
    var previousMissed: Boolean = false

    @ColumnInfo(name = "note")
    var note: String? = null
}