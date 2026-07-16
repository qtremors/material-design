package dev.qtremors.materialdesign

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    private val container by lazy { MaterialAppContainer(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val galleryViewModel: GalleryViewModel = viewModel(factory = GalleryViewModel.factory(container))
            MaterialGalleryApp(galleryViewModel, container.demoRegistry)
        }
    }
}
