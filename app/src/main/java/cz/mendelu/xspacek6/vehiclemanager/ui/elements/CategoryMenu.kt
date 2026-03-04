package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryMenu(
    categoryValue: MutableState<ExpenseCategory?>,
    onCategoryChanged: (ExpenseCategory) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = if (categoryValue.value != null) stringResource(id = categoryValue.value!!.title) else "",
            onValueChange = { },
            label = { Text(stringResource(id = R.string.category)) },
        )


        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            ExpenseCategory.values().forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(color = selectionOption.color, CircleShape)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Icon(
                                    painter = painterResource(id = selectionOption.icon),
                                    contentDescription = "My Icon",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.Center)
                                )
                            }
                            Text(
                                modifier = Modifier.padding(start = MaterialTheme.spacing.small),
                                text = stringResource(id = selectionOption.title),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }

                    },
                    onClick = {
                        categoryValue.value = selectionOption
                        onCategoryChanged(selectionOption)
                        expanded.value = false
                    }
                )
            }
        }
    }
}