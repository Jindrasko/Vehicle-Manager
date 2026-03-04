package cz.mendelu.xspacek6.vehiclemanager.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import java.io.Serializable

@Entity(tableName = "expenses")
data class Expense(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "vehicle_id") var vehicleId: Long,
    @ColumnInfo(name = "date") var date: Long
): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "expense_id")
    var expenseId: Long? = null

    @ColumnInfo(name = "mileage")
    var mileage: Int? = null

    @ColumnInfo(name = "expense_category")
    var expenseCategory: ExpenseCategory? = null

    @ColumnInfo(name = "cost_parts")
    var costParts: Double? = null

    @ColumnInfo(name = "cost_services")
    var costServices: Double? = null

    @ColumnInfo(name = "note")
    var note: String? = null

}