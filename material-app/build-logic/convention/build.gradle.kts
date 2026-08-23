plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "materialdesign.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "materialdesign.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "materialdesign.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("kotlinSerialization") {
            id = "materialdesign.kotlin.serialization"
            implementationClass = "KotlinSerializationConventionPlugin"
        }
    }
}
