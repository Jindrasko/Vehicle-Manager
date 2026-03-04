package cz.mendelu.xspacek6.vehiclemanager.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun returnBack()
    fun navigateToListOfVehicles()
    fun navigateToAddVehicle(vehicleId: Long?)
    fun navigateToMainScreen(vehicleId: Long, vehicleName: String)
    fun navigateToAddRefill(vehicleId: Long, refillId: Long?)
    fun navigateToAddExpense(vehicleId: Long, expenseId: Long?)
    fun navigateToAddEvent(vehicleId: Long, eventId: Long?)
    fun navigateToAddTrip(vehicleId: Long, tripId: Long?)
    fun navigateToMileageLog(vehicleId: Long)
    fun navigateToTrips(vehicleId: Long)
    fun navigateToStatistics(vehicleId: Long)
    fun navigateToCharts(vehicleId: Long)
    fun navigateToCalculator()
    fun navigateToSettings()


    fun navigateToDrive(vehicleId: Long)
    fun navigateToTripDetail(vehicleId: Long, tripId: Long)



    fun navigateToDestination(destination: Destination, vehicleId: Long?)

}