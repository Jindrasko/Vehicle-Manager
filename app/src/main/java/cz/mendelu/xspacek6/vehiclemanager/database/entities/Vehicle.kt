package cz.mendelu.xspacek6.vehiclemanager.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.xspacek6.vehiclemanager.constants.GasType
import java.io.Serializable

@Entity(tableName = "vehicles")
data class Vehicle(
    @ColumnInfo(name = "name") var name: String
): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "vehicle_id")
    var vehicleId: Long? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "manufacturer")
    var manufacturer: String? = null

    @ColumnInfo(name = "model")
    var model: String? = null

    @ColumnInfo(name = "licence_plate")
    var licencePlate: String? = null

    @ColumnInfo(name = "vin")
    var vin: String? = null

    @ColumnInfo(name = "color")
    var color: String? = null

    @ColumnInfo(name = "year_of_manufacture")
    var yearOfManufacture: Short? = null

    @ColumnInfo(name = "date_of_purchase")
    var dateOfPurchase: Long? = null

    @ColumnInfo(name = "fuel_type")
    var fuelType: GasType? = null

    @ColumnInfo(name = "fuel_tank_volume")
    var fuelTankVolume: Int? = null

    @ColumnInfo(name = "imgPath")
    var imagePath: String? = null

}