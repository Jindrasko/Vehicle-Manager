package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagFAB
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagHomeBottomItem
import cz.mendelu.xspacek6.vehiclemanager.navigation.Destination
import cz.mendelu.xspacek6.vehiclemanager.navigation.HomeNavGraph
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.MileageDialog
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.CheckmarkGreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigation: INavigationRouter,
    vehicleId: Long,
    vehicleName: String,
    viewModel: HomeScreenViewModel = getViewModel()
) {

    viewModel.vehicleId = vehicleId

    val snackbarHostState = remember { SnackbarHostState() }

    val homeNavController = rememberNavController()
    val currentBackStack = homeNavController.currentBackStackEntryAsState()

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )
    BackHandler(drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
    }
    val sheetState = rememberModalBottomSheetState()

    val showOdoDialog = remember { mutableStateOf(false) }
    val lastMileage = remember { mutableStateOf("") } //rememberSaveable
    val isDoneFilter = remember { mutableStateOf(false) }

    viewModel.homeScreenUiState.value.let {
        when(it) {
            is HomeScreenUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadLastMileage()
                }
            }
            is HomeScreenUiState.LastMileageLoaded -> {
                lastMileage.value = viewModel.lastMileage.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = vehicleName,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(start = 0.dp)
                                .weight(1.5f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) { drawerState.open() }
                            else {
                                drawerState.close()
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "menu"
                        )
                    }
                },
                actions = {
                    if(currentBackStack.value?.destination?.route == Destination.EventsScreen.route) {
                        IconButton(
                            onClick = {
                                isDoneFilter.value = !isDoneFilter.value
                                coroutineScope.launch{
                                    snackbarHostState.showSnackbar(
                                        message = if (isDoneFilter.value) { appContext.getString(R.string.snackbar_showing_done) }
                                        else { appContext.getString(R.string.snackbar_showing_upcoming) },
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }) {
                            Icon(
                                tint = if (isDoneFilter.value) CheckmarkGreen else MaterialTheme.colorScheme.onSurface,
                                painter = painterResource(
                                    id = if (isDoneFilter.value) R.drawable.check_circle_24
                                    else R.drawable.check_circle_outline_24
                                ), contentDescription = "Done filter"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            val bottomBarItems = listOf(
                BottomNav.Dashboard,
                BottomNav.Refills,
                BottomNav.Expenses,
                BottomNav.Events
            )

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            ) {
                val navBackStackEntry = homeNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route

                bottomBarItems.forEach {item ->
                    NavigationBarItem(
                        modifier = Modifier.testTag(TestTagHomeBottomItem + stringResource(id = item.title)),
                        icon = { Icon(painter = painterResource(id = item.icon), contentDescription = stringResource(id = item.title)) },
                        label = { Text(text = stringResource(id = item.title)) },
                        alwaysShowLabel = true,
                        selected = currentRoute == item.screenRoute.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface
                        ) ,
                        onClick = {
                            homeNavController.navigate(item.screenRoute.route) {

                                homeNavController.graph.startDestinationRoute?.let { screenRoute ->
                                    popUpTo(screenRoute) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        })
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        sheetState.show()
                    }
                },
                modifier = Modifier.testTag(TestTagFAB)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "plus"
                )
            }
        },
        content = {

            if(showOdoDialog.value){
                MileageDialog(show = showOdoDialog, lastMileage = lastMileage.value, vehicleId = vehicleId) {
                    viewModel.saveMileage(it)
                    lastMileage.value = it.mileage.toString()
                }
            }

            SideMenuNav(
                navigation = navigation,
                drawerState = drawerState,
                coroutineScope = coroutineScope,
                vehicleId = vehicleId,
            ) {

                HomeScreenBottomSheet(
                    sheetState = sheetState,
                    coroutineScope = coroutineScope,
                    showOdoDialog = showOdoDialog,
                    vehicleId = vehicleId,
                    navigation = navigation
                )

                MainScreenContent(
                    paddingValues = it,
                    navigation = navigation,
                    homeNavController = homeNavController,
                    vehicleId = vehicleId,
                    isDone = isDoneFilter
                )
            }


        },
    )


}


@Composable
fun MainScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    homeNavController: NavHostController,
    vehicleId: Long,
    isDone: MutableState<Boolean>
) {
    HomeNavGraph(
        homeNavController = homeNavController,
        paddingValues = paddingValues,
        vehicleId = vehicleId,
        navigation = navigation,
        isDone = isDone
        )
}