import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal val Project.materialNamespace: String
    get() = "dev.qtremors.material" + path.replace(':', '.')

internal fun Project.configureAndroidCommon(commonExtension: CommonExtension) {
    commonExtension.compileSdk = 37
    commonExtension.defaultConfig.minSdk = 24
    commonExtension.compileOptions.sourceCompatibility = JavaVersion.VERSION_11
    commonExtension.compileOptions.targetCompatibility = JavaVersion.VERSION_11

    commonExtension.packaging.jniLibs.keepDebugSymbols += setOf(
        "**/libandroidx.graphics.path.so",
        "**/libdatastore_shared_counter.so",
    )

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
}
