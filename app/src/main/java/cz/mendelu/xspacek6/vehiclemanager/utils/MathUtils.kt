package cz.mendelu.xspacek6.vehiclemanager.utils

class MathUtils {
    companion object{

        fun calculatePercentage(minValue: Float, maxValue: Float, currentValue: Float): Float {
            return (1 - (currentValue - minValue) / (maxValue - minValue))
        }

    }
}