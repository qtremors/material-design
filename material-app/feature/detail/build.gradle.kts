plugins {
    alias(libs.plugins.materialdesign.android.library)
    alias(libs.plugins.materialdesign.android.compose)
}

dependencies {
    implementation(project(":core:ui"))
    implementation(libs.androidx.compose.ui)
}
