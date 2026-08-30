package dev.qtremors.materialdesign

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.qtremors.material.core.catalog.ComponentDemoRegistry
import dev.qtremors.material.samples.actions.ButtonsSample
import dev.qtremors.material.samples.actions.ButtonGroupsSample
import dev.qtremors.material.samples.actions.FabMenusSample
import dev.qtremors.material.samples.actions.FloatingActionButtonsSample
import dev.qtremors.material.samples.actions.IconButtonsSample
import dev.qtremors.material.samples.actions.SplitButtonsSample
import dev.qtremors.material.samples.actions.ToggleButtonsSample
import dev.qtremors.material.samples.containment.CardsSample
import dev.qtremors.material.samples.communication.BadgesSample
import dev.qtremors.material.samples.communication.BottomSheetsSample
import dev.qtremors.material.samples.communication.GroupedDropdownMenusSample
import dev.qtremors.material.samples.communication.DialogsSample
import dev.qtremors.material.samples.communication.LoadingIndicatorsSample
import dev.qtremors.material.samples.communication.MenusSample
import dev.qtremors.material.samples.communication.ProgressIndicatorsSample
import dev.qtremors.material.samples.communication.SnackbarsSample
import dev.qtremors.material.samples.communication.TooltipsSample
import dev.qtremors.material.samples.containment.CarouselsSample
import dev.qtremors.material.samples.containment.HeroCarouselsSample
import dev.qtremors.material.samples.containment.ListsSample
import dev.qtremors.material.samples.containment.PullToRefreshSample
import dev.qtremors.material.samples.containment.SegmentedListItemsSample
import dev.qtremors.material.samples.containment.SwipeToDismissSample
import dev.qtremors.material.samples.foundations.ScaffoldFoundationSample
import dev.qtremors.material.samples.foundations.TypographySample
import dev.qtremors.material.samples.foundations.AccessibilitySample
import dev.qtremors.material.samples.foundations.ColorSample
import dev.qtremors.material.samples.foundations.ElevationSample
import dev.qtremors.material.samples.foundations.LayoutSample
import dev.qtremors.material.samples.foundations.MotionSample
import dev.qtremors.material.samples.foundations.ShapeSample
import dev.qtremors.material.samples.navigation.FloatingToolbarsSample
import dev.qtremors.material.samples.navigation.BottomAppBarsSample
import dev.qtremors.material.samples.navigation.FlexibleBottomAppBarSample
import dev.qtremors.material.samples.navigation.NavigationBarSample
import dev.qtremors.material.samples.navigation.NavigationRailSample
import dev.qtremors.material.samples.navigation.NavigationDrawerSample
import dev.qtremors.material.samples.navigation.ScrollableTabRowsSample
import dev.qtremors.material.samples.navigation.ShortNavigationBarSample
import dev.qtremors.material.samples.navigation.TabsSample
import dev.qtremors.material.samples.navigation.TopAppBarsSample
import dev.qtremors.material.samples.navigation.WideNavigationRailSample
import dev.qtremors.material.samples.selection.ChipsSample
import dev.qtremors.material.samples.selection.BasicAlertDialogsSample
import dev.qtremors.material.samples.selection.DatePickerSample
import dev.qtremors.material.samples.selection.DividersSample
import dev.qtremors.material.samples.selection.SearchSample
import dev.qtremors.material.samples.selection.SegmentedButtonsSample
import dev.qtremors.material.samples.selection.SelectionControlsSample
import dev.qtremors.material.samples.selection.SlidersSample
import dev.qtremors.material.samples.selection.TextFieldsSample
import dev.qtremors.material.samples.selection.TimePickerDialogsSample
import dev.qtremors.material.samples.selection.TimePickerSample
import dev.qtremors.material.samples.selection.TopSearchBarsSample
import dev.qtremors.material.samples.selection.VerticalSlidersSample

class MaterialDemoRegistry : ComponentDemoRegistry {
    override val registeredKeys: Set<String> = setOf(
        "actions.buttons",
        "actions.button-groups",
        "actions.toggle-buttons",
        "actions.split-buttons",
        "actions.fabs",
        "actions.icon-buttons",
        "actions.fab-menus",
        "communication.progress",
        "communication.loading-indicators",
        "communication.dialogs",
        "communication.bottom-sheets",
        "communication.snackbars",
        "communication.tooltips",
        "communication.menus",
        "communication.badges",
        "communication.grouped-dropdown-menus",
        "communication.basic-alert-dialogs",
        "containment.segmented-lists",
        "containment.carousels",
        "containment.hero-carousels",
        "containment.swipe-to-dismiss",
        "containment.pull-to-refresh",
        "containment.cards",
        "containment.lists",
        "selection.chips",
        "selection.segmented-buttons",
        "selection.controls",
        "selection.text-fields",
        "selection.search",
        "selection.sliders",
        "selection.vertical-sliders",
        "selection.date-pickers",
        "selection.time-pickers",
        "selection.time-picker-dialogs",
        "selection.top-search-bars",
        "navigation.floating-toolbars",
        "navigation.bars",
        "navigation.short-navigation-bars",
        "navigation.bottom-app-bars",
        "navigation.flexible-bottom-app-bars",
        "navigation.rails",
        "navigation.wide-navigation-rails",
        "navigation.drawers",
        "navigation.tabs",
        "navigation.scrollable-tab-rows",
        "navigation.top-app-bars",
        "foundations.typography",
        "foundations.color",
        "foundations.shape",
        "foundations.motion",
        "foundations.elevation",
        "foundations.layout",
        "foundations.scaffold",
        "foundations.dividers",
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
            "actions.icon-buttons" -> IconButtonsSample()
            "actions.fab-menus" -> FabMenusSample()
            "communication.progress" -> ProgressIndicatorsSample()
            "communication.loading-indicators" -> LoadingIndicatorsSample()
            "communication.dialogs" -> DialogsSample()
            "communication.bottom-sheets" -> BottomSheetsSample()
            "communication.snackbars" -> SnackbarsSample()
            "communication.tooltips" -> TooltipsSample()
            "communication.menus" -> MenusSample()
            "communication.badges" -> BadgesSample()
            "communication.grouped-dropdown-menus" -> GroupedDropdownMenusSample()
            "communication.basic-alert-dialogs" -> BasicAlertDialogsSample()
            "containment.segmented-lists" -> SegmentedListItemsSample()
            "containment.carousels" -> CarouselsSample()
            "containment.hero-carousels" -> HeroCarouselsSample()
            "containment.swipe-to-dismiss" -> SwipeToDismissSample()
            "containment.pull-to-refresh" -> PullToRefreshSample()
            "containment.cards" -> CardsSample()
            "containment.lists" -> ListsSample()
            "selection.chips" -> ChipsSample()
            "selection.segmented-buttons" -> SegmentedButtonsSample()
            "selection.controls" -> SelectionControlsSample()
            "selection.text-fields" -> TextFieldsSample()
            "selection.search" -> SearchSample()
            "selection.sliders" -> SlidersSample()
            "selection.vertical-sliders" -> VerticalSlidersSample()
            "selection.date-pickers" -> DatePickerSample()
            "selection.time-pickers" -> TimePickerSample()
            "selection.time-picker-dialogs" -> TimePickerDialogsSample()
            "selection.top-search-bars" -> TopSearchBarsSample()
            "navigation.floating-toolbars" -> FloatingToolbarsSample()
            "navigation.bars" -> NavigationBarSample()
            "navigation.short-navigation-bars" -> ShortNavigationBarSample()
            "navigation.bottom-app-bars" -> BottomAppBarsSample()
            "navigation.flexible-bottom-app-bars" -> FlexibleBottomAppBarSample()
            "navigation.rails" -> NavigationRailSample()
            "navigation.wide-navigation-rails" -> WideNavigationRailSample()
            "navigation.drawers" -> NavigationDrawerSample()
            "navigation.tabs" -> TabsSample()
            "navigation.scrollable-tab-rows" -> ScrollableTabRowsSample()
            "navigation.top-app-bars" -> TopAppBarsSample()
            "foundations.typography" -> TypographySample()
            "foundations.color" -> ColorSample()
            "foundations.shape" -> ShapeSample()
            "foundations.motion" -> MotionSample()
            "foundations.elevation" -> ElevationSample()
            "foundations.layout" -> LayoutSample()
            "foundations.scaffold" -> ScaffoldFoundationSample()
            "foundations.dividers" -> DividersSample()
            "foundations.accessibility" -> AccessibilitySample()
            else -> Text("Missing working demo: $demoKey")
        }
    }
}
