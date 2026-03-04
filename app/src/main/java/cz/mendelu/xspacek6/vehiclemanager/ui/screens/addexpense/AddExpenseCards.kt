package cz.mendelu.xspacek6.vehiclemanager.ui.screens.addexpense

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Expense
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CategoryMenu
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomDatePicker
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomOutlinedTextField
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.IconRow

@Composable
fun ExpenseDateCard(
    expense: Expense,
    lastMileage: Int?
) {
    var odometer by rememberSaveable { mutableStateOf(if (expense.mileage != null) expense.mileage.toString() else "") }

    CustomBasicCard {
        IconRow(icon = R.drawable.today_24) {
            CustomDatePicker(
                label = stringResource(id = R.string.date),
                date = expense.date,
                onDateChange = {
                    expense.date = it!!
                }
            )
        }

        IconRow(icon = R.drawable.speed_24) {
            CustomOutlinedTextField(
                value = odometer,
                label = stringResource(id = R.string.mileage),
                underMessage = if (lastMileage != null) stringResource(id = R.string.odometer) + ": $lastMileage" else "",
                keyboardType = KeyboardType.Number,
                onValueChange = {
                    if(it.isNullOrEmpty()) {
                        odometer = ""
                        expense.mileage = null
                    } else if(it.toIntOrNull() != null){
                        odometer = it.filter { it.isDigit() }
                        expense.mileage = it.filter { it.isDigit() }.toInt()
                    }
                }
            )
        }
    }
}


@Composable
fun ExpenseBaseCard(
    expense: Expense,
    expenseTitleErrorMessage: String
) {
    var title by rememberSaveable { mutableStateOf(expense.title) }
    var parts by rememberSaveable { mutableStateOf(if (expense.costParts != null) expense.costParts.toString() else "") }
    var service by rememberSaveable { mutableStateOf(if (expense.costServices != null) expense.costServices.toString() else "") }
    val expenseCategory = rememberSaveable { mutableStateOf(expense.expenseCategory) }

    CustomBasicCard {
        IconRow(icon = R.drawable.title_24) {
            CustomOutlinedTextField(
                value = title,
                errorMessage = expenseTitleErrorMessage,
                label = stringResource(id = R.string.title),
                onValueChange = {
                    title = it
                    expense.title = it
                })
        }
        
        IconRow(icon = R.drawable.label_24) {
            CategoryMenu(
                categoryValue = expenseCategory,
                onCategoryChanged = {
                    expense.expenseCategory = it
                }
            )
        }
        

        IconRow(icon = R.drawable.settings_24) {
                CustomOutlinedTextField(
                    value = parts,
                    label = stringResource(id = R.string.parts),
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        if (it.isEmpty()){
                            parts = it
                            expense.costParts = null
                        } else {
                            when (it.toDoubleOrNull()) {
                                null -> {
                                    if(parts.isEmpty()){
                                        expense.costParts = null
                                    } else {
                                        expense.costParts = parts.toDouble()
                                    }
                                }
                                else -> {
                                    parts = it //new value
                                    expense.costParts = it.toDouble()
                                }
                            }
                        }
                    })
        }

        IconRow(icon = R.drawable.build_24) {
            CustomOutlinedTextField(
                value = service,
                label = stringResource(id = R.string.services),
                keyboardType = KeyboardType.Number,
                onValueChange = {
                    if (it.isEmpty()){
                        service = it
                        expense.costServices = null
                    } else {
                        when (it.toDoubleOrNull()) {
                            null -> {
                                if(service.isEmpty()){
                                    expense.costServices = null
                                } else {
                                    expense.costServices = service.toDouble()
                                }
                            }
                            else -> {
                                service = it //new value
                                expense.costServices = it.toDouble()
                            }
                        }
                    }
                })
        }

    }
}


@Composable
fun ExpenseNoteCard(
    expense: Expense
) {
    var note by rememberSaveable { mutableStateOf(expense.note) }

    CustomBasicCard {
        IconRow(icon = R.drawable.sticky_note_2_24) {
            CustomOutlinedTextField(
                value = note,
                label = stringResource(id = R.string.note),
                maxLines = 10,
                onValueChange = {
                    note = it
                    expense.note = it
                })
        }
    }
}

