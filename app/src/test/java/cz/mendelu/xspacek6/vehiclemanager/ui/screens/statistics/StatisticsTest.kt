package cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics

import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.calculateAverageFuelConsumption
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.getAverageGasPrice
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.getBestGasPrice
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.getExpenseCostByCategory
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.getExpensesCost
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.getFuelCost
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.getTotalDistance
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.getTotalGas
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics.Companion.getWorstGasPrice
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StatisticsTest {

    // getFuelCost

    @Test
    fun testGetFuelCostWithEmptyList() {
        val refillList = emptyList<Refill>()
        val result = getFuelCost(refillList)
        assertEquals(0.0, result, 0.01)
    }

    @Test
    fun testGetFuelCostWithSingleRefill() {
        val refillList = listOf(
            createRefillWithTotalCost(vehicleId = 1, date = 1637164800000, totalCost = 70.0)
        )
        val result = getFuelCost(refillList)
        assertEquals(70.0, result, 0.01)
    }

    @Test
    fun testGetFuelCostWithMultipleRefills() {
        val refillList = listOf(
            createRefillWithTotalCost(vehicleId = 1, date = 1637164800000, totalCost = 70.0),
            createRefillWithTotalCost(vehicleId = 1, date = 1637251200000, totalCost = 65.0),
            createRefillWithTotalCost(vehicleId = 2, date = 1637337600000, totalCost = 80.0)
        )
        val result = getFuelCost(refillList)
        assertEquals(215.0, result, 0.01)
    }

    @Test
    fun testGetFuelCostWithNullTotalCost() {
        val refillList = listOf(
            createRefillWithTotalCost(vehicleId = 1, date = 1637164800000, totalCost = null),
            createRefillWithTotalCost(vehicleId = 1, date = 1637251200000, totalCost = 65.0),
            createRefillWithTotalCost(vehicleId = 2, date = 1637337600000, totalCost = 80.0)
        )
        val result = getFuelCost(refillList)
        assertEquals(145.0, result, 0.01)
    }



    // getTotalGas

    @Test
    fun testGetTotalGasWithEmptyList() {
        val refillList = emptyList<Refill>()
        val result = getTotalGas(refillList)
        assertEquals(0.0, result, 0.01)
    }

    @Test
    fun testGetTotalGasWithSingleRefill() {
        val refillList = listOf(
            createRefillWithTotalCost(vehicleId = 1, date = 1637164800000, totalCost = 70.0)
        )
        val result = getTotalGas(refillList)
        assertEquals(0.0, result, 0.01) // Total gas should be 0 as volume is not provided in this test
    }

    @Test
    fun testGetTotalGasWithMultipleRefills() {
        val refillList = listOf(
            createRefillWithVolume(vehicleId = 1, date = 1637164800000, volume = 40.0),
            createRefillWithVolume(vehicleId = 1, date = 1637251200000, volume = 35.0),
            createRefillWithVolume(vehicleId = 2, date = 1637337600000, volume = 45.0)
        )
        val result = getTotalGas(refillList)
        assertEquals(120.0, result, 0.01)
    }

    @Test
    fun testGetTotalGasWithNullVolume() {
        val refillList = listOf(
            createRefillWithTotalCost(vehicleId = 1, date = 1637164800000, totalCost = 70.0),
            createRefillWithVolume(vehicleId = 1, date = 1637251200000, volume = null),
            createRefillWithVolume(vehicleId = 2, date = 1637337600000, volume = 45.0)
        )
        val result = getTotalGas(refillList)
        assertEquals(45.0, result, 0.01)
    }


    // getBestGasPrice

    @Test
    fun testGetBestGasPriceWithEmptyList() {
        val refillList = emptyList<Refill>()
        val result = getBestGasPrice(refillList)
        assertEquals(0.0, result, 0.01)
    }

    @Test
    fun testGetBestGasPriceWithSingleRefill() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = 2.5)
        )
        val result = getBestGasPrice(refillList)
        assertEquals(2.5, result, 0.01)
    }

    @Test
    fun testGetBestGasPriceWithMultipleRefills() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = 2.5),
            createRefillWithFuelCost(vehicleId = 1, date = 1637251200000, fuelCost = 2.3),
            createRefillWithFuelCost(vehicleId = 2, date = 1637337600000, fuelCost = 2.8)
        )
        val result = getBestGasPrice(refillList)
        assertEquals(2.3, result, 0.01)
    }

    @Test
    fun testGetBestGasPriceWithNullFuelCost() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = null),
            createRefillWithFuelCost(vehicleId = 1, date = 1637251200000, fuelCost = 2.3),
            createRefillWithFuelCost(vehicleId = 2, date = 1637337600000, fuelCost = 2.8)
        )
        val result = getBestGasPrice(refillList)
        assertEquals(2.3, result, 0.01)
    }


    // getWorstGasPrice

    @Test
    fun testGetWorstGasPriceWithEmptyList() {
        val refillList = emptyList<Refill>()
        val result = getWorstGasPrice(refillList)
        assertEquals(0.0, result, 0.01)
    }

    @Test
    fun testGetWorstGasPriceWithSingleRefill() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = 2.5)
        )
        val result = getWorstGasPrice(refillList)
        assertEquals(2.5, result, 0.01)
    }

    @Test
    fun testGetWorstGasPriceWithMultipleRefills() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = 2.5),
            createRefillWithFuelCost(vehicleId = 1, date = 1637251200000, fuelCost = 2.3),
            createRefillWithFuelCost(vehicleId = 2, date = 1637337600000, fuelCost = 2.8)
        )
        val result = getWorstGasPrice(refillList)
        assertEquals(2.8, result, 0.01)
    }

    @Test
    fun testGetWorstGasPriceWithNullFuelCost() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = null),
            createRefillWithFuelCost(vehicleId = 1, date = 1637251200000, fuelCost = 2.3),
            createRefillWithFuelCost(vehicleId = 2, date = 1637337600000, fuelCost = 2.8)
        )
        val result = getWorstGasPrice(refillList)
        assertEquals(2.8, result, 0.01)
    }


    // getAverageGasPrice

    @Test
    fun testGetAverageGasPriceWithEmptyList() {
        val refillList = emptyList<Refill>()
        val result = getAverageGasPrice(refillList)
        assertEquals(0.0, result, 0.01)
    }

    @Test
    fun testGetAverageGasPriceWithSingleRefill() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = 2.5)
        )
        val result = getAverageGasPrice(refillList)
        assertEquals(2.5, result, 0.01)
    }

    @Test
    fun testGetAverageGasPriceWithMultipleRefills() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = 2.5),
            createRefillWithFuelCost(vehicleId = 1, date = 1637251200000, fuelCost = 2.3),
            createRefillWithFuelCost(vehicleId = 2, date = 1637337600000, fuelCost = 2.8)
        )
        val result = getAverageGasPrice(refillList)
        assertEquals(2.53, result, 0.01)
    }

    @Test
    fun testGetAverageGasPriceWithNullFuelCost() {
        val refillList = listOf(
            createRefillWithFuelCost(vehicleId = 1, date = 1637164800000, fuelCost = null),
            createRefillWithFuelCost(vehicleId = 1, date = 1637251200000, fuelCost = 2.3),
            createRefillWithFuelCost(vehicleId = 2, date = 1637337600000, fuelCost = 2.8)
        )
        val result = getAverageGasPrice(refillList)
        assertEquals(2.55, result, 0.01)
    }


    // calculateAverageFuelConsumption

    @Test
    fun testCalculateAverageFuelConsumptionWithEmptyList() {
        val refillList = emptyList<Refill>()
        val result = calculateAverageFuelConsumption(refillList)
        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun testCalculateAverageFuelConsumptionWithSingleRefill() {
        val refillList = listOf(
            createRefill(vehicleId = 1, date = 1637164800000, mileage = 10000, volume = 30.0, full = true)
        )
        val result = calculateAverageFuelConsumption(refillList)
        assertEquals(0.0, result, 0.0) // As there is only one refill, the consumption is not calculable.
    }

    @Test
    fun testCalculateAverageFuelConsumptionWithMultipleRefills() {
        val refillList = listOf(
            createRefill(vehicleId = 1, date = 1637164800000, mileage = 10000, volume = 30.0, full = true),
            createRefill(vehicleId = 1, date = 1637251200000, mileage = 10500, volume = 25.0, full = true),
            createRefill(vehicleId = 1, date = 1637337600000, mileage = 11000, volume = 20.0, full = true)
        )
        val result = calculateAverageFuelConsumption(refillList)
        assertEquals(4.5, result, 0.01)
    }

    @Test
    fun testCalculateAverageFuelConsumptionWithMissedRefill() {
        val refillList = listOf(
            createRefill(vehicleId = 1, date = 1637164800000, mileage = 10000, volume = 30.0, full = true),
            createRefill(vehicleId = 1, date = 1637251200000, previousMissed = true),
            createRefill(vehicleId = 1, date = 1637337600000, mileage = 11000, volume = 20.0, full = true)
        )
        val result = calculateAverageFuelConsumption(refillList)
        assertEquals(0.0, result) // As there is a missed refill, the consumption is not calculable.
    }

    @Test
    fun testCalculateAverageFuelConsumptionWithTwoValidSets() {
        val refillList = listOf(
            createRefill(vehicleId = 1, date = 1637164800000, mileage = 10000, volume = 30.0, full = true),
            createRefill(vehicleId = 1, date = 1637251200000, mileage = 11000, volume = 20.0, full = true),
            createRefill(vehicleId = 1, date = 1637260000000, previousMissed = true),
            createRefill(vehicleId = 1, date = 1637350000000, mileage = 12000, volume = 25.0, full = true),
            createRefill(vehicleId = 1, date = 1637436400000, mileage = 13000, volume = 15.0, full = true)
        )
        val result = calculateAverageFuelConsumption(refillList)

        val expectedTotalConsumption = 20 + 15
        val expectedTotalDistance = 2000
        val expectedAverage = (expectedTotalConsumption * 100.0) / expectedTotalDistance

        assertEquals(expectedAverage, result, 0.0)
    }



    // getTotalMileage

    @Test
    fun testGetTotalDistanceWithEmptyList() {
        val mileageList = emptyList<Mileage>()
        val result = getTotalDistance(mileageList)
        assertEquals(0, result)
    }

    @Test
    fun testGetTotalDistanceWithSingleMileage() {
        val mileageList = listOf(
            Mileage(vehicleId = 1, date = 1637164800000, mileage = 100)
        )
        val result = getTotalDistance(mileageList)
        assertEquals(0, result)
    }

    @Test
    fun testGetTotalDistanceWithMultipleMileages() {
        val mileageList = listOf(
            Mileage(vehicleId = 1, date = 1637164800000, mileage = 100),
            Mileage(vehicleId = 1, date = 1637251200000, mileage = 200)
        )
        val result = getTotalDistance(mileageList)
        assertEquals(100, result)
    }


    // getExpenseCostByCategory

    @Test
    fun testGetExpenseCostByCategoryWithEmptyList() {
        val expenseList = emptyList<Expense>()
        val result = getExpenseCostByCategory(expenseList, ExpenseCategory.MAINTENANCE)
        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun testGetExpenseCostByCategoryWithSingleExpense() {
        val expenseList = listOf(
            createExpense(vehicleId = 1, date = 1637164800000, expenseCategory = ExpenseCategory.MAINTENANCE, costParts = 50.0)
        )
        val result = getExpenseCostByCategory(expenseList, ExpenseCategory.MAINTENANCE)
        assertEquals(50.0, result, 0.0)
    }

    @Test
    fun testGetExpenseCostByCategoryWithMultipleExpenses() {
        val expenseList = listOf(
            createExpense(vehicleId = 1, date = 1637164800000, expenseCategory = ExpenseCategory.MAINTENANCE, costParts = 50.0),
            createExpense(vehicleId = 1, date = 1637251200000, expenseCategory = ExpenseCategory.REPAIR, costServices = 30.0),
            createExpense(vehicleId = 1, date = 1637368500000, expenseCategory = ExpenseCategory.MAINTENANCE, costServices = 35.5, costParts = 40.0)
        )
        val result = getExpenseCostByCategory(expenseList, ExpenseCategory.MAINTENANCE)
        assertEquals(125.5, result, 0.0)
    }

    @Test
    fun testGetExpenseCostByCategoryWithNoMatchingCategory() {
        val expenseList = listOf(
            createExpense(vehicleId = 1, date = 1637164800000, expenseCategory = ExpenseCategory.REPAIR, costServices = 30.0)
        )
        val result = getExpenseCostByCategory(expenseList, ExpenseCategory.MAINTENANCE)
        assertEquals(0.0, result, 0.0)
    }


    // getExpensesCost

    @Test
    fun testGetExpensesCostWithEmptyList() {
        val expenseList = emptyList<Expense>()
        val result = getExpensesCost(expenseList)
        assertEquals(0.0, result, 0.0)
    }

    @Test
    fun testGetExpensesCostWithSingleExpense() {
        val expenseList = listOf(
            createExpense(vehicleId = 1, date = 1637164800000, costParts = 50.0)
        )
        val result = getExpensesCost(expenseList)
        assertEquals(50.0, result, 0.0)
    }

    @Test
    fun testGetExpensesCostWithMultipleExpenses() {
        val expenseList = listOf(
            createExpense(vehicleId = 1, date = 1637164800000, costParts = 50.0),
            createExpense(vehicleId = 1, date = 1637251200000, costServices = 30.0)
        )
        val result = getExpensesCost(expenseList)
        assertEquals(80.0, result, 0.0)
    }

    @Test
    fun testGetExpensesCostWithExpensesHavingNoCost() {
        val expenseList = listOf(
            createExpense(vehicleId = 1, date = 1637164800000),
            createExpense(vehicleId = 1, date = 1637251200000)
        )
        val result = getExpensesCost(expenseList)
        assertEquals(0.0, result, 0.0)
    }





    private fun createRefillWithTotalCost(
        vehicleId: Long,
        date: Long,
        totalCost: Double?
    ): Refill {
        val refill = Refill(vehicleId, date)
        refill.totalCost = totalCost
        return refill
    }

    private fun createRefillWithVolume(
        vehicleId: Long,
        date: Long,
        volume: Double?
    ): Refill {
        val refill = Refill(vehicleId, date)
        refill.volume = volume
        return refill
    }

    private fun createRefillWithFuelCost(
        vehicleId: Long,
        date: Long,
        fuelCost: Double?
    ): Refill {
        val refill = Refill(vehicleId, date)
        refill.fuelCost = fuelCost
        return refill
    }

    private fun createRefill(
        vehicleId: Long,
        date: Long,
        mileage: Int? = null,
        volume: Double? = null,
        full: Boolean = false,
        previousMissed: Boolean = false
    ): Refill {
        val refillItem = Refill(vehicleId, date)
        refillItem.refillId = 1
        refillItem.mileage = mileage
        refillItem.volume = volume
        refillItem.full = full
        refillItem.previousMissed = previousMissed
        return refillItem
    }

    private fun createExpense(
        vehicleId: Long,
        date: Long,
        expenseCategory: ExpenseCategory? = null,
        costParts: Double? = null,
        costServices: Double? = null
    ): Expense {
        val expenseItem = Expense("Expense", vehicleId, date)
        expenseItem.expenseId = 1 // Set an expense ID for testing purposes
        expenseItem.expenseCategory = expenseCategory
        expenseItem.costParts = costParts
        expenseItem.costServices = costServices
        return expenseItem
    }

}