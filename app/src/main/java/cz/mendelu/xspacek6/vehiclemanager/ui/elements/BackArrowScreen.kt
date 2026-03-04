package cz.mendelu.xspacek6.vehiclemanager.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagBackButton
import cz.mendelu.xspacek6.vehiclemanager.constants.TestConstants.TestTagColumn
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackArrowScreen(
    topBarText: String,
    disablePadding: Boolean = false,
    drawFullScreenContent: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    onBackClick: () -> Unit,
    content: @Composable () -> Unit,) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = topBarText,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(start = 0.dp)
                                .weight(1.5f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.testTag(TestTagBackButton),
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton
    ) {
        if (!drawFullScreenContent) {
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                modifier = Modifier.padding(paddingValues = it)
                .testTag(TestTagColumn)
            ) {
                item {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .padding(if (!disablePadding) MaterialTheme.spacing.medium else 0.dp)
                    ) {
                        content()
                    }
                }
            }
        } else {
            content()
        }
    }
}

