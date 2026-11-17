package dev.qtremors.materialdesign

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.qtremors.material.core.catalog.CatalogRepository
import dev.qtremors.material.core.designsystem.MaterialDesignTheme
import dev.qtremors.material.core.designsystem.components.EmptyState
import dev.qtremors.material.core.designsystem.components.FloatingTabBar
import dev.qtremors.material.core.designsystem.components.LoadingState
import dev.qtremors.material.core.designsystem.components.LocalNavAnimatedScope
import dev.qtremors.material.core.designsystem.components.LocalSharedTransitionScope
import dev.qtremors.material.core.designsystem.components.TabItem
import dev.qtremors.material.core.designsystem.spacing
import dev.qtremors.material.feature.detail.ComponentDetailScreen
import dev.qtremors.material.feature.detail.DetailSection
import dev.qtremors.material.feature.settings.AboutScreen
import dev.qtremors.material.feature.settings.SettingsScreen
import dev.qtremors.materialdesign.shell.GalleryRail
import dev.qtremors.materialdesign.shell.GalleryShell
import dev.qtremors.materialdesign.shell.RootTabs
import dev.qtremors.materialdesign.shell.SearchScreen
import androidx.window.core.layout.WindowSizeClass
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable private object GalleryRoute
@Serializable private data class DetailRoute(val id: String)
@Serializable private object SearchRoute
@Serializable private object SettingsRoute
@Serializable private object AboutRoute

@Composable
fun MaterialGalleryApp(viewModel: GalleryViewModel, demoRegistry: MaterialDemoRegistry) {
    val catalogLoad by viewModel.catalogLoad.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    MaterialDesignTheme(themeState = settings.themeState) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
            when (val load = catalogLoad) {
                CatalogLoad.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { LoadingState() }
                is CatalogLoad.Failed -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyState(
                        icon = Icons.Rounded.CloudOff,
                        title = stringResource(R.string.app_error_catalog_load_title),
                        message = stringResource(R.string.app_error_catalog_load_body),
                        actionLabel = stringResource(R.string.app_retry),
                        onAction = viewModel::retryCatalogLoad,
                    )
                }
                is CatalogLoad.Ready -> GalleryAppContent(viewModel, demoRegistry, load.repository)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun GalleryAppContent(
    viewModel: GalleryViewModel,
    demoRegistry: MaterialDemoRegistry,
    repository: CatalogRepository,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val catalog by viewModel.catalog.collectAsStateWithLifecycle()
    val library by viewModel.library.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val searchState by viewModel.search.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val rootDestination by viewModel.rootDestination.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val apiStability by viewModel.apiStability.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val writeFailedMessage = stringResource(R.string.app_error_library_write_failed)
    val unavailableMessage = stringResource(R.string.app_error_component_unavailable)
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                GalleryEvent.LibraryWriteFailed -> snackbarHostState.showSnackbar(writeFailedMessage)
                GalleryEvent.ComponentUnavailable -> {
                    navController.popBackStack()
                    snackbarHostState.showSnackbar(unavailableMessage)
                }
            }
        }
    }

    // Expanded windows (tablets, unfolded foldables, desktop) keep the list beside the entry.
    val wide = currentWindowAdaptiveInfoV2().windowSizeClass
        .isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    val paneNavigator = rememberListDetailPaneScaffoldNavigator<String>()
    val scope = rememberCoroutineScope()

    val openEntry: (String) -> Unit = { id ->
        // Recorded once per navigation, not from composition.
        viewModel.recordRecent(id)
        if (wide) {
            if (navController.currentDestination?.hasRoute<GalleryRoute>() != true) {
                navController.popBackStack(GalleryRoute, inclusive = false)
            }
            scope.launch { paneNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id) }
        } else {
            navController.navigate(DetailRoute(id))
        }
    }

    val entryDetail: @Composable (String) -> Unit = { id ->
        val catalogEntry = remember(id) { repository.entry(id) }
        if (catalogEntry == null || !demoRegistry.contains(catalogEntry.demoKey)) {
            LaunchedEffect(id) { viewModel.notifyComponentUnavailable() }
        } else {
            var section by rememberSaveable(catalogEntry.id) { mutableStateOf(DetailSection.PREVIEW) }
            ComponentDetailScreen(
                entry = catalogEntry,
                selectedSection = section,
                bookmarked = catalogEntry.id in library.bookmarks,
                onSectionSelected = { section = it },
                onBookmarkClick = { viewModel.setBookmarked(catalogEntry.id, it) },
                demo = { demoRegistry.Render(catalogEntry.demoKey) },
            )
        }
    }

    // Shared spatial and effects specs from the Expressive motion scheme. With Reduce motion on, the
    // scheme snaps, so screens change at once while keeping the same structure.
    val spatial = MaterialTheme.motionScheme.defaultSpatialSpec<IntOffset>()
    val effects = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(spatial) { it / 5 } + fadeIn(effects)
    }
    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(spatial) { -it / 10 } + fadeOut(effects)
    }
    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(spatial) { -it / 10 } + fadeIn(effects)
    }
    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(spatial) { it / 5 } + fadeOut(effects)
    }

    val destination = backStackEntry?.destination
    val onTabs = destination == null || destination.hasRoute<GalleryRoute>()
    val onSearch = destination?.hasRoute<SearchRoute>() == true
    val settingsTitle = stringResource(R.string.app_destination_settings)
    val aboutTitle = stringResource(R.string.app_destination_about)
    val backTitle = when {
        destination == null -> null
        destination.hasRoute<DetailRoute>() -> repository.entry(backStackEntry!!.toRoute<DetailRoute>().id)?.officialName
        destination.hasRoute<SettingsRoute>() -> settingsTitle
        destination.hasRoute<AboutRoute>() -> aboutTitle
        else -> null
    }

    // On expanded windows the wide navigation rail replaces the floating tab bar at the top level.
    val selectRoot: (RootDestination) -> Unit = { destination ->
        if (!onTabs) navController.popBackStack(GalleryRoute, inclusive = false)
        viewModel.selectRoot(destination)
    }

    Row(Modifier.fillMaxSize()) {
        if (wide) {
            GalleryRail(
                selected = rootDestination,
                onSelect = selectRoot,
                onSearch = {
                    viewModel.updateQuery("")
                    navController.navigate(SearchRoute)
                },
            )
        }
        Box(Modifier.weight(1f).fillMaxHeight()) {
            SharedTransitionLayout {
                CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                    NavHost(
                        navController = navController,
                        startDestination = GalleryRoute,
                        enterTransition = enter,
                        exitTransition = exit,
                        popEnterTransition = popEnter,
                        popExitTransition = popExit,
                    ) {
                        composable<GalleryRoute> {
                            ProvideNavScope(this) {
                                val shell: @Composable () -> Unit = {
                                    GalleryShell(
                                        catalog = catalog,
                                        search = searchState,
                                        library = library,
                                        rootDestination = rootDestination,
                                        selectedCategory = selectedCategory,
                                        apiStability = apiStability,
                                        onRootSelected = viewModel::selectRoot,
                                        onCategorySelected = viewModel::selectCategory,
                                        onApiStabilitySelected = viewModel::selectApiStability,
                                        onBookmarkClick = viewModel::setBookmarked,
                                        onEntryClick = openEntry,
                                        onSearch = { initial ->
                                            viewModel.updateQuery(initial)
                                            navController.navigate(SearchRoute)
                                        },
                                        onSettingsClick = { navController.navigate(SettingsRoute) },
                                    )
                                }
                                if (wide) {
                                    TwoPaneGallery(navigator = paneNavigator, list = shell, detail = entryDetail)
                                } else {
                                    shell()
                                }
                            }
                        }
                        composable<SearchRoute> {
                            ProvideNavScope(this) {
                                // Search is its own page, so leaving it clears the query instead of filtering other tabs.
                                DisposableEffect(Unit) { onDispose { viewModel.updateQuery("") } }
                                val recent = remember(library.recent, catalog.entries) {
                                    val byId = catalog.entries.associateBy { it.id }
                                    library.recent.mapNotNull(byId::get).take(6)
                                }
                                SearchScreen(
                                    query = query,
                                    results = searchState.results,
                                    recent = recent,
                                    bookmarkedIds = library.bookmarks,
                                    onQueryChange = viewModel::updateQuery,
                                    onEntryClick = openEntry,
                                    onBookmarkClick = viewModel::setBookmarked,
                                    onBack = navController::popBackStack,
                                )
                            }
                        }
                        composable<DetailRoute> { entry ->
                            ProvideNavScope(this) { entryDetail(entry.toRoute<DetailRoute>().id) }
                        }
                        composable<SettingsRoute> {
                            SettingsScreen(
                                settings = settings,
                                appVersion = BuildConfig.APP_VERSION_NAME,
                                material3Version = catalog.material3Version,
                                bookmarkCount = library.bookmarks.size,
                                recentCount = library.recent.size,
                                onThemeStateChange = viewModel::updateThemeState,
                                onClearBookmarks = viewModel::clearBookmarks,
                                onClearRecent = viewModel::clearRecent,
                                onNavigateToAbout = { navController.navigate(AboutRoute) },
                            )
                        }
                        composable<AboutRoute> {
                            AboutScreen(
                                appVersion = BuildConfig.APP_VERSION_NAME,
                                material3Version = catalog.material3Version,
                                composeUiVersion = catalog.composeUiVersion,
                            )
                        }
                    }
                }
            }

            // One control at the bottom of every page: tabs at the top level, Back and the page title elsewhere.
            // Called by its full name: inside the Row, the RowScope overload would otherwise apply.
            androidx.compose.animation.AnimatedVisibility(
                visible = !onSearch && !(wide && onTabs),
                enter = slideInVertically(spatial) { it } + fadeIn(effects),
                exit = slideOutVertically(spatial) { it } + fadeOut(effects),
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                FloatingTabBar(
                    tabs = RootTabs.map { TabItem(it.icon, stringResource(it.labelRes)) },
                    selectedIndex = RootTabs.indexOfFirst { it.destination == rootDestination },
                    onSelect = { index -> viewModel.selectRoot(RootTabs[index].destination) },
                    backTitle = if (onTabs) null else backTitle,
                    onBack = if (onTabs) null else ({ navController.popBackStack() }),
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = MaterialTheme.spacing.toolbarClearance),
            )
        }
    }
}

/** Gives shared elements inside a destination the scope of that destination's transition. */
@Composable
private fun ProvideNavScope(scope: AnimatedVisibilityScope, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalNavAnimatedScope provides scope, content = content)
}

/**
 * The official list-detail scaffold: the destination's list beside the open entry on expanded
 * windows, with Material's pane motion and predictive back between entries.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun TwoPaneGallery(
    navigator: ThreePaneScaffoldNavigator<String>,
    list: @Composable () -> Unit,
    detail: @Composable (String) -> Unit,
) {
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = { AnimatedPane { list() } },
        detailPane = {
            AnimatedPane {
                // Panes animate on their own; shared elements stay with full-page navigation.
                CompositionLocalProvider(LocalNavAnimatedScope provides null) {
                    val id = navigator.currentDestination?.contentKey
                    if (id != null) {
                        detail(id)
                    } else {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            EmptyState(
                                icon = Icons.Rounded.TouchApp,
                                title = stringResource(R.string.app_pane_empty_title),
                                message = stringResource(R.string.app_pane_empty_body),
                            )
                        }
                    }
                }
            }
        },
    )
}
