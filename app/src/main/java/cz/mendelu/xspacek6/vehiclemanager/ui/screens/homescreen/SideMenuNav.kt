package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun SideMenuRow(
    onRowClick: () -> Unit,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    title: Int,
    icon: Int
) {

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    drawerState.close()
                }
                onRowClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = ""
        )

        Text(
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.medium),
            text = stringResource(id = title),
            textAlign = TextAlign.Center
        )
    }

}



@Composable
fun SideMenuNav(
    navigation: INavigationRouter,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    vehicleId: Long?,
    screenContent: @Composable () -> Unit
) {


    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxSize(),
        drawerContent = {


            ModalDrawerSheet(
                drawerShape = ShapeDefaults.Medium,
                drawerContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
                modifier = Modifier.width(250.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(
                            top = MaterialTheme.spacing.extraLarge,
                            bottom = MaterialTheme.spacing.extraLarge,
                            start = MaterialTheme.spacing.small,
                            end = MaterialTheme.spacing.small
                        ),
//                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(MaterialTheme.spacing.small)
                    )

                    Divider(modifier = Modifier.padding(MaterialTheme.spacing.small))

                    if (vehicleId != null) {
                        SideMenuRow(
                            onRowClick = { navigation.returnBack() },
                            drawerState = drawerState,
                            coroutineScope = coroutineScope,
                            title = R.string.vehicles,
                            icon = R.drawable.directions_car_24
                        )


                        SideMenuRow(
                            onRowClick = {
                                navigation.navigateToMileageLog(vehicleId!!)
                            },
                            drawerState = drawerState,
                            coroutineScope = coroutineScope,
                            title = R.string.mileage_log,
                            icon = R.drawable.format_list_bulleted_24
                        )



                        SideMenuRow(
                            onRowClick = {
                                navigation.navigateToTrips(vehicleId!!)
                            },
                            drawerState = drawerState,
                            coroutineScope = coroutineScope,
                            title = R.string.trips,
                            icon = R.drawable.route_24
                        )



                        SideMenuRow(
                            onRowClick = {
                                navigation.navigateToStatistics(vehicleId!!)
                            },
                            drawerState = drawerState,
                            coroutineScope = coroutineScope,
                            title = R.string.statistics,
                            icon = R.drawable.bar_chart_24
                        )


                        SideMenuRow(
                            onRowClick = {
                                navigation.navigateToCharts(vehicleId!!)
                            },
                            drawerState = drawerState,
                            coroutineScope = coroutineScope,
                            title = R.string.charts,
                            icon = R.drawable.show_chart_24
                        )
                    }


                    SideMenuRow(
                        onRowClick = { navigation.navigateToCalculator() },
                        drawerState = drawerState,
                        coroutineScope = coroutineScope,
                        title = R.string.calculator,
                        icon = R.drawable.calculate_24
                    )

                    SideMenuRow(
                        onRowClick = { navigation.navigateToSettings() },
                        drawerState = drawerState,
                        coroutineScope = coroutineScope,
                        title = R.string.settings,
                        icon = R.drawable.settings_24
                    )

                }

            }

        }
    ) {
        screenContent()
    }

}
