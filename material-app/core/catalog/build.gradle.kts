plugins {
    alias(libs.plugins.materialdesign.android.library)
    alias(libs.plugins.materialdesign.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
}

// Catalog tests read checked-in files outside the source sets; declare them so edits re-run the tests.
tasks.withType<Test>().configureEach {
    inputs.dir(layout.projectDirectory.dir("api")).withPropertyName("materialApiInventory")
    inputs.file(rootProject.layout.projectDirectory.file("gradle/libs.versions.toml")).withPropertyName("versionCatalog")
    inputs.file(layout.projectDirectory.file("src/main/assets/catalog/catalog.json")).withPropertyName("catalog")
}
