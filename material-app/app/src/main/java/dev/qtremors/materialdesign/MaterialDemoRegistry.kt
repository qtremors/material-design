package dev.qtremors.materialdesign

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.qtremors.material.core.catalog.ComponentDemoRegistry
import dev.qtremors.material.samples.actions.ButtonGroupSample
import dev.qtremors.material.samples.actions.ButtonSample
import dev.qtremors.material.samples.actions.FabMenuSample
import dev.qtremors.material.samples.actions.FloatingActionButtonSample
import dev.qtremors.material.samples.actions.IconButtonSample
import dev.qtremors.material.samples.actions.SplitButtonSample
import dev.qtremors.material.samples.actions.ToggleButtonSample
import dev.qtremors.material.samples.communication.BadgeSample
import dev.qtremors.material.samples.communication.BasicAlertDialogSample
import dev.qtremors.material.samples.communication.BottomSheetSample
import dev.qtremors.material.samples.communication.DialogSample
import dev.qtremors.material.samples.communication.DropdownMenuGroupSample
import dev.qtremors.material.samples.communication.LoadingIndicatorSample
import dev.qtremors.material.samples.communication.MenuSample
import dev.qtremors.material.samples.communication.ProgressIndicatorSample
import dev.qtremors.material.samples.communication.SnackbarSample
import dev.qtremors.material.samples.communication.TooltipSample
import dev.qtremors.material.samples.containment.CardSample
import dev.qtremors.material.samples.containment.CarouselSample
import dev.qtremors.material.samples.containment.HeroCarouselSample
import dev.qtremors.material.samples.containment.ListSample
import dev.qtremors.material.samples.containment.PullToRefreshSample
import dev.qtremors.material.samples.containment.SegmentedListItemSample
import dev.qtremors.material.samples.containment.SwipeToDismissSample
import dev.qtremors.material.samples.foundations.AccessibilitySample
import dev.qtremors.material.samples.foundations.ColorSample
import dev.qtremors.material.samples.foundations.DividerSample
import dev.qtremors.material.samples.foundations.ElevationSample
import dev.qtremors.material.samples.foundations.LayoutSample
import dev.qtremors.material.samples.foundations.MotionSample
import dev.qtremors.material.samples.foundations.ScaffoldSample
import dev.qtremors.material.samples.foundations.ShapeSample
import dev.qtremors.material.samples.foundations.TypographySample
import dev.qtremors.material.samples.navigation.BottomAppBarSample
import dev.qtremors.material.samples.navigation.FlexibleBottomAppBarSample
import dev.qtremors.material.samples.navigation.FloatingToolbarSample
import dev.qtremors.material.samples.navigation.NavigationBarSample
import dev.qtremors.material.samples.navigation.NavigationDrawerSample
import dev.qtremors.material.samples.navigation.NavigationRailSample
import dev.qtremors.material.samples.navigation.ScrollableTabRowSample
import dev.qtremors.material.samples.navigation.ShortNavigationBarSample
import dev.qtremors.material.samples.navigation.TabSample
import dev.qtremors.material.samples.navigation.TopAppBarSample
import dev.qtremors.material.samples.navigation.WideNavigationRailSample
import dev.qtremors.material.samples.selection.CheckboxSample
import dev.qtremors.material.samples.selection.ChipSample
import dev.qtremors.material.samples.selection.DatePickerSample
import dev.qtremors.material.samples.selection.RadioButtonSample
import dev.qtremors.material.samples.selection.SearchSample
import dev.qtremors.material.samples.selection.SegmentedButtonSample
import dev.qtremors.material.samples.selection.SliderSample
import dev.qtremors.material.samples.selection.SwitchSample
import dev.qtremors.material.samples.selection.TextFieldSample
import dev.qtremors.material.samples.selection.TimePickerDialogSample
import dev.qtremors.material.samples.selection.TimePickerSample
import dev.qtremors.material.samples.selection.TopSearchBarSample
import dev.qtremors.material.samples.selection.VerticalSliderSample

/**
 * Maps each catalog `demoKey` (`<sample module>.<entry id>`) to its working reference.
 *
 * One map is both the set of registered keys and the renderer, so a key can never be registered
 * without a demo. `DemoRegistryTest` checks this map against the bundled catalog.
 */
class MaterialDemoRegistry : ComponentDemoRegistry {
    private val demos: Map<String, @Composable () -> Unit> = buildMap<String, @Composable () -> Unit> {
        demo("actions.button-groups") { ButtonGroupSample() }
        demo("actions.buttons") { ButtonSample() }
        demo("actions.fab-menus") { FabMenuSample() }
        demo("actions.floating-action-buttons") { FloatingActionButtonSample() }
        demo("actions.icon-buttons") { IconButtonSample() }
        demo("actions.split-buttons") { SplitButtonSample() }
        demo("actions.toggle-buttons") { ToggleButtonSample() }
        demo("communication.badges") { BadgeSample() }
        demo("communication.basic-alert-dialogs") { BasicAlertDialogSample() }
        demo("communication.bottom-sheets") { BottomSheetSample() }
        demo("communication.dialogs") { DialogSample() }
        demo("communication.grouped-dropdown-menus") { DropdownMenuGroupSample() }
        demo("communication.loading-indicators") { LoadingIndicatorSample() }
        demo("communication.menus") { MenuSample() }
        demo("communication.progress-indicators") { ProgressIndicatorSample() }
        demo("communication.snackbars") { SnackbarSample() }
        demo("communication.tooltips") { TooltipSample() }
        demo("containment.cards") { CardSample() }
        demo("containment.carousels") { CarouselSample() }
        demo("containment.hero-carousels") { HeroCarouselSample() }
        demo("containment.lists") { ListSample() }
        demo("containment.pull-to-refresh") { PullToRefreshSample() }
        demo("containment.segmented-list-items") { SegmentedListItemSample() }
        demo("containment.swipe-to-dismiss") { SwipeToDismissSample() }
        demo("foundations.accessibility") { AccessibilitySample() }
        demo("foundations.color") { ColorSample() }
        demo("foundations.dividers") { DividerSample() }
        demo("foundations.elevation") { ElevationSample() }
        demo("foundations.layout") { LayoutSample() }
        demo("foundations.motion") { MotionSample() }
        demo("foundations.scaffold") { ScaffoldSample() }
        demo("foundations.shape") { ShapeSample() }
        demo("foundations.typography") { TypographySample() }
        demo("navigation.bottom-app-bars") { BottomAppBarSample() }
        demo("navigation.flexible-bottom-app-bars") { FlexibleBottomAppBarSample() }
        demo("navigation.floating-toolbars") { FloatingToolbarSample() }
        demo("navigation.navigation-bars") { NavigationBarSample() }
        demo("navigation.navigation-drawers") { NavigationDrawerSample() }
        demo("navigation.navigation-rails") { NavigationRailSample() }
        demo("navigation.scrollable-tab-rows") { ScrollableTabRowSample() }
        demo("navigation.short-navigation-bars") { ShortNavigationBarSample() }
        demo("navigation.tabs") { TabSample() }
        demo("navigation.top-app-bars") { TopAppBarSample() }
        demo("navigation.wide-navigation-rails") { WideNavigationRailSample() }
        demo("selection.checkbox") { CheckboxSample() }
        demo("selection.chips") { ChipSample() }
        demo("selection.date-pickers") { DatePickerSample() }
        demo("selection.radio-button") { RadioButtonSample() }
        demo("selection.search") { SearchSample() }
        demo("selection.segmented-buttons") { SegmentedButtonSample() }
        demo("selection.sliders") { SliderSample() }
        demo("selection.switch") { SwitchSample() }
        demo("selection.text-fields") { TextFieldSample() }
        demo("selection.time-picker-dialogs") { TimePickerDialogSample() }
        demo("selection.time-pickers") { TimePickerSample() }
        demo("selection.top-search-bars") { TopSearchBarSample() }
        demo("selection.vertical-sliders") { VerticalSliderSample() }
    }

    override val registeredKeys: Set<String> = demos.keys

    @Composable
    fun Render(demoKey: String) {
        val demo = demos[demoKey]
        if (demo != null) demo() else Text("Missing working demo: $demoKey")
    }

    private fun MutableMap<String, @Composable () -> Unit>.demo(key: String, content: @Composable () -> Unit) {
        check(put(key, content) == null) { "Duplicate demo key $key" }
    }
}
