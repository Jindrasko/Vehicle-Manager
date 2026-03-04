package cz.mendelu.xspacek6.vehiclemanager.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

@Entity(tableName = "trips")
data class Trip(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "vehicle_id") var vehicleId: Long
): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trip_id")
    var tripId: Long? = null

    @ColumnInfo(name = "path_points")
    var pathPoints: List<List<LatLng>>? = null

    @ColumnInfo(name = "average_speed")
    var averageSpeed: Float? = null

    @ColumnInfo(name = "distance")
    var distance: Int? = null

    @ColumnInfo(name = "time_driven")
    var timeDriven: Long? = null

    @ColumnInfo(name = "start_location")
    var startLocation: String? = null

    @ColumnInfo(name = "finish_location")
    var finishLocation: String? = null

    @ColumnInfo(name = "note")
    var note: String? = null

}





/*
@Entity(tableName = "trips")
data class Trip(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "vehicle_id") var vehicleId: Long
): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "trip_id")
    var tripId: Long? = null

    @ColumnInfo(name = "distance")
    var distance: Double? = null

    @ColumnInfo(name = "start_time")
    var startTime: Long? = null

    @ColumnInfo(name = "end_time")
    var endTime: Long? = null

    @ColumnInfo(name = "start_location")
    var startLocation: String? = null

    @ColumnInfo(name = "finish_location")
    var finishLocation: String? = null
}

 */