package cz.mendelu.xspacek6.vehiclemanager.constants

import androidx.annotation.StringRes
import cz.mendelu.xspacek6.vehiclemanager.R

enum class GasType(@StringRes val title: Int) {
    PETROL(R.string.petrol),
    DIESEL(R.string.diesel),
    LPG(R.string.lpg),
    CNG(R.string.cng),
    ETHANOL(R.string.ethanol)
}