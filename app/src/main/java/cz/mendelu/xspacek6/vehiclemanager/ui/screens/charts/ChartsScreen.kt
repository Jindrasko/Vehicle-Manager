package cz.mendelu.xspacek6.vehiclemanager.ui.screens.charts


import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.getString
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import cz.mendelu.xspacek6.vehiclemanager.R
import cz.mendelu.xspacek6.vehiclemanager.VehicleManagerApplication.Companion.appContext
import cz.mendelu.xspacek6.vehiclemanager.models.ScreenState
import cz.mendelu.xspacek6.vehiclemanager.navigation.INavigationRouter
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.BackArrowScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.ErrorScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.elements.LoadingScreen
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.CleaningColor
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.DarkOnSecondary
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.EmptyCategory
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.InsuranceColor
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.MaintenanceColor
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.RepairColor
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.TollColor
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.TuningColor
import cz.mendelu.xspacek6.vehiclemanager.ui.theme.spacing
import org.koin.androidx.compose.getViewModel


@Composable
fun ChartsScreen(
    vehicleId: Long?,
    navigation: INavigationRouter,
    viewModel: ChartsViewModel = getViewModel()
) {
    viewModel.vehicleId = vehicleId

    val screenState: MutableState<ScreenState<ChartsStateData>> = rememberSaveable {
        mutableStateOf(ScreenState.Loading())
    }

    val showDialog = remember { mutableStateOf(false) }

    viewModel.chartsUiState.value.let {
        when(it){
            is ChartsUiState.Default -> {
                screenState.value = ScreenState.Loading()
                LaunchedEffect(it) {
                    viewModel.loadData()
                }
            }
            is ChartsUiState.Loaded -> {
                screenState.value = ScreenState.DataLoaded(it.data)
            }
            is ChartsUiState.Error -> {
                screenState.value = ScreenState.Error(it.error)
            }
        }
    }

    BackArrowScreen(
        topBarText = stringResource(id = R.string.charts),
        onBackClick = { navigation.returnBack() },
        actions = {
            IconButton(onClick = { showDialog.value = !showDialog.value }) {
                Icon(
                    painter = painterResource(id = R.drawable.filter_alt_24),
                    contentDescription = "filter")
            }
        }
    ) {
        ChartsScreenContent(
            screenState = screenState.value,
            viewModel = viewModel
        )
    }

    if(showDialog.value) {
        ChartsFilterDialog(showDialog = showDialog, viewModel = viewModel)
    }

}

@Composable
fun ChartsScreenContent(
    screenState: ScreenState<ChartsStateData>,
    viewModel: ChartsViewModel
) {
    screenState.let {
        when(it){
            is ScreenState.DataLoaded -> ChartsCards(
                stateData = it.data,
                currency = viewModel.currency
            )
            is ScreenState.Error -> ErrorScreen(text = stringResource(id = it.error))
            is ScreenState.Loading -> LoadingScreen()
        }
    }
}



@Composable
fun ChartsCards(
    stateData: ChartsStateData,
    currency: String
) {

//    Text(text = stringResource(id = R.string.expenses_by_category), style = MaterialTheme.typography.titleMedium)
    ChartsTextField(title = stringResource(id = R.string.expenses_by_category))
    ExpensesPieChart(expenses = stateData.expensesPieChartData)

    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))

//    Text(text = stringResource(id = R.string.mileage), style = MaterialTheme.typography.titleMedium)
    ChartsTextField(title = stringResource(id = R.string.mileage))
    MileageChart(
        mileages = stateData.mileagesChartData,
        backGroundColor = MaterialTheme.colorScheme.background,
        textColor = MaterialTheme.colorScheme.onBackground,
        primaryColor = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))

//    Text(text = stringResource(id = R.string.expenses), style = MaterialTheme.typography.titleMedium)
    ChartsTextField(title = stringResource(id = R.string.expenses))
    ExpensesByMonth(
        expenses = stateData.expensesByMonth,
        backGroundColor = MaterialTheme.colorScheme.background,
        textColor = MaterialTheme.colorScheme.onBackground,
        primaryColor = MaterialTheme.colorScheme.primary,
        currency = currency
    )

    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))

//    Text(text = stringResource(id = R.string.fuel_price), style = MaterialTheme.typography.titleMedium)
    ChartsTextField(title = stringResource(id = R.string.fuel_price))
    FuelPriceChart(
        fuelPrices = stateData.fuelPriceChartData,
        backGroundColor = MaterialTheme.colorScheme.background,
        textColor = MaterialTheme.colorScheme.onBackground,
        primaryColor = MaterialTheme.colorScheme.primary,
        currency = currency
    )
    
}


@Composable
fun ChartsTextField(
    title: String
) {
    Card(modifier = Modifier
        .padding(start = MaterialTheme.spacing.small, end = MaterialTheme.spacing.small),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = MaterialTheme.spacing.small, horizontal = MaterialTheme.spacing.medium))
    }

}



@Composable
fun ExpensesByMonth(
    expenses: List<ChartDuo>,
    backGroundColor: Color,
    textColor: Color,
    primaryColor: Color,
    currency: String
) {
    Column(
        modifier = Modifier
            .padding(MaterialTheme.spacing.small)
            .heightIn(0.dp, 320.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Crossfade(targetState = expenses, label = "") { chartData ->
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    BarChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        this.description.isEnabled = false
                        this.setBackgroundColor(backGroundColor.toArgb())

                        this.axisRight.isEnabled = false

                        this.isDragEnabled = true
                        this.isDragXEnabled = true
                        this.isDragYEnabled = false

                        this.isScaleYEnabled = false

                        this.legend.isEnabled = true
                        this.legend.textColor = textColor.toArgb()
                        this.legend.textSize = 14F

                        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        this.xAxis.gridColor = textColor.toArgb()
                        this.xAxis.granularity = 1f
                        this.xAxis.textColor = textColor.toArgb()
                        this.xAxis.valueFormatter = IndexAxisValueFormatter(
                            chartData.map {
                                it.title
                            }
                        )

                        this.axisLeft.textColor = textColor.toArgb()
                        this.axisLeft.gridColor = textColor.toArgb()
                    }
                },
                update = { barChart ->

                    val barEntries = chartData.mapIndexed { index, chartDuo ->
                        BarEntry(index.toFloat(), chartDuo.value, chartDuo.title)
                    }

                    val dataSet = BarDataSet(barEntries, currency)
                    dataSet.valueTextSize = 12f
                    dataSet.valueTextColor = textColor.toArgb()
                    dataSet.color = primaryColor.toArgb()
//                    dataSet.setCircleColor(primaryColor.toArgb())

                    val barData = BarData(dataSet)
                    barChart.data = barData
                }
            )

        }
    }
}


@Composable
fun FuelPriceChart(
    fuelPrices: List<ChartDuo>,
    backGroundColor: Color,
    textColor: Color,
    primaryColor: Color,
    currency: String
) {
    Column(
        modifier = Modifier
            .padding(MaterialTheme.spacing.small)
            .heightIn(0.dp, 320.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Crossfade(targetState = fuelPrices, label = "") { chartData ->
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    LineChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        this.description.isEnabled = false
                        this.setBackgroundColor(backGroundColor.toArgb())

                        this.axisRight.isEnabled = false

                        this.isDragEnabled = true
                        this.isDragXEnabled = true
                        this.isDragYEnabled = false

                        this.isScaleYEnabled = false

                        this.legend.isEnabled = true
                        this.legend.textColor = textColor.toArgb()
                        this.legend.textSize = 14F

                        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        this.xAxis.gridColor = textColor.toArgb()
                        this.xAxis.granularity = 1f
                        this.xAxis.textColor = textColor.toArgb()
                        this.xAxis.valueFormatter = IndexAxisValueFormatter(
                            chartData.map {
                                it.title
                            }
                        )

                        this.axisLeft.textColor = textColor.toArgb()
                        this.axisLeft.gridColor = textColor.toArgb()
                    }
                },
                update = { lineChart ->

                    val entries = chartData.mapIndexed { index, dataPoint ->
                        Entry(index.toFloat(), dataPoint.value, dataPoint.title)
                    }


                    val dataSet = LineDataSet(entries, currency)
                    dataSet.valueTextSize = 12f
                    dataSet.valueTextColor = textColor.toArgb()
                    dataSet.color = primaryColor.toArgb()
                    dataSet.setCircleColor(primaryColor.toArgb())

                    val lineData = LineData(dataSet)
                    lineChart.data = lineData
                }
            )
        }
    }
}


@Composable
fun MileageChart(
    mileages: List<ChartDuo>,
    backGroundColor: Color,
    textColor: Color,
    primaryColor: Color
) {
    Column(
        modifier = Modifier
            .padding(MaterialTheme.spacing.small)
            .heightIn(0.dp, 320.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Crossfade(targetState = mileages, label = "") { chartData ->
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    LineChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        this.description.isEnabled = false
                        this.setBackgroundColor(backGroundColor.toArgb())

                        this.axisRight.isEnabled = false

                        this.isDragEnabled = true
                        this.isDragXEnabled = true
                        this.isDragYEnabled = false

                        this.isScaleYEnabled = false

                        this.legend.isEnabled = true
                        this.legend.textColor = textColor.toArgb()
                        this.legend.textSize = 14F

                        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        this.xAxis.gridColor = textColor.toArgb()
                        this.xAxis.granularity = 1f
                        this.xAxis.textColor = textColor.toArgb()
                        this.xAxis.valueFormatter = IndexAxisValueFormatter(
                            chartData.map {
                                it.title
                            }
                        )

                        this.axisLeft.textColor = textColor.toArgb()
                        this.axisLeft.gridColor = textColor.toArgb()
                    }
                },
                update = { lineChart ->
                    val entries = chartData.mapIndexed { index, dataPoint ->
                        Entry(index.toFloat(), dataPoint.value, dataPoint.title)
                    }

                    val dataSet = LineDataSet(entries, getString(appContext, R.string.km))
                    dataSet.valueTextSize = 12f
                    dataSet.valueTextColor = textColor.toArgb()
                    dataSet.color = primaryColor.toArgb()
                    dataSet.setCircleColor(primaryColor.toArgb())

                    val lineData = LineData(dataSet)
                    lineChart.data = lineData
                }
            )
        }

    }
}


@Composable
fun ExpensesPieChart(
    expenses: ExpensesPieChartData
) {
    Column(
        modifier = Modifier
            .padding(MaterialTheme.spacing.small)
            .heightIn(0.dp, 320.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        Crossfade(targetState = expenses, label = "cross fade") { chartData ->
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    PieChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        this.description.isEnabled = false

                        this.isDrawHoleEnabled = false

//                        this.minAngleForSlices = 20f
                        this.setDrawEntryLabels(false)

                        this.legend.isEnabled = true
                        this.legend.textColor = DarkOnSecondary.toArgb()
                        this.legend.textSize = 14F
                        this.legend.isWordWrapEnabled = true
                        this.legend.horizontalAlignment =
                            Legend.LegendHorizontalAlignment.CENTER
                        this.setEntryLabelColor(resources.getColor(R.color.white))
                    }
                },
                update = { pieChart ->
                    val entries: MutableList<PieEntry> = mutableListOf()

                    if (chartData.maintenance > 0) {
                        entries.add(
                            PieEntry(
                                chartData.maintenance,
                                getString(appContext, R.string.maintenance)
                            )
                        )
                    }
                    if (chartData.repair > 0) {
                        entries.add(
                            PieEntry(
                                chartData.repair,
                                getString(appContext, R.string.repair)
                            )
                        )
                    }
                    if (chartData.insurance > 0) {
                        entries.add(
                            PieEntry(
                                chartData.insurance,
                                getString(appContext, R.string.insurance)
                            )
                        )
                    }
                    if (chartData.tuning > 0) {
                        entries.add(
                            PieEntry(
                                chartData.tuning,
                                getString(appContext, R.string.tuning)
                            )
                        )
                    }
                    if (chartData.cleaning > 0) {
                        entries.add(
                            PieEntry(
                                chartData.cleaning,
                                getString(appContext, R.string.cleaning)
                            )
                        )
                    }
                    if (chartData.toll > 0) {
                        entries.add(
                            PieEntry(
                                chartData.toll,
                                getString(appContext, R.string.toll)
                            )
                        )
                    }
                    if (chartData.other > 0) {
                        entries.add(
                            PieEntry(
                                chartData.other,
                                getString(appContext, R.string.other)
                            )
                        )
                    }

                    val dataSet = PieDataSet(entries, "")

                    dataSet.colors = arrayListOf()
                    if (chartData.maintenance > 0) {
                        dataSet.colors.add(MaintenanceColor.toArgb())
                    }
                    if (chartData.repair > 0) {
                        dataSet.colors.add(RepairColor.toArgb())
                    }
                    if (chartData.insurance > 0) {
                        dataSet.colors.add(InsuranceColor.toArgb())
                    }
                    if (chartData.tuning > 0) {
                        dataSet.colors.add(TuningColor.toArgb())
                    }
                    if (chartData.cleaning > 0) {
                        dataSet.colors.add(CleaningColor.toArgb())
                    }
                    if (chartData.toll > 0) {
                        dataSet.colors.add(TollColor.toArgb())
                    }
                    if (chartData.other > 0) {
                        dataSet.colors.add(EmptyCategory.toArgb())
                    }

                    dataSet.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
                    dataSet.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
                    dataSet.sliceSpace = 2f
                    dataSet.valueTextColor = pieChart.context.resources.getColor(R.color.white)
                    dataSet.valueTextSize = 18f
                    dataSet.valueTypeface = Typeface.DEFAULT_BOLD

                    val d = PieData(dataSet)

                    pieChart.data = d
                }
            )

        }
    }
}