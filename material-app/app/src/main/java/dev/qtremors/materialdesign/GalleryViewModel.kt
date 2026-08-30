package dev.qtremors.materialdesign

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.catalog.CatalogKind
import dev.qtremors.material.core.catalog.CatalogRepository
import dev.qtremors.material.core.data.AppSettings
import dev.qtremors.material.core.data.MotionMode
import dev.qtremors.material.core.data.UserLibraryRepository
import dev.qtremors.material.core.designsystem.ThemeMode
import dev.qtremors.material.core.designsystem.ThemeState
import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class RootDestination { EXPLORE, CATALOG, APIS, FOUNDATIONS }

/** Load state of the bundled catalog; the app renders loading/error surfaces until ready. */
sealed interface CatalogLoad {
    data object Loading : CatalogLoad
    data class Ready(val repository: CatalogRepository) : CatalogLoad
    data class Failed(val cause: Throwable) : CatalogLoad
}

@Immutable
data class CatalogSnapshot(
    val entries: List<CatalogEntry> = emptyList(),
    val componentEntries: List<CatalogEntry> = emptyList(),
    val foundationEntries: List<CatalogEntry> = emptyList(),
    val allCategories: List<String> = emptyList(),
    val material3Version: String = "",
    val composeUiVersion: String = "",
    val stableBaseline: String = "",
)

@Immutable
data class LibraryState(
    val bookmarks: Set<String> = emptySet(),
    val recent: List<String> = emptyList(),
)

@Immutable
data class SearchState(
    val query: String = "",
    val results: List<CatalogEntry> = emptyList(),
    val visibleCatalogEntries: List<CatalogEntry> = emptyList(),
)

sealed interface GalleryEvent {
    data object LibraryWriteFailed : GalleryEvent
    data object ComponentUnavailable : GalleryEvent
}

class GalleryViewModel(
    private val catalogLoader: suspend () -> CatalogRepository,
    private val userLibraryRepository: UserLibraryRepository,
    private val searchDispatcher: kotlinx.coroutines.CoroutineDispatcher = Dispatchers.Default,
    private val ioDispatcher: kotlinx.coroutines.CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _catalogLoad = MutableStateFlow<CatalogLoad>(CatalogLoad.Loading)
    val catalogLoad: StateFlow<CatalogLoad> = _catalogLoad.asStateFlow()

    private val _catalog = MutableStateFlow(CatalogSnapshot())
    val catalog: StateFlow<CatalogSnapshot> = _catalog.asStateFlow()

    private val _library = MutableStateFlow(LibraryState())
    val library: StateFlow<LibraryState> = _library.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private val _rootDestination = MutableStateFlow(RootDestination.EXPLORE)
    val rootDestination: StateFlow<RootDestination> = _rootDestination.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _apiStability = MutableStateFlow(ApiStability.STABLE)
    val apiStability: StateFlow<ApiStability> = _apiStability.asStateFlow()

    private val queryFlow = MutableStateFlow("")
    val query: StateFlow<String> = queryFlow.asStateFlow()

    private val _events = MutableSharedFlow<GalleryEvent>(
        extraBufferCapacity = 8,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val events: SharedFlow<GalleryEvent> = _events

    init {
        loadCatalog()
        observeLibrary()
    }

    fun retryCatalogLoad() {
        if (_catalogLoad.value is CatalogLoad.Failed) loadCatalog()
    }

    private fun loadCatalog() {
        viewModelScope.launch {
            _catalogLoad.value = CatalogLoad.Loading
            _catalogLoad.value = try {
                val repository = withContext(ioDispatcher) { catalogLoader() }
                publishCatalogSnapshot(repository)
                CatalogLoad.Ready(repository)
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (throwable: Throwable) {
                Log.e(TAG, "Bundled catalog failed to load", throwable)
                CatalogLoad.Failed(throwable)
            }
        }
    }

    private fun publishCatalogSnapshot(repository: CatalogRepository) {
        val components = repository.entries.filter { it.kind == CatalogKind.COMPONENT }
        val allCategories = components.map {
            if (it.category.startsWith("Selection", ignoreCase = true)) "Selection" else it.category
        }.distinct().sorted()
        _catalog.value = CatalogSnapshot(
            entries = repository.entries,
            componentEntries = components,
            foundationEntries = repository.entries.filter { it.kind == CatalogKind.FOUNDATION },
            allCategories = allCategories,
            material3Version = repository.document.material3Version,
            composeUiVersion = repository.document.composeUiVersion,
            stableBaseline = repository.document.stableBaseline,
        )
    }

    private fun observeLibrary() {
        viewModelScope.launch {
            combine(
                userLibraryRepository.bookmarks,
                userLibraryRepository.recent,
                userLibraryRepository.settings,
            ) { bookmarks, recent, settings -> Triple(bookmarks, recent, settings) }
                .collect { (bookmarks, recent, settings) ->
                    val snapshot = _catalog.value
                    if (snapshot != CatalogSnapshot()) {
                        val validIds = snapshot.entries.mapTo(mutableSetOf(), CatalogEntry::id)
                        _library.value = LibraryState(
                            bookmarks = bookmarks.intersect(validIds),
                            recent = recent.filter(validIds::contains),
                        )
                    } else {
                        _library.value = LibraryState(bookmarks = bookmarks, recent = recent)
                    }
                    _settings.value = settings
                }
        }
    }

    /** Debounced, off-main-thread derivation of search results and filtered catalog entries. */
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val search: StateFlow<SearchState> = combine(
        _catalogLoad,
        queryFlow,
        _selectedCategory,
    ) { load, query, category -> Triple(load, query, category) }
        .debounce { (_, query, _) -> if (query.isBlank()) 0L else SearchDebounceMillis }
        .mapLatest { (load, query, category) ->
            val repository = (load as? CatalogLoad.Ready)?.repository ?: return@mapLatest SearchState(query = query)
            computeSearchState(repository, query, category)
        }
        .flowOn(searchDispatcher)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SearchState())

    fun selectRoot(destination: RootDestination) {
        _rootDestination.value = destination
    }

    fun updateQuery(query: String) {
        queryFlow.value = query
        if (query.isNotBlank()) _selectedCategory.value = null
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun selectApiStability(stability: ApiStability) {
        _apiStability.value = stability
    }

    fun setBookmarked(id: String, bookmarked: Boolean) = launchSafely {
        userLibraryRepository.setBookmarked(id, bookmarked)
    }

    fun recordRecent(id: String) = launchSafely {
        userLibraryRepository.recordRecent(id)
    }

    fun clearBookmarks() = launchSafely { userLibraryRepository.clearBookmarks() }

    fun clearRecent() = launchSafely { userLibraryRepository.clearRecent() }

    fun updateThemeState(themeState: ThemeState) = launchSafely {
        userLibraryRepository.updateThemeState(themeState)
    }

    fun updateThemeMode(mode: ThemeMode) = launchSafely { userLibraryRepository.updateThemeMode(mode) }

    fun updateDynamicColor(enabled: Boolean) = launchSafely {
        userLibraryRepository.updateDynamicColor(enabled)
    }

    fun updateMotionMode(mode: MotionMode) = launchSafely {
        userLibraryRepository.updateMotionMode(mode)
    }

    fun notifyComponentUnavailable() {
        _events.tryEmit(GalleryEvent.ComponentUnavailable)
    }

    private fun launchSafely(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (cancellation: CancellationException) {
                throw cancellation
            } catch (throwable: Throwable) {
                Log.e(TAG, "User library write failed", throwable)
                _events.tryEmit(GalleryEvent.LibraryWriteFailed)
            }
        }
    }

    companion object {
        private const val TAG = "GalleryViewModel"
        const val SearchDebounceMillis = 120L
        const val MaxQueryResults = 16

        /**
         * Pure search/filter computation shared by the ViewModel pipeline and unit tests.
         * Runs a single corpus search per invocation.
         */
        internal fun computeSearchState(
            repository: CatalogRepository,
            query: String,
            category: String?,
        ): SearchState {
            val searched = if (query.isBlank()) null else repository.search(query)
            val base = searched?.map { it.entry } ?: repository.entries
            val visible = base.filter { entry ->
                val matchesCategory = category == null ||
                    entry.category.equals(category, ignoreCase = true) ||
                    (category.equals("Selection", ignoreCase = true) && entry.category.startsWith("Selection", ignoreCase = true))
                if (query.isBlank()) {
                    entry.kind == CatalogKind.COMPONENT && matchesCategory
                } else {
                    matchesCategory
                }
            }
            return SearchState(
                query = query,
                results = searched?.take(MaxQueryResults)?.map { it.entry } ?: emptyList(),
                visibleCatalogEntries = visible,
            )
        }

        fun factory(
            container: MaterialAppContainer,
            catalogLoader: suspend () -> CatalogRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = GalleryViewModel(
                catalogLoader = catalogLoader,
                userLibraryRepository = container.userLibraryRepository,
            ) as T
        }
    }
}
