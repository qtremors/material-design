package dev.qtremors.material.core.catalog

import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Holds the catalog accountable to the complete public API of the pinned Material artifacts.
 *
 * The inventory under `core/catalog/api/inventory` is generated from the pinned sources jars by
 * `./gradlew :app:generateMaterialApiInventory`; the ledger is reconciled by
 * `./gradlew :app:updateApiCoverageLedger`. These tests read only checked-in files, so they run
 * offline and fail as soon as a version bump, a catalog edit, or a new upstream API is not triaged.
 */
class ApiCoverageTest {
    private val json = Json { ignoreUnknownKeys = false }
    private val repositoryRoot = generateSequence(
        File(requireNotNull(System.getProperty("user.dir"))).absoluteFile,
    ) { it.parentFile }
        .first { File(it, "material-app").isDirectory && File(it, "README.md").isFile }
    private val appRoot = File(repositoryRoot, "material-app")
    private val apiRoot = File(appRoot, "core/catalog/api")

    private val document by lazy {
        json.decodeFromString<CatalogDocument>(
            File(appRoot, "core/catalog/src/main/assets/catalog/catalog.json").readText(),
        )
    }
    private val manifest by lazy {
        json.decodeFromString<InventoryManifest>(File(apiRoot, "inventory/manifest.json").readText())
    }
    private val inventories by lazy {
        manifest.artifacts.associate { artifact ->
            artifact.artifact to json.decodeFromString<Inventory>(File(apiRoot, "inventory/${artifact.file}").readText())
        }
    }
    private val baselines by lazy {
        manifest.artifacts.filter { it.baselineFile != null }.associate { artifact ->
            artifact.artifact to json.decodeFromString<Inventory>(File(apiRoot, "inventory/${artifact.baselineFile}").readText())
        }
    }
    private val ledger by lazy {
        json.decodeFromString<Ledger>(File(apiRoot, "coverage-ledger.json").readText())
    }
    private val pinnedVersions by lazy {
        val toml = File(appRoot, "gradle/libs.versions.toml").readText()
        Regex("""(?m)^(\w+)\s*=\s*"([^"]+)"""").findAll(toml.substringBefore("[libraries]"))
            .associate { it.groupValues[1] to it.groupValues[2] }
    }
    private val references by lazy {
        document.entries.flatMap { entry -> entry.apiReferences.map { entry.id to it } }
    }

    @Test
    fun inventoryAndCatalogMatchPinnedVersions() {
        manifest.artifacts.forEach { artifact ->
            assertEquals(
                "${artifact.artifact} inventory is stale; run generateMaterialApiInventory",
                pinnedVersions[artifact.versionKey],
                artifact.version,
            )
            assertEquals(artifact.version, inventories.getValue(artifact.artifact).version)
        }
        manifest.artifacts.filter { it.baselineVersionKey != null }.forEach { artifact ->
            assertEquals(pinnedVersions[artifact.baselineVersionKey], artifact.baselineVersion)
            assertEquals(artifact.baselineVersion, baselines.getValue(artifact.artifact).version)
        }
        assertEquals(pinnedVersions["composeBom"], manifest.composeBom)
        assertEquals(pinnedVersions["composeMaterial3StableBaseline"], document.stableBaseline)
        assertEquals(inventories.getValue(MATERIAL3).version, document.material3Version)
        assertEquals(manifest.composeUiVersion, document.composeUiVersion)
    }

    @Test
    fun everyTrackedReferenceExistsWithItsReviewedVersionAndStability() {
        val problems = references.mapNotNull { (id, api) ->
            val inventory = inventories[api.artifact] ?: return@mapNotNull null
            val topLevel = inventory.symbols.associateBy { it.fqName }
            val symbol = topLevel[api.symbol]
            val owner = topLevel[api.symbol.substringBeforeLast('.')]
            when {
                api.reviewedVersion != inventory.version ->
                    "$id ${api.symbol}: reviewed ${api.reviewedVersion}, pinned ${inventory.version}"
                symbol == null && owner?.members?.contains(api.symbol.substringAfterLast('.')) != true ->
                    "$id ${api.symbol}: not public in ${api.artifact}:${inventory.version}"
                symbol == null -> null // A member reference; stability is reviewed with its owner.
                symbol.kind == "deprecated" -> "$id ${api.symbol}: deprecated in ${inventory.version}"
                symbol.stability == "stable" && api.stability == ApiStability.EXPERIMENTAL ->
                    "$id ${api.symbol}: stable in ${inventory.version} but recorded as experimental"
                symbol.stability == "experimental" && api.stability == ApiStability.STABLE ->
                    "$id ${api.symbol}: experimental in ${inventory.version} but recorded as stable"
                api.stability == ApiStability.EXPERIMENTAL &&
                    api.optInAnnotation?.substringAfterLast('.') !in symbol.optIns ->
                    "$id ${api.symbol}: opt-in ${api.optInAnnotation} does not match ${symbol.optIns}"
                else -> null
            }
        }
        assertTrue(problems.joinToString("\n", prefix = "Catalog API references out of date:\n"), problems.isEmpty())
    }

    /**
     * `stable_artifact` means the symbol already ships in the newest stable release; `alpha_only`
     * means it exists only in the pinned pre-release line. Artifacts pinned to a stable version
     * are available in a stable artifact by definition.
     */
    @Test
    fun everyTrackedReferenceRecordsItsTrueAvailability() {
        val problems = references.mapNotNull { (id, api) ->
            val inventory = inventories[api.artifact] ?: return@mapNotNull null
            val baseline = baselines[api.artifact]
            val inStable = when {
                baseline == null -> '-' !in inventory.version
                else -> {
                    val topLevel = baseline.symbols.filter { it.kind != "deprecated" }.associateBy { it.fqName }
                    api.symbol in topLevel ||
                        topLevel[api.symbol.substringBeforeLast('.')]?.members?.contains(api.symbol.substringAfterLast('.')) == true
                }
            }
            val expected = if (inStable) ApiAvailability.STABLE_ARTIFACT else ApiAvailability.ALPHA_ONLY
            if (api.availability == expected) null else "$id ${api.symbol}: recorded ${api.availability}, actually $expected"
        }
        assertTrue(problems.joinToString("\n", prefix = "Catalog availability out of date:\n"), problems.isEmpty())
    }

    @Test
    fun untrackedComposeReferencesUseTheResolvedComposeVersion() {
        // Compose UI, foundation, and animation come from the stable BOM, so they ship in a stable artifact.
        val bomIsStable = '-' !in manifest.composeUiVersion
        val stale = references.filter { (_, api) ->
            api.artifact !in inventories && api.artifact.startsWith("androidx.compose.") &&
                (api.reviewedVersion != manifest.composeUiVersion ||
                    (bomIsStable && api.availability != ApiAvailability.STABLE_ARTIFACT))
        }.map { (id, api) -> "$id ${api.symbol} ${api.reviewedVersion} ${api.availability}" }
        assertTrue(stale.joinToString("\n", prefix = "Expected ${manifest.composeUiVersion}:\n"), stale.isEmpty())
    }

    @Test
    fun everyPublicSymbolIsCoveredPlannedOrExcluded() {
        val covered = references.flatMap { (_, api) ->
            listOfNotNull(api.symbol, api.symbol.substringBeforeLast('.'), api.optInAnnotation)
        }.toSet()
        val ledgerByName = ledger.symbols.associateBy { it.fqName }
        val live = inventories.values.flatMap { it.symbols }.filter { it.kind != "deprecated" }
        val untracked = live.filter { it.fqName !in covered && it.fqName !in ledgerByName }.map { it.fqName }
        val untriaged = ledger.symbols.filter { it.status == "new" }.map { it.fqName }
        val liveNames = live.map { it.fqName }.toSet()
        val stale = ledger.symbols.filter { it.fqName !in liveNames || it.fqName in covered }.map { it.fqName }
        val unexplained = ledger.symbols.filter { it.status == "excluded" && it.note.isBlank() }.map { it.fqName }
        val unknownStatus = ledger.symbols.filter { it.status !in ledger.statuses }.map { it.fqName }

        assertTrue("Not in the ledger; run updateApiCoverageLedger: $untracked", untracked.isEmpty())
        assertTrue("Triage these new symbols as planned or excluded: $untriaged", untriaged.isEmpty())
        assertTrue("Covered or removed symbols remain in the ledger; run updateApiCoverageLedger: $stale", stale.isEmpty())
        assertTrue("Excluded symbols need a reason: $unexplained", unexplained.isEmpty())
        assertTrue("Unknown ledger status: $unknownStatus", unknownStatus.isEmpty())
    }

    @Serializable
    private data class InventoryManifest(
        val composeBom: String,
        val composeUiVersion: String,
        val artifacts: List<ManifestArtifact>,
    )

    @Serializable
    private data class ManifestArtifact(
        val artifact: String,
        val version: String,
        val versionKey: String,
        val file: String,
        val baselineVersion: String? = null,
        val baselineVersionKey: String? = null,
        val baselineFile: String? = null,
    )

    @Serializable
    private data class Inventory(val artifact: String, val version: String, val symbols: List<InventorySymbol>)

    @Serializable
    private data class InventorySymbol(
        val fqName: String,
        val kind: String,
        val stability: String,
        val optIns: List<String>,
        val overloads: Int,
        val group: String,
        val members: List<String>,
    )

    @Serializable
    private data class Ledger(val statuses: Map<String, String>, val symbols: List<LedgerSymbol>)

    @Serializable
    private data class LedgerSymbol(
        val fqName: String,
        val artifact: String,
        val status: String,
        val group: String,
        val note: String,
    )

    private companion object {
        const val MATERIAL3 = "androidx.compose.material3:material3"
    }
}
