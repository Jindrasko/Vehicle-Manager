package cz.mendelu.xspacek6.vehiclemanager.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.xspacek6.vehiclemanager.constants.Constants.ACTION_SHOW_DRIVE_SCREEN
import cz.mendelu.xspacek6.vehiclemanager.navigation.Destination
import cz.mendelu.xspacek6.vehiclemanager.navigation.NavGraph
import cz.mendelu.xspacek6.vehiclemanager.navigation.NavigationRouterImpl
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.VehicleManagerTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalFoundationApi::class)
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    val viewModel: MainActivityViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        installSplashScreen()

//        lifecycleScope.launchWhenResumed {
//        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.mainActivityScreenState.collect {value ->
                    when(value) {
                        MainActivityUiState.Default -> {
                            installSplashScreen()
                            viewModel.checkAppState()
                        }
                        MainActivityUiState.ContinueToApp -> {
                            setContent {
                                VehicleManagerTheme {
                                    // A surface container using the 'background' color from the theme
                                    Surface(
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.background
                                    ) {
                                        navController = rememberNavController()
                                        NavGraph(
                                            navController = navController,
                                            startDestination = Destination.ListOfVehiclesScreen.route
                                        )

                                        navigateToDriveScreenIfNeeded(intent)
                                    }
                                }
                            }
                        }
                        MainActivityUiState.RunForAFirstTime -> {
                            viewModel.setToContinue()

                            val intent = Intent(this@MainActivity, AppIntroActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }


            }
        }


//        setContent {
//            VehicleManagerTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    navController = rememberNavController()
//                    NavGraph(
//                        navController = navController,
//                        startDestination = Destination.ListOfVehiclesScreen.route
//                    )
//
//                    navigateToDriveScreenIfNeeded(intent)
//                }
//            }
//        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToDriveScreenIfNeeded(intent)
    }

    private fun navigateToDriveScreenIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_DRIVE_SCREEN) {
            if (navController.currentBackStackEntry?.destination?.route != Destination.DriveScreen.route + "/{vehicleId}") {
                NavigationRouterImpl(navController).navigateToDrive(intent.extras!!.getLong("vehicleId"))
            }
        }
    }
}

