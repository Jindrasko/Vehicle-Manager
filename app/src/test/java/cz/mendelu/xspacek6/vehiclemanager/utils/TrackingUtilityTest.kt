package cz.mendelu.xspacek6.vehiclemanager.utils

import cz.mendelu.xspacek6.vehiclemanager.utils.TrackingUtility.getFormattedStopWatchTime
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TrackingUtilityTest {

    @Test
    fun testGetFormattedStopWatchTimeWithoutMillis() {
        val result = getFormattedStopWatchTime(3661000, includeMillis = false)
        assertEquals("01:01:01", result)
    }

    @Test
    fun testGetFormattedStopWatchTimeWithMillis() {
        val result = getFormattedStopWatchTime(3661123, includeMillis = true)
        assertEquals("01:01:01:12", result)
    }

    @Test
    fun testGetFormattedStopWatchTimeWithLeadingZeros() {
        val result = getFormattedStopWatchTime(61000, includeMillis = false)
        assertEquals("00:01:01", result)
    }

    @Test
    fun testGetFormattedStopWatchTimeWithLeadingZerosAndMillis() {
        val result = getFormattedStopWatchTime(6123, includeMillis = true)
        assertEquals("00:00:06:12", result)
    }

    @Test
    fun testGetFormattedStopWatchTimeWithSingleDigitValues() {
        val result = getFormattedStopWatchTime(3600123, includeMillis = true)
        assertEquals("01:00:00:12", result)
    }

    @Test
    fun testGetFormattedStopWatchTimeWithZero() {
        val result = getFormattedStopWatchTime(0, includeMillis = true)
        assertEquals("00:00:00:00", result)
    }

}