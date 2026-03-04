package cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.constants.ExpenseCategory
import cz.mendelu.xspacek6.vehiclemanager.database.entities.Event
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.CustomRecordCard
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.EmptyRecordsText
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.EventProgressRow
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.EmptyCategory
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import cz.mendelu.xspacek6.vehiclemanager.utils.DateUtils
import cz.mendelu.xspacek6.vehiclemanager.utils.MathUtils
import org.koin.androidx.compose.getViewModel
import java.util.Date

@Composable
fun EventsScreen(
    vehicleId: Long,
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    isDone: MutableState<Boolean>,
    viewModel: EventsViewModel = getViewModel()) {

    viewModel.vehicleId = vehicleId

    val screenState: MutableState<ScreenState<List<Event>>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    val eventsUiState: EventsUiState<List<Event>>? by viewModel.eventsUiState.collectAsState()

    isDone.value.let {
        viewModel.doneFilter = it
        viewModel.reload()
    }

    eventsUiState?.let {
        when(it){
            is EventsUiState.Default -> {
                LaunchedEffect(it) {
                    viewModel.loadEvents()
                }
            }
            is EventsUiState.Loaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is EventsUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
        }
    }

    EventsScreenContent(
        navigation = navigation,
        paddingValues = paddingValues,
        vehicleId = vehicleId,
        screenState = screenState.value,
        viewModel = viewModel)

}


@Composable
fun EventsScreenContent(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    vehicleId: Long,
    screenState: ScreenState<List<Event>>,
    viewModel: EventsViewModel
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> ListOfEvents(
                navigation = navigation,
                paddingValues = paddingValues,
                vehicleId = vehicleId,
                events = it.data,
                viewModel = viewModel
            )
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}


@Composable
fun ListOfEvents(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    vehicleId: Long,
    events: List<Event>,
    viewModel: EventsViewModel
) {

    Column(modifier = Modifier.padding(paddingValues)) {
        LazyRow {
            ExpenseCategory.entries.forEach { category ->
                item {
                    FilterChip(
                        selected = viewModel.selectedCategories.value.contains(category.ordinal),
                        onClick = { viewModel.updateSelectedCategories(category.ordinal) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = category.icon),
                                    contentDescription = category.name
                                )
                                Text(text = category.name, modifier = Modifier.padding(start = MaterialTheme.spacing.small))
                            }
                        },
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                    )
                }
            }
            item {
                FilterChip(
                    selected = viewModel.selectedCategories.value.contains(null),
                    onClick = { viewModel.updateSelectedCategories(null) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.question_mark_24),
                                contentDescription = stringResource(id = R.string.other)
                            )
                            Text(text = stringResource(id = R.string.other), modifier = Modifier.padding(start = MaterialTheme.spacing.small))
                        }
                    },
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall)
                )
            }
        }


        LazyColumn {
            if (events.isEmpty()) {
                item {
                    EmptyRecordsText(
                        text = stringResource(id = R.string.empty_event_list),
                        actionText = stringResource(id = R.string.add_new_event)
                    ) {
                        navigation.navigateToAddEvent(
                            vehicleId = vehicleId,
                            eventId = -1L
                        )
                    }
                }
            }

            events.forEach {
                if (viewModel.selectedCategories.value.contains(it.eventCategory?.ordinal) || viewModel.selectedCategories.value.isEmpty()) {
                    item(key = it.eventId) {
                        EventRow(event = it, viewModel = viewModel) {
                            navigation.navigateToAddEvent(
                                vehicleId = it.vehicleId,
                                eventId = it.eventId,
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(MaterialTheme.spacing.omegaSpace)) }
        }
    }
}


@Composable
fun EventRow(
    event: Event,
    viewModel: EventsViewModel,
    onRowClick: () -> Unit,
) {
    CustomRecordCard(
        title = event.title,
        done = event.done,
        date = null,
        odometer = null,
        icon = event.eventCategory?.icon ?: R.drawable.question_mark_24,
        color = event.eventCategory?.color ?: EmptyCategory,
        cost = null,
        currency = "",
        onClick = onRowClick
    ) {

        EventProgress(event = event, lastMileage = viewModel.lastMileage)

        if(event.everyKm != null || event.everyMonths != null) {
            Text(
                text = stringResource(id = R.string.repeat_every)
                        + ": " + viewModel.getRepeatText(everyKm = event.everyKm, everyMonths = event.everyMonths) ,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
            )
        }

    }
}


@Composable
fun EventProgress(
    event: Event,
    lastMileage: Int?
) {
    if (!event.done) {
        if (event.onceAtKm != null) {
            EventProgressRow(
                progress = MathUtils.calculatePercentage(
                    event.initialKm.toFloat(),
                    event.onceAtKm!!.toFloat(),
                    lastMileage?.toFloat() ?: 0.0f
                ),
                value = (event.onceAtKm!! - (lastMileage ?: 0)).toString(),
                unit = stringResource(id = R.string.km)
            )
//                Text(text = (viewModel.calculatePercentage(event.initialKm.toFloat(), event.onceAtKm!!.toFloat())).toString())
        }

        if (event.onceDate != null) {
            EventProgressRow(
                progress = DateUtils.getPercentageOfPastDays(
                    event.initialDate,
                    event.onceDate!!
                ),
                value = DateUtils.getDaysBetween(Date().time, event.onceDate!!).toString(),
                unit = stringResource(id = R.string.days)
            )
//                Text(text = DateUtils.getPercentageOfPastDays(event.initialDate, event.onceDate!!).toString())
        }
    } else {
        if (event.finalKm != null) {
            Text(
                text = stringResource(id = R.string.completed_at) + " ${event.finalKm}" + stringResource(id = R.string.km),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .defaultMinSize(minWidth = 120.dp)
            )
        }
        if (event.finalDate != null) {
            Text(
                text = stringResource(id = R.string.completed) + " ${DateUtils.getDateString(event.finalDate!!)}",
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .defaultMinSize(minWidth = 120.dp)
            )
        }
    }


}