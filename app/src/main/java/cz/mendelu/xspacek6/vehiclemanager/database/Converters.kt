package cz.mendelu.xspacek6.vehiclemanager.database

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.constants.GasType

class Converters {

    @TypeConverter
    fun toLatLngList(value: String?): List<List<LatLng>> {
        if (value.isNullOrEmpty()) { return emptyList() }

        val listType = object : TypeToken<List<List<LatLng>>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromLatLngList(value: List<List<LatLng>>): String {
        return Gson().toJson(value)
    }

//    @TypeConverter
//    fun fromBitmap(bitmap: Bitmap): ByteArray {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
//        return outputStream.toByteArray()
//    }
//    @TypeConverter
//    fun toBitmap(byteArray: ByteArray): Bitmap {
//        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//    }

    @TypeConverter
    fun toGasType(value: Int?) = if(value != null) enumValues<GasType>()[value] else null
    @TypeConverter
    fun fromGasType(value: GasType?) = value?.ordinal

    @TypeConverter
    fun toExpenseCategory(value: Int?) = if (value != null) enumValues<ExpenseCategory>()[value] else null

    @TypeConverter
    fun fromExpenseCategory(value: ExpenseCategory?) = value?.ordinal

}