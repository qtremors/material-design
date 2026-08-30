package dev.qtremors.materialdesign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.qtremors.material.core.catalog.CatalogRepository
import dev.qtremors.material.core.designsystem.MaterialDesignTheme
import dev.qtremors.material.feature.detail.ComponentDetailScreen
import dev.qtremors.material.feature.detail.DetailSection
import dev.qtremors.material.feature.settings.AboutScreen
import dev.qtremors.material.feature.settings.SettingsScreen
import dev.qtremors.materialdesign.shell.GalleryShell
import kotlinx.serialization.Serializable

@Serializable private object GalleryRoute
@Serializable private data class DetailRoute(val id: String)
@Serializable private object SettingsRoute
@Serializable private object AboutRoute

@Composable
fun MaterialGalleryApp(viewModel: GalleryViewModel, demoRegistry: MaterialDemoRegistry) {
    val catalogLoad by viewModel.catalogLoad.collectAsState()
    val settings by viewModel.settings.collectAsState()

    MaterialDesignTheme(themeState = settings.themeState) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            when (val load = catalogLoad) {
                CatalogLoad.Loading -> CatalogLoadingScreen()
                is CatalogLoad.Failed -> CatalogErrorScreen(onRetry = viewModel::retryCatalogLoad)
                is CatalogLoad.Ready ->
                    GalleryAppContent(
                        viewModel = viewModel,
                        demoRegistry = demoRegistry,
                        repository = load.repository,
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun CatalogLoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularWavyProgressIndicator()
    }
}

@Composable
private fun CatalogErrorScreen(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Default.CloudOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp),
            )
            Text(
                text = stringResource(R.string.app_error_catalog_load_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(R.string.app_error_catalog_load_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Button(onClick = onRetry) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                Text(stringResource(R.string.app_retry))
            }
        }
    }
}

@Composable
private fun GalleryAppContent(
    viewModel: GalleryViewModel,
    demoRegistry: MaterialDemoRegistry,
    repository: CatalogRepository,
) {
    val navController = rememberNavController()

    val catalog by viewModel.catalog.collectAsState()
    val library by viewModel.library.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val searchState by viewModel.search.collectAsState()
    val query by viewModel.query.collectAsState()
    val rootDestination by viewModel.rootDestination.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val apiStability by viewModel.apiStability.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    // Resolved in composable scope so the LaunchedEffect below can emit them later.
    val writeFailedMessage = stringResource(R.string.app_error_library_write_failed)
    val unavailableMessage = stringResource(R.string.app_error_component_unavailable)

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                GalleryEvent.LibraryWriteFailed -> snackbarHostState.showSnackbar(writeFailedMessage)
                GalleryEvent.ComponentUnavailable -> {
                    snackbarHostState.showSnackbar(unavailableMessage)
                    navController.popBackStack()
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = GalleryRoute) {
            composable<GalleryRoute> {
                GalleryShell(
                    catalog = catalog,
                    search = searchState,
                    query = query,
                    library = library,
                    rootDestination = rootDestination,
                    selectedCategory = selectedCategory,
                    apiStability = apiStability,
                    onRootSelected = viewModel::selectRoot,
                    onQueryChange = viewModel::updateQuery,
                    onCategorySelected = viewModel::selectCategory,
                    onApiStabilitySelected = viewModel::selectApiStability,
                    onBookmarkClick = viewModel::setBookmarked,
                    onEntryClick = { id ->
                        // Recorded once per actual navigation action, not from composition.
                        viewModel.recordRecent(id)
                        navController.navigate(DetailRoute(id))
                    },
                    onSettingsClick = { navController.navigate(SettingsRoute) },
                )
            }
            composable<DetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<DetailRoute>()
                val entry = remember(route.id) { repository.entry(route.id) }
                if (entry == null || !demoRegistry.contains(entry.demoKey)) {
                    LaunchedEffect(route.id) { viewModel.notifyComponentUnavailable() }
                    Box(Modifier.fillMaxSize())
                } else {
                    var section by rememberSaveable(entry.id) { mutableStateOf(DetailSection.PREVIEW) }
                    ComponentDetailScreen(
                        entry = entry,
                        selectedSection = section,
                        bookmarked = entry.id in library.bookmarks,
                        onSectionSelected = { section = it },
                        onBookmarkClick = { viewModel.setBookmarked(entry.id, it) },
                        onBack = navController::popBackStack,
                        demo = { demoRegistry.Render(entry.demoKey) },
                    )
                }
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
                    onBack = navController::popBackStack,
                )
            }
            composable<AboutRoute> {
                AboutScreen(
                    appVersion = BuildConfig.APP_VERSION_NAME,
                    material3Version = catalog.material3Version,
                    composeUiVersion = catalog.composeUiVersion,
                    onBack = navController::popBackStack,
                )
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}
