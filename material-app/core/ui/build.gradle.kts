plugins {
    alias(libs.plugins.materialdesign.android.library)
    alias(libs.plugins.materialdesign.android.compose)
}

// Catalog-aware UI shared by the app shell and feature screens.
dependencies {
    api(project(":core:catalog"))
    api(project(":core:designsystem"))
    implementation(libs.androidx.compose.ui)
}
