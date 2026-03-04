package cz.mendelu.xspacek6.vehiclemanager.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "mileage")
data class Mileage(
    @ColumnInfo(name = "mileage") var mileage: Int,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "vehicle_id") var vehicleId: Long,
    @ColumnInfo(name = "refill_id") var refillId: Long? = null,
    @ColumnInfo(name = "expense_id") var expenseId: Long? = null,
    @ColumnInfo(name = "event_id") var eventId: Long? = null
): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "mileage_id")
    var mileageId: Long? = null

}