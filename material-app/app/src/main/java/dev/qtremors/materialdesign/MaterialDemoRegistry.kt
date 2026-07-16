package dev.qtremors.materialdesign

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.qtremors.material.core.catalog.ComponentDemoRegistry
import dev.qtremors.material.samples.actions.ButtonsSample
import dev.qtremors.material.samples.actions.ButtonGroupsSample
import dev.qtremors.material.samples.actions.FloatingActionButtonsSample
import dev.qtremors.material.samples.actions.SplitButtonsSample
import dev.qtremors.material.samples.actions.ToggleButtonsSample
import dev.qtremors.material.samples.containment.CardsSample
import dev.qtremors.material.samples.communication.ProgressIndicatorsSample
import dev.qtremors.material.samples.containment.CarouselsSample
import dev.qtremors.material.samples.containment.ListsSample
import dev.qtremors.material.samples.containment.SegmentedListItemsSample
import dev.qtremors.material.samples.foundations.TypographySample
import dev.qtremors.material.samples.navigation.FloatingToolbarsSample
import dev.qtremors.material.samples.selection.ChipsSample
import dev.qtremors.material.samples.selection.SegmentedButtonsSample
import dev.qtremors.material.samples.selection.SelectionControlsSample

class MaterialDemoRegistry : ComponentDemoRegistry {
    override val registeredKeys: Set<String> = setOf(
        "actions.buttons",
        "actions.button-groups",
        "actions.toggle-buttons",
        "actions.split-buttons",
        "actions.fabs",
        "communication.progress",
        "containment.segmented-lists",
        "containment.carousels",
        "containment.cards",
        "containment.lists",
        "selection.chips",
        "selection.segmented-buttons",
        "selection.controls",
        "navigation.floating-toolbars",
        "foundations.typography",
    )

    @Composable
    fun Render(demoKey: String) {
        when (demoKey) {
            "actions.buttons" -> ButtonsSample()
            "actions.button-groups" -> ButtonGroupsSample()
            "actions.toggle-buttons" -> ToggleButtonsSample()
            "actions.split-buttons" -> SplitButtonsSample()
            "actions.fabs" -> FloatingActionButtonsSample()
            "communication.progress" -> ProgressIndicatorsSample()
            "containment.segmented-lists" -> SegmentedListItemsSample()
            "containment.carousels" -> CarouselsSample()
            "containment.cards" -> CardsSample()
            "containment.lists" -> ListsSample()
            "selection.chips" -> ChipsSample()
            "selection.segmented-buttons" -> SegmentedButtonsSample()
            "selection.controls" -> SelectionControlsSample()
            "navigation.floating-toolbars" -> FloatingToolbarsSample()
            "foundations.typography" -> TypographySample()
            else -> Text("Missing working demo: $demoKey")
        }
    }
}
