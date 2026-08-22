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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Close
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
import dev.qtremors.material.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable private object GalleryRoute
@Serializable private data class DetailRoute(val id: String)
@Serializable private object SettingsRoute

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
                    ArcileTopBar(
                        title = title,
                        scrollBehavior = scrollBehavior,
                        onSearchClick = { showSearchBar = true },
                        onSettingsClick = onSettingsClick,
                    )
                }
            },
        ) { innerPadding ->
            Box(Modifier.fillMaxSize()) {
                GalleryContent(
                    state = state,
                    onEntryClick = onEntryClick,
                    onCategorySelected = onCategorySelected,
                    onRootSelected = onRootSelected,
                    onApiStabilitySelected = onApiStabilitySelected,
                    onBookmarkClick = onBookmarkClick,
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = compactNavHeightDp.dp + CompactNavContentSpacing.dp,
                    ),
                )
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
                        ArcileTopBar(
                            title = title,
                            scrollBehavior = scrollBehavior,
                            onSearchClick = { showSearchBar = true },
                            onSettingsClick = onSettingsClick,
                        )
                    }
                },
            ) { innerPadding ->
                Box(Modifier.fillMaxSize()) {
                    GalleryContent(
                        state = state,
                        onEntryClick = onEntryClick,
                        onCategorySelected = onCategorySelected,
                        onRootSelected = onRootSelected,
                        onApiStabilitySelected = onApiStabilitySelected,
                        onBookmarkClick = onBookmarkClick,
                        contentPadding = innerPadding,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArcileTopBar(
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
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                shadowElevation = 2.dp,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { onSearchClick() },
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                shadowElevation = 2.dp,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onSettingsClick),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "App settings",
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            shadowElevation = 3.dp,
            tonalElevation = 2.dp,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable { onClose() },
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Close search",
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Spacer(Modifier.width(8.dp))
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
                    placeholder = "Search Material Design",
                    textColor = MaterialTheme.colorScheme.onSurface,
                    placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    onSearch = onSearch,
                    modifier = Modifier.weight(1f),
                )
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
                contentPadding = contentPadding,
            )
            RootDestination.CATALOG -> CatalogScreen(
                entries = state.visibleCatalogEntries,
                selectedCategory = state.selectedCategory,
                bookmarkedIds = state.bookmarks,
                onCategorySelected = onCategorySelected,
                onEntryClick = onEntryClick,
                onBookmarkClick = onBookmarkClick,
                contentPadding = contentPadding,
            )
            RootDestination.APIS -> ApisScreen(
                entries = state.entries,
                selectedStability = state.apiStability,
                onStabilitySelected = onApiStabilitySelected,
                onEntryClick = onEntryClick,
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
