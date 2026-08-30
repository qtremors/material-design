package dev.qtremors.materialdesign.shell

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.materialdesign.CatalogSnapshot
import dev.qtremors.materialdesign.LibraryState
import dev.qtremors.materialdesign.R
import dev.qtremors.materialdesign.RootDestination
import dev.qtremors.materialdesign.SearchState

private const val DefaultCompactNavContentPadding = 80f
private const val CompactNavContentSpacing = 16

@StringRes
internal fun shellTitle(rootDestination: RootDestination): Int = when (rootDestination) {
    RootDestination.EXPLORE -> R.string.app_title_material_design
    RootDestination.CATALOG -> R.string.app_destination_catalog
    RootDestination.APIS -> R.string.app_destination_apis
    RootDestination.FOUNDATIONS -> R.string.app_destination_foundations
}

/**
 * Single adaptive gallery shell shared by compact and expanded window sizes.
 * The only layout difference is the navigation slot and the bottom content inset.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GalleryShell(
    catalog: CatalogSnapshot,
    search: SearchState,
    query: String,
    library: LibraryState,
    rootDestination: RootDestination,
    selectedCategory: String?,
    apiStability: ApiStability,
    onRootSelected: (RootDestination) -> Unit,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onApiStabilitySelected: (ApiStability) -> Unit,
    onBookmarkClick: (String, Boolean) -> Unit,
    onEntryClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    val density = LocalDensity.current
    val isCompact =
        !currentWindowAdaptiveInfoV2().windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    var compactNavHeightDp by remember { mutableFloatStateOf(DefaultCompactNavContentPadding) }
    val isSearchActive = showSearchBar || query.isNotEmpty()

    BackHandler(enabled = isSearchActive) {
        showSearchBar = false
        if (query.isNotEmpty()) onQueryChange("")
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    fun closeSearch() {
        showSearchBar = false
        if (query.isNotEmpty()) onQueryChange("")
    }

    val topBar: @Composable () -> Unit = {
        if (isSearchActive) {
            SearchTopBar(
                query = query,
                onQueryChange = onQueryChange,
                onClose = ::closeSearch,
                onSearch = { showSearchBar = false },
            )
        } else {
            GalleryTopBar(
                title = stringResource(shellTitle(rootDestination)),
                scrollBehavior = scrollBehavior,
                onSearchClick = { showSearchBar = true },
                onSettingsClick = onSettingsClick,
            )
        }
    }

    if (isCompact) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
            topBar = topBar,
        ) { innerPadding ->
            Box(Modifier.fillMaxSize()) {
                GalleryShellContent(
                    catalog = catalog,
                    search = search,
                    query = query,
                    library = library,
                    rootDestination = rootDestination,
                    selectedCategory = selectedCategory,
                    apiStability = apiStability,
                    onRootSelected = onRootSelected,
                    onCategorySelected = onCategorySelected,
                    onApiStabilitySelected = onApiStabilitySelected,
                    onBookmarkClick = onBookmarkClick,
                    onEntryClick = onEntryClick,
                    onQueryChange = onQueryChange,
                    onResultSelected = ::closeSearch,
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = compactNavHeightDp.dp + CompactNavContentSpacing.dp,
                    ),
                )
                CompactFloatingNavigationBar(
                    selected = rootDestination,
                    onSelected = onRootSelected,
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onToolbarHeightChanged = { heightPx ->
                        compactNavHeightDp = with(density) { heightPx.toDp().value }
                    },
                )
            }
        }
    } else {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                rootDestinations.forEach { destination ->
                    item(
                        selected = rootDestination == destination.destination,
                        onClick = { onRootSelected(destination.destination) },
                        icon = { Icon(destination.icon, contentDescription = stringResource(destination.labelRes)) },
                        label = { Text(stringResource(destination.labelRes)) },
                    )
                }
            },
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                containerColor = Color.Transparent,
                topBar = topBar,
            ) { innerPadding ->
                Box(Modifier.fillMaxSize()) {
                    GalleryShellContent(
                        catalog = catalog,
                        search = search,
                        query = query,
                        library = library,
                        rootDestination = rootDestination,
                        selectedCategory = selectedCategory,
                        apiStability = apiStability,
                        onRootSelected = onRootSelected,
                        onCategorySelected = onCategorySelected,
                        onApiStabilitySelected = onApiStabilitySelected,
                        onBookmarkClick = onBookmarkClick,
                        onEntryClick = onEntryClick,
                        onQueryChange = onQueryChange,
                        onResultSelected = ::closeSearch,
                        contentPadding = innerPadding,
                    )
                }
            }
        }
    }
}
