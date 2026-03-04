package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.dashboard


import cz.mendelu.xspacek6.vehiclemanager.architecture.BaseViewModel
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.database.ILocalVehiclesRepository
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Refill
import cz.mendelu.xspacek6.vehiclemanager.datastore.IDataStoreRepository
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.Statistics
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import cz.mendelu.xspacek6.vehiclemanager.utils.MathUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date



data class DashboardStateData(
    var events: List<Event>,
    var thisMonth: MonthExpenses,
    var lastMonth: MonthExpenses
)

data class MonthExpenses(
    var refills: Double = 0.0,
    var maintenance: Double = 0.0,
    var repair: Double = 0.0,
    var insurance: Double = 0.0,
    var tuning: Double = 0.0,
    var cleaning: Double = 0.0,
    var toll: Double = 0.0,
    var other: Double = 0.0,
)


class DashboardViewModel(private val databaseRepository: ILocalVehiclesRepository, private val dataStoreRepository: IDataStoreRepository) : BaseViewModel() {

    var vehicleId: Long? = null
    var lastMileage: Int? = null
    var currency = ""

    init {
        launch {
            currency = dataStoreRepository.getCurrency()
        }
    }

    private val _dashboardState = MutableStateFlow<DashboardUiState<DashboardStateData>>(DashboardUiState.Default())
    val dashboardState: StateFlow<DashboardUiState<DashboardStateData>> = _dashboardState


    var events: List<Event> = listOf()
    var refills: List<Refill> = listOf()
    var expenses: List<Expense> = listOf()

    val dashboardData2 = DashboardStateData(listOf(),MonthExpenses(), MonthExpenses())

    fun loadData() {
        launch {
            lastMileage = databaseRepository.getLastMileage(vehicleId!!)?.mileage
        }.invokeOnCompletion {
            launch {
                events = databaseRepository.getUpcomingEvents(vehicleId!!, lastMileage?:0)
                refills = databaseRepository.getLastRefills(vehicleId!!, DateUtils.addMonthsToDate(Date().time, -2))
                expenses = databaseRepository.getLastExpenses(vehicleId!!, DateUtils.addMonthsToDate(Date().time, -2))
            }.invokeOnCompletion {

                println("EVENTS: " + events)
                println("REFILLS: " + refills)
                println("EXPENSES: " + expenses)
                calculateExpenses()
                _dashboardState.value = DashboardUiState.Loaded(dashboardData2)
            }
        }
    }

    private fun calculateExpenses() {
        val todayDate = Date().time
        val lastMonthDate = DateUtils.addMonthsToDate(todayDate, -1)

        val thisMonthRefills = refills.filter { it.date in (lastMonthDate + 1)..todayDate}
        val lastMonthRefills = refills.filter { it.date !in (lastMonthDate + 1)..todayDate}

        val thisMonthExpenses = expenses.filter { it.date in (lastMonthDate + 1)..todayDate}
        val lastMonthExpenses = expenses.filter { it.date !in (lastMonthDate + 1)..todayDate}

        dashboardData2.events = events
            .filter { !it.done }
            .sortedBy { orderByPercentage(it) }

//        dashboardData.refills.forEach {refill ->
//            if(refill.date in (lastMonthDate + 1)..todayDate) {
//                dashboardData2.thisMonth.refills += refill.totalCost ?: 0.0
//            } else {
//                dashboardData2.lastMonth.refills += refill.totalCost ?: 0.0
//            }
//        }

        dashboardData2.thisMonth.refills = Statistics.getFuelCost(thisMonthRefills)
        dashboardData2.lastMonth.refills = Statistics.getFuelCost(lastMonthRefills)

        dashboardData2.thisMonth.maintenance = Statistics.getExpenseCostByCategory(thisMonthExpenses, ExpenseCategory.MAINTENANCE)
        dashboardData2.lastMonth.maintenance = Statistics.getExpenseCostByCategory(lastMonthExpenses, ExpenseCategory.MAINTENANCE)

        dashboardData2.thisMonth.repair = Statistics.getExpenseCostByCategory(thisMonthExpenses, ExpenseCategory.REPAIR)
        dashboardData2.lastMonth.repair = Statistics.getExpenseCostByCategory(lastMonthExpenses, ExpenseCategory.REPAIR)

        dashboardData2.thisMonth.insurance = Statistics.getExpenseCostByCategory(thisMonthExpenses, ExpenseCategory.INSURANCE)
        dashboardData2.lastMonth.insurance = Statistics.getExpenseCostByCategory(lastMonthExpenses, ExpenseCategory.INSURANCE)

        dashboardData2.thisMonth.tuning = Statistics.getExpenseCostByCategory(thisMonthExpenses, ExpenseCategory.TUNING)
        dashboardData2.lastMonth.tuning = Statistics.getExpenseCostByCategory(lastMonthExpenses, ExpenseCategory.TUNING)

        dashboardData2.thisMonth.cleaning = Statistics.getExpenseCostByCategory(thisMonthExpenses, ExpenseCategory.CLEANING)
        dashboardData2.lastMonth.cleaning = Statistics.getExpenseCostByCategory(lastMonthExpenses, ExpenseCategory.CLEANING)

        dashboardData2.thisMonth.toll = Statistics.getExpenseCostByCategory(thisMonthExpenses, ExpenseCategory.TOLL)
        dashboardData2.lastMonth.toll = Statistics.getExpenseCostByCategory(lastMonthExpenses, ExpenseCategory.TOLL)

        dashboardData2.thisMonth.other = Statistics.getExpenseCostByCategory(thisMonthExpenses, null)
        dashboardData2.lastMonth.other = Statistics.getExpenseCostByCategory(lastMonthExpenses, null)

//        dashboardData.expenses.forEach { expense ->
//            val expenseValue = (expense.costParts ?: 0.0) + (expense.costServices ?: 0.0)
//            if (expense.date in (lastMonthDate + 1)..todayDate){
//                when (expense.expenseCategory) {
//                    ExpenseCategory.MAINTENANCE -> dashboardData2.thisMonth.maintenance += expenseValue
//                    ExpenseCategory.REPAIR -> dashboardData2.thisMonth.repair += expenseValue
//                    ExpenseCategory.INSURANCE -> dashboardData2.thisMonth.insurance += expenseValue
//                    ExpenseCategory.TUNING -> dashboardData2.thisMonth.tuning += expenseValue
//                    ExpenseCategory.CLEANING -> dashboardData2.thisMonth.cleaning += expenseValue
//                    ExpenseCategory.TOLL -> dashboardData2.thisMonth.toll += expenseValue
//                    else -> dashboardData2.thisMonth.other += expenseValue
//                }
//            } else {
//                when (expense.expenseCategory) {
//                    ExpenseCategory.MAINTENANCE -> dashboardData2.lastMonth.maintenance += expenseValue
//                    ExpenseCategory.REPAIR -> dashboardData2.lastMonth.repair += expenseValue
//                    ExpenseCategory.INSURANCE -> dashboardData2.lastMonth.insurance += expenseValue
//                    ExpenseCategory.TUNING -> dashboardData2.lastMonth.tuning += expenseValue
//                    ExpenseCategory.CLEANING -> dashboardData2.lastMonth.cleaning += expenseValue
//                    ExpenseCategory.TOLL -> dashboardData2.lastMonth.toll += expenseValue
//                    else -> dashboardData2.lastMonth.other += expenseValue
//                }
//            }
//        }

//        dashboardState.value = DashboardUiState.Loaded(dashboardData2)
    }

    private fun orderByPercentage(event: Event): Float {
        var percentage = 1.0f
        if (!event.done){
            if (event.onceAtKm != null){
                percentage = MathUtils.calculatePercentage(
                    event.initialKm.toFloat(),
                    event.onceAtKm!!.toFloat(),
                    lastMileage?.toFloat() ?: 0.0f)
            }
            if (event.onceDate != null){
                val timePercentage = DateUtils.getPercentageOfPastDays(
                    event.initialDate,
                    event.onceDate!!
                )
                if (timePercentage < percentage) {
                    percentage = timePercentage
                }
            }
        }
        return percentage
    }

    fun setDefaultState() {
        _dashboardState.value = DashboardUiState.Default()
    }

}