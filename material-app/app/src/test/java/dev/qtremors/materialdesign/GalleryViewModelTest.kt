package dev.qtremors.materialdesign

import dev.qtremors.material.core.catalog.ApiReference
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogDocument
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.catalog.CatalogGuidance
import dev.qtremors.material.core.catalog.CatalogKind
import dev.qtremors.material.core.catalog.CatalogRepository
import dev.qtremors.material.core.catalog.ImplementationKind
import dev.qtremors.material.core.catalog.SearchResult
import dev.qtremors.material.core.data.AppSettings
import dev.qtremors.material.core.data.UserLibraryRepository
import dev.qtremors.material.core.designsystem.ThemeMode
import dev.qtremors.material.core.designsystem.ThemeState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun entry(
        id: String,
        category: String = "Actions",
        kind: CatalogKind = CatalogKind.COMPONENT,
    ) = CatalogEntry(
        id = id,
        officialName = id.replace('-', ' ').replaceFirstChar(Char::uppercase),
        aliases = listOf(id),
        kind = kind,
        category = category,
        summary = "Summary for $id",
        guidance = guidance(id),
        collections = emptyList(),
        demoKey = "demo.$id",
        sourceLocations = listOf(dev.qtremors.material.core.catalog.SourceLocation("path/$id.kt", listOf(id))),
        apiReferences = listOf(ApiReference("$id.Symbol", "artifact", "1.0.0", dev.qtremors.material.core.catalog.ApiAvailability.STABLE_ARTIFACT, ApiStability.STABLE, null, "https://example.com")),
        officialUrls = listOf("https://example.com"),
        implementation = ImplementationKind.OFFICIAL_API,
        addedIn = "1.0.0",
        reviewedOn = "2026-01-01",
    )

    private fun guidance(id: String) = CatalogGuidance(
        purpose = "Purpose of $id",
        useWhen = listOf("Use $id"),
        avoidWhen = listOf("Avoid $id"),
        behavior = listOf("Behaves like $id"),
        accessibility = listOf("Accessible $id"),
        adaptive = listOf("Adapts $id"),
    )

    private class FakeCatalogRepository(
        catalogEntries: List<CatalogEntry>,
        retiredIds: Map<String, String> = emptyMap(),
    ) : CatalogRepository {
        override val document = CatalogDocument(1, "m3", "ui", "baseline", "2026-01-01", catalogEntries, retiredIds)
        override val entries: List<CatalogEntry> = catalogEntries
        override fun entry(id: String): CatalogEntry? = entries.firstOrNull { it.id == id }
        override fun search(query: String): List<SearchResult> =
            entries.mapNotNull { item ->
                if (item.officialName.contains(query, ignoreCase = true)) SearchResult(item, 100) else null
            }
    }

    private class FakeUserLibraryRepository : UserLibraryRepository {
        val bookmarkFlow = MutableStateFlow(emptySet<String>())
        val recentFlow = MutableStateFlow(emptyList<String>())
        val settingsFlow = MutableStateFlow(AppSettings())
        var failNextWrite = false

        private fun failIfRequested() {
            if (failNextWrite) {
                failNextWrite = false
                throw java.io.IOException("Injected write failure")
            }
        }

        override val bookmarks: Flow<Set<String>> = bookmarkFlow
        override val recent: Flow<List<String>> = recentFlow
        override val settings: Flow<AppSettings> = settingsFlow

        override suspend fun setBookmarked(id: String, bookmarked: Boolean) {
            failIfRequested()
            bookmarkFlow.value = if (bookmarked) bookmarkFlow.value + id else bookmarkFlow.value - id
        }

        override suspend fun recordRecent(id: String) {
            failIfRequested()
            recentFlow.value = listOf(id) + recentFlow.value.filterNot { it == id }
        }

        override suspend fun clearBookmarks() {
            failIfRequested()
            bookmarkFlow.value = emptySet()
        }

        override suspend fun clearRecent() {
            failIfRequested()
            recentFlow.value = emptyList()
        }

        override suspend fun updateThemeState(themeState: ThemeState) {
            failIfRequested()
            settingsFlow.value = settingsFlow.value.copy(themeState = themeState)
        }

        override suspend fun migrateIds(redirects: Map<String, String>) {
            failIfRequested()
            bookmarkFlow.value = bookmarkFlow.value.mapTo(linkedSetOf()) { redirects[it] ?: it }
            recentFlow.value = recentFlow.value.map { redirects[it] ?: it }.distinct()
        }
    }

    private val sampleEntries = listOf(
        entry("buttons"),
        entry("dialogs", category = "Communication"),
        entry("color", kind = CatalogKind.FOUNDATION),
    )

    /** Keeps the WhileSubscribed search pipeline active, mirroring the UI collector. */
    private fun kotlinx.coroutines.test.TestScope.collectSearch(viewModel: GalleryViewModel) {
        backgroundScope.launch(kotlinx.coroutines.test.UnconfinedTestDispatcher(testScheduler)) {
            viewModel.search.collect {}
        }
    }

    @Test
    fun failedCatalogLoadSurfacesRecoverableErrorAndRetrySucceeds() = kotlinx.coroutines.test.runTest {
        val library = FakeUserLibraryRepository()
        var shouldFail = true
        val viewModel = GalleryViewModel(
            catalogLoader = {
                if (shouldFail) throw IllegalStateException("corrupt asset")
                FakeCatalogRepository(sampleEntries)
            },
            userLibraryRepository = library,
            searchDispatcher = mainDispatcherRule.testDispatcher,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
        collectSearch(viewModel)
        advanceUntilIdle()
        assertTrue(viewModel.catalogLoad.value is CatalogLoad.Failed)

        shouldFail = false
        viewModel.retryCatalogLoad()
        advanceUntilIdle()

        assertTrue(viewModel.catalogLoad.value is CatalogLoad.Ready)
        assertEquals(sampleEntries.size, viewModel.catalog.value.entries.size)
        assertFalse(viewModel.search.value.visibleCatalogEntries.isEmpty())
    }

    @Test
    fun storedIdsArePrunedEvenWhenStorageEmitsBeforeTheCatalogLoads() = kotlinx.coroutines.test.runTest {
        val library = FakeUserLibraryRepository().apply {
            bookmarkFlow.value = setOf("buttons", "removed-entry")
            recentFlow.value = listOf("removed-entry", "dialogs")
        }
        val catalogGate = kotlinx.coroutines.CompletableDeferred<Unit>()
        val viewModel = GalleryViewModel(
            catalogLoader = {
                catalogGate.await()
                FakeCatalogRepository(sampleEntries)
            },
            userLibraryRepository = library,
            searchDispatcher = mainDispatcherRule.testDispatcher,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
        runCurrent()
        // Storage has emitted while the catalog is still loading.
        assertEquals(setOf("buttons", "removed-entry"), viewModel.library.value.bookmarks)

        catalogGate.complete(Unit)
        advanceUntilIdle()

        assertEquals(setOf("buttons"), viewModel.library.value.bookmarks)
        assertEquals(listOf("dialogs"), viewModel.library.value.recent)
    }

    @Test
    fun retiredIdsAreMigratedToTheirReplacementOnLoad() = kotlinx.coroutines.test.runTest {
        val library = FakeUserLibraryRepository().apply {
            bookmarkFlow.value = setOf("old-buttons")
            recentFlow.value = listOf("old-buttons", "dialogs")
        }
        val viewModel = GalleryViewModel(
            catalogLoader = { FakeCatalogRepository(sampleEntries, retiredIds = mapOf("old-buttons" to "buttons")) },
            userLibraryRepository = library,
            searchDispatcher = mainDispatcherRule.testDispatcher,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
        advanceUntilIdle()

        assertEquals(setOf("buttons"), library.bookmarkFlow.value)
        assertEquals(setOf("buttons"), viewModel.library.value.bookmarks)
        assertEquals(listOf("buttons", "dialogs"), viewModel.library.value.recent)
    }

    @Test
    fun debouncedQueryRunsSingleSearchAndFiltersCatalog() = kotlinx.coroutines.test.runTest {
        val viewModel = GalleryViewModel(
            catalogLoader = { FakeCatalogRepository(sampleEntries) },
            userLibraryRepository = FakeUserLibraryRepository(),
            searchDispatcher = mainDispatcherRule.testDispatcher,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
        collectSearch(viewModel)
        advanceUntilIdle()
        assertTrue(viewModel.catalogLoad.value is CatalogLoad.Ready)

        viewModel.updateQuery("dia")
        // Before the debounce window elapses the query is accepted but not searched.
        runCurrent()
        viewModel.selectCategory("Communication")
        advanceTimeBy(GalleryViewModel.SearchDebounceMillis + 1)
        advanceUntilIdle()

        val state = viewModel.search.value
        assertEquals("dia", state.query)
        assertEquals(listOf("dialogs"), state.results.map { it.id })
        assertEquals(listOf("dialogs"), state.visibleCatalogEntries.map { it.id })
    }

    @Test
    fun computeSearchStateWithoutQueryShowsOnlyComponentsForSelectedCategory() {
        val repository = FakeCatalogRepository(sampleEntries)
        val state = GalleryViewModel.computeSearchState(repository, "", "Actions")
        assertEquals(listOf("buttons"), state.visibleCatalogEntries.map { it.id })
        assertTrue(state.results.isEmpty())

        val allComponents = GalleryViewModel.computeSearchState(repository, "", null)
        assertEquals(listOf("buttons", "dialogs"), allComponents.visibleCatalogEntries.map { it.id })
    }

    @Test
    fun writeFailureEmitsEventInsteadOfCrashing() = kotlinx.coroutines.test.runTest {
        val library = FakeUserLibraryRepository().apply { failNextWrite = true }
        val viewModel = GalleryViewModel(
            catalogLoader = { FakeCatalogRepository(sampleEntries) },
            userLibraryRepository = library,
            searchDispatcher = mainDispatcherRule.testDispatcher,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
        advanceUntilIdle()

        val received = mutableListOf<GalleryEvent>()
        val collector = launch { viewModel.events.collect(received::add) }
        runCurrent()

        viewModel.recordRecent("buttons")
        advanceUntilIdle()
        runCurrent()

        assertEquals(listOf<GalleryEvent>(GalleryEvent.LibraryWriteFailed), received)
        collector.cancel()
    }

    @Test
    fun recordRecentIsPersistedThroughTheRepositoryOncePerCall() = kotlinx.coroutines.test.runTest {
        val library = FakeUserLibraryRepository()
        val viewModel = GalleryViewModel(
            catalogLoader = { FakeCatalogRepository(sampleEntries) },
            userLibraryRepository = library,
            searchDispatcher = mainDispatcherRule.testDispatcher,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
        advanceUntilIdle()

        viewModel.recordRecent("buttons")
        viewModel.recordRecent("buttons")
        advanceUntilIdle()

        assertEquals(listOf("buttons"), library.recentFlow.value)
    }

    @Test
    fun unknownEntryIdsAreFilteredOutOfLibraryState() = kotlinx.coroutines.test.runTest {
        val library = FakeUserLibraryRepository()
        library.bookmarkFlow.value = setOf("buttons", "ghost-entry")
        library.recentFlow.value = listOf("ghost-entry", "buttons")
        val viewModel = GalleryViewModel(
            catalogLoader = { FakeCatalogRepository(sampleEntries) },
            userLibraryRepository = library,
            searchDispatcher = mainDispatcherRule.testDispatcher,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
        advanceUntilIdle()

        assertEquals(setOf("buttons"), viewModel.library.value.bookmarks)
        assertEquals(listOf("buttons"), viewModel.library.value.recent)
    }

    @Test
    fun themeModeUpdateIsObservedInSettingsState() = kotlinx.coroutines.test.runTest {
        val library = FakeUserLibraryRepository()
        val viewModel = GalleryViewModel(
            catalogLoader = { FakeCatalogRepository(sampleEntries) },
            userLibraryRepository = library,
            searchDispatcher = mainDispatcherRule.testDispatcher,
            ioDispatcher = mainDispatcherRule.testDispatcher,
        )
        advanceUntilIdle()

        assertTrue(viewModel.settingsLoaded.value)
        viewModel.updateThemeState(ThemeState(themeMode = ThemeMode.DARK, pureBlack = true))
        advanceUntilIdle()

        assertEquals(ThemeMode.DARK, viewModel.settings.value.themeState.themeMode)
        assertTrue(viewModel.settings.value.themeState.pureBlack)
    }
}
