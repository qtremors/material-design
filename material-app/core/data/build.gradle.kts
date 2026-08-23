plugins {
    alias(libs.plugins.materialdesign.android.library)
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
}
