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
import dev.qtremors.material.samples.communication.BadgesSample
import dev.qtremors.material.samples.communication.BottomSheetsSample
import dev.qtremors.material.samples.communication.DialogsSample
import dev.qtremors.material.samples.communication.MenusSample
import dev.qtremors.material.samples.communication.ProgressIndicatorsSample
import dev.qtremors.material.samples.communication.SnackbarsSample
import dev.qtremors.material.samples.communication.TooltipsSample
import dev.qtremors.material.samples.containment.CarouselsSample
import dev.qtremors.material.samples.containment.ListsSample
import dev.qtremors.material.samples.containment.SegmentedListItemsSample
import dev.qtremors.material.samples.foundations.TypographySample
import dev.qtremors.material.samples.foundations.AccessibilitySample
import dev.qtremors.material.samples.foundations.ColorSample
import dev.qtremors.material.samples.foundations.ElevationSample
import dev.qtremors.material.samples.foundations.LayoutSample
import dev.qtremors.material.samples.foundations.MotionSample
import dev.qtremors.material.samples.foundations.ShapeSample
import dev.qtremors.material.samples.navigation.FloatingToolbarsSample
import dev.qtremors.material.samples.navigation.NavigationBarSample
import dev.qtremors.material.samples.navigation.NavigationRailSample
import dev.qtremors.material.samples.navigation.NavigationDrawerSample
import dev.qtremors.material.samples.navigation.TabsSample
import dev.qtremors.material.samples.navigation.TopAppBarsSample
import dev.qtremors.material.samples.selection.ChipsSample
import dev.qtremors.material.samples.selection.DatePickerSample
import dev.qtremors.material.samples.selection.SearchSample
import dev.qtremors.material.samples.selection.SegmentedButtonsSample
import dev.qtremors.material.samples.selection.SelectionControlsSample
import dev.qtremors.material.samples.selection.SlidersSample
import dev.qtremors.material.samples.selection.TextFieldsSample
import dev.qtremors.material.samples.selection.TimePickerSample

class MaterialDemoRegistry : ComponentDemoRegistry {
    override val registeredKeys: Set<String> = setOf(
        "actions.buttons",
        "actions.button-groups",
        "actions.toggle-buttons",
        "actions.split-buttons",
        "actions.fabs",
        "communication.progress",
        "communication.dialogs",
        "communication.bottom-sheets",
        "communication.snackbars",
        "communication.tooltips",
        "communication.menus",
        "communication.badges",
        "containment.segmented-lists",
        "containment.carousels",
        "containment.cards",
        "containment.lists",
        "selection.chips",
        "selection.segmented-buttons",
        "selection.controls",
        "selection.text-fields",
        "selection.search",
        "selection.sliders",
        "selection.date-pickers",
        "selection.time-pickers",
        "navigation.floating-toolbars",
        "navigation.bars",
        "navigation.rails",
        "navigation.drawers",
        "navigation.tabs",
        "navigation.top-app-bars",
        "foundations.typography",
        "foundations.color",
        "foundations.shape",
        "foundations.motion",
        "foundations.elevation",
        "foundations.layout",
        "foundations.accessibility",
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
            "communication.dialogs" -> DialogsSample()
            "communication.bottom-sheets" -> BottomSheetsSample()
            "communication.snackbars" -> SnackbarsSample()
            "communication.tooltips" -> TooltipsSample()
            "communication.menus" -> MenusSample()
            "communication.badges" -> BadgesSample()
            "containment.segmented-lists" -> SegmentedListItemsSample()
            "containment.carousels" -> CarouselsSample()
            "containment.cards" -> CardsSample()
            "containment.lists" -> ListsSample()
            "selection.chips" -> ChipsSample()
            "selection.segmented-buttons" -> SegmentedButtonsSample()
            "selection.controls" -> SelectionControlsSample()
            "selection.text-fields" -> TextFieldsSample()
            "selection.search" -> SearchSample()
            "selection.sliders" -> SlidersSample()
            "selection.date-pickers" -> DatePickerSample()
            "selection.time-pickers" -> TimePickerSample()
            "navigation.floating-toolbars" -> FloatingToolbarsSample()
            "navigation.bars" -> NavigationBarSample()
            "navigation.rails" -> NavigationRailSample()
            "navigation.drawers" -> NavigationDrawerSample()
            "navigation.tabs" -> TabsSample()
            "navigation.top-app-bars" -> TopAppBarsSample()
            "foundations.typography" -> TypographySample()
            "foundations.color" -> ColorSample()
            "foundations.shape" -> ShapeSample()
            "foundations.motion" -> MotionSample()
            "foundations.elevation" -> ElevationSample()
            "foundations.layout" -> LayoutSample()
            "foundations.accessibility" -> AccessibilitySample()
            else -> Text("Missing working demo: $demoKey")
        }
    }
}
