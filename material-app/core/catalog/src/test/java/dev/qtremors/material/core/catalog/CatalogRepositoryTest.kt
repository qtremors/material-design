package dev.qtremors.material.core.catalog

import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatalogRepositoryTest {
    private val repositoryRoot = generateSequence(
        File(requireNotNull(System.getProperty("user.dir"))).absoluteFile,
    ) { it.parentFile }
        .first { File(it, "material-app").isDirectory && File(it, "README.md").isFile }
    private val document by lazy {
        Json.decodeFromString<CatalogDocument>(
            File(repositoryRoot, "material-app/core/catalog/src/main/assets/catalog/catalog.json").readText(),
        )
    }

    @Test
    fun bundledCatalogPassesRuntimeValidation() {
        // Version agreement with the pinned dependencies is covered by ApiCoverageTest.
        CatalogValidator.validate(document)
    }

    @Test
    fun aliasesAndSymbolsRankCanonicalEntriesFirst() {
        assertEquals("floating-action-buttons", CatalogSearch.search(document.entries, "fab").first().entry.id)
        assertEquals("segmented-list-items", CatalogSearch.search(document.entries, "grouped lists").first().entry.id)
        assertEquals("progress-indicators", CatalogSearch.search(document.entries, "wavy progress").first().entry.id)
        assertEquals("split-buttons", CatalogSearch.search(document.entries, "SplitButtonLayout").first().entry.id)
        assertEquals("button-groups", CatalogSearch.search(document.entries, "connected buttons").first().entry.id)
        assertEquals("chips", CatalogSearch.search(document.entries, "input chip").first().entry.id)
        assertEquals("checkbox", CatalogSearch.search(document.entries, "tri state checkbox").first().entry.id)
        assertEquals("motion", CatalogSearch.search(document.entries, "spring physics").first().entry.id)
        assertEquals("layout", CatalogSearch.search(document.entries, "panes").first().entry.id)
        assertEquals("accessibility", CatalogSearch.search(document.entries, "talkback").first().entry.id)
        assertEquals("text-fields", CatalogSearch.search(document.entries, "form field").first().entry.id)
        assertEquals("search", CatalogSearch.search(document.entries, "query").first().entry.id)
        assertEquals("dialogs", CatalogSearch.search(document.entries, "destructive confirmation").first().entry.id)
        assertEquals("bottom-sheets", CatalogSearch.search(document.entries, "mobile sheet").first().entry.id)
        assertEquals("snackbars", CatalogSearch.search(document.entries, "undo").first().entry.id)
        assertEquals("tooltips", CatalogSearch.search(document.entries, "hover label").first().entry.id)
        assertEquals("menus", CatalogSearch.search(document.entries, "overflow menu").first().entry.id)
    }

    @Test
    fun behaviorAndAccessibilityGuidanceIsSearchable() {
        assertEquals("progress-indicators", CatalogSearch.search(document.entries, "meaningful stalls").first().entry.id)
        assertEquals("cards", CatalogSearch.search(document.entries, "nesting cards").first().entry.id)
        assertEquals("segmented-buttons", CatalogSearch.search(document.entries, "radio semantics").first().entry.id)
        assertEquals("shape", CatalogSearch.search(document.entries, "uniform excessive rounding").first().entry.id)
        assertEquals("elevation", CatalogSearch.search(document.entries, "shadow is used as decoration").first().entry.id)
        assertEquals("text-fields", CatalogSearch.search(document.entries, "labels remain identifiable").first().entry.id)
        assertEquals("search", CatalogSearch.search(document.entries, "clearing restores the prior state").first().entry.id)
        assertEquals("dialogs", CatalogSearch.search(document.entries, "focus remains within the dialog").first().entry.id)
        assertEquals("snackbars", CatalogSearch.search(document.entries, "messages queue rather than overlap").first().entry.id)
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
    fun everyListedSourceSymbolIsDeclaredInItsFile() {
        val missing = document.entries.flatMap { entry ->
            entry.sourceLocations.flatMap { source ->
                val text = File(repositoryRoot, source.path).readText()
                source.symbols.filterNot { symbol ->
                    Regex("""\b(fun(\s+<[^>]+>)?|val|object|class|interface)\s+$symbol\b""").containsMatchIn(text)
                }.map { "${entry.id}: $it in ${source.path}" }
            }
        }
        assertTrue("Declared symbols not found:\n${missing.joinToString("\n")}", missing.isEmpty())
    }

    /**
     * Naming contract: an entry's working reference is `<OfficialName>Sample`, listed first in its
     * first source location, and registered as `<sample module>.<id>`.
     */
    @Test
    fun entriesFollowTheSampleNamingContract() {
        val problems = document.entries.mapNotNull { entry ->
            val first = entry.sourceLocations.first()
            val module = first.path.substringAfter("/samples/").substringBefore('/')
            val sample = entry.officialName.split(' ', '-')
                .joinToString("") { word -> word.lowercase().replaceFirstChar(Char::uppercaseChar) } + "Sample"
            when {
                entry.demoKey != "$module.${entry.id}" -> "${entry.id}: demoKey ${entry.demoKey}, expected $module.${entry.id}"
                first.symbols.firstOrNull() != sample -> "${entry.id}: first symbol ${first.symbols.firstOrNull()}, expected $sample"
                else -> null
            }
        }
        assertTrue(problems.joinToString("\n"), problems.isEmpty())
    }

    @Test
    fun retiredIdsRedirectToActiveEntries() {
        val active = document.entries.map(CatalogEntry::id).toSet()
        document.retiredIds.forEach { (retired, replacement) ->
            assertTrue("$retired is retired but still active", retired !in active)
            assertTrue("$retired redirects to missing $replacement", replacement in active)
        }
    }

    @Test
    fun emptyQueryReturnsTheWorkingCatalogInSourceOrder() {
        assertEquals(document.entries, CatalogSearch.search(document.entries, "").map { it.entry })
    }
}
