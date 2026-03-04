package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagErrorMessage
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@Composable
fun CustomTextField(
    value: String?,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    underMessage: String? = null,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Ascii,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        TextField(
            value = value ?: "",
            onValueChange = onValueChange,
            label = { Text(text = label) },
            enabled = enabled,
            maxLines = maxLines,
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = modifier.fillMaxWidth(),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = maxLines == 1,
            isError = !errorMessage.isNullOrEmpty(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
        if (!underMessage.isNullOrEmpty()){
            Text(text = underMessage,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.small)
                    .alpha(if (underMessage.isNotEmpty()) 100f else 0f)
            )
        }
        if (!errorMessage.isNullOrEmpty()){
            Text(text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.small)
                    .alpha(if (errorMessage.isNotEmpty()) 100f else 0f)
                    .testTag(TestTagErrorMessage)
            )
        }
    }

}



@Composable
fun CustomOutlinedTextField(
    value: String?,
    label: String,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    underMessage: String? = null,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Ascii,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column {
        OutlinedTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            label = { Text(text = label) },
            maxLines = maxLines,
            readOnly = readOnly,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = modifier.fillMaxWidth(),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = maxLines == 1,
            isError = !errorMessage.isNullOrEmpty()
        )
        if (!underMessage.isNullOrEmpty()){
            Text(text = underMessage,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.small)
                    .alpha(if (underMessage.isNotEmpty()) 100f else 0f)
            )
        }
        if (!errorMessage.isNullOrEmpty()){
            Text(text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(start = MaterialTheme.spacing.small)
                    .alpha(if (errorMessage.isNotEmpty()) 100f else 0f)
                    .testTag(TestTagErrorMessage)
            )
        }
    }

}


