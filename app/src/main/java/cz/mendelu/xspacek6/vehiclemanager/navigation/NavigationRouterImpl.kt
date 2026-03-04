package cz.mendelu.xspacek6.vehiclemanager.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun getNavController(): NavController = navController

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToListOfVehicles() {
        navController.navigate(Destination.ListOfVehiclesScreen.route)
    }

    override fun navigateToAddVehicle(vehicleId: Long?) {
        navController.navigate(Destination.AddVehicleScreen.route + "/" + vehicleId)
    }

    override fun navigateToMainScreen(vehicleId: Long, vehicleName: String) {
        navController.navigate(Destination.MainScreen.route + "/" + vehicleId + "/" + vehicleName)
    }

    override fun navigateToAddRefill(vehicleId: Long, refillId: Long?) {
        navController.navigate(Destination.AddRefillScreen.route + "/" + vehicleId + "/" + refillId)
    }

    override fun navigateToAddExpense(vehicleId: Long, expenseId: Long?) {
        navController.navigate(Destination.AddExpenseScreen.route + "/" + vehicleId + "/" + expenseId)
    }

    override fun navigateToAddEvent(vehicleId: Long, eventId: Long?) {
        navController.navigate(Destination.AddEventScreen.route + "/" + vehicleId + "/" + eventId)
    }

    override fun navigateToAddTrip(vehicleId: Long, tripId: Long?) {
        navController.navigate(Destination.AddTripScreen.route + "/" + vehicleId + "/" + tripId)
    }

    override fun navigateToMileageLog(vehicleId: Long) {
        navController.navigate(Destination.MileageLogScreen.route + "/" + vehicleId)
    }

    override fun navigateToTrips(vehicleId: Long) {
        navController.navigate(Destination.TripsScreen.route + "/" + vehicleId)
    }

    override fun navigateToStatistics(vehicleId: Long) {
        navController.navigate(Destination.StatisticsScreen.route + "/$vehicleId")
    }

    override fun navigateToCharts(vehicleId: Long) {
        navController.navigate(Destination.ChartsScreen.route + "/" + vehicleId)
    }

    override fun navigateToCalculator() {
        navController.navigate(Destination.CalculatorScreen.route)
    }

    override fun navigateToSettings() {
        navController.navigate(Destination.SettingsScreen.route)
    }


    override fun navigateToDrive(vehicleId: Long) {
        navController.navigate(Destination.DriveScreen.route + "/$vehicleId")
    }

    override fun navigateToTripDetail(vehicleId: Long, tripId: Long) {
        navController.navigate(Destination.TripDetailScreen.route + "/" + vehicleId + "/" + tripId)
    }


    override fun navigateToDestination(destination: Destination, vehicleId: Long?) {
        navController.navigate(destination.route+"/$vehicleId")
    }


}