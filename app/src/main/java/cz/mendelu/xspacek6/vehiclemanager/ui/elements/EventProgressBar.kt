package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R


@Composable
fun EventProgressRow(
    progress: Float,
    value: String,
    unit: String,
) {
    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.`in`) + " $value $unit",
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .defaultMinSize(minWidth = 120.dp)
        )

        EventProgressBar(progress = progress)
    }
}


@Composable
fun EventProgressBar(progress: Float) {

    val progressColor = when {
        progress <= 0.2 -> Color.Red
        progress <= 0.5 -> Color.Yellow
        else -> Color.Green
    }

    val trackColor = when {
        progress < 0 -> Color.Red
        else -> ProgressIndicatorDefaults.linearTrackColor
    }

    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = progressColor,
        trackColor = trackColor,
        strokeCap = StrokeCap.Square
    )

}