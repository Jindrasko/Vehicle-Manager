package cz.mendelu.xspacek6.vehiclemanager.ui.screens.charts

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import kotlinx.coroutines.launch
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


data class ChartsStateData(
    var expensesPieChartData: ExpensesPieChartData = ExpensesPieChartData(),
    var mileagesChartData: List<ChartDuo>,
    var fuelPriceChartData: List<ChartDuo>,
    var expensesByMonth: List<ChartDuo>
): Serializable

data class ChartDuo(
    var title: String,
    var value: Float = 0.0F
): Serializable

data class ExpensesPieChartData(
    var maintenance: Float = 0.0F,
    var repair: Float = 0.0F,
    var insurance: Float = 0.0F,
    var tuning: Float = 0.0F,
    var cleaning: Float = 0.0F,
    var toll: Float = 0.0F,
    var other: Float = 0.0F
): Serializable



class ChartsViewModel(private val databaseRepository: ILocalVehiclesRepository, private val dataStoreRepository: IDataStoreRepository) : BaseViewModel() {

    var vehicleId: Long? = null
    var currency = ""

    val chartsUiState: MutableState<ChartsUiState<ChartsStateData>> = mutableStateOf(ChartsUiState.Default)

    var chartTimeInterval: Int = 12

    init {
        launch {
            currency = dataStoreRepository.getCurrency()
        }
    }

    fun loadData() {
        lateinit var expenses: List<Expense>
        lateinit var mileages: List<Mileage>
        lateinit var refills: List<Refill>
        launch {
            if (chartTimeInterval == 100) {
                expenses = databaseRepository.getAllExpenses(vehicleId!!)
                mileages = databaseRepository.getAllMileages(vehicleId!!)
                refills = databaseRepository.getAllRefills(vehicleId!!)
            } else {
                expenses = databaseRepository.getLastExpenses(vehicleId!!, DateUtils.addMonthsToDate(Date().time, -chartTimeInterval))
                mileages = databaseRepository.getLastMileages(vehicleId!!, DateUtils.addMonthsToDate(Date().time, -chartTimeInterval))
                refills = databaseRepository.getLastRefills(vehicleId!!, DateUtils.addMonthsToDate(Date().time, -chartTimeInterval))
            }

        }.invokeOnCompletion {
            chartsUiState.value = ChartsUiState.Loaded(calculateChartData(expenses, mileages, refills))
        }
    }

    fun calculateChartData(expenses: List<Expense>, mileages: List<Mileage>, refills: List<Refill>): ChartsStateData {
        val chartData = ChartsStateData(
            expensesPieChartData = calculateExpensesPieChartData(expenses),
            mileagesChartData = findLastMileageForEachMonth(mileages),
            fuelPriceChartData = getFuelPrices(refills),
            expensesByMonth = calculateMonthlyExpensesSum(expenses)
        )

        return chartData
    }

    private fun getFuelPrices(refills: List<Refill>): List<ChartDuo>{
        val result = mutableListOf<ChartDuo>()

        refills.forEach {refill ->
            if (refill.fuelCost != null){
                result.add(ChartDuo(title = DateUtils.getDateString(refill.date), value = refill.fuelCost!!.toFloat()))
            }
        }

        return result
    }

    private fun calculateMonthlyExpensesSum(expenses: List<Expense>): List<ChartDuo> {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val monthlySumMap = mutableMapOf<String, Double>()

        for (expense in expenses) {
            val monthYear = dateFormat.format(Date(expense.date))
            val currentSum = monthlySumMap.getOrDefault(monthYear, 0.0)
            monthlySumMap[monthYear] = currentSum + (expense.costParts ?: 0.0) + (expense.costServices ?: 0.0)
        }

        return monthlySumMap.map { (monthYear, sum) ->
            ChartDuo(
                title = monthYear,
                value = sum.toFloat()
            )
        }
    }

    private fun findLastMileageForEachMonth(inputList: List<Mileage>): List<ChartDuo> {
        val result = mutableListOf<ChartDuo>()

        val lastObjectByYearMonth = mutableMapOf<Pair<Int, Int>, ChartDuo>()

        for (obj in inputList) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = obj.date
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)

            val key = Pair(year, month)
            if (lastObjectByYearMonth.containsKey(key)) {
                val lastObject = lastObjectByYearMonth[key]!!

                if (obj.date > lastObject.value) {
                    lastObjectByYearMonth[key] = ChartDuo(DateUtils.getDateString(obj.date), obj.mileage.toFloat())
                }
            } else {
                lastObjectByYearMonth[key] = ChartDuo(DateUtils.getDateString(obj.date), obj.mileage.toFloat())
            }
        }

        for (entry in lastObjectByYearMonth.values) {
            result.add(entry)
        }

        return result
    }


    private fun calculateExpensesPieChartData(expenses: List<Expense>): ExpensesPieChartData {
        val chartData = ExpensesPieChartData()

        expenses.forEach { expense ->
            val expenseValue = ((expense.costParts ?: 0.0) + (expense.costServices ?: 0.0)).toFloat()

            when(expense.expenseCategory) {
                ExpenseCategory.MAINTENANCE -> chartData.maintenance += expenseValue
                ExpenseCategory.REPAIR -> chartData.repair += expenseValue
                ExpenseCategory.INSURANCE -> chartData.insurance += expenseValue
                ExpenseCategory.TUNING -> chartData.tuning += expenseValue
                ExpenseCategory.CLEANING -> chartData.cleaning += expenseValue
                ExpenseCategory.TOLL -> chartData.toll += expenseValue
                else -> chartData.other += expenseValue
            }
        }

        return chartData
    }



}