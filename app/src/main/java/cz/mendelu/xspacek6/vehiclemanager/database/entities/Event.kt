package cz.mendelu.xspacek6.vehiclemanager.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import java.io.Serializable

@Entity(tableName = "events")
data class Event(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "vehicle_id") var vehicleId: Long,
    @ColumnInfo(name = "initial_date") var initialDate: Long,
    @ColumnInfo(name = "initial_km") var initialKm: Int
): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    var eventId: Long? = null

    @ColumnInfo(name = "note")
    var note: String? = null

    @ColumnInfo(name = "done")
    var done: Boolean = false

    @ColumnInfo(name = "once_date")
    var onceDate: Long? = null

    @ColumnInfo(name = "every_months")
    var everyMonths: Int? = null

    @ColumnInfo(name = "final_date")
    var finalDate: Long? = null

    @ColumnInfo(name = "once_at_km")
    var onceAtKm: Int? = null

    @ColumnInfo(name = "every_km")
    var everyKm: Int? = null

    @ColumnInfo(name = "final_km")
    var finalKm: Int? = null

    @ColumnInfo(name = "event_category")
    var eventCategory: ExpenseCategory? = null

}