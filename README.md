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

## Implementation highlights

### TrackingService (foreground GPS recording)
- A `LifecycleService` that runs trip recording in the background with a persistent notification.
- Handles start/pause/stop via intent actions (`ACTION_START_SERVICE`, `ACTION_PAUSE_SERVICE`, `ACTION_STOP_SERVICE`).
- Streams state through `LiveData`: `isTracking`, `pathPoints`, `timeDriveInMillis`.
- Collects frequent location updates from Fused Location Provider and builds polylines for map rendering.  
Files: `services/TrackingService.kt`

### startTimer() (background timer + segment handling)
- Starts a coroutine loop updating elapsed time while tracking is active.
- Creates a new empty polyline segment on each start/resume (`addEmptyPolyline()`).
- Emits `timeDriveInMillis` continuously and `timeDriveInSeconds` once per second.
- On pause, it accumulates elapsed time into `driveTime` so resume continues correctly.  
File: `services/TrackingService.kt` (`startTimer`)

### updateLocationTracking() + locationCallback (live GPS ingestion)
- When tracking starts, requests high accuracy location updates at a configured interval.
- When tracking stops/pauses, removes updates to save battery.
- `locationCallback` iterates received locations and appends them into the active polyline.
- Route is stored as `List<List<LatLng>>` so pauses create separate route segments.  
File: `services/TrackingService.kt`

### updateNotificationTrackingState() (pause/resume from notification)
- Dynamically swaps the notification action between **Pause** and **Resume**.
- Uses `PendingIntent.getService(...)` to send commands back into the service.
- Updates the existing notification in-place via `NotificationManager.notify(...)`.
- Keeps the user in control of tracking without reopening the app.  
File: `services/TrackingService.kt`

### DriveScreen (Compose UI observing background state)
- The driving UI observes `TrackingService.pathPoints` and redraws the route live on the map.
- Observes `TrackingService.timeDriveInMillis` to show current trip duration.
- Auto-updates camera position to follow the latest recorded point.
- Wraps user cancellation in a confirmation dialog before stopping the service.  
File: `ui/screens/trips/drive/DriveScreen.kt`

### endAndSaveDrive() (derive metrics + persist trip)
- Removes empty polyline segments (created by pause/resume) before saving.
- Computes total distance by summing segment distances (point-to-point using `Location.distanceBetween`).
- Calculates average speed from (distance / duration).
- Persists a `Trip` entity to Room including the recorded `pathPoints`.  
File: `ui/screens/trips/drive/DriveViewModel.kt`

### Room persistence for routes (TypeConverter)
- The `Trip` entity stores the whole route as `pathPoints: List<List<LatLng>>`.
- A Room `TypeConverter` serializes/deserializes the nested list using Gson.
- This keeps trips fully replayable (route + metrics) from the local database.  
Files: `database/entities/Trip.kt`, `database/Converters.kt`


## Preview
<img width="370" height="729" alt="02" src="https://github.com/user-attachments/assets/1a98852e-6bbe-4170-bc54-8c791cfb19a0" /> <img width="370" height="729" alt="03" src="https://github.com/user-attachments/assets/80569917-e93d-4cb1-a388-f0661d68c24f" /> <img width="370" height="731" alt="04" src="https://github.com/user-attachments/assets/f6280ae6-a899-4392-9b90-86e2455c5fc3" /> <img width="371" height="729" alt="05" src="https://github.com/user-attachments/assets/f56b9a4c-667c-42f9-a5f2-f58d20b4e371" /> <img width="367" height="731" alt="06" src="https://github.com/user-attachments/assets/df567635-84e0-4721-bc56-b2bb8202f2ad" /> <img width="367" height="729" alt="07" src="https://github.com/user-attachments/assets/b58b440b-7958-4a32-849f-d834232c31ce" /> 

### GPS Tracking
https://youtu.be/az4mmNLkuco




