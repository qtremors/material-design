import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal val Project.materialNamespace: String
    get() = "dev.qtremors.material" + path.replace(':', '.')

internal fun configureAndroidCommon(commonExtension: CommonExtension) {
    commonExtension.compileSdk = 37
    commonExtension.defaultConfig.minSdk = 24
    commonExtension.compileOptions.sourceCompatibility = JavaVersion.VERSION_11
    commonExtension.compileOptions.targetCompatibility = JavaVersion.VERSION_11
}
