package dev.qtremors.material.core.catalog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogDocument(
    val schemaVersion: Int,
    val material3Version: String,
    val stableBaseline: String,
    val reviewedOn: String,
    val entries: List<CatalogEntry>,
)

@Serializable
data class CatalogEntry(
    val id: String,
    val officialName: String,
    val aliases: List<String>,
    val kind: CatalogKind,
    val category: String,
    val summary: String,
    val collections: List<String>,
    val demoKey: String,
    val sourceLocations: List<SourceLocation>,
    val apiReferences: List<ApiReference>,
    val officialUrls: List<String>,
    val implementation: ImplementationKind,
    val addedIn: String,
    val reviewedOn: String,
)

@Serializable
data class SourceLocation(
    val path: String,
    val symbols: List<String>,
)

@Serializable
data class ApiReference(
    val symbol: String,
    val artifact: String,
    val reviewedVersion: String,
    val availability: ApiAvailability,
    val stability: ApiStability,
    val optInAnnotation: String? = null,
    val url: String,
)

@Serializable
enum class CatalogKind {
    @SerialName("component") COMPONENT,
    @SerialName("foundation") FOUNDATION,
}

@Serializable
enum class ImplementationKind {
    @SerialName("official_api") OFFICIAL_API,
    @SerialName("project_implementation") PROJECT_IMPLEMENTATION,
}

@Serializable
enum class ApiAvailability {
    @SerialName("stable_artifact") STABLE_ARTIFACT,
    @SerialName("alpha_only") ALPHA_ONLY,
}

@Serializable
enum class ApiStability {
    @SerialName("stable") STABLE,
    @SerialName("experimental") EXPERIMENTAL,
}

data class SearchResult(val entry: CatalogEntry, val score: Int)

interface CatalogRepository {
    val document: CatalogDocument
    val entries: List<CatalogEntry>
    fun entry(id: String): CatalogEntry?
    fun search(query: String): List<SearchResult>
}

interface ComponentDemoRegistry {
    val registeredKeys: Set<String>
    fun contains(demoKey: String): Boolean = demoKey in registeredKeys
}
