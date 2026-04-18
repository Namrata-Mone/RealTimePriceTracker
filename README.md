# Real Time Price Tracker App

A Jetpack Compose–based Android app built as part of a coding exercise to demonstrate real-time data handling, reactive UI updates, and clean architecture practices.

The app simulates stock price movements, streams them through a WebSocket echo service, and reflects those updates live across multiple screens.

---

## Overview

* Displays a list of **25 stock symbols** in a scrollable feed
* Generates and publishes price updates every 2 seconds
* Uses a WebSocket echo endpoint to simulate a streaming backend
* Updates UI based on echoed responses
* Keeps the list sorted by **highest price**
* Indicates price movement direction (up/down)
* Navigates to a dedicated details screen per symbol
* Supports deep linking via `stocks://symbol/{symbol}`
* Supports **light/dark + dynamic theming** (Material You where available)

---

## Stack

* Kotlin
* Jetpack Compose (Material 3)
* Navigation Compose
* Coroutines + Flow / StateFlow
* OkHttp (WebSocket)
* Kotlinx Serialization
* Hilt (DI)
* MVVM with layered architecture

---

## Architectural approach

The app follows a **layered architecture with feature-based organization**, keeping responsibilities isolated while allowing shared reactive state across the app.

### Layers

* **core**

    * Shared utilities (symbols, constants, metadata)
    * Centralized UI system (theme, reusable components, dynamic theming)

* **data**

    * WebSocket integration (OkHttp)
    * Data models and mapping
    * Repository implementation

* **domain**

    * Repository contract
    * Domain models
    * Use cases encapsulating business logic

* **presentation**

    * Feature modules (`feed`, `details`)
    * ViewModels
    * UI state holders
    * Compose UI

* **di**

    * Dependency injection setup

---

### Data flow

* A single repository instance owns the WebSocket connection
* Once started:

    * price updates are generated per symbol on a fixed interval
    * updates are sent to `wss://ws.postman-echo.com/raw`
    * echoed responses are parsed into domain models
* Repository exposes a `StateFlow` of current prices
* ViewModels consume this via use cases
* UI collects and renders updates reactively

This ensures:

* a single active stream across the app
* no duplicated connections
* consistent state between feed and details

---

### Feature design

Each feature is scoped independently:

**Feed**

* Aggregates all symbols
* Uses use cases for:

    * observing prices
    * observing connection state
    * controlling feed lifecycle

**Details**

* Focused on a single symbol
* Retrieves argument via `SavedStateHandle`
* Subscribes to the shared price stream
* Does not initiate its own data source

---

## Screens

### Feed

* `LazyColumn` listing all symbols
* Each row shows:

    * symbol
    * current price
    * directional indicator (↑ / ↓)
* Sorted by price (descending)
* Top bar includes:

    * connection status indicator
    * Start / Stop control
* Row tap navigates to details

---

### Details

* Displays selected symbol
* Shows current price with movement indicator
* Includes a short description
* Observes shared live data

---

## Additional work

* Price change highlight (temporary color flash)
* Dynamic theming (Material You) + light/dark support
* Deep linking (`stocks://symbol/{symbol}`)
* Unit tests for ViewModels
* Basic Compose UI test coverage

---

## Deep link

```bash
adb shell am start -a android.intent.action.VIEW -d "stocks://symbol/AAPL"
```

---

## Setup

### Requirements

* Android Studio (latest stable)
* JDK 11+
* Android SDK / emulator

### Run

1. Open project in Android Studio
2. Sync Gradle
3. Run on device or emulator

---

## Tests

```bash
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

---

## Project structure

```text
app/src/main/java/com/multibank/realtimepricetracker/
  core/
    common/
    ui/
  data/
    datasource/
    model/
    mapper/
    repository/
  domain/
    model/
    repository/
    usecase/
  presentation/
    feed/
    details/
    navigation/
  di/
  MainActivity.kt
```
