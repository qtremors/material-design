package dev.qtremors.materialdesign

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.ShortNavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.data.MotionMode
import dev.qtremors.material.core.data.ThemeMode
import dev.qtremors.material.core.designsystem.MaterialDesignTheme
import dev.qtremors.material.core.designsystem.ExpressiveMotion
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.MaterialThemeMode
import dev.qtremors.material.core.designsystem.expressiveSpring
import dev.qtremors.material.feature.apis.ApisScreen
import dev.qtremors.material.feature.catalog.CatalogScreen
import dev.qtremors.material.feature.detail.ComponentDetailScreen
import dev.qtremors.material.feature.detail.DetailSection
import dev.qtremors.material.feature.explore.ExploreScreen
import dev.qtremors.material.feature.foundations.FoundationsScreen
import dev.qtremors.material.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable private object GalleryRoute
@Serializable private data class DetailRoute(val id: String)
@Serializable private object SettingsRoute

@Composable
fun MaterialGalleryApp(viewModel: GalleryViewModel, demoRegistry: MaterialDemoRegistry) {
    val state by viewModel.state.collectAsState()
    val themeMode = when (state.settings.themeMode) {
        ThemeMode.SYSTEM -> MaterialThemeMode.SYSTEM
        ThemeMode.LIGHT -> MaterialThemeMode.LIGHT
        ThemeMode.DARK -> MaterialThemeMode.DARK
    }
    MaterialDesignTheme(
        mode = themeMode,
        dynamicColor = state.settings.dynamicColor,
        reducedMotion = state.settings.motionMode == MotionMode.REDUCED,
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = GalleryRoute) {
            composable<GalleryRoute> {
                GalleryShell(
                    state = state,
                    onRootSelected = viewModel::selectRoot,
                    onQueryChange = viewModel::updateQuery,
                    onCategorySelected = viewModel::selectCategory,
                    onApiStabilitySelected = viewModel::selectApiStability,
                    onBookmarkClick = viewModel::setBookmarked,
                    onEntryClick = { id -> navController.navigate(DetailRoute(id)) },
                    onSettingsClick = { navController.navigate(SettingsRoute) },
                )
            }
            composable<DetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<DetailRoute>()
                val entry = state.entries.firstOrNull { it.id == route.id }
                if (entry == null || !demoRegistry.contains(entry.demoKey)) {
                    navController.popBackStack()
                } else {
                    LaunchedEffect(entry.id) { viewModel.recordRecent(entry.id) }
                    var section by rememberSaveable(entry.id) { mutableStateOf(DetailSection.PREVIEW) }
                    ComponentDetailScreen(
                        entry = entry,
                        selectedSection = section,
                        bookmarked = entry.id in state.bookmarks,
                        onSectionSelected = { section = it },
                        onBookmarkClick = { viewModel.setBookmarked(entry.id, it) },
                        onBack = navController::popBackStack,
                        demo = { demoRegistry.Render(entry.demoKey) },
                    )
                }
            }
            composable<SettingsRoute> {
                SettingsScreen(
                    settings = state.settings,
                    bookmarkCount = state.bookmarks.size,
                    recentCount = state.recent.size,
                    onThemeModeChange = viewModel::updateThemeMode,
                    onDynamicColorChange = viewModel::updateDynamicColor,
                    onMotionModeChange = viewModel::updateMotionMode,
                    onClearBookmarks = viewModel::clearBookmarks,
                    onClearRecent = viewModel::clearRecent,
                    onBack = navController::popBackStack,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GalleryShell(
    state: GalleryUiState,
    onRootSelected: (RootDestination) -> Unit,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onApiStabilitySelected: (ApiStability) -> Unit,
    onBookmarkClick: (String, Boolean) -> Unit,
    onEntryClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    BoxWithConstraints {
        if (maxWidth < 600.dp) {
            Box(Modifier.fillMaxSize()) {
                GalleryScaffold(
                    state = state,
                    onQueryChange = onQueryChange,
                    onCategorySelected = onCategorySelected,
                    onApiStabilitySelected = onApiStabilitySelected,
                    onBookmarkClick = onBookmarkClick,
                    onEntryClick = onEntryClick,
                    onSettingsClick = onSettingsClick,
                    contentBottomPadding = 100.dp,
                )
                CompactFloatingNavigationBar(
                    selected = state.rootDestination,
                    onSelected = onRootSelected,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        } else {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    rootDestinations.forEach { destination ->
                        item(
                            selected = state.rootDestination == destination.destination,
                            onClick = { onRootSelected(destination.destination) },
                            icon = { Icon(destination.icon, contentDescription = destination.label) },
                            label = { Text(destination.label) },
                        )
                    }
                },
            ) {
                GalleryScaffold(
                    state = state,
                    onQueryChange = onQueryChange,
                    onCategorySelected = onCategorySelected,
                    onApiStabilitySelected = onApiStabilitySelected,
                    onBookmarkClick = onBookmarkClick,
                    onEntryClick = onEntryClick,
                    onSettingsClick = onSettingsClick,
                )
            }
        }
    }
}

@Composable
private fun GalleryScaffold(
    state: GalleryUiState,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onApiStabilitySelected: (ApiStability) -> Unit,
    onBookmarkClick: (String, Boolean) -> Unit,
    onEntryClick: (String) -> Unit,
    onSettingsClick: () -> Unit,
    contentBottomPadding: androidx.compose.ui.unit.Dp = 0.dp,
) {
    Scaffold(
        topBar = { GalleryTopBar(state.query, onQueryChange, onSettingsClick) },
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottom = contentBottomPadding),
        ) {
            when (state.rootDestination) {
                RootDestination.EXPLORE -> ExploreScreen(state.entries, state.bookmarks, state.recent, onEntryClick)
                RootDestination.CATALOG -> CatalogScreen(state.visibleCatalogEntries, state.selectedCategory, state.bookmarks, onCategorySelected, onEntryClick, onBookmarkClick)
                RootDestination.APIS -> ApisScreen(state.entries, state.apiStability, onApiStabilitySelected, onEntryClick)
                RootDestination.FOUNDATIONS -> FoundationsScreen(state.foundationEntries, onEntryClick)
                }
        }
    }
}

@Composable
private fun GalleryTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            placeholder = {
                Text(
                    text = "Search Material Design",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            shape = MaterialTheme.shapes.extraLarge,
        )
        IconButton(onClick = onSettingsClick, modifier = Modifier.padding(start = 8.dp)) {
            Icon(Icons.Default.Settings, contentDescription = "App settings")
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CompactFloatingNavigationBar(
    selected: RootDestination,
    onSelected: (RootDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reducedMotion = LocalReducedMotion.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        HorizontalFloatingToolbar(
            expanded = true,
            modifier = (if (reducedMotion) Modifier else Modifier.animateContentSize())
                .height(68.dp),
            shape = RoundedCornerShape(100),
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
            colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
                toolbarContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
        ) {
            rootDestinations.forEach { destination ->
                val isSelected = selected == destination.destination
                ShortNavigationBarItem(
                    selected = isSelected,
                    onClick = { onSelected(destination.destination) },
                    icon = {
                        val extraHeight by animateDpAsState(
                            targetValue = if (isSelected) ExpressiveMotion.SelectedIconLiftDp.dp else 0.dp,
                            animationSpec = expressiveSpring(),
                            label = "${destination.label} indicator height",
                        )
                        Box(
                            modifier = Modifier.height(24.dp + extraHeight),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.label,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    },
                    label = {
                        AnimatedVisibility(
                            visible = isSelected,
                            enter = if (reducedMotion) EnterTransition.None else fadeIn() +
                                slideInHorizontally(
                                    initialOffsetX = { -15 },
                                    animationSpec = expressiveSpring(),
                                ) + expandHorizontally(expandFrom = Alignment.Start),
                            exit = if (reducedMotion) ExitTransition.None else fadeOut() +
                                slideOutHorizontally(targetOffsetX = { -15 }) +
                                shrinkHorizontally(shrinkTowards = Alignment.Start),
                        ) {
                            Text(
                                text = destination.label,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                modifier = Modifier.padding(start = 2.dp, end = 4.dp),
                            )
                        }
                    },
                    iconPosition = NavigationItemIconPosition.Start,
                    colors = ShortNavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .fillMaxHeight(),
                )
            }
        }
    }
}

private data class RootDestinationItem(
    val destination: RootDestination,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private val rootDestinations = listOf(
    RootDestinationItem(RootDestination.EXPLORE, "Explore", Icons.Default.Explore),
    RootDestinationItem(RootDestination.CATALOG, "Catalog", Icons.Default.Apps),
    RootDestinationItem(RootDestination.APIS, "APIs", Icons.Default.Api),
    RootDestinationItem(RootDestination.FOUNDATIONS, "Foundations", Icons.Default.Palette),
)
