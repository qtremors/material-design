package dev.qtremors.materialdesign

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Document
import org.w3c.dom.Element

class BackupRulesTest {
    private val preferencesFileName = "datastore/material_reference.preferences_pb"

    private fun repositoryRoot(): File =
        generateSequence(File(System.getProperty("user.dir") ?: ".").absoluteFile) { it.parentFile }
            .first { File(it, "material-app").isDirectory && File(it, "README.md").isFile }

    private fun parse(relativePath: String): Document {
        val file = File(repositoryRoot(), "material-app/app/src/main/$relativePath")
        assertTrue("Missing rule file: $file", file.isFile)
        val builder = DocumentBuilderFactory.newInstance()
        val factory = builder.newDocumentBuilder()
        return factory.parse(file)
    }

    private fun excludedPaths(document: Document, parentTag: String): List<String> {
        val roots = document.getElementsByTagName(parentTag)
        assertNotNull(roots)
        val paths = mutableListOf<String>()
        for (i in 0 until roots.length) {
            val root = roots.item(i) as Element
            val excludes = root.getElementsByTagName("exclude")
            for (j in 0 until excludes.length) {
                val exclude = excludes.item(j) as Element
                paths += exclude.getAttribute("path")
            }
        }
        return paths
    }

    @Test
    fun legacyBackupRulesParseAndExcludePreferenceFile() {
        val document = parse("res/xml/backup_rules.xml")
        assertEquals(1, document.getElementsByTagName("full-backup-content").length)
        val excluded = excludedPaths(document, "full-backup-content")
        assertTrue(
            "Expected $preferencesFileName to be excluded, got $excluded",
            excluded.contains(preferencesFileName)
        )
    }

    @Test
    fun dataExtractionRulesParseAndExcludePreferenceFileFromBackupAndTransfer() {
        val document = parse("res/xml/data_extraction_rules.xml")
        assertEquals(1, document.getElementsByTagName("data-extraction-rules").length)
        for (section in listOf("cloud-backup", "device-transfer")) {
            val excluded = excludedPaths(document, section)
            assertTrue(
                "Expected $section to exclude $preferencesFileName, got $excluded",
                excluded.contains(preferencesFileName)
            )
        }
    }

    @Test
    fun manifestEnablesBackupWithExplicitRules() {
        val manifest = File(repositoryRoot(), "material-app/app/src/main/AndroidManifest.xml").readText()
        assertTrue(manifest.contains("android:allowBackup=\"true\""))
        assertTrue(manifest.contains("@xml/data_extraction_rules"))
        assertTrue(manifest.contains("@xml/backup_rules"))
    }
}
