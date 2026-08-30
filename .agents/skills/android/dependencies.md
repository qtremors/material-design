# Android Build and Dependencies for Material 3 Apps

This skill guides you through configuring Gradle, dependencies, version catalogs, convention plugins, SDK levels, optimization, and signing when building modern Android applications with Material Design 3 and Material 3 Expressive.

## Version Catalog Configuration (`gradle/libs.versions.toml`)

Centralize all dependency coordinates, plugin declarations, and version definitions in a Gradle version catalog:

```toml
[versions]
# Build toolchain & plugins
agp = "9.3.1"
kotlin = "2.4.10"

# AndroidX platform & lifecycle
coreKtx = "1.19.0"
appcompat = "1.7.0"
activityCompose = "1.13.0"
lifecycleRuntimeKtx = "2.11.0"

# Jetpack Compose & Material 3
composeBom = "2026.08.00"
composeMaterial3 = "1.5.0-alpha26"
composeMaterial3Adaptive = "1.3.0"

# Navigation, persistence, and serialization
navigationCompose = "2.9.8"
datastore = "1.2.1"
kotlinxSerialization = "1.11.0"
kotlinxCoroutines = "1.11.0"

# Testing
junit4 = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"

[libraries]
# Platform & Lifecycle
androidx-core-ktx = { group = "androidx.core", name = "core-ktx", version.ref = "coreKtx" }
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-runtime-compose = { group = "androidx.lifecycle", name = "lifecycle-runtime-compose", version.ref = "lifecycleRuntimeKtx" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version.ref = "lifecycleRuntimeKtx" }

# Compose BOM & UI
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-ui-graphics = { group = "androidx.compose.ui", name = "ui-graphics" }
androidx-compose-ui-tooling = { group = "androidx.compose.ui", name = "ui-tooling" }
androidx-compose-ui-tooling-preview = { group = "androidx.compose.ui", name = "ui-tooling-preview" }
androidx-compose-material-icons-extended = { group = "androidx.compose.material", name = "material-icons-extended" }

# Material 3 & Expressive / Adaptive
# Note: composeMaterial3 and adaptive are explicitly pinned for expressive alpha APIs
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3", version.ref = "composeMaterial3" }
androidx-compose-material3-adaptive = { group = "androidx.compose.material3.adaptive", name = "adaptive", version.ref = "composeMaterial3Adaptive" }
androidx-compose-material3-adaptive-layout = { group = "androidx.compose.material3.adaptive", name = "adaptive-layout", version.ref = "composeMaterial3Adaptive" }
androidx-compose-material3-adaptive-navigation = { group = "androidx.compose.material3.adaptive", name = "adaptive-navigation", version.ref = "composeMaterial3Adaptive" }
androidx-compose-material3-adaptive-navigation-suite = { group = "androidx.compose.material3", name = "material3-adaptive-navigation-suite", version.ref = "composeMaterial3" }

# Navigation & Architecture
androidx-navigation-compose = { group = "androidx.navigation", name = "navigation-compose", version.ref = "navigationCompose" }
androidx-datastore-preferences = { group = "androidx.datastore", name = "datastore-preferences", version.ref = "datastore" }
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinxSerialization" }

# Testing
junit = { group = "junit", name = "junit", version.ref = "junit4" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
androidx-compose-ui-test-junit4 = { group = "androidx.compose.ui", name = "ui-test-junit4" }
androidx-compose-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "kotlinxCoroutines" }

# Build plugins
android-gradle-plugin = { group = "com.android.tools.build", name = "gradle", version.ref = "agp" }
kotlin-gradle-plugin = { group = "org.jetbrains.kotlin", name = "kotlin-gradle-plugin", version.ref = "kotlin" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
android-library = { id = "com.android.library", version.ref = "agp" }
kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }
kotlin-compose = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
kotlin-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
```

## Toolchain and SDK Contracts

For modern Material 3 and Compose applications:

- **Compile SDK:** `compileSdk = 37` (supports modern Android platform APIs and Edge-to-Edge window insets).
- **Minimum SDK:** `minSdk = 24` (Android 7.0+, providing broad device support with modern language features).
- **Target SDK:** `targetSdk = 36` (configured in the application module).
- **Java / Kotlin Target:** JVM 11 bytecode compatibility (`JavaVersion.VERSION_11` / `JvmTarget.JVM_11`).
- **Compose Compiler:** The Kotlin Compose compiler plugin (`org.jetbrains.kotlin.plugin.compose`) is bundled directly with Kotlin 2.0+.

## Modular Gradle Convention Plugins (`build-logic`)

In a multi-module architecture, avoid repeating SDK levels, compiler options, and dependency bundles by creating Gradle convention plugins in a `build-logic` composite build.

### 1. Common Android Configuration (`configureAndroidCommon`)

```kotlin
internal fun Project.configureAndroidCommon(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.compileSdk = 37
    commonExtension.defaultConfig.minSdk = 24
    commonExtension.compileOptions.sourceCompatibility = JavaVersion.VERSION_11
    commonExtension.compileOptions.targetCompatibility = JavaVersion.VERSION_11

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}
```

### 2. Standard Convention Plugins

| Plugin ID | Responsibility |
| --- | --- |
| `myapp.android.application` | Applies `com.android.application`, sets common SDKs, packaging options, and default build types. |
| `myapp.android.library` | Applies `com.android.library`, sets common SDKs and namespace conventions for feature and data modules. |
| `myapp.android.compose` | Enables `buildFeatures.compose = true`, applies Compose compiler plugin, and adds Compose dependencies (`ui`, `graphics`, `material3`, `tooling-preview`). |
| `myapp.kotlin.serialization` | Applies `kotlinx.serialization` compiler plugin and adds `kotlinx-serialization-json`. |

## Dependency Management Best Practices

### BOM vs Explicit Pinned Versions
- **Compose BOM (`compose-bom`):** Ensures UI foundation libraries (`ui`, `ui-graphics`, `ui-tooling`) use mutually compatible stable versions.
- **Explicit Material 3 Alpha Pinning:** When using new Material 3 Expressive features (such as `SplitButton`, `FloatingToolbar`, `WavyProgressIndicator`, or `NavigationSuiteScaffold`), pin `androidx.compose.material3` (e.g. `1.5.0-alpha26`) and `androidx.compose.material3.adaptive` (e.g. `1.3.0`) directly, as the stable BOM line may not yet include these components.

### Handling Experimental Opt-Ins
When consuming alpha or evolving Material 3 APIs, require explicit opt-in annotations where appropriate:
- `@OptIn(ExperimentalMaterial3ExpressiveApi::class)`
- `@OptIn(ExperimentalMaterial3AdaptiveApi::class)`

## Optimization, R8, and Packaging

In your application module (`app/build.gradle.kts`):

```kotlin
android {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }
}
```

### Native Debug Symbols
Retain debug symbols for critical native graphics and data libraries:
```kotlin
packaging {
    jniLibs.keepDebugSymbols += setOf(
        "**/libandroidx.graphics.path.so",
        "**/libdatastore_shared_counter.so"
    )
}
```

## Release Signing Workflow

Keep release signing configuration local and ignored by version control:

1. Create a `signing.properties` file (or use `local.properties`):
   ```properties
   signing.storeFile=/path/to/keystore.jks
   signing.storePassword=yourStorePassword
   signing.keyAlias=yourKeyAlias
   signing.keyPassword=yourKeyPassword
   ```
2. Read the properties securely in `app/build.gradle.kts` to populate `signingConfigs.create("release")`.
3. Verify the resulting APK using Android SDK `apksigner`:
   ```powershell
   apksigner verify --verbose --print-certs app-release.apk
   ```

## Build and Verification Commands

```powershell
# Run build-logic convention plugin tests
.\gradlew.bat -p build-logic :convention:test

# Run unit tests across all modules
.\gradlew.bat testDebugUnitTest

# Run Android Lint
.\gradlew.bat lintDebug

# Build debug and release APKs
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:assembleRelease
```
