import java.util.Properties

plugins {
    alias(libs.plugins.materialdesign.android.application)
    alias(libs.plugins.materialdesign.android.compose)
    alias(libs.plugins.materialdesign.kotlin.serialization)
}

val appVersionName = providers.gradleProperty("appVersionName").get()
val appVersionCode = providers.gradleProperty("appVersionCode").get().toInt()

android {
    namespace = "dev.qtremors.materialdesign"

    defaultConfig {
        applicationId = "dev.qtremors.materialdesign"
        targetSdk = 37
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "APP_VERSION_NAME", "\"$appVersionName\"")
    }

    val keystoreProperties = Properties()
    var keystorePropertiesFile = rootProject.file("signing.properties")
    if (!keystorePropertiesFile.exists()) {
        keystorePropertiesFile = rootProject.file("local.properties")
    }
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use {
            keystoreProperties.load(it)
        }
    }

    val storeFileProp = keystoreProperties["signing.storeFile"]?.toString()
    val storePasswordProp = keystoreProperties["signing.storePassword"]?.toString()
    val keyAliasProp = keystoreProperties["signing.keyAlias"]?.toString()
    val keyPasswordProp = keystoreProperties["signing.keyPassword"]?.toString()

    val hasSigningConfig = storeFileProp != null && storePasswordProp != null && keyAliasProp != null && keyPasswordProp != null

    if (hasSigningConfig) {
        signingConfigs {
            create("release") {
                storeFile = file(storeFileProp!!)
                storePassword = storePasswordProp
                keyAlias = keyAliasProp
                keyPassword = keyPasswordProp
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            manifestPlaceholders["appLabel"] = "Material Design Debug"
        }
        release {
            // AGP 9.3+ DSL: enables R8 code optimization and resource shrinking,
            // including the default Android keep rules.
            optimization {
                enable = true
            }
            manifestPlaceholders["appLabel"] = "Material Design"
            if (hasSigningConfig) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        jniLibs {
            keepDebugSymbols += setOf(
                "**/libandroidx.graphics.path.so",
                "**/libdatastore_shared_counter.so"
            )
        }
    }

    testOptions {
        unitTests {
            // Allow android.util.Log calls exercised by ViewModel error paths in JVM tests.
            isReturnDefaultValues = true
        }
    }
}

val androidComponents = project.extensions.getByType<com.android.build.api.variant.ApplicationAndroidComponentsExtension>()
androidComponents.onVariants { variant ->
    variant.outputs.forEach { output ->
        if (variant.name.equals("debug", ignoreCase = true)) {
            output.outputFileName.set("Material Design-$appVersionName-Debug.apk")
        } else {
            output.outputFileName.set("Material Design-$appVersionName.apk")
        }
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler/reports")
    metricsDestination = layout.buildDirectory.dir("compose_compiler/metrics")
}

dependencies {
    implementation(project(":core:catalog"))
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":feature:explore"))
    implementation(project(":feature:catalog"))
    implementation(project(":feature:apis"))
    implementation(project(":feature:foundations"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:settings"))
    implementation(project(":samples:actions"))
    implementation(project(":samples:communication"))
    implementation(project(":samples:containment"))
    implementation(project(":samples:navigation"))
    implementation(project(":samples:foundations"))
    implementation(project(":samples:selection"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4.accessibility)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
