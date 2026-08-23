package dev.qtremors.materialdesign

import android.content.Context
import dev.qtremors.material.core.catalog.BundledCatalogRepository
import dev.qtremors.material.core.catalog.CatalogRepository
import dev.qtremors.material.core.data.DataStoreUserLibraryRepository
import dev.qtremors.material.core.data.UserLibraryRepository

class MaterialAppContainer(private val context: Context) {
    val userLibraryRepository: UserLibraryRepository = DataStoreUserLibraryRepository(context.applicationContext)
    val demoRegistry = MaterialDemoRegistry()

    /**
     * Builds the bundled catalog repository. Parsing and validation are strict;
     * failures surface through [GalleryViewModel] as a recoverable error state.
     */
    suspend fun createCatalogRepository(): CatalogRepository =
        BundledCatalogRepository(context.applicationContext)
}
