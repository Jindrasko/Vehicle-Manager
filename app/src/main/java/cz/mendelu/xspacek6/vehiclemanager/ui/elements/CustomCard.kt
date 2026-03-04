package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@Composable
fun CustomBasicCard(
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.small),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        content = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)) {
                content()
            }
        }
    )
}


@Composable
fun DashboardCard(
    icon: Painter,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier
        .padding(start = MaterialTheme.spacing.small, end = MaterialTheme.spacing.small, top = MaterialTheme.spacing.medium),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.small, horizontal = MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(painter = icon, contentDescription = "icon", modifier = Modifier.padding(end = MaterialTheme.spacing.medium))
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        }
    }
    CustomBasicCard {
        content()
    }

}