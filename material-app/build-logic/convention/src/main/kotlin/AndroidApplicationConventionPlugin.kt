import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply("com.android.application")

            extensions.configure(CommonExtension::class.java) {
                configureAndroidCommon(this)
            }

            registerBuildConventions()
        }
    }
}
