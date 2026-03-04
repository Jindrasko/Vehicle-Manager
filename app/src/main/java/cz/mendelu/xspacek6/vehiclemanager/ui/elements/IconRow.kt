package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@Composable
fun IconRow(
    icon: Int,
    modifier: Modifier = Modifier,
    rowItems: @Composable () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        Icon(painter = painterResource(id = icon), contentDescription = "", modifier = Modifier.padding(end = MaterialTheme.spacing.medium) )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            rowItems()
        }

    }
}