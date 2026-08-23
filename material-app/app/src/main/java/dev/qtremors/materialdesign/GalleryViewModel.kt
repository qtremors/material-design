package dev.qtremors.materialdesign

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.catalog.CatalogKind
import dev.qtremors.material.core.catalog.CatalogRepository
import dev.qtremors.material.core.catalog.SearchResult
import dev.qtremors.material.core.data.AppSettings
import dev.qtremors.material.core.data.MotionMode
import dev.qtremors.material.core.data.UserLibraryRepository
import dev.qtremors.material.core.designsystem.ThemeMode
import dev.qtremors.material.core.designsystem.ThemeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class RootDestination { EXPLORE, CATALOG, APIS, FOUNDATIONS }

data class GalleryUiState(
    val entries: List<CatalogEntry> = emptyList(),
    val visibleCatalogEntries: List<CatalogEntry> = emptyList(),
    val foundationEntries: List<CatalogEntry> = emptyList(),
    val allCategories: List<String> = emptyList(),
    val bookmarks: Set<String> = emptySet(),
    val recent: List<String> = emptyList(),
    val settings: AppSettings = AppSettings(),
    val rootDestination: RootDestination = RootDestination.EXPLORE,
    val query: String = "",
    val queryResults: List<CatalogEntry> = emptyList(),
    val selectedCategory: String? = null,
    val apiStability: ApiStability = ApiStability.STABLE,
)

class GalleryViewModel(
    private val catalogRepository: CatalogRepository,
    private val userLibraryRepository: UserLibraryRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(baseState())
    val state: StateFlow<GalleryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userLibraryRepository.bookmarks,
                userLibraryRepository.recent,
                userLibraryRepository.settings,
            ) { bookmarks, recent, settings -> Triple(bookmarks, recent, settings) }
                .collect { (bookmarks, recent, settings) ->
                    val validIds = catalogRepository.entries.mapTo(mutableSetOf(), CatalogEntry::id)
                    _state.update {
                        it.copy(
                            bookmarks = bookmarks.intersect(validIds),
                            recent = recent.filter(validIds::contains),
                            settings = settings,
                        )
                    }
                }
        }
    }

    fun selectRoot(destination: RootDestination) = _state.update { it.copy(rootDestination = destination) }

    fun updateQuery(query: String) {
        _state.update { current ->
            val category = if (query.isNotBlank()) null else current.selectedCategory
            current.copy(
                query = query,
                selectedCategory = category,
                visibleCatalogEntries = filterEntries(query, category),
                queryResults = searchResults(query),
            )
        }
    }

    fun selectCategory(category: String?) = _state.update {
        it.copy(selectedCategory = category, visibleCatalogEntries = filterEntries(it.query, category))
    }

    fun selectApiStability(stability: ApiStability) = _state.update { it.copy(apiStability = stability) }

    fun setBookmarked(id: String, bookmarked: Boolean) = viewModelScope.launch {
        userLibraryRepository.setBookmarked(id, bookmarked)
    }

    fun recordRecent(id: String) = viewModelScope.launch { userLibraryRepository.recordRecent(id) }
    fun clearBookmarks() = viewModelScope.launch { userLibraryRepository.clearBookmarks() }
    fun clearRecent() = viewModelScope.launch { userLibraryRepository.clearRecent() }
    fun updateThemeState(themeState: dev.qtremors.material.core.designsystem.ThemeState) = viewModelScope.launch {
        userLibraryRepository.updateThemeState(themeState)
    }
    fun updateThemeMode(mode: ThemeMode) = viewModelScope.launch { userLibraryRepository.updateThemeMode(mode) }
    fun updateDynamicColor(enabled: Boolean) = viewModelScope.launch { userLibraryRepository.updateDynamicColor(enabled) }
    fun updateMotionMode(mode: MotionMode) = viewModelScope.launch { userLibraryRepository.updateMotionMode(mode) }

    private fun baseState(): GalleryUiState {
        val components = catalogRepository.entries.filter { it.kind == CatalogKind.COMPONENT }
        val allCategories = components.map {
            if (it.category.startsWith("Selection", ignoreCase = true)) "Selection" else it.category
        }.distinct().sorted()
        return GalleryUiState(
            entries = catalogRepository.entries,
            visibleCatalogEntries = components,
            foundationEntries = catalogRepository.entries.filter { it.kind == CatalogKind.FOUNDATION },
            allCategories = allCategories,
        )
    }

    private fun filterEntries(query: String, category: String?): List<CatalogEntry> {
        val base = if (query.isBlank()) catalogRepository.entries else catalogRepository.search(query).map { it.entry }
        return base.filter { entry ->
            val matchesCategory = category == null ||
                entry.category.equals(category, ignoreCase = true) ||
                (category.equals("Selection", ignoreCase = true) && entry.category.startsWith("Selection", ignoreCase = true))
            if (query.isBlank()) {
                entry.kind == CatalogKind.COMPONENT && matchesCategory
            } else {
                matchesCategory
            }
        }
    }

    private fun searchResults(query: String): List<CatalogEntry> =
        if (query.isBlank()) {
            emptyList()
        } else {
            catalogRepository.search(query).map(SearchResult::entry).take(MaxQueryResults)
        }

    companion object {
        private const val MaxQueryResults = 16

        fun factory(container: MaterialAppContainer): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = GalleryViewModel(
                container.catalogRepository,
                container.userLibraryRepository,
            ) as T
        }
    }
}
