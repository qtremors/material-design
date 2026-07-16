package dev.qtremors.material.core.catalog

import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogRepositoryTest {
    private val repositoryRoot = generateSequence(File(System.getProperty("user.dir")).absoluteFile) { it.parentFile }
        .first { File(it, "material-app").isDirectory && File(it, "README.md").isFile }
    private val document by lazy {
        Json.decodeFromString<CatalogDocument>(
            File(repositoryRoot, "material-app/core/catalog/src/main/assets/catalog/catalog.json").readText(),
        )
    }

    @Test
    fun bundledCatalogIsValidAndMatchesPinnedMaterialVersion() {
        CatalogValidator.validate(document)
        assertEquals(BundledCatalogRepository.MATERIAL3_VERSION, document.material3Version)
        assertEquals(BundledCatalogRepository.STABLE_BASELINE, document.stableBaseline)
    }

    @Test
    fun aliasesAndSymbolsRankCanonicalEntriesFirst() {
        assertEquals("floating-action-buttons", CatalogSearch.search(document.entries, "fab").first().entry.id)
        assertEquals("segmented-list-items", CatalogSearch.search(document.entries, "grouped lists").first().entry.id)
        assertEquals("progress-indicators", CatalogSearch.search(document.entries, "wavy progress").first().entry.id)
        assertEquals("split-buttons", CatalogSearch.search(document.entries, "SplitButtonLayout").first().entry.id)
    }

    @Test
    fun everySourceLocationExistsInTheRepository() {
        document.entries.flatMap(CatalogEntry::sourceLocations).forEach { source ->
            assertTrue("Missing source ${source.path}", File(repositoryRoot, source.path).isFile)
        }
    }

    @Test
    fun emptyQueryReturnsTheWorkingCatalogInSourceOrder() {
        assertEquals(document.entries, CatalogSearch.search(document.entries, "").map { it.entry })
    }
}
