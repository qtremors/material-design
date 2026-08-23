plugins {
    alias(libs.plugins.materialdesign.android.library)
    alias(libs.plugins.materialdesign.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
}
