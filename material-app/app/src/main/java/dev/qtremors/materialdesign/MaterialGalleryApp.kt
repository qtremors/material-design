package dev.qtremors.materialdesign

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextAlign
import dev.qtremors.material.core.catalog.CatalogKind
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationItemIconPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.ShortNavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.window.core.layout.WindowWidthSizeClass
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.designsystem.ExpressiveMotion
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.MaterialDesignTheme
import dev.qtremors.material.core.designsystem.expressiveSpring
import dev.qtremors.material.feature.apis.ApisScreen
import dev.qtremors.material.feature.catalog.CatalogScreen
import dev.qtremors.material.feature.detail.ComponentDetailScreen
import dev.qtremors.material.feature.detail.DetailSection
import dev.qtremors.material.feature.explore.ExploreScreen
import dev.qtremors.material.feature.foundations.FoundationsScreen
import dev.qtremors.material.feature.settings.AboutScreen
import dev.qtremors.material.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable private object GalleryRoute
@Serializable private data class DetailRoute(val id: String)
@Serializable private object SettingsRoute
@Serializable private object AboutRoute

@Composable
fun MaterialGalleryApp(viewModel: GalleryViewModel, demoRegistry: MaterialDemoRegistry) {
    val state by viewModel.state.collectAsState()
    MaterialDesignTheme(
        themeState = state.settings.themeState,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
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
                        onThemeStateChange = viewModel::updateThemeState,
                        onClearBookmarks = viewModel::clearBookmarks,
                        onClearRecent = viewModel::clearRecent,
                        onNavigateToAbout = { navController.navigate(AboutRoute) },
                        onBack = navController::popBackStack,
                    )
                }
                composable<AboutRoute> {
                    AboutScreen(
                        onBack = navController::popBackStack,
                    )
                }
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
    val density = LocalDensity.current
    val windowWidthSizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val isCompact = windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    var compactNavHeightDp by remember { mutableFloatStateOf(DefaultCompactNavContentPadding) }
    var showSearchBar by rememberSaveable { mutableStateOf(false) }

    val isSearchActive = showSearchBar || state.query.isNotEmpty()

    BackHandler(enabled = isSearchActive) {
        showSearchBar = false
        if (state.query.isNotEmpty()) {
            onQueryChange("")
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val title = when (state.rootDestination) {
        RootDestination.EXPLORE -> "Material Design"
        RootDestination.CATALOG -> "Catalog"
        RootDestination.APIS -> "APIs"
        RootDestination.FOUNDATIONS -> "Foundations"
    }

    if (isCompact) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = Color.Transparent,
            topBar = {
                if (isSearchActive) {
                    SearchTopBar(
                        query = state.query,
                        onQueryChange = onQueryChange,
                        onClose = {
                            showSearchBar = false
                            if (state.query.isNotEmpty()) {
                                onQueryChange("")
                            }
                        },
                        onSearch = {
                            showSearchBar = false
                        },
                    )
                } else {
                    GalleryTopBar(
                        title = title,
                        scrollBehavior = scrollBehavior,
                        onSearchClick = { showSearchBar = true },
                        onSettingsClick = onSettingsClick,
                    )
                }
            },
        ) { innerPadding ->
            Box(Modifier.fillMaxSize()) {
                if (state.query.isNotBlank()) {
                    InstantSearchResultsOverlay(
                        query = state.query,
                        results = state.queryResults,
                        onEntryClick = { id ->
                            showSearchBar = false
                            onQueryChange("")
                            onEntryClick(id)
                        },
                        contentPadding = PaddingValues(
                            top = innerPadding.calculateTopPadding(),
                            bottom = compactNavHeightDp.dp + CompactNavContentSpacing.dp,
                        ),
                    )
                } else {
                    GalleryContent(
                        state = state,
                        onEntryClick = onEntryClick,
                        onCategorySelected = onCategorySelected,
                        onRootSelected = onRootSelected,
                        onApiStabilitySelected = onApiStabilitySelected,
                        onBookmarkClick = onBookmarkClick,
                        onQueryChange = onQueryChange,
                        contentPadding = PaddingValues(
                            top = innerPadding.calculateTopPadding(),
                            bottom = compactNavHeightDp.dp + CompactNavContentSpacing.dp,
                        ),
                    )
                }
                CompactFloatingNavigationBar(
                    selected = state.rootDestination,
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
                        selected = state.rootDestination == destination.destination,
                        onClick = { onRootSelected(destination.destination) },
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                    )
                }
            },
        ) {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                containerColor = Color.Transparent,
                topBar = {
                    if (isSearchActive) {
                        SearchTopBar(
                            query = state.query,
                            onQueryChange = onQueryChange,
                            onClose = {
                                showSearchBar = false
                                if (state.query.isNotEmpty()) {
                                    onQueryChange("")
                                }
                            },
                            onSearch = {
                                showSearchBar = false
                            },
                        )
                    } else {
                        GalleryTopBar(
                            title = title,
                            scrollBehavior = scrollBehavior,
                            onSearchClick = { showSearchBar = true },
                            onSettingsClick = onSettingsClick,
                        )
                    }
                },
            ) { innerPadding ->
                Box(Modifier.fillMaxSize()) {
                    if (state.query.isNotBlank()) {
                        InstantSearchResultsOverlay(
                            query = state.query,
                            results = state.queryResults,
                            onEntryClick = { id ->
                                showSearchBar = false
                                onQueryChange("")
                                onEntryClick(id)
                            },
                            contentPadding = innerPadding,
                        )
                    } else {
                        GalleryContent(
                            state = state,
                            onEntryClick = onEntryClick,
                            onCategorySelected = onCategorySelected,
                            onRootSelected = onRootSelected,
                            onApiStabilitySelected = onApiStabilitySelected,
                            onBookmarkClick = onBookmarkClick,
                            onQueryChange = onQueryChange,
                            contentPadding = innerPadding,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryTopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LargeTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
            )
        },
        actions = {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                modifier = Modifier.size(40.dp),
            ) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search components",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                modifier = Modifier.size(40.dp),
            ) {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Open settings",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.95f),
        ),
        modifier = modifier,
    )
}

@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = 3.dp,
            tonalElevation = 2.dp,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(16.dp))
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(8.dp))
                SearchPillTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = "Search components, APIs, foundations...",
                    textColor = MaterialTheme.colorScheme.onSurface,
                    placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    onSearch = onSearch,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            shadowElevation = 2.dp,
            modifier = Modifier.size(48.dp),
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close search",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun InstantSearchResultsOverlay(
    query: String,
    results: List<CatalogEntry>,
    onEntryClick: (String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(48.dp),
                    )
                    Text(
                        text = "No results found for \"$query\"",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "Try searching for components (Buttons, Dialogs), foundations (Shapes, Colors), or APIs (rememberDatePickerState).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = contentPadding.calculateTopPadding() + 8.dp,
                    bottom = contentPadding.calculateBottomPadding() + 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    Text(
                        text = "${results.size} results for \"$query\"",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    )
                }
                items(results, key = { it.id }) { entry ->
                    val matchingApi = remember(entry, query) {
                        entry.apiReferences.firstOrNull { it.symbol.contains(query, ignoreCase = true) }
                    }
                    val isFoundation = entry.kind == CatalogKind.FOUNDATION
                    Card(
                        onClick = { onEntryClick(entry.id) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isFoundation) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(44.dp),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isFoundation) Icons.Default.Category else Icons.Default.Widgets,
                                        contentDescription = null,
                                        tint = if (isFoundation) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Text(
                                        text = entry.officialName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                    ) {
                                        Text(
                                            text = if (isFoundation) "Foundation" else entry.category,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        )
                                    }
                                }
                                if (matchingApi != null) {
                                    Text(
                                        text = "API: ${matchingApi.symbol.substringAfterLast('.')}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium,
                                    )
                                } else {
                                    Text(
                                        text = entry.summary,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GalleryContent(
    state: GalleryUiState,
    onEntryClick: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onRootSelected: (RootDestination) -> Unit,
    onApiStabilitySelected: (ApiStability) -> Unit,
    onBookmarkClick: (String, Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    contentPadding: PaddingValues,
) {
    Box(Modifier.fillMaxSize()) {
        when (state.rootDestination) {
            RootDestination.EXPLORE -> ExploreScreen(
                entries = state.entries,
                bookmarkedIds = state.bookmarks,
                recentIds = state.recent,
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
                onFoundationsClick = {
                    onRootSelected(RootDestination.FOUNDATIONS)
                },
                contentPadding = contentPadding,
            )
            RootDestination.CATALOG -> CatalogScreen(
                entries = state.visibleCatalogEntries,
                selectedCategory = state.selectedCategory,
                bookmarkedIds = state.bookmarks,
                onCategorySelected = onCategorySelected,
                onEntryClick = onEntryClick,
                onBookmarkClick = onBookmarkClick,
                allCategories = state.allCategories,
                contentPadding = contentPadding,
            )
            RootDestination.APIS -> ApisScreen(
                entries = state.entries,
                selectedStability = state.apiStability,
                onStabilitySelected = onApiStabilitySelected,
                onEntryClick = onEntryClick,
                query = state.query,
                contentPadding = contentPadding,
            )
            RootDestination.FOUNDATIONS -> FoundationsScreen(
                entries = state.foundationEntries,
                onEntryClick = onEntryClick,
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
private fun SearchPillTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    textColor: Color,
    placeholderColor: Color,
    cursorColor: Color,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = textColor),
        singleLine = true,
        cursorBrush = SolidColor(cursorColor),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch()
            },
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = placeholderColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    innerTextField()
                }
                if (value.isNotEmpty()) {
                    IconButton(
                        onClick = { onValueChange("") },
                        modifier = Modifier.size(36.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search",
                            tint = placeholderColor,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CompactFloatingNavigationBar(
    selected: RootDestination,
    onSelected: (RootDestination) -> Unit,
    onToolbarHeightChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val reducedMotion = LocalReducedMotion.current

    HorizontalFloatingToolbar(
        expanded = true,
        colors = FloatingToolbarDefaults.standardFloatingToolbarColors(
            toolbarContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            toolbarContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = modifier
            .onSizeChanged { size -> onToolbarHeightChanged(size.height.toFloat()) }
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            rootDestinations.forEach { item ->
                CompactExpressiveDestinationPill(
                    item = item,
                    selected = selected == item.destination,
                    reducedMotion = reducedMotion,
                    onClick = { onSelected(item.destination) },
                )
            }
        }
    }
}

@Composable
private fun CompactExpressiveDestinationPill(
    item: RootDestinationItem,
    selected: Boolean,
    reducedMotion: Boolean,
    onClick: () -> Unit,
) {
    val targetRadius = if (selected) 28.dp else 12.dp
    val animatedCornerRadius by animateDpAsState(
        targetValue = targetRadius,
        animationSpec = expressiveSpring(),
        label = "navPillCornerRadius",
    )

    Surface(
        selected = selected,
        onClick = onClick,
        shape = RoundedCornerShape(animatedCornerRadius),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.height(48.dp),
    ) {
        Row(
            modifier = Modifier
                .animateContentSize(animationSpec = expressiveSpring())
                .padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
            )
            AnimatedVisibility(
                visible = selected,
                enter = if (reducedMotion) EnterTransition.None else fadeIn(expressiveSpring()) + expandHorizontally(expressiveSpring()),
                exit = if (reducedMotion) ExitTransition.None else fadeOut(expressiveSpring()) + shrinkHorizontally(expressiveSpring()),
            ) {
                Row {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

private val rootDestinations = listOf(
    RootDestinationItem(RootDestination.EXPLORE, "Explore", Icons.Default.Explore),
    RootDestinationItem(RootDestination.CATALOG, "Catalog", Icons.Default.Apps),
    RootDestinationItem(RootDestination.APIS, "APIs", Icons.Default.Api),
    RootDestinationItem(RootDestination.FOUNDATIONS, "Foundations", Icons.Default.Palette),
)

private data class RootDestinationItem(
    val destination: RootDestination,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
)

private const val DefaultCompactNavContentPadding = 80f
private const val CompactNavContentSpacing = 16
