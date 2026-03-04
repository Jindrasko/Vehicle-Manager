package cz.mendelu.xspacek6.vehiclemanager.constants

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.*

enum class ExpenseCategory(@StringRes val title: Int,@DrawableRes val icon: Int, val color: Color) {
    MAINTENANCE(R.string.maintenance,R.drawable.construction_24, MaintenanceColor),
    REPAIR(R.string.repair, R.drawable.car_repair_24, RepairColor),
    INSURANCE(R.string.insurance, R.drawable.request_page_24, InsuranceColor),
    TUNING(R.string.tuning, R.drawable.publish_24, TuningColor),
    CLEANING(R.string.cleaning, R.drawable.local_car_wash_24, CleaningColor),
    TOLL(R.string.toll, R.drawable.attach_money_24, TollColor),

}