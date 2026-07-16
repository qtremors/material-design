pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Material Design"
include(":app")
include(":core:catalog")
include(":core:data")
include(":core:designsystem")
include(":feature:explore")
include(":feature:catalog")
include(":feature:apis")
include(":feature:foundations")
include(":feature:detail")
include(":feature:settings")
include(":samples:actions")
include(":samples:communication")
include(":samples:containment")
include(":samples:navigation")
include(":samples:foundations")
include(":samples:selection")
