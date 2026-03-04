package cz.mendelu.xspacek6.vehiclemanager.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "specifications")
data class Specification(
    @ColumnInfo(name = "title") var specTitle: String,
    @ColumnInfo(name = "value") var specValue: String,
    @ColumnInfo(name = "vehicle_id") var vehicleId: Long
): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "specification_id")
    var specificationsId: Long? = null

}