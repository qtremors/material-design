package dev.qtremors.materialdesign

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    private val container by lazy { MaterialAppContainer(applicationContext) }
    private val galleryViewModel: GalleryViewModel by viewModels {
        GalleryViewModel.factory(container = container, catalogLoader = container::createCatalogRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Keep the splash until the saved theme arrives, with a ceiling so a slow disk never blocks launch.
        val shownAt = SystemClock.uptimeMillis()
        splash.setKeepOnScreenCondition {
            !galleryViewModel.settingsLoaded.value && SystemClock.uptimeMillis() - shownAt < SplashCeilingMillis
        }
        enableEdgeToEdge()
        setContent {
            MaterialGalleryApp(galleryViewModel, container.demoRegistry)
        }
    }

    private companion object {
        const val SplashCeilingMillis = 2_000L
    }
}
