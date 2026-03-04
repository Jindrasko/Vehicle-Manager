package cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Mileage
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Date


data class StatisticsStateData(
    var totalGas: ValuesAtTime = ValuesAtTime(),
    var totalExpenses: ValuesAtTime = ValuesAtTime(),
    var totalDistance: ValuesAtTime = ValuesAtTime(),
    var thisYearBestPrice: Double = 0.0,
    var lastYearBestPrice: Double = 0.0,
    var thisYearAveragePrice: Double = 0.0,
    var lastYearAveragePrice: Double = 0.0,
    var thisYearWorstPrice: Double = 0.0,
    var lastYearWorstPrice: Double = 0.0,
    var thisYearAverageConsumption: Double = 0.0,
    var lastYearAverageConsumption: Double = 0.0
) : Serializable

data class ValuesAtTime(
    var thisMonth: Double = 0.0,
    var lastMonth: Double = 0.0,
    var thisYear: Double = 0.0,
    var lastYear: Double = 0.0
) : Serializable

data class SplitRecords(
    var lastYearRefills: List<Refill> = listOf(),
    var thisYearRefills: List<Refill> = listOf(),
    var lastMonthRefills: List<Refill> = listOf(),
    var thisMonthRefills: List<Refill> = listOf(),
    var lastYearExpenses: List<Expense> = listOf(),
    var thisYearExpenses: List<Expense> = listOf(),
    var lastMonthExpenses: List<Expense> = listOf(),
    var thisMonthExpenses: List<Expense> = listOf(),
    var lastYearMileages: List<Mileage> = listOf(),
    var thisYearMileages: List<Mileage> = listOf(),
    var lastMonthMileages: List<Mileage> = listOf(),
    var thisMonthMileages: List<Mileage> = listOf(),
) : Serializable


class StatisticsViewModel(private val databaseRepository: ILocalVehiclesRepository, private val dataStoreRepository: IDataStoreRepository) : BaseViewModel() {

    var vehicleId: Long? = null

    val statisticsUiState: MutableState<StatisticsUiState<StatisticsStateData>> = mutableStateOf(StatisticsUiState.Default)
    var currency = ""

    init {
        launch {
            currency = dataStoreRepository.getCurrency()
        }
    }

    fun loadData() {
        var refills: List<Refill> = listOf()
        var expenses: List<Expense> = listOf()
        var mileages: List<Mileage> = listOf()

        launch {
            refills = databaseRepository.getLastTwoYearsRefills(vehicleId!!)
            expenses = databaseRepository.getLastTwoYearsExpenses(vehicleId!!)
            mileages = databaseRepository.getLastTwoYearsMileages(vehicleId!!)
        }.invokeOnCompletion {
            statisticsUiState.value = StatisticsUiState.Loaded(calculateStatistics(refills = refills, expenses = expenses, mileages = mileages))
        }
    }

    private fun calculateStatistics(refills: List<Refill>, expenses: List<Expense>, mileages: List<Mileage>): StatisticsStateData {
        val records: SplitRecords = splitRecordsByTime(refills, expenses, mileages)
        val statistics: StatisticsStateData = StatisticsStateData()

        statistics.totalGas.thisMonth = Statistics.getTotalGas(records.thisMonthRefills)
        statistics.totalGas.lastMonth = Statistics.getTotalGas(records.lastMonthRefills)
        statistics.totalGas.thisYear = Statistics.getTotalGas(records.thisYearRefills)
        statistics.totalGas.lastYear = Statistics.getTotalGas(records.lastYearRefills)

        statistics.totalExpenses.thisMonth = Statistics.getExpensesCost(records.thisMonthExpenses)
        statistics.totalExpenses.lastMonth = Statistics.getExpensesCost(records.lastMonthExpenses)
        statistics.totalExpenses.thisYear = Statistics.getExpensesCost(records.thisYearExpenses)
        statistics.totalExpenses.lastYear = Statistics.getExpensesCost(records.lastYearExpenses)

        statistics.thisYearBestPrice = Statistics.getBestGasPrice(records.thisYearRefills)
        statistics.lastYearBestPrice = Statistics.getBestGasPrice(records.lastYearRefills)
        statistics.thisYearAveragePrice = Statistics.getAverageGasPrice(records.thisYearRefills)
        statistics.lastYearAveragePrice = Statistics.getAverageGasPrice(records.lastYearRefills)
        statistics.thisYearWorstPrice = Statistics.getWorstGasPrice(records.thisYearRefills)
        statistics.lastYearWorstPrice = Statistics.getWorstGasPrice(records.lastYearRefills)

        statistics.thisYearAverageConsumption = Statistics.calculateAverageFuelConsumption(records.thisYearRefills)
        statistics.lastYearAverageConsumption = Statistics.calculateAverageFuelConsumption(records.lastYearRefills)

        statistics.totalDistance.thisMonth = Statistics.getTotalDistance(records.thisMonthMileages).toDouble()
        statistics.totalDistance.lastMonth = Statistics.getTotalDistance(records.lastMonthMileages).toDouble()
        statistics.totalDistance.thisYear = Statistics.getTotalDistance(records.thisYearMileages).toDouble()
        statistics.totalDistance.lastYear = Statistics.getTotalDistance(records.lastYearMileages).toDouble()



        return statistics
    }

    private fun splitRecordsByTime(refills: List<Refill>, expenses: List<Expense>, mileages: List<Mileage>): SplitRecords {
        val resultRecords: SplitRecords = SplitRecords()

        val todayDate = Date().time
        val lastMonthDate = DateUtils.addMonthsToDate(todayDate, -1)
        val lastTwoMonthsDate = DateUtils.addMonthsToDate(todayDate, -2)
        val lastYearDate = DateUtils.addMonthsToDate(todayDate, -12)
        val lastTwoYearsDate = DateUtils.addMonthsToDate(todayDate, -24)

        resultRecords.thisYearRefills = refills.filter { it.date in (lastYearDate + 1)..todayDate}
        resultRecords.lastYearRefills = refills.filter { it.date in (lastTwoYearsDate + 1)..(lastYearDate + 1)}
        resultRecords.thisMonthRefills = resultRecords.thisYearRefills.filter { it.date in (lastMonthDate + 1)..todayDate}
        resultRecords.lastMonthRefills = resultRecords.thisYearRefills.filter { it.date in (lastTwoMonthsDate + 1)..(lastMonthDate + 1)}

        resultRecords.thisYearExpenses = expenses.filter { it.date in (lastYearDate + 1)..todayDate}
        resultRecords.lastYearExpenses = expenses.filter { it.date in (lastTwoYearsDate + 1)..(lastYearDate + 1)}
        resultRecords.thisMonthExpenses = resultRecords.thisYearExpenses.filter { it.date in (lastMonthDate + 1)..todayDate}
        resultRecords.lastMonthExpenses = resultRecords.thisYearExpenses.filter { it.date in (lastTwoMonthsDate + 1)..(lastMonthDate + 1)}

        resultRecords.thisYearMileages = mileages.filter { it.date in (lastYearDate + 1)..todayDate }
        resultRecords.lastYearMileages = mileages.filter { it.date in (lastTwoYearsDate + 1)..(lastYearDate + 1) }
        resultRecords.thisMonthMileages = resultRecords.thisYearMileages.filter { it.date in (lastMonthDate + 1)..todayDate}
        resultRecords.lastMonthMileages = resultRecords.thisYearMileages.filter { it.date in (lastTwoMonthsDate + 1)..(lastMonthDate + 1)}

        return resultRecords
    }


}