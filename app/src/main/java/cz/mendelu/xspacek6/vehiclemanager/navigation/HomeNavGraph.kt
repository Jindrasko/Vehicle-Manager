package cz.mendelu.xspacek6.vehiclemanager.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.dashboard.DashboardScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.events.EventsScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.expenses.ExpensesScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.refills.RefillsScreen

@Composable
fun HomeNavGraph(
    navigation: INavigationRouter,
    homeNavController: NavHostController,
    paddingValues: PaddingValues,
    vehicleId: Long,
    isDone: MutableState<Boolean>
){

    NavHost(
        navController = homeNavController,
        startDestination = Destination.DashboardScreen.route
    ) {

        composable(route = Destination.DashboardScreen.route) {
            DashboardScreen(vehicleId = vehicleId, paddingValues = paddingValues, navigation = navigation, homeNavController = homeNavController)
        }

        composable(route = Destination.RefillsScreen.route) {
            RefillsScreen(vehicleId = vehicleId, paddingValues = paddingValues, navigation = navigation)
        }

        composable(route = Destination.ExpensesScreen.route) {
            ExpensesScreen(vehicleId = vehicleId, paddingValues = paddingValues, navigation = navigation)
        }

        composable(route = Destination.EventsScreen.route) {
            EventsScreen(vehicleId = vehicleId, paddingValues = paddingValues, navigation = navigation, isDone = isDone)
        }

    }
}