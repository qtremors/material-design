import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            extensions.configure(CommonExtension::class.java) {
                buildFeatures.compose = true
            }

            // Material 3 Expressive is the app's baseline, so every Compose module opts in once here.
            tasks.withType<KotlinCompilationTask<*>>().configureEach {
                compilerOptions.optIn.addAll(
                    "androidx.compose.material3.ExperimentalMaterial3Api",
                    "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
                )
            }
        }
    }
}
