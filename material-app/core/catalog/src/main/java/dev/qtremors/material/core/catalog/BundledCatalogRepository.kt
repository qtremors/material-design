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

    override fun entry(id: String): CatalogEntry? = entries.firstOrNull { it.id == id }

    override fun search(query: String): List<SearchResult> = CatalogSearch.search(entries, query)

    companion object {
        const val CATALOG_PATH = "catalog/catalog.json"
        const val MATERIAL3_VERSION = "1.5.0-alpha23"
        const val STABLE_BASELINE = "1.4.0"
    }
}

object CatalogValidator {
    fun validate(document: CatalogDocument) {
        require(document.schemaVersion == 1) { "Unsupported catalog schema ${document.schemaVersion}" }
        require(document.material3Version == BundledCatalogRepository.MATERIAL3_VERSION) {
            "Catalog Material3 ${document.material3Version} does not match ${BundledCatalogRepository.MATERIAL3_VERSION}"
        }
        require(document.stableBaseline == BundledCatalogRepository.STABLE_BASELINE)
        require(document.entries.isNotEmpty())
        require(document.entries.map { it.id }.distinct().size == document.entries.size) { "Catalog IDs must be unique" }
        require(document.entries.map { it.officialName.lowercase() }.distinct().size == document.entries.size) {
            "Official names must be unique"
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
    fun search(entries: List<CatalogEntry>, rawQuery: String): List<SearchResult> {
        val query = normalize(rawQuery)
        if (query.isBlank()) return entries.map { SearchResult(it, 0) }

        return entries.mapNotNull { entry ->
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

            val score = when {
                officialName == query -> 1_000
                apiSymbols.any { it == query } -> 950
                aliases.any { it == query } -> 900
                officialName.startsWith(query) -> 800
                apiSymbols.any { it.startsWith(query) } -> 750
                aliases.any { it.startsWith(query) } -> 700
                officialName.contains(query) -> 600
                apiSymbols.any { it.contains(query) } -> 550
                aliases.any { it.contains(query) } -> 500
                category.contains(query) -> 300
                summary.contains(query) -> 100
                guidance.contains(query) -> 50
                else -> 0
            }
            score.takeIf { it > 0 }?.let { SearchResult(entry, it) }
        }.sortedWith(compareByDescending<SearchResult> { it.score }.thenBy { it.entry.officialName })
    }

    private fun normalize(value: String): String = Normalizer.normalize(value, Normalizer.Form.NFKD)
        .lowercase(Locale.ROOT)
        .replace(Regex("[^a-z0-9]+"), " ")
        .trim()
}
