package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.CheckmarkGreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing


@Composable
fun CustomRecordCard(
    title: String,
    date: String?,
    odometer: Int?,
    done: Boolean? = null,
    icon: Int,
    color: Color,
    cost: Double?,
    currency: String,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = MaterialTheme.spacing.large)
                .clickable { onClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.medium,
                        top = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.small
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (done != null) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = MaterialTheme.spacing.medium),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        Text(
                            text = title,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        if(done) {
                            Icon(
                                painter = painterResource(id = R.drawable.check_circle_24),
                                contentDescription = stringResource(id = R.string.done),
                                tint = CheckmarkGreen)
                        }
                    }

                } else {
                    Column(modifier = Modifier.weight(1f, fill = true)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (currency.isNotEmpty() || cost != null) {
                            Text(
                                text = if (cost != null) String.format(
                                    "%.2f",
                                    cost
                                ) + " $currency" else "--- $currency"
                            )
                        } else {
                            Text(text = "")
                        }
                    }
                    Column(modifier = Modifier.weight(0.65f).wrapContentWidth(Alignment.End).padding(top = MaterialTheme.spacing.extraSmall, bottom = MaterialTheme.spacing.small)) {
                        if (date != null) {
                            Text(
                                text = date,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                        if (odometer != null){
                            Text(
                                text = stringResource(id = R.string.odometer_short) + ": $odometer",
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(
                top = 0.dp,
                bottom = MaterialTheme.spacing.medium,
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
            )) {
                content()
            }

        }

        Box(
            modifier = Modifier
                .offset(x = 0.dp, y = MaterialTheme.spacing.medium)
                .size(55.dp)
                .background(MaterialTheme.colorScheme.background, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(color = color, CircleShape)
                    .align(Alignment.Center)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "My Icon",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Center)
                )
            }
        }

    }


}


@Composable
fun CustomRecordDescriptionText(
    icon: Int,
    title: String,
    value: String?
) {
    Row(modifier = Modifier.padding(top = MaterialTheme.spacing.extraSmall)) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "title",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = if (value.isNullOrEmpty()) title else "$title: $value",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = MaterialTheme.spacing.small)
        )
    }
}