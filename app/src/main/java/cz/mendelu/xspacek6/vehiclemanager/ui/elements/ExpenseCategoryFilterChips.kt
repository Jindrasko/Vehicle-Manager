package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing


@Composable
fun ExpenseCategoryFilterChips(
    selected: MutableList<Int?>,
    onClick: () -> Unit
) {

    LazyRow {
        ExpenseCategory.entries.forEach { category ->
            item {
                FilterChip(
                    selected = selected.contains(category.ordinal),
                    onClick = onClick,
                    label = {
                        Row{
                            Icon(painter = painterResource(id = category.icon), contentDescription = category.name)
                            Text(text = category.name)
                        }
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                )
            }
        }
    }

}