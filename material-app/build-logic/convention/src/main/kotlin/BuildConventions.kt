import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildConventionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.registerBuildConventions()
    }
}

internal fun Project.registerBuildConventions() {
    val versionCatalog = rootProject.layout.projectDirectory.file("gradle/libs.versions.toml")
    val gradleProperties = rootProject.layout.projectDirectory.file("gradle.properties")
    val appBuildFile = layout.projectDirectory.file("build.gradle.kts")

    val verifyVersionCatalogFreshness = tasks.register("verifyVersionCatalogFreshness") {
        group = "verification"
        description = "Verifies that dependency freshness checks have an active version catalog to inspect."
        inputs.file(versionCatalog)
        doLast {
            val catalog = versionCatalog.asFile
            require(catalog.exists()) {
                "Missing gradle/libs.versions.toml; dependency freshness checks need a version catalog."
            }
            val text = catalog.readText()
            require("[versions]" in text && "[libraries]" in text && "[plugins]" in text) {
                "gradle/libs.versions.toml must keep [versions], [libraries], and [plugins] sections."
            }
        }
    }

    val verifyReleaseVersionMetadata = tasks.register("verifyReleaseVersionMetadata") {
        group = "verification"
        description = "Checks that the app declares explicit versionName and versionCode metadata."
        inputs.files(gradleProperties, appBuildFile)
        doLast {
            val propsText = gradleProperties.asFile.readText()
            require(Regex("""appVersionName\s*=\s*\d+\.\d+\.\d+""").containsMatchIn(propsText)) {
                "gradle.properties must declare appVersionName in X.Y.Z format."
            }
            require(Regex("""appVersionCode\s*=\s*\d+""").containsMatchIn(propsText)) {
                "gradle.properties must declare appVersionCode as an integer."
            }
            val buildFileText = appBuildFile.asFile.readText()
            require("versionName" in buildFileText && "versionCode" in buildFileText) {
                "app/build.gradle.kts must consume versionName and versionCode."
            }
        }
    }

    if (rootProject.tasks.findByName("checkProductionStrings") == null) {
        val rootDirFile = rootProject.layout.projectDirectory.asFile
        val sources = rootProject.fileTree(rootProject.projectDir) {
            include("**/src/main/java/**/*.kt")
            include("**/src/main/kotlin/**/*.kt")
            exclude("**/build/**")
            exclude("**/src/test/**")
            exclude("**/src/androidTest/**")
            exclude("**/samples/**")
            exclude("**/core/designsystem/**")
        }
        rootProject.tasks.register("checkProductionStrings") {
            group = "verification"
            description = "Flags obvious hardcoded production UI strings across production Android sources."
            inputs.files(sources)

            doLast {
                val suspiciousPattern = Regex(
                    """(Text\(\s*"[^"]*[A-Za-z][^"]*"|contentDescription\s*=\s*"[^"]*[A-Za-z][^"]*"|placeholder\s*=\s*"[^"]*[A-Za-z][^"]*"|title\s*=\s*"[^"]*[A-Za-z][^"]*"|Toast\.makeText\([^,]+,\s*"[^"]*[A-Za-z][^"]*"|createChooser\([^,]+,\s*"[^"]*[A-Za-z][^"]*"|fileOperationStatusMessage\s*=\s*"[^"]*[A-Za-z][^"]*"|setContentTitle\("([^"]*[A-Za-z][^"]*)"|setContentText\("([^"]*[A-Za-z][^"]*)"|addAction\([^"]*"[^"]*[A-Za-z][^"]*")"""
                )
                val allowedFragments = listOf(
                    "android.os.Build.",
                    "Text(\".\${",
                    "Text(\"\${",
                    "Text(\"•\"",
                    "Text(\"Missing working demo:",
                    "AppLogger.",
                    "Regex(",
                    "SimpleDateFormat(",
                    "DateTimeFormatter",
                    "ImageRequest.Builder",
                    "mutableStateOf(\"\")",
                    "SavedStateHandle",
                    "MIME",
                    "mimeType",
                    "contentType =",
                    "label =",
                    "label = \"",
                    "label = {",
                    "cacheKey",
                    "content://",
                    "Material Design-$",
                )
                val offenders = sources.files.flatMap { sourceFile ->
                    val relativePath = sourceFile.relativeTo(rootDirFile).invariantSeparatorsPath
                    sourceFile.readLines().mapIndexedNotNull { index, line ->
                        val trimmed = line.trim()
                        if (suspiciousPattern.containsMatchIn(trimmed) &&
                            allowedFragments.none { trimmed.contains(it) } &&
                            !trimmed.contains("R.string.") &&
                            !trimmed.contains("R.plurals.") &&
                            !trimmed.contains("stringResource(") &&
                            !trimmed.contains("pluralStringResource(") &&
                            !trimmed.contains("getString(")
                        ) {
                            "$relativePath:${index + 1}: $trimmed"
                        } else {
                            null
                        }
                    }
                }
                if (offenders.isNotEmpty()) {
                    throw GradleException(buildString {
                        appendLine("Found hardcoded production UI strings:")
                        offenders.forEach { appendLine(it) }
                    })
                }
            }
        }
    }

    tasks.register("verifyMaterialBuildConventions") {
        group = "verification"
        description = "Runs Material Design build convention checks used for release readiness."
        dependsOn(verifyVersionCatalogFreshness, verifyReleaseVersionMetadata)
    }
}
