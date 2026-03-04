package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@Composable
fun CustomIconWithTextButton(
    modifier: Modifier = Modifier,
    title: String,
    icon: Int,
    bgColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = modifier
                .width(45.dp)
                .height(45.dp),
            contentPadding = PaddingValues(0.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = bgColor, contentColor = Color.White),
            shape = MaterialTheme.shapes.medium
            ) {
            Icon(painter = painterResource(id = icon), contentDescription = title)
        }
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}