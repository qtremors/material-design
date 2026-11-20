package dev.qtremors.material.core.catalog

import android.content.Context
import java.text.Normalizer
import java.util.Locale
import kotlinx.serialization.json.Json

class BundledCatalogRepository(context: Context) : CatalogRepository {
    private val json = Json { ignoreUnknownKeys = false }

    override val document: CatalogDocument = context.assets
        .open(CATALOG_PATH)
        .bufferedReader()
        .use { json.decodeFromString<CatalogDocument>(it.readText()) }
        .also(CatalogValidator::validate)

    override val entries: List<CatalogEntry> = document.entries

    private val searchIndex = CatalogSearchIndex(entries)

    override fun entry(id: String): CatalogEntry? = entries.firstOrNull { it.id == id }

    override fun search(query: String): List<SearchResult> = searchIndex.search(query)

    companion object {
        const val CATALOG_PATH = "catalog/catalog.json"
    }
}

/**
 * Structural validation that must hold for the bundled catalog to be usable at runtime.
 *
 * Version agreement between the catalog, the pinned dependencies, and the upstream API inventory
 * is verified at build time by `ApiCoverageTest`, so a metadata mismatch can never stop the
 * installed app from opening.
 */
object CatalogValidator {
    fun validate(document: CatalogDocument) {
        require(document.schemaVersion == 1) { "Unsupported catalog schema ${document.schemaVersion}" }
        require(document.material3Version.isNotBlank() && document.composeUiVersion.isNotBlank())
        require(document.stableBaseline.isNotBlank())
        require(document.entries.isNotEmpty())
        require(document.entries.map { it.id }.distinct().size == document.entries.size) { "Catalog IDs must be unique" }
        require(document.entries.map { it.officialName.lowercase() }.distinct().size == document.entries.size) {
            "Official names must be unique"
        }
        val activeIds = document.entries.mapTo(mutableSetOf(), CatalogEntry::id)
        document.retiredIds.forEach { (retired, replacement) ->
            require(retired !in activeIds) { "Retired ID $retired is still an active entry" }
            require(replacement in activeIds) { "Retired ID $retired points to missing entry $replacement" }
        }
        document.entries.forEach { entry ->
            require(entry.id.matches(Regex("[a-z0-9-]+"))) { "Invalid catalog ID ${entry.id}" }
            require(entry.officialName.isNotBlank())
            require(entry.summary.isNotBlank())
            require(entry.guidance.purpose.isNotBlank()) { "${entry.id} has no purpose guidance" }
            require(entry.guidance.useWhen.isNotEmpty()) { "${entry.id} has no use guidance" }
            require(entry.guidance.avoidWhen.isNotEmpty()) { "${entry.id} has no avoid guidance" }
            require(entry.guidance.behavior.isNotEmpty()) { "${entry.id} has no behavior guidance" }
            require(entry.guidance.accessibility.isNotEmpty()) { "${entry.id} has no accessibility guidance" }
            require(entry.guidance.adaptive.isNotEmpty()) { "${entry.id} has no adaptive guidance" }
            require(
                listOf(
                    entry.guidance.useWhen,
                    entry.guidance.avoidWhen,
                    entry.guidance.behavior,
                    entry.guidance.accessibility,
                    entry.guidance.adaptive,
                ).flatten().none(String::isBlank),
            ) { "${entry.id} contains blank guidance" }
            require(entry.demoKey.isNotBlank()) { "${entry.id} has no working demo" }
            require(entry.sourceLocations.isNotEmpty()) { "${entry.id} has no source location" }
            require(entry.apiReferences.isNotEmpty()) { "${entry.id} has no API reference" }
            entry.apiReferences.forEach { api ->
                require(api.symbol.isNotBlank() && api.reviewedVersion.isNotBlank())
                require(api.stability == ApiStability.STABLE || !api.optInAnnotation.isNullOrBlank()) {
                    "Experimental API ${api.symbol} must declare its opt-in annotation"
                }
            }
        }
    }
}

object CatalogSearch {
    fun search(entries: List<CatalogEntry>, rawQuery: String): List<SearchResult> =
        CatalogSearchIndex(entries).search(rawQuery)
}

private fun normalize(value: String): String = Normalizer.normalize(value, Normalizer.Form.NFKD)
    .lowercase(Locale.ROOT)
    .replace(Regex("[^a-z0-9]+"), " ")
    .trim()

/**
 * Precomputes the normalized search fields for every entry once so repeated
 * queries do not re-normalize the whole corpus on each keystroke.
 */
class CatalogSearchIndex(entries: List<CatalogEntry>) {
    private val indexedEntries: List<IndexedEntry> = entries.map(::IndexedEntry)

    fun search(rawQuery: String): List<SearchResult> {
        val query = normalize(rawQuery)
        if (query.isBlank()) return indexedEntries.map { SearchResult(it.entry, 0) }

        return indexedEntries.mapNotNull { indexed ->
            val score = when {
                indexed.officialName == query -> 1_000
                indexed.apiSymbols.any { it == query } -> 950
                indexed.aliases.any { it == query } -> 900
                indexed.officialName.startsWith(query) -> 800
                indexed.apiSymbols.any { it.startsWith(query) } -> 750
                indexed.aliases.any { it.startsWith(query) } -> 700
                indexed.officialName.contains(query) -> 600
                indexed.apiSymbols.any { it.contains(query) } -> 550
                indexed.aliases.any { it.contains(query) } -> 500
                indexed.category.contains(query) -> 300
                indexed.summary.contains(query) -> 100
                indexed.guidance.contains(query) -> 50
                else -> 0
            }
            score.takeIf { it > 0 }?.let { SearchResult(indexed.entry, it) }
        }.sortedWith(compareByDescending<SearchResult> { it.score }.thenBy { it.entry.officialName })
    }

    private class IndexedEntry(entry: CatalogEntry) {
        val entry: CatalogEntry = entry
        val officialName = normalize(entry.officialName)
        val apiSymbols = entry.apiReferences.map { normalize(it.symbol.substringAfterLast('.')) }
        val aliases = entry.aliases.map(::normalize)
        val category = normalize(entry.category)
        val summary = normalize(entry.summary)
        val guidance = normalize(
            buildList {
                add(entry.guidance.purpose)
                addAll(entry.guidance.useWhen)
                addAll(entry.guidance.avoidWhen)
                addAll(entry.guidance.behavior)
                addAll(entry.guidance.accessibility)
                addAll(entry.guidance.adaptive)
            }.joinToString(" "),
        )
    }
}
