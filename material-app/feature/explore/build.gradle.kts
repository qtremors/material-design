plugins {
    alias(libs.plugins.materialdesign.android.library)
    alias(libs.plugins.materialdesign.android.compose)
}

dependencies {
    implementation(project(":core:catalog"))
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
}
