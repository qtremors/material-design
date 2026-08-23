package dev.qtremors.materialdesign.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.materialdesign.CatalogSnapshot
import dev.qtremors.materialdesign.LibraryState
import dev.qtremors.materialdesign.RootDestination
import dev.qtremors.materialdesign.SearchState
import dev.qtremors.material.feature.apis.ApisScreen
import dev.qtremors.material.feature.catalog.CatalogScreen
import dev.qtremors.material.feature.explore.ExploreScreen
import dev.qtremors.material.feature.foundations.FoundationsScreen

@Composable
internal fun GalleryShellContent(
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
    onQueryChange: (String) -> Unit,
    onResultSelected: () -> Unit,
    contentPadding: PaddingValues,
) {
    Box(Modifier.fillMaxSize()) {
        if (search.query.isNotBlank()) {
            InstantSearchResultsOverlay(
                query = search.query,
                results = search.results,
                onEntryClick = { id ->
                    onQueryChange("")
                    onResultSelected()
                    onEntryClick(id)
                },
                contentPadding = contentPadding,
            )
        } else {
            when (rootDestination) {
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
                    onExpressiveClick = {
                        onQueryChange("expressive")
                        onRootSelected(RootDestination.CATALOG)
                    },
                    onFoundationsClick = { onRootSelected(RootDestination.FOUNDATIONS) },
                    contentPadding = contentPadding,
                )
                RootDestination.CATALOG -> CatalogScreen(
                    entries = search.visibleCatalogEntries,
                    selectedCategory = selectedCategory,
                    bookmarkedIds = library.bookmarks,
                    onCategorySelected = onCategorySelected,
                    onEntryClick = onEntryClick,
                    onBookmarkClick = onBookmarkClick,
                    allCategories = catalog.allCategories,
                    contentPadding = contentPadding,
                )
                RootDestination.APIS -> ApisScreen(
                    entries = catalog.entries,
                    selectedStability = apiStability,
                    onStabilitySelected = onApiStabilitySelected,
                    onEntryClick = onEntryClick,
                    query = search.query,
                    material3Version = catalog.material3Version,
                    composeUiVersion = catalog.composeUiVersion,
                    stableBaseline = catalog.stableBaseline,
                    contentPadding = contentPadding,
                )
                RootDestination.FOUNDATIONS -> FoundationsScreen(
                    entries = catalog.foundationEntries,
                    onEntryClick = onEntryClick,
                    contentPadding = contentPadding,
                )
            }
        }
    }
}
