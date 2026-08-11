# Mango Store

An Android app that lists a product catalogue from [fakestoreapi.com](https://fakestoreapi.com), lets you save products as favorites, and keeps those favorites on the device.

Built with Kotlin, Coroutines, Jetpack Compose and a multi-module Clean-ish architecture.

## What it does

Three tabs, reachable from a bottom navigation bar:

- **Products** — the full catalogue, loaded from the network. Tapping the heart on a card saves or removes a favorite.
- **Favorites** — everything you have saved, most recent first. You can remove items from here too.
- **Profile** — the current user's details plus a live count of saved favorites.

Every screen handles the three states explicitly: loading, error and content, with a retry action wherever retrying can change the outcome. Favorites survive closing the app, because they live in a local database rather than in memory.

The app ships in English and Spanish, and follows the system's light or dark theme.

## Getting started

**Requirements:** JDK 17, Android Studio with the Android SDK 37 installed, and a device or emulator running Android 8.0 (API 26) or newer.

```bash
# install a debug build on a connected device or running emulator
./gradlew installDebug

# run the unit tests (no device needed)
./gradlew test

# run the instrumented tests (needs a device or emulator)
./gradlew connectedDebugAndroidTest
```

No API keys or local configuration are needed — the API is public.

## Architecture

The project is split into eight Gradle modules. The rule that governs all of them is simple: **dependencies point inwards, towards the domain, and the domain knows nothing about Android.**

```
        app
         │
    ┌────┴────┬──────────┐
    │         │          │
 feature/  feature/   feature/
 products  favorites  profile
    │         │          │
    └────┬────┴──────────┘
         │
      core/ui
         │
      domain  ◄──── data
```

| Module | What lives there |
| --- | --- |
| `:app` | Application, main activity, navigation between tabs |
| `:feature:products` · `:feature:favorites` · `:feature:profile` | One screen each: its UI, its state and its ViewModel |
| `:core:ui` | Design system, theme, shared components, all user-facing strings |
| `:core:testing` | Test rules, fakes and test data shared by every module |
| `:domain` | Models, repository contracts and use cases. Pure Kotlin — no Android at all |
| `:data` | Network, database, and the implementations of the domain contracts |

`:domain` being pure Kotlin is not decoration: it means its tests run in milliseconds on the JVM, and it makes the separation real rather than aspirational. Everything inside `:data` is internal to that module, so the rest of the app can only see the interfaces the domain defines.

## Decisions worth explaining

**One source of truth per piece of data.** The catalogue comes from the network; favorites live in the local database. Neither one owns the other. When you tap a heart, only the database changes — and because the products screen observes the database, the list updates on its own. There is no manual refresh and no state to keep in sync by hand.

**Errors are values, not exceptions.** Every repository returns either a success or a typed error (`no connection`, `timeout`, `HTTP <code>`, `bad data`, `unknown`). Nothing throws across a layer boundary, so the UI can never receive a surprise, and the compiler forces every screen to decide what to show for each case.

**Screens are split in two.** Each screen has a small outer part that asks for its ViewModel, and an inner part that only receives state and callbacks. The inner part is what previews and UI tests use, which is why the UI tests need no dependency injection and no network.

**Data loading stops when nobody is looking.** Screens stop collecting data five seconds after they stop being visible, and resume with the value they already had. In practice: rotating the phone does not trigger a new request and shows no loading spinner, while coming back after a while does refresh — without flashing an empty screen first.

**The API contract is treated as untrusted.** The published documentation for fakestoreapi and its actual responses do not agree, so only the fields guaranteed by both are required. Everything else is optional with a sensible fallback — a user with no name block falls back to their username. A test parses the real API payload to make sure this stays true.

**Navigation owns its own back stack.** Built with Navigation 3, with a deliberately small rule: the stack is either `[Products]` or `[Products, Tab]`. Back from any tab returns to Products; back from Products leaves the app — the behaviour users expect, in about ten lines.

## Tests

**61 tests — 40 unit and 21 instrumented — plus 7 Compose snapshot tests.**

The guiding rule is that **a test should protect a decision, not mirror an implementation.** A use case whose whole body is `return repository.getThing()` has no decision in it, so a test there would only restate the code and would need updating every time the signature changes, without ever catching anything.

What the suite protects, in order of importance:

- Changing a favorite re-renders the product list **without calling the network again** — the single most valuable behaviour in the app
- Exceptions are mapped to errors in the right order, and cancellation is never swallowed
- Real SQL: ordering, replace-instead-of-duplicate, and that the database notifies observers when it changes
- An unexpected failure shows a generic error **without breaking the retry button**
- An empty list produces an "empty" state rather than an empty list on screen
- The favorites counter on the profile updates without reloading the profile

Deliberately not tested: pure delegations, field-to-field mappers, and anything that would require weakening the design to make it reachable from a test.

### Snapshot tests

Seven snapshot tests render the shared components and compare them against reference images kept in the repository, so a change in padding, colour or text size shows up as a visual diff instead of going unnoticed. They cover the product card in four variants — plain, saved, with a title long enough to be truncated, and in dark theme — plus the empty and error states.

The loading spinner is deliberately left out: it is animated, and a snapshot of a moving target is a test that fails at random.

These run on the JVM using the same renderer as Android Studio previews, so no emulator is involved:

```bash
# record the reference images (first run, or after an intended visual change)
./gradlew :core:ui:updateDebugScreenshotTest

# compare the current UI against those references
./gradlew :core:ui:validateDebugScreenshotTest
```

The visual diff report is written to `core/ui/build/reports/screenshotTest/preview/debug/index.html`.

This uses Google's Compose Preview Screenshot Testing plugin, which is still in alpha — at the time of writing it is the only screenshot testing tool that supports AGP 9 on JDK 17, since both Paparazzi and Roborazzi currently require JDK 21.

## Known limitations

- No offline cache for the catalogue: without a connection, the product list shows an error rather than stale data. Favorites remain available, since they are stored locally.
- No pagination — the API returns the whole catalogue in a single call.
- The profile is fixed to user `8`, because the API has no authentication.
- Prices are formatted using the device locale, but the currency is fixed to USD. The API returns a bare number with no currency field, so that is an assumption on our side rather than something the response tells us. In production the currency would be part of the model and come from the backend.

## Built with

Kotlin · Coroutines and Flow · Jetpack Compose (Material 3) · Navigation 3 · Hilt · Retrofit and OkHttp · kotlinx.serialization · Room · Coil · JUnit, MockK and Turbine
