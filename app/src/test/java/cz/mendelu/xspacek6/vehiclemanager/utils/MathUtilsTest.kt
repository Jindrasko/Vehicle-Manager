package cz.mendelu.xspacek6.vehiclemanager.utils

import cz.mendelu.xspacek6.vehiclemanager.utils.MathUtils.Companion.calculatePercentage
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MathUtilsTest {

    @Test
    fun testCalculatePercentageWithMinValue() {
        val minValue = 0f
        val maxValue = 10f
        val currentValue = 0f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(1f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageWithMaxValue() {
        val minValue = 0f
        val maxValue = 10f
        val currentValue = 10f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageInBetweenValues() {
        val minValue = 0f
        val maxValue = 10f
        val currentValue = 5f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0.5f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageWithNegativeValues() {
        val minValue = -10f
        val maxValue = 10f
        val currentValue = 0f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0.5f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageWithDifferentRange() {
        val minValue = 5f
        val maxValue = 15f
        val currentValue = 10f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0.5f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageWithValue0_3() {
        val minValue = 0f
        val maxValue = 1f
        val currentValue = 0.3f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0.7f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageWithValue0_7() {
        val minValue = 0f
        val maxValue = 1f
        val currentValue = 0.7f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0.3f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageWithValue0_8() {
        val minValue = 0f
        val maxValue = 1f
        val currentValue = 0.8f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0.2f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageWithDecimalValue0_789() {
        val minValue = 0f
        val maxValue = 1f
        val currentValue = 0.789f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0.211f, result, 0.01f)
    }

    @Test
    fun testCalculatePercentageWithLargeNumbers() {
        val minValue = 1000f
        val maxValue = 5000f
        val currentValue = 3000f

        val result = calculatePercentage(minValue, maxValue, currentValue)

        assertEquals(0.5f, result, 0.01f)
    }



}