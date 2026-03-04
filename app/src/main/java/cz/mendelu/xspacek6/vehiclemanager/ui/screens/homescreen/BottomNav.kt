package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.navigation.Destination


sealed class BottomNav(var title: Int, var icon: Int, var screenRoute: Destination){

    object Dashboard : BottomNav(R.string.dashboard, R.drawable.home_24, Destination.DashboardScreen)
    object Refills: BottomNav(R.string.refills, R.drawable.local_gas_station_24, Destination.RefillsScreen)
    object Expenses: BottomNav(R.string.expenses, R.drawable.receipt_24, Destination.ExpensesScreen)
    object Events: BottomNav(R.string.events, R.drawable.event_note_24, Destination.EventsScreen)

}