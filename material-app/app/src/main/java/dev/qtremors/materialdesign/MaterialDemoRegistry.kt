package dev.qtremors.materialdesign

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.qtremors.material.core.catalog.ComponentDemoRegistry
import dev.qtremors.material.samples.actions.ButtonsSample
import dev.qtremors.material.samples.actions.FloatingActionButtonsSample
import dev.qtremors.material.samples.actions.SplitButtonsSample
import dev.qtremors.material.samples.communication.ProgressIndicatorsSample
import dev.qtremors.material.samples.containment.CarouselsSample
import dev.qtremors.material.samples.containment.SegmentedListItemsSample
import dev.qtremors.material.samples.foundations.TypographySample
import dev.qtremors.material.samples.navigation.FloatingToolbarsSample

class MaterialDemoRegistry : ComponentDemoRegistry {
    override val registeredKeys: Set<String> = setOf(
        "actions.buttons",
        "actions.split-buttons",
        "actions.fabs",
        "communication.progress",
        "containment.segmented-lists",
        "containment.carousels",
        "navigation.floating-toolbars",
        "foundations.typography",
    )

    @Composable
    fun Render(demoKey: String) {
        when (demoKey) {
            "actions.buttons" -> ButtonsSample()
            "actions.split-buttons" -> SplitButtonsSample()
            "actions.fabs" -> FloatingActionButtonsSample()
            "communication.progress" -> ProgressIndicatorsSample()
            "containment.segmented-lists" -> SegmentedListItemsSample()
            "containment.carousels" -> CarouselsSample()
            "navigation.floating-toolbars" -> FloatingToolbarsSample()
            "foundations.typography" -> TypographySample()
            else -> Text("Missing working demo: $demoKey")
        }
    }
}
