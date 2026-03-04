package cz.mendelu.xspacek6.vehiclemanager.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomBasicCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomTextField
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(
    navigation: INavigationRouter,
    viewModel: SettingsViewModel = getViewModel()
) {

    val screenState: MutableState<ScreenState<SettingsData>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    viewModel.settingsUiState.value.let {
        when(it){
            is SettingsUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadSettingsData()
                }
            }
            is SettingsUiState.SettingsLoaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is SettingsUiState.SettingsSaved -> {
                LaunchedEffect(it) {
                    navigation.returnBack()
                }
            }
        }
    }


    BackArrowScreen(
        topBarText = stringResource(id = R.string.settings),
        content = {

            SettingsScreenContent(
                screenState = screenState.value
            )

        },
        actions = {
            IconButton(onClick = {
                viewModel.saveSettingsData()
            }) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "Save")
            }
        },
        onBackClick = { navigation.returnBack() }
    )

}


@Composable
fun SettingsScreenContent(
    screenState: ScreenState<SettingsData>
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> Settings(
                settingsData = it.data
            )
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}



@Composable
fun Settings(
    settingsData: SettingsData
) {
    var currency by rememberSaveable{ mutableStateOf(settingsData.currency) }

    CustomBasicCard {

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.currency)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                CustomTextField(
                    value = currency,
                    label = stringResource(id = R.string.currency),
                    onValueChange = {
                        currency = it
                        settingsData.currency = it
                    })
            }
        }

    }

}




