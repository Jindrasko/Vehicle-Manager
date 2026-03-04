package cz.mendelu.xspacek6.vehiclemanager.di

import cz.mendelu.xspacek6.vehiclemanager.ui.activities.AppIntroViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.activities.MainActivityViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addevent.AddEventViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addexpense.AddExpenseViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addrefill.AddRefillViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.addvehicle.AddVehicleViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.charts.ChartsViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.HomeScreenViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.dashboard.DashboardViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.events.EventsViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.expenses.ExpensesViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.homescreen.refills.RefillsViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.mileagelog.MileageLogViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.settings.SettingsViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.statistics.StatisticsViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.TripsViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.addtrip.AddTripViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.drive.DriveViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.trips.tripdetail.TripDetailViewModel
import cz.mendelu.xspacek6.vehiclemanager.ui.screens.vehicles.ListOfVehiclesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MainActivityViewModel(get()) }
    viewModel { AppIntroViewModel(get()) }

    viewModel { ListOfVehiclesViewModel(get()) }
    viewModel { AddVehicleViewModel(get()) }
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { RefillsViewModel(get(), get()) }
    viewModel { ExpensesViewModel(get(), get()) }
    viewModel { EventsViewModel(get()) }
    viewModel { TripsViewModel(get()) }

    viewModel { HomeScreenViewModel(get()) }
    viewModel { AddRefillViewModel(get()) }
    viewModel { AddExpenseViewModel(get()) }
    viewModel { AddEventViewModel(get()) }
    viewModel { AddTripViewModel(get()) }

    viewModel { MileageLogViewModel(get()) }
    viewModel { StatisticsViewModel(get(),get()) }
    viewModel { ChartsViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { DriveViewModel(get()) }
    viewModel { TripDetailViewModel(get()) }

}