package cz.mendelu.xspacek6.vehiclemanager.ui.screens.vehicles

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagFAB
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagLazyList
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagVehicle
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagVehicleInfoButton
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Vehicle
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.EmptyRecordsText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.SideMenuNav
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOfVehiclesScreen(
    navigation: INavigationRouter,
    viewModel: ListOfVehiclesViewModel = getViewModel()
) {

    val screenState: MutableState<ScreenState<List<Vehicle>>> = remember {
        mutableStateOf(ScreenState.Loading())
    }

    val listOfVehiclesUiState: ListOfVehiclesUiState<List<Vehicle>>? by viewModel.listOfVehiclesUiState.collectAsState()

    listOfVehiclesUiState?.let {
        when(it) {
            is ListOfVehiclesUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadVehicles()
                }
            }
            is ListOfVehiclesUiState.Loaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is ListOfVehiclesUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
        }
    }



    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    BackHandler(drawerState.isOpen) {
        coroutineScope.launch { drawerState.close() }
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
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
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
                    } }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "menu"
                        )

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag(TestTagFAB),
                onClick = {
                    navigation.navigateToAddVehicle(vehicleId = -1L)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "plus"
                )
            }
        },
        content = {

            SideMenuNav(
                navigation = navigation,
                drawerState = drawerState,
                coroutineScope = coroutineScope,
                vehicleId = null) {

                ListOfVehiclesScreenContent(
                    paddingValues = it,
                    navigation = navigation,
                    screenState = screenState.value,
                    viewModel = viewModel
                )
            }

        },
    )

}



@Composable
fun ListOfVehiclesScreenContent(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    screenState: ScreenState<List<Vehicle>>,
    viewModel: ListOfVehiclesViewModel
) {

    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> ListOfVehicles(
                paddingValues = paddingValues,
                navigation = navigation,
                vehicles = it.data,
                viewModel = viewModel)
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }

}



@Composable
fun ListOfVehicles(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    vehicles: List<Vehicle>,
    viewModel: ListOfVehiclesViewModel
) {

    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(paddingValues)
            .testTag(TestTagLazyList)
    ) {
        
        if(vehicles.isEmpty()){
            item {
                EmptyRecordsText(
                    text = stringResource(id = R.string.empty_vehicle_list),
                    actionText = stringResource(id = R.string.add_new_vehicle)
                ) {
                    navigation.navigateToAddVehicle(vehicleId = -1L)
                }
            }
        }
        
        vehicles.forEach{
            item(key = it.vehicleId) {
                VehicleRow(vehicle = it,
                    modifier = Modifier.testTag(TestTagVehicle+it.name),
                    onInfoClick = { navigation.navigateToAddVehicle(vehicleId = it.vehicleId) },
                    onRowClick = { navigation.navigateToMainScreen(vehicleId = it.vehicleId!!, vehicleName = it.name) },
                    viewModel = viewModel
                )
            }
        }
        item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace)) }
    }
}


@Composable
fun VehicleRow(
    modifier: Modifier = Modifier,
    vehicle: Vehicle,
    onInfoClick: () -> Unit,
    onRowClick: () -> Unit,
    viewModel: ListOfVehiclesViewModel
) {

    Card(
        modifier = modifier
            .padding(MaterialTheme.spacing.medium)
            .clickable(onClick = onRowClick),
        colors = CardDefaults.cardColors(

        )
        ) {
        Column {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.FillWidth,
                    bitmap = viewModel.getImageBitmap(vehicle.imagePath!!, vehicle.name + ".jpg").asImageBitmap(),
                    contentDescription = "")

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = vehicle.name,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.align(Alignment.CenterVertically))
                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier.testTag("$TestTagVehicleInfoButton ${vehicle.vehicleId!!}")
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = "info")
                }
            }
        }
    }

}