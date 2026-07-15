package dev.qtremors.materialdesign

import android.content.Context
import dev.qtremors.material.core.catalog.BundledCatalogRepository
import dev.qtremors.material.core.catalog.CatalogRepository
import dev.qtremors.material.core.data.DataStoreUserLibraryRepository
import dev.qtremors.material.core.data.UserLibraryRepository

class MaterialAppContainer(context: Context) {
    val catalogRepository: CatalogRepository = BundledCatalogRepository(context)
    val userLibraryRepository: UserLibraryRepository = DataStoreUserLibraryRepository(context)
    val demoRegistry = MaterialDemoRegistry()
}
