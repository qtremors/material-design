# Android Libraries and Versions

Use this skill when changing Android dependencies, Gradle plugins, SDK levels, or version-catalog entries under `material-app/`.

The checked-in files are always the source of truth:

- Versions and aliases: `material-app/gradle/libs.versions.toml`
- App dependencies and Android configuration: `material-app/app/build.gradle.kts`
- Root plugins: `material-app/build.gradle.kts`
- Included modules and repositories: `material-app/settings.gradle.kts`
- Gradle daemon JVM: `material-app/gradle/gradle-daemon-jvm.properties`

Do not describe a dependency as “latest” in repository documentation. Compare the catalog with official release notes at the time of an upgrade, then document the version actually pinned.

## Current Toolchain

| Tool | Pinned value |
| --- | --- |
| Android Gradle Plugin | `9.3.0` |
| Kotlin | `2.2.10` |
| KSP | `2.3.2` |
| Java/Kotlin target | `11` |
| Gradle daemon JVM | `21` |
| `compileSdk` | `37` |
| `minSdk` | `24` |
| `targetSdk` | `37` |

## Gradle Plugins

| Alias | Plugin ID | Version source |
| --- | --- | --- |
| `android-application` | `com.android.application` | `agp` |
| `android-library` | `com.android.library` | `agp` |
| `kotlin-android` | `org.jetbrains.kotlin.android` | `kotlin` |
| `kotlin-compose` | `org.jetbrains.kotlin.plugin.compose` | `kotlin` |
| `kotlin-serialization` | `org.jetbrains.kotlin.plugin.serialization` | `kotlin` |
| `ksp` | `com.google.devtools.ksp` | `ksp` |

Use the Kotlin Compose compiler plugin for Compose modules. Apply only the plugins a module needs.

## Compose and Material

| Library | Pinned value |
| --- | --- |
| Compose BOM | `2026.08.00` |
| Compose Material 3 | `1.5.0-alpha26` |
| Material 3 Adaptive | `1.3.0` |

Material 3 is explicit because the project uses APIs from the alpha track. Adaptive is also pinned explicitly. Never assume the BOM supplies those versions.

Primary catalog aliases:

```toml
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3", version.ref = "composeMaterial3" }
androidx-compose-material3-adaptive = { group = "androidx.compose.material3.adaptive", name = "adaptive", version.ref = "composeMaterial3Adaptive" }
androidx-compose-material3-adaptive-layout = { group = "androidx.compose.material3.adaptive", name = "adaptive-layout", version.ref = "composeMaterial3Adaptive" }
androidx-compose-material3-adaptive-navigation = { group = "androidx.compose.material3.adaptive", name = "adaptive-navigation", version.ref = "composeMaterial3Adaptive" }
androidx-compose-material3-adaptive-navigation-suite = { group = "androidx.compose.material3", name = "material3-adaptive-navigation-suite", version.ref = "composeMaterial3" }
```

Typical app declarations:

```kotlin
implementation(platform(libs.androidx.compose.bom))
implementation(libs.androidx.compose.ui)
implementation(libs.androidx.compose.ui.graphics)
implementation(libs.androidx.compose.ui.tooling.preview)
implementation(libs.androidx.compose.material3)
implementation(libs.androidx.compose.material3.adaptive)
implementation(libs.androidx.compose.material3.adaptive.layout)
implementation(libs.androidx.compose.material3.adaptive.navigation)
implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
```

Verify alpha API changes in the [official Compose Material 3 release notes](https://developer.android.com/jetpack/androidx/releases/compose-material3) before changing the pin.

## AndroidX, Data, and Serialization

| Alias | Pinned version |
| --- | --- |
| `androidx-core-ktx` | `1.18.0` |
| `androidx-appcompat` | `1.7.0` |
| `androidx-lifecycle-runtime-ktx` | `2.10.0` |
| `androidx-lifecycle-runtime-compose` | `2.10.0` |
| `androidx-lifecycle-viewmodel-compose` | `2.10.0` |
| `androidx-activity-compose` | `1.13.0` |
| `androidx-navigation-compose` | `2.8.9` |
| `androidx-datastore-preferences` | `1.2.0` |
| `kotlinx-serialization-json` | `1.9.0` |

## Testing

| Alias | Pinned version |
| --- | --- |
| `junit` | `4.13.2` |
| `androidx-junit` | `1.3.0` |
| `androidx-espresso-core` | `3.7.0` |
| `androidx-compose-ui-test-junit4` | Compose BOM |
| `androidx-compose-ui-test-junit4-accessibility` | Compose BOM |
| `androidx-compose-ui-test-manifest` | Compose BOM |

## Adding or Updating a Dependency

1. Read the current version and alias from `material-app/gradle/libs.versions.toml`.
2. Check the official release notes and migration guidance.
3. Change the smallest applicable catalog entry.
4. Add or update the module dependency under the correct configuration.
5. Sync and compile the affected modules.
6. Run focused tests, then `testDebugUnitTest`, `lintDebug`, and `assembleDebug` when the change can affect the whole app.
7. Update this Markdown file and `docs/skills/android/dependencies.html`.
8. Add a concise entry to the current section of `CHANGELOG.md`.

Do not change `versionName`, `versionCode`, or the project version unless the user explicitly requests a version bump.
