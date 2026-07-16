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
        assertEquals("button-groups", CatalogSearch.search(document.entries, "connected buttons").first().entry.id)
        assertEquals("chips", CatalogSearch.search(document.entries, "input chip").first().entry.id)
        assertEquals("selection-controls", CatalogSearch.search(document.entries, "tri state checkbox").first().entry.id)
    }

    @Test
    fun behaviorAndAccessibilityGuidanceIsSearchable() {
        assertEquals("progress-indicators", CatalogSearch.search(document.entries, "meaningful stalls").first().entry.id)
        assertEquals("cards", CatalogSearch.search(document.entries, "nesting cards").first().entry.id)
        assertEquals("segmented-buttons", CatalogSearch.search(document.entries, "radio semantics").first().entry.id)
    }

    @Test
    fun everyEntryContainsCompleteDevelopmentGuidance() {
        document.entries.forEach { entry ->
            assertTrue(entry.guidance.purpose.isNotBlank())
            assertTrue(entry.guidance.useWhen.isNotEmpty())
            assertTrue(entry.guidance.avoidWhen.isNotEmpty())
            assertTrue(entry.guidance.behavior.isNotEmpty())
            assertTrue(entry.guidance.accessibility.isNotEmpty())
            assertTrue(entry.guidance.adaptive.isNotEmpty())
        }
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
