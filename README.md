# FinalProject — Weather App (Android • Kotlin • Jetpack Compose)

An Android weather application built with **Kotlin** and **Jetpack Compose**. It provides current weather, 5‑day forecast, favorites, map-based city selection, and scheduled weather alerts with sound + Snooze/Dismiss actions.

## Features

- **Current weather** by device location
- **5‑day forecast** (hourly forecast aggregated into daily rows)
- **Favorites**: save/remove favorite locations (Room)
- **Map picker**: select locations using **OSMDroid**
- **Weather alerts**:
  - Notification channels (silent vs sound)
  - Full‑screen alert experience with **Snooze** and **Dismiss**
  - Scheduled via **WorkManager**

## Tech stack

- **UI**: Jetpack Compose (Material 3), Navigation Compose
- **Architecture**: layered approach (`presentation` / `domain` / `data`)
- **Networking**: Retrofit + Gson, OkHttp logging interceptor
- **Images**: Coil (Compose)
- **Local storage**: Room (+ KSP)
- **Background work**: WorkManager
- **Maps**: OSMDroid
- **Testing**: JUnit4, kotlinx-coroutines-test, AndroidX instrumentation (Room DAO)

## Project structure

Main packages (under `app/src/main/java/iti/student/finalproject/`):

- **`presentation/`**: Compose screens, ViewModels, UI components
- **`domain/`**: models, repository interfaces, mappers
- **`data/`**: repository implementations, local/remote data sources, Room entities/DAOs, DTOs
- **`worker/`**: WorkManager workers (alerts/notifications)

## Requirements

- **Android Studio** (recent stable)
- **JDK 11**
- **minSdk 24**, **targetSdk 36**, **compileSdk 36**

## Setup & run

1. Clone the project and open it in Android Studio.
2. Sync Gradle.
3. Run the `app` configuration on an emulator or a real device.

### Permissions

The app requests:

- **Location**: `ACCESS_FINE_LOCATION`
- **Notifications** (Android 13+): `POST_NOTIFICATIONS`
- **Network**: `INTERNET`, `ACCESS_NETWORK_STATE`

## API key (OpenWeather)

This project calls OpenWeather endpoints via Retrofit (`api.openweathermap.org`).

- Current implementation contains an API key in code (`data/remote/api/RetrofitInstance.kt`).
- Recommended for production: move the key to a secure source (e.g., `local.properties`, CI secret, or encrypted storage) and inject it via `BuildConfig`.

## Alerts behavior

- **Sound alerts** use a dedicated notification channel with alarm usage attributes.
- **Snooze** schedules the alert again after a short delay.
- **Dismiss** cancels the active notification and removes the alert schedule/data.

Relevant files:

- `worker/AlertNotificationWorker.kt`
- `presentation/alarm/AlertRingingActivity.kt`

## Testing

### Unit tests

Run unit tests (Windows PowerShell):

```powershell
cd "d:/ITI/Courses/Android (Kotlin)/FinalProject"
.\gradlew :app:testDebugUnitTest
```

### Instrumented tests (DAO / Room)

Run on an emulator/device:

```powershell
.\gradlew :app:connectedDebugAndroidTest
```

## Notes

- Generated build outputs (e.g., `app/build/`) should not be committed.
- If you face notification issues on newer Android versions, ensure notification permission is granted and channels are enabled in system settings.

