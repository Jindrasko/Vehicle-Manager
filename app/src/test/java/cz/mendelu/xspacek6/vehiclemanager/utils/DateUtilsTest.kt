package cz.mendelu.xspacek6.vehiclemanager.utils

import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils.Companion.addMonthsToDate
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils.Companion.calculatePercentageOfPastDays
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils.Companion.getDaysBetween
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Calendar

class DateUtilsTest {


    private fun createDate(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.timeInMillis
    }


    // addMonthsToDate

    @Test
    fun testAddMonthsToDatePositive() {
        val initialDate = createDate(2023, Calendar.JANUARY, 15)
        val result = addMonthsToDate(initialDate, 3)
        val expectedDate = createDate(2023, Calendar.APRIL, 15)
        assertEquals(expectedDate/10*10, result/10*10)
    }

    @Test
    fun testAddMonthsToDateNegative() {
        val initialDate = createDate(2023, Calendar.APRIL, 15)
        val result = addMonthsToDate(initialDate, -3)
        val expectedDate = createDate(2023, Calendar.JANUARY, 15)
        assertEquals(expectedDate/10*10, result/10*10)
    }

    @Test
    fun testAddMonthsToDateZero() {
        val initialDate = createDate(2022, Calendar.NOVEMBER, 5)
        val result = addMonthsToDate(initialDate, 0)
        assertEquals(initialDate, result)
    }

    @Test
    fun testAddMonthsToDateAcrossYears() {
        val initialDate = createDate(2022, Calendar.DECEMBER, 25)
        val result = addMonthsToDate(initialDate, 2)
        val expectedDate = createDate(2023, Calendar.FEBRUARY, 25)
        assertEquals(expectedDate, result)
    }




    // getDaysBetween
    @Test
    fun test_getDaysBetween_sameDates_shouldReturnZero() {
        val initialDate = createDate(2022, Calendar.DECEMBER, 1)
        val result = getDaysBetween(initialDate, initialDate)
        assertEquals(0, result)
    }

    @Test
    fun test_getDaysBetween_oneDayDifference_shouldReturnOne() {
        val initialDate = createDate(2022, Calendar.DECEMBER, 1)
        val finalDate = createDate(2022, Calendar.DECEMBER, 2)
        val result = getDaysBetween(initialDate, finalDate)
        assertEquals(1, result)
    }

    @Test
    fun test_getDaysBetween_negativeDifference_shouldReturnNegative() {
        val initialDate = createDate(2022, Calendar.DECEMBER, 10)
        val finalDate = createDate(2022, Calendar.DECEMBER, 9)
        val result = getDaysBetween(initialDate, finalDate)
        assertEquals(-1, result)
    }

    @Test
    fun test_getDaysBetween_datesInDifferentYears_shouldReturnCorrectDays() {
        val initialDate = createDate(2022, Calendar.DECEMBER, 1)
        val finalDate = createDate(2023, Calendar.DECEMBER, 1)
        val result = getDaysBetween(initialDate, finalDate)
        assertEquals(365, result)
    }

    @Test
    fun test_getDaysBetween_leapYear_shouldReturnCorrectDays() {
        val initialDate = createDate(2024, Calendar.FEBRUARY, 28)
        val finalDate = createDate(2024, Calendar.MARCH, 1)
        val result = getDaysBetween(initialDate, finalDate)
        assertEquals(2, result)
    }



    // calculatePercentageOfPastDays

    @Test
    fun testPercentageForCurrentTimeWithinInterval() {
        val currentDate = createDate(2022, Calendar.DECEMBER, 25)
        val initialDate = createDate(2022, Calendar.DECEMBER, 24)
        val finalDate = createDate(2022, Calendar.DECEMBER, 26)

        val percentage = calculatePercentageOfPastDays(initialDate, finalDate, currentDate)

        assertEquals(0.5f, percentage,0.001f)
    }

    @Test
    fun testPercentageForLongDecimalResult() {
        val currentDate = createDate(2022, Calendar.DECEMBER, 20)
        println(currentDate)
        val initialDate = createDate(2022, Calendar.DECEMBER, 18)
        val finalDate = createDate(2022, Calendar.DECEMBER, 24)


        val percentage = calculatePercentageOfPastDays(initialDate, finalDate, currentDate)

        assertEquals(0.666f, percentage, 0.001f) // 2/3 of the way through the interval
    }

    @Test
    fun testPercentageForCurrentTimeBeforeInterval() {
        val currentDate = createDate(2022, Calendar.DECEMBER, 20)
        val initialDate = createDate(2022, Calendar.DECEMBER, 22)
        val finalDate = createDate(2022, Calendar.DECEMBER, 25)

        val percentage = calculatePercentageOfPastDays(initialDate, finalDate, currentDate)

        assertEquals(1.0f, percentage)
    }

    @Test
    fun testPercentageForCurrentTimeAfterInterval() {
        val currentDate = createDate(2022, Calendar.DECEMBER, 20)
        val initialDate = createDate(2022, Calendar.DECEMBER, 18)
        val finalDate = createDate(2022, Calendar.DECEMBER, 19)

        val percentage = calculatePercentageOfPastDays(initialDate, finalDate, currentDate)

        assertEquals(0.0f, percentage) // Current time is after the interval
    }

    @Test
    fun testPercentageForSameInitialAndFinalDate() {
        val currentDate = createDate(2022, Calendar.DECEMBER, 20)
        val percentage = calculatePercentageOfPastDays(currentDate, currentDate, currentDate)

        assertEquals(0.0f, percentage) // Same initial and final date, should be 0%
    }

    @Test
    fun testPercentageForNegativeInterval() {
        val currentDate = createDate(2022, Calendar.DECEMBER, 20)
        val initialDate = createDate(2022, Calendar.DECEMBER, 21)
        val finalDate = createDate(2022, Calendar.DECEMBER, 19)

        val percentage = calculatePercentageOfPastDays(initialDate, finalDate, currentDate)

        assertEquals(0.0f, percentage)
    }



}