package cz.mendelu.xspacek6.vehiclemanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addevent.AddEventScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addexpense.AddExpenseScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addrefill.AddRefillScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle.AddVehicleScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.calculator.CalculatorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.charts.ChartsScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.HomeScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.mileagelog.MileageLogScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.settings.SettingsScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.StatisticsScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.TripsScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.addtrip.AddTripScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.drive.DriveScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.tripdetail.TripDetailScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.vehicles.ListOfVehiclesScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Destination.ListOfVehiclesScreen.route) {
            ListOfVehiclesScreen(navigation = navigation)
        }

        composable(route = Destination.AddVehicleScreen.route + "/{vehicleId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            AddVehicleScreen(
                navigation = navigation,
                vehicleId = if(vehicleId != -1L) vehicleId else null
            )
        }

        composable(route = Destination.MainScreen.route + "/{vehicleId}/{vehicleName}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("vehicleName") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            val vehicleName = it.arguments?.getString("vehicleName")
            HomeScreen(navigation = navigation, vehicleId = vehicleId!!, vehicleName = vehicleName!!)
        }

        composable(route = Destination.AddRefillScreen.route + "/{vehicleId}/{refillId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument(Constants.REFILL_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            val refillId = it.arguments?.getLong(Constants.REFILL_ID)
            AddRefillScreen(
                navigation = navigation,
                vehicleId = vehicleId!!,
                refillId = if(refillId != -1L) refillId else null
            )
        }

        composable(route = Destination.AddExpenseScreen.route + "/{vehicleId}/{expenseId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument(Constants.EXPENSE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            val expenseId = it.arguments?.getLong(Constants.EXPENSE_ID)
            AddExpenseScreen(
                navigation = navigation,
                vehicleId = vehicleId!!,
                expenseId = if(expenseId != -1L) expenseId else null
            )
        }

        composable(route = Destination.AddEventScreen.route + "/{vehicleId}/{eventId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument(Constants.EVENT_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            val eventId = it.arguments?.getLong(Constants.EVENT_ID)
            AddEventScreen(
                navigation = navigation,
                vehicleId = vehicleId!!,
                eventId = if(eventId != -1L) eventId else null
            )
        }

        composable(route = Destination.MileageLogScreen.route + "/{vehicleId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            MileageLogScreen(navigation = navigation, vehicleId = vehicleId!!)
        }

        composable(route = Destination.StatisticsScreen.route + "/{vehicleId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            StatisticsScreen(vehicleId = vehicleId!!, navigation = navigation)
        }

        composable(route = Destination.ChartsScreen.route + "/{vehicleId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            ChartsScreen(vehicleId = vehicleId!!, navigation = navigation)
        }


        composable(route = Destination.CalculatorScreen.route) {
            CalculatorScreen(navigation = navigation)
        }

        composable(route = Destination.SettingsScreen.route) {
            SettingsScreen(navigation = navigation)
        }


        composable(route = Destination.TripsScreen.route + "/{vehicleId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            TripsScreen(vehicleId = vehicleId!!, navigation = navigation)
        }


        composable(route = Destination.DriveScreen.route + "/{vehicleId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )

        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            DriveScreen(navigation = navigation, vehicleId = vehicleId!!)
        }

        composable(route = Destination.TripDetailScreen.route + "/{vehicleId}/{tripId}",
            arguments = listOf(
                navArgument(Constants.VEHICLE_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument(Constants.TRIP_ID) {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            val tripId = it.arguments?.getLong(Constants.TRIP_ID)
            TripDetailScreen(
                navigation = navigation,
                vehicleId = vehicleId!!,
                tripId = tripId!!
            )
        }

        composable(route = Destination.AddTripScreen.route + "/{vehicleId}/{tripId}",
                arguments = listOf(
                    navArgument(Constants.VEHICLE_ID) {
                        type = NavType.LongType
                        defaultValue = -1L
                    },
                    navArgument(Constants.TRIP_ID) {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) {
            val vehicleId = it.arguments?.getLong(Constants.VEHICLE_ID)
            val tripId = it.arguments?.getLong(Constants.TRIP_ID)
            AddTripScreen(
                navigation = navigation,
                vehicleId = vehicleId!!,
                tripId = if(tripId != -1L) tripId else null
            )
        }


    }

}