package dev.qtremors.materialdesign

import dev.qtremors.material.core.catalog.CatalogDocument
import java.io.File
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class DemoRegistryTest {
    @Test
    fun everyCatalogEntryHasExactlyOneRegisteredDemo() {
        val workingDirectory = requireNotNull(System.getProperty("user.dir"))
        val repositoryRoot = generateSequence(File(workingDirectory).absoluteFile) { it.parentFile }
            .first { File(it, "material-app").isDirectory && File(it, "README.md").isFile }
        val catalogFile = File(repositoryRoot, "material-app/core/catalog/src/main/assets/catalog/catalog.json")
        val document = Json.decodeFromString<CatalogDocument>(catalogFile.readText())
        assertEquals(document.entries.map { it.demoKey }.toSet(), MaterialDemoRegistry().registeredKeys)
    }
}
