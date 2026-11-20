package dev.qtremors.materialdesign.shell

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.components.AppScaffold
import dev.qtremors.material.feature.apis.ApisScreen
import dev.qtremors.material.feature.catalog.CatalogScreen
import dev.qtremors.material.feature.explore.ExploreScreen
import dev.qtremors.material.feature.foundations.FoundationsScreen
import dev.qtremors.materialdesign.CatalogSnapshot
import dev.qtremors.materialdesign.LibraryState
import dev.qtremors.materialdesign.R
import dev.qtremors.materialdesign.RootDestination
import dev.qtremors.materialdesign.SearchState

internal data class RootTab(val destination: RootDestination, val icon: ImageVector, @StringRes val labelRes: Int)

internal val RootTabs = listOf(
    RootTab(RootDestination.EXPLORE, Icons.Rounded.Explore, R.string.app_destination_explore),
    RootTab(RootDestination.CATALOG, Icons.Rounded.Apps, R.string.app_destination_catalog),
    RootTab(RootDestination.APIS, Icons.Rounded.DataObject, R.string.app_destination_apis),
    RootTab(RootDestination.FOUNDATIONS, Icons.Rounded.Palette, R.string.app_destination_foundations),
)

@StringRes
private fun RootDestination.titleRes(): Int = when (this) {
    RootDestination.EXPLORE -> R.string.app_title_material_design
    RootDestination.CATALOG -> R.string.app_destination_catalog
    RootDestination.APIS -> R.string.app_destination_apis
    RootDestination.FOUNDATIONS -> R.string.app_destination_foundations
}

/**
 * The four top-level destinations. Each has its own large flexible top bar that collapses on
 * scroll; switching tabs fades through and keeps every tab's scroll position.
 */
@Composable
internal fun GalleryShell(
    catalog: CatalogSnapshot,
    search: SearchState,
    library: LibraryState,
    rootDestination: RootDestination,
    selectedCategory: String?,
    apiStability: ApiStability,
    onRootSelected: (RootDestination) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onApiStabilitySelected: (ApiStability) -> Unit,
    onBookmarkClick: (String, Boolean) -> Unit,
    onEntryClick: (String) -> Unit,
    onSearch: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val stateHolder = rememberSaveableStateHolder()
    val reducedMotion = LocalReducedMotion.current

    AnimatedContent(
        targetState = rootDestination,
        transitionSpec = {
            if (reducedMotion) {
                EnterTransition.None togetherWith ExitTransition.None
            } else {
                // Material fade through: the outgoing tab leaves quickly, the new one settles in.
                (fadeIn(tween(210, delayMillis = 90)) + scaleIn(tween(210, delayMillis = 90), initialScale = 0.96f)) togetherWith
                    fadeOut(tween(90))
            }
        },
        label = "rootDestination",
    ) { destination ->
        stateHolder.SaveableStateProvider(destination.name) {
            AppScaffold(
                title = stringResource(destination.titleRes()),
                subtitle = if (destination == RootDestination.EXPLORE) stringResource(R.string.app_explore_subtitle) else null,
                actions = {
                    IconButton(onClick = { onSearch("") }, shapes = IconButtonDefaults.shapes()) {
                        Icon(Icons.Rounded.Search, contentDescription = stringResource(R.string.app_cd_search_components))
                    }
                    IconButton(onClick = onSettingsClick, shapes = IconButtonDefaults.shapes()) {
                        Icon(Icons.Rounded.Settings, contentDescription = stringResource(R.string.app_cd_open_settings))
                    }
                },
            ) { padding ->
                val content = padding
                when (destination) {
                    RootDestination.EXPLORE -> ExploreScreen(
                        entries = catalog.entries,
                        bookmarkedIds = library.bookmarks,
                        recentIds = library.recent,
                        onEntryClick = onEntryClick,
                        onCategoryClick = { category ->
                            onCategorySelected(category)
                            onRootSelected(RootDestination.CATALOG)
                        },
                        onAllComponentsClick = {
                            onCategorySelected(null)
                            onRootSelected(RootDestination.CATALOG)
                        },
                        onExpressiveClick = { onSearch("expressive") },
                        onFoundationsClick = { onRootSelected(RootDestination.FOUNDATIONS) },
                        onApisClick = { onRootSelected(RootDestination.APIS) },
                        onBookmarkClick = onBookmarkClick,
                        contentPadding = content,
                    )
                    RootDestination.CATALOG -> CatalogScreen(
                        entries = search.visibleCatalogEntries,
                        selectedCategory = selectedCategory,
                        bookmarkedIds = library.bookmarks,
                        onCategorySelected = onCategorySelected,
                        onEntryClick = onEntryClick,
                        onBookmarkClick = onBookmarkClick,
                        allCategories = catalog.allCategories,
                        contentPadding = content,
                    )
                    RootDestination.APIS -> ApisScreen(
                        entries = catalog.entries,
                        selectedStability = apiStability,
                        onStabilitySelected = onApiStabilitySelected,
                        onEntryClick = onEntryClick,
                        material3Version = catalog.material3Version,
                        composeUiVersion = catalog.composeUiVersion,
                        stableBaseline = catalog.stableBaseline,
                        contentPadding = content,
                    )
                    RootDestination.FOUNDATIONS -> FoundationsScreen(
                        entries = catalog.foundationEntries,
                        onEntryClick = onEntryClick,
                        bookmarkedIds = library.bookmarks,
                        onBookmarkClick = onBookmarkClick,
                        contentPadding = content,
                    )
                }
            }
        }
    }
}
