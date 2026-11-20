import groovy.json.JsonSlurper
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.TaskProvider
import java.io.File

/**
 * Official artifacts whose complete public API the catalog is accountable for.
 *
 * [sourcesCoordinate] is the Android publication that carries a `-sources` jar; [artifact] is the
 * coordinate catalog entries cite; [versionKey] is the `libs.versions.toml` key that pins it.
 */
internal data class TrackedArtifact(
    val artifact: String,
    val sourcesCoordinate: String,
    val versionKey: String,
    val file: String,
    /** Version key of the newest stable release, used to verify catalog `availability`. */
    val baselineVersionKey: String? = null,
) {
    val baselineFile: String get() = file.removeSuffix(".json") + ".baseline.json"
}

internal val trackedMaterialArtifacts = listOf(
    TrackedArtifact(
        artifact = "androidx.compose.material3:material3",
        sourcesCoordinate = "androidx.compose.material3:material3-android",
        versionKey = "composeMaterial3",
        file = "material3.json",
        baselineVersionKey = STABLE_BASELINE_KEY,
    ),
    TrackedArtifact(
        artifact = "androidx.compose.material3:material3-adaptive-navigation-suite",
        sourcesCoordinate = "androidx.compose.material3:material3-adaptive-navigation-suite-android",
        versionKey = "composeMaterial3",
        file = "material3-adaptive-navigation-suite.json",
        baselineVersionKey = STABLE_BASELINE_KEY,
    ),
    TrackedArtifact(
        artifact = "androidx.compose.material3.adaptive:adaptive",
        sourcesCoordinate = "androidx.compose.material3.adaptive:adaptive-android",
        versionKey = "composeMaterial3Adaptive",
        file = "adaptive.json",
    ),
    TrackedArtifact(
        artifact = "androidx.compose.material3.adaptive:adaptive-layout",
        sourcesCoordinate = "androidx.compose.material3.adaptive:adaptive-layout-android",
        versionKey = "composeMaterial3Adaptive",
        file = "adaptive-layout.json",
    ),
    TrackedArtifact(
        artifact = "androidx.compose.material3.adaptive:adaptive-navigation",
        sourcesCoordinate = "androidx.compose.material3.adaptive:adaptive-navigation-android",
        versionKey = "composeMaterial3Adaptive",
        file = "adaptive-navigation.json",
    ),
)

private const val API_DIRECTORY = "core/catalog/api"
private const val INVENTORY_DIRECTORY = "$API_DIRECTORY/inventory"
private const val MANIFEST_FILE = "$INVENTORY_DIRECTORY/manifest.json"
private const val LEDGER_FILE = "$API_DIRECTORY/coverage-ledger.json"
private const val COVERAGE_REPORT = "$API_DIRECTORY/COVERAGE.md"
private const val CATALOG_FILE = "core/catalog/src/main/assets/catalog/catalog.json"
private const val COMPOSE_BOM_KEY = "composeBom"
private const val STABLE_BASELINE_KEY = "composeMaterial3StableBaseline"

/** Registers the API inventory, coverage ledger, and inventory verification tasks. */
internal fun Project.registerMaterialApiTasks(): TaskProvider<*> {
    val rootDir = rootProject.layout.projectDirectory.asFile

    tasks.register("generateMaterialApiInventory") {
        group = "material api"
        description = "Reads the pinned Material sources jars and rewrites the checked-in public API inventory."
        notCompatibleWithConfigurationCache("Resolves detached source configurations on demand.")
        doLast {
            val inventoryDir = File(rootDir, INVENTORY_DIRECTORY).apply { mkdirs() }
            val manifestArtifacts = trackedMaterialArtifacts.map { tracked ->
                val version = catalogVersion(tracked.versionKey)
                val jar = resolveSingle("${tracked.sourcesCoordinate}:$version:sources@jar")
                val symbols = MaterialApiInventory.read(jar)
                File(inventoryDir, tracked.file).writeText(
                    MaterialApiInventory.toJson(tracked.artifact, version, symbols),
                )
                logger.lifecycle("${tracked.artifact}:$version -> ${symbols.size} public symbols")
                val baseline = tracked.baselineVersionKey?.let { key ->
                    val baselineVersion = catalogVersion(key)
                    val baselineJar = resolveSingle("${tracked.sourcesCoordinate}:$baselineVersion:sources@jar")
                    val baselineSymbols = MaterialApiInventory.read(baselineJar)
                    File(inventoryDir, tracked.baselineFile).writeText(
                        MaterialApiInventory.toJson(tracked.artifact, baselineVersion, baselineSymbols),
                    )
                    logger.lifecycle("${tracked.artifact}:$baselineVersion (stable baseline) -> ${baselineSymbols.size} public symbols")
                    baselineVersion
                }
                ManifestEntry(tracked, version, baseline)
            }
            val bomVersion = catalogVersion(COMPOSE_BOM_KEY)
            val bomPom = resolveSingle("androidx.compose:compose-bom:$bomVersion@pom").readText()
            val composeUiVersion = Regex(
                """<groupId>androidx\.compose\.ui</groupId>\s*<artifactId>ui</artifactId>\s*<version>([^<]+)</version>""",
            ).find(bomPom)?.groupValues?.get(1)
                ?: throw GradleException("compose-bom $bomVersion does not manage androidx.compose.ui:ui")
            File(rootDir, MANIFEST_FILE).writeText(
                manifestJson(bomVersion, composeUiVersion, manifestArtifacts),
            )
        }
    }

    tasks.register("updateApiCoverageLedger") {
        group = "material api"
        description = "Reconciles the coverage ledger with the inventory and catalog, then rewrites COVERAGE.md."
        doLast {
            val inventory = readInventory(rootDir)
            val covered = coveredSymbols(File(rootDir, CATALOG_FILE))
            val previous = readLedger(File(rootDir, LEDGER_FILE)).associateBy { it.fqName }
            val ledger = inventory.flatMap { (artifact, symbols) ->
                symbols.filter { it.kind != "deprecated" && it.fqName !in covered }.map { symbol ->
                    previous[symbol.fqName]?.copy(artifact = artifact, group = symbol.group)
                        ?: LedgerEntry(symbol.fqName, artifact, status = "new", group = symbol.group, note = "")
                }
            }.sortedWith(compareBy({ it.artifact }, { it.fqName }))
            File(rootDir, LEDGER_FILE).writeText(ledgerJson(ledger))
            File(rootDir, COVERAGE_REPORT).writeText(coverageReport(inventory, covered, ledger).trimEnd() + "\n")
            val fresh = ledger.count { it.status == "new" }
            if (fresh > 0) logger.warn("$fresh newly published symbols need triage (status \"new\") in $LEDGER_FILE")
        }
    }

    return tasks.register("verifyMaterialApiInventory") {
        group = "verification"
        description = "Verifies the checked-in API inventory matches the versions pinned in libs.versions.toml."
        inputs.files(File(rootDir, MANIFEST_FILE), rootProject.file("gradle/libs.versions.toml"))
        val pinned = trackedVersionKeys().associateWith { key -> catalogVersionOrNull(key) }
        doLast {
            val manifestFile = File(rootDir, MANIFEST_FILE)
            if (!manifestFile.isFile) throw GradleException("Missing $MANIFEST_FILE; run generateMaterialApiInventory.")
            @Suppress("UNCHECKED_CAST")
            val manifest = JsonSlurper().parse(manifestFile) as Map<String, Any?>
            @Suppress("UNCHECKED_CAST")
            val artifacts = manifest["artifacts"] as List<Map<String, String>>
            val stale = artifacts.mapNotNull { entry ->
                val expected = pinned[entry.getValue("versionKey")]
                    ?: return@mapNotNull "${entry["artifact"]}: libs.versions.toml has no ${entry["versionKey"]}"
                val inventoryFile = File(rootDir, "$INVENTORY_DIRECTORY/${entry.getValue("file")}")
                @Suppress("UNCHECKED_CAST")
                val inventoryVersion = (JsonSlurper().parse(inventoryFile) as Map<String, Any?>)["version"]
                val baselineKey = entry["baselineVersionKey"]
                when {
                    entry["version"] != expected -> "${entry["artifact"]}: inventory ${entry["version"]}, pinned $expected"
                    inventoryVersion != expected -> "${entry["file"]}: records $inventoryVersion, pinned $expected"
                    baselineKey != null && entry["baselineVersion"] != pinned[baselineKey] ->
                        "${entry["artifact"]}: baseline ${entry["baselineVersion"]}, pinned ${pinned[baselineKey]}"
                    else -> null
                }
            }
            val bom = pinned[COMPOSE_BOM_KEY]
            val bomIssue = if (bom != null && manifest["composeBom"] != bom) {
                "compose-bom: inventory ${manifest["composeBom"]}, pinned $bom"
            } else {
                null
            }
            val issues = stale + listOfNotNull(bomIssue)
            if (issues.isNotEmpty()) {
                throw GradleException(
                    "The Material API inventory is stale. Run generateMaterialApiInventory, then updateApiCoverageLedger.\n" +
                        issues.joinToString("\n"),
                )
            }
        }
    }
}

private fun Project.trackedVersionKeys(): Set<String> {
    val manifest = File(rootProject.layout.projectDirectory.asFile, MANIFEST_FILE)
    @Suppress("UNCHECKED_CAST")
    val manifestKeys = if (manifest.isFile) {
        ((JsonSlurper().parse(manifest) as Map<String, Any?>)["artifacts"] as List<Map<String, String>>)
            .flatMap { listOfNotNull(it["versionKey"], it["baselineVersionKey"]) }
    } else {
        emptyList()
    }
    return (manifestKeys + COMPOSE_BOM_KEY).toSet()
}

private fun Project.catalogVersionOrNull(key: String): String? =
    extensions.findByType(VersionCatalogsExtension::class.java)
        ?.find("libs")?.orElse(null)
        ?.findVersion(key)?.orElse(null)
        ?.requiredVersion

private fun Project.catalogVersion(key: String): String =
    catalogVersionOrNull(key) ?: throw GradleException("libs.versions.toml does not define version '$key'")

private fun Project.resolveSingle(notation: String): File {
    val configuration = configurations.detachedConfiguration(dependencies.create(notation)).apply {
        isTransitive = false
    }
    return configuration.resolve().singleOrNull()
        ?: throw GradleException("Expected exactly one file for $notation")
}

private data class ManifestEntry(val tracked: TrackedArtifact, val version: String, val baselineVersion: String?)

private fun manifestJson(
    composeBom: String,
    composeUiVersion: String,
    artifacts: List<ManifestEntry>,
): String = buildString {
    val q = MaterialApiInventory::quote
    appendLine("{")
    appendLine("  \"composeBom\": ${q(composeBom)},")
    appendLine("  \"composeUiVersion\": ${q(composeUiVersion)},")
    appendLine("  \"artifacts\": [")
    artifacts.forEachIndexed { index, (tracked, version, baselineVersion) ->
        append("    {\"artifact\": ${q(tracked.artifact)}, \"version\": ${q(version)}, ")
        append("\"versionKey\": ${q(tracked.versionKey)}, \"file\": ${q(tracked.file)}")
        if (baselineVersion != null) {
            append(", \"baselineVersion\": ${q(baselineVersion)}, \"baselineVersionKey\": ${q(tracked.baselineVersionKey!!)}")
            append(", \"baselineFile\": ${q(tracked.baselineFile)}")
        }
        append("}")
        appendLine(if (index == artifacts.lastIndex) "" else ",")
    }
    appendLine("  ]")
    appendLine("}")
}

private data class InventorySymbol(val fqName: String, val kind: String, val group: String)

private fun readInventory(rootDir: File): Map<String, List<InventorySymbol>> {
    @Suppress("UNCHECKED_CAST")
    val manifest = JsonSlurper().parse(File(rootDir, MANIFEST_FILE)) as Map<String, Any?>
    @Suppress("UNCHECKED_CAST")
    val artifacts = manifest["artifacts"] as List<Map<String, String>>
    return artifacts.associate { entry ->
        @Suppress("UNCHECKED_CAST")
        val inventory = JsonSlurper().parse(File(rootDir, "$INVENTORY_DIRECTORY/${entry.getValue("file")}")) as Map<String, Any?>
        @Suppress("UNCHECKED_CAST")
        val symbols = (inventory["symbols"] as List<Map<String, Any?>>).map {
            InventorySymbol(it["fqName"] as String, it["kind"] as String, it["group"] as String)
        }
        entry.getValue("artifact") to symbols
    }
}

/** Top-level symbols the catalog references directly, or through one of their members. */
private fun coveredSymbols(catalogFile: File): Set<String> {
    @Suppress("UNCHECKED_CAST")
    val catalog = JsonSlurper().parse(catalogFile) as Map<String, Any?>
    @Suppress("UNCHECKED_CAST")
    val entries = catalog["entries"] as List<Map<String, Any?>>
    @Suppress("UNCHECKED_CAST")
    val references = entries.flatMap { entry -> entry["apiReferences"] as List<Map<String, Any?>> }
    // Opt-in annotations are documented by every experimental reference that requires them.
    val referenced = references.map { it["symbol"] as String } +
        references.mapNotNull { it["optInAnnotation"] as? String }
    return referenced.flatMap { symbol -> listOf(symbol, symbol.substringBeforeLast('.')) }.toSet()
}

internal data class LedgerEntry(
    val fqName: String,
    val artifact: String,
    val status: String,
    val group: String,
    val note: String,
)

private fun readLedger(file: File): List<LedgerEntry> {
    if (!file.isFile) return emptyList()
    @Suppress("UNCHECKED_CAST")
    val ledger = JsonSlurper().parse(file) as Map<String, Any?>
    @Suppress("UNCHECKED_CAST")
    return (ledger["symbols"] as List<Map<String, String>>).map {
        LedgerEntry(it.getValue("fqName"), it.getValue("artifact"), it.getValue("status"), it.getValue("group"), it["note"].orEmpty())
    }
}

private fun ledgerJson(entries: List<LedgerEntry>): String = buildString {
    val q = MaterialApiInventory::quote
    appendLine("{")
    appendLine("  \"statuses\": {")
    appendLine("    \"planned\": \"Official public API that a catalog entry must still demonstrate and cite.\",")
    appendLine("    \"excluded\": \"Deliberately not demonstrated; the note records why.\",")
    appendLine("    \"new\": \"Appeared after a version bump and has not been triaged yet. Tests fail until it is planned or excluded.\"")
    appendLine("  },")
    appendLine("  \"symbols\": [")
    entries.forEachIndexed { index, entry ->
        append("    {\"fqName\": ${q(entry.fqName)}, \"artifact\": ${q(entry.artifact)}, ")
        append("\"status\": ${q(entry.status)}, \"group\": ${q(entry.group)}, \"note\": ${q(entry.note)}}")
        appendLine(if (index == entries.lastIndex) "" else ",")
    }
    appendLine("  ]")
    appendLine("}")
}

private fun coverageReport(
    inventory: Map<String, List<InventorySymbol>>,
    covered: Set<String>,
    ledger: List<LedgerEntry>,
): String = buildString {
    val ledgerByName = ledger.associateBy { it.fqName }
    appendLine("# Material API Coverage")
    appendLine()
    appendLine("Generated by `./gradlew :app:updateApiCoverageLedger` from the checked-in inventory in `inventory/`,")
    appendLine("the bundled catalog, and `coverage-ledger.json`. Do not edit by hand.")
    appendLine()
    appendLine("A symbol is **covered** when a catalog entry cites it (or one of its members) in `apiReferences`.")
    appendLine("Deprecated symbols are tracked in the inventory but are not required.")
    appendLine()
    appendLine("| Artifact | Public symbols | Covered | Planned | Excluded | New | Coverage |")
    appendLine("| :--- | ---: | ---: | ---: | ---: | ---: | ---: |")
    var totalLive = 0
    var totalCovered = 0
    inventory.forEach { (artifact, symbols) ->
        val live = symbols.filter { it.kind != "deprecated" }
        val coveredCount = live.count { it.fqName in covered }
        val statuses = live.mapNotNull { ledgerByName[it.fqName]?.status }.groupingBy { it }.eachCount()
        totalLive += live.size
        totalCovered += coveredCount
        appendLine(
            "| `$artifact` | ${live.size} | $coveredCount | ${statuses["planned"] ?: 0} | " +
                "${statuses["excluded"] ?: 0} | ${statuses["new"] ?: 0} | ${percent(coveredCount, live.size)} |",
        )
    }
    appendLine("| **Total** | **$totalLive** | **$totalCovered** | | | | **${percent(totalCovered, totalLive)}** |")
    appendLine()
    appendLine("## Remaining work by source group")
    appendLine()
    appendLine("Groups follow the source file that declares each symbol, which mirrors how the library organizes a component.")
    appendLine()
    ledger.filter { it.status != "excluded" }.groupBy { it.artifact }.forEach { (artifact, entries) ->
        appendLine("### `$artifact`")
        appendLine()
        appendLine("| Group | Symbols |")
        appendLine("| :--- | :--- |")
        entries.groupBy { it.group }.toSortedMap().forEach { (group, groupEntries) ->
            val names = groupEntries.joinToString(", ") { entry ->
                val simple = entry.fqName.substringAfterLast('.')
                if (entry.status == "new") "`$simple` (new)" else "`$simple`"
            }
            appendLine("| $group | $names |")
        }
        appendLine()
    }
    val excluded = ledger.filter { it.status == "excluded" }
    if (excluded.isNotEmpty()) {
        appendLine("## Excluded")
        appendLine()
        appendLine("| Symbol | Reason |")
        appendLine("| :--- | :--- |")
        excluded.forEach { appendLine("| `${it.fqName}` | ${it.note} |") }
        appendLine()
    }
}

private fun percent(part: Int, whole: Int): String =
    if (whole == 0) "n/a" else "${(part * 1000 / whole) / 10.0}%"
