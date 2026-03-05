# VehicleManager

VehicleManager is an Android application designed to help users manage one or more vehicles in one place. It allows tracking of fuel refills, expenses, events, and trips with live GPS route recording — providing a full picture of a vehicle's history and costs.

---

## Technologies

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI Framework | Jetpack Compose + Material 3 |
| Architecture | MVVM (ViewModel + LiveData/StateFlow) |
| Dependency Injection | Koin |
| Local Database | Room |
| Preferences Storage | DataStore |
| Navigation | Jetpack Navigation Compose |
| Maps & Location | Google Maps SDK, Google Play Location Services |
| Image Loading | Coil |
| Charts | MPAndroidChart |
| Build System | Gradle (Kotlin DSL) |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 34 (Android 14) |

---

## Features

### Vehicle Management
- Add and manage multiple vehicles
- Each vehicle has its own dedicated dashboard

### Fuel Refills
- Log refills with amount, price, mileage, and fuel type
- Supported fuel types: Petrol, Diesel, LPG, CNG, Ethanol

### Expenses
- Record vehicle-related expenses by category:
  - Maintenance, Repair, Insurance, Tuning, Cleaning, Toll
- View full expense history per vehicle

### Events
- Add scheduled or past events for a vehicle (e.g. service appointments, inspections)

### Trip Tracking
- Record trips using a live GPS foreground service
- Route is drawn on an interactive Google Map
- View trip duration, distance, and route detail
- Pause and resume trip recording with a persistent notification

### Statistics & Charts
- View aggregated statistics per vehicle
- Visual charts for expenses and fuel costs over time (MPAndroidChart)

### Mileage Log
- Track mileage entries over time
