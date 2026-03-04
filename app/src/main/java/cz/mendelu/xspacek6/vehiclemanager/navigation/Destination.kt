package cz.mendelu.xspacek6.vehiclemanager.navigation

sealed class Destination(
    val route: String
) {

    object ListOfVehiclesScreen : Destination(route = "list_of_vehicles")
    object AddVehicleScreen : Destination(route = "add_vehicle")
    object MainScreen : Destination(route = "main_screen")

    object DashboardScreen : Destination(route = "dashboard")
    object RefillsScreen : Destination(route = "refills")
    object ExpensesScreen : Destination(route = "expenses")
    object EventsScreen : Destination(route = "events")

    object AddRefillScreen : Destination(route = "add_refill")
    object AddExpenseScreen : Destination(route = "add_expense")
    object AddEventScreen : Destination(route = "add_event")
    object AddTripScreen : Destination(route = "add_trip")

    object MileageLogScreen : Destination(route = "mileage_log")
    object TripsScreen : Destination(route = "trips_screen")
    object TripDetailScreen : Destination(route = "trip_detail")
    object StatisticsScreen : Destination(route = "statistics")
    object ChartsScreen : Destination(route = "charts")
    object CalculatorScreen : Destination(route = "calculator")
    object SettingsScreen : Destination(route = "settings")


    object DriveScreen : Destination(route = "drive")

}