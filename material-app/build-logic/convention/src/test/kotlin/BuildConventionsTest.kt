import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.nio.file.Files
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BuildConventionsTest {

    @Test
    fun `production string check flags hardcoded feature ui string`() {
        val projectDir = Files.createTempDirectory("material-build-logic-test")
        projectDir.resolve("settings.gradle.kts").writeText("")
        val catalog = projectDir.resolve("gradle/libs.versions.toml")
        catalog.parent.createDirectories()
        catalog.writeText(
            """
            [versions]
            sample = "1.0"

            [libraries]
            sample = { module = "example:sample", version.ref = "sample" }

            [plugins]
            sample = { id = "example.sample", version.ref = "sample" }
            """.trimIndent()
        )
        projectDir.resolve("gradle.properties").writeText(
            """
            appVersionName=2.0.8
            appVersionCode=208
            """.trimIndent()
        )
        projectDir.resolve("build.gradle.kts").writeText(
            """
            plugins {
                id("materialdesign.build.conventions")
            }
            val versionName = "2.0.8"
            val versionCode = 208
            """.trimIndent()
        )
        val source = projectDir
            .resolve("feature/sample/src/main/java/dev/qtremors/material/feature/sample/SampleScreen.kt")
        source.parent.createDirectories()
        source.writeText(
            """
            package dev.qtremors.material.feature.sample

            fun SampleScreen() {
                Text("Hardcoded production string")
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir.toFile())
            .withPluginClasspath()
            .withArguments("checkProductionStrings")
            .buildAndFail()

        assertEquals(TaskOutcome.FAILED, result.task(":checkProductionStrings")?.outcome)
        assertTrue(result.output.contains("Found hardcoded production UI strings:"))
        assertTrue(result.output.contains("feature/sample/src/main/java/dev/qtremors/material/feature/sample/SampleScreen.kt:4"))
    }

    @Test
    fun `verifyVersionCatalogFreshness and release metadata succeed with valid setup`() {
        val projectDir = Files.createTempDirectory("material-build-logic-valid-test")
        projectDir.resolve("settings.gradle.kts").writeText("")
        val catalog = projectDir.resolve("gradle/libs.versions.toml")
        catalog.parent.createDirectories()
        catalog.writeText(
            """
            [versions]
            agp = "9.3.0"

            [libraries]
            sample = { module = "example:sample", version.ref = "agp" }

            [plugins]
            sample = { id = "example.sample", version.ref = "agp" }
            """.trimIndent()
        )
        projectDir.resolve("gradle.properties").writeText(
            """
            appVersionName=2.0.8
            appVersionCode=208
            """.trimIndent()
        )
        projectDir.resolve("build.gradle.kts").writeText(
            """
            plugins {
                id("materialdesign.build.conventions")
            }
            val versionName = "2.0.8"
            val versionCode = 208
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir.toFile())
            .withPluginClasspath()
            .withArguments("verifyMaterialBuildConventions")
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyVersionCatalogFreshness")?.outcome)
        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyReleaseVersionMetadata")?.outcome)
        assertEquals(TaskOutcome.SUCCESS, result.task(":verifyMaterialBuildConventions")?.outcome)
    }
}
