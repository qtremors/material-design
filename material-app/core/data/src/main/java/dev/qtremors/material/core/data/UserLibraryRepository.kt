package dev.qtremors.material.core.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode { SYSTEM, LIGHT, DARK }
enum class MotionMode { SYSTEM, REDUCED }

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = true,
    val motionMode: MotionMode = MotionMode.SYSTEM,
)

interface UserLibraryRepository {
    val bookmarks: Flow<Set<String>>
    val recent: Flow<List<String>>
    val settings: Flow<AppSettings>
    suspend fun setBookmarked(id: String, bookmarked: Boolean)
    suspend fun recordRecent(id: String)
    suspend fun clearBookmarks()
    suspend fun clearRecent()
    suspend fun updateThemeMode(mode: ThemeMode)
    suspend fun updateDynamicColor(enabled: Boolean)
    suspend fun updateMotionMode(mode: MotionMode)
}

private val Context.materialPreferences by preferencesDataStore(name = "material_reference")

class DataStoreUserLibraryRepository(private val context: Context) : UserLibraryRepository {
    override val bookmarks: Flow<Set<String>> = context.materialPreferences.data.map { preferences ->
        preferences[Keys.bookmarks].orEmpty()
    }

    override val recent: Flow<List<String>> = context.materialPreferences.data.map { preferences ->
        preferences[Keys.recent].orEmpty().split(',').filter(String::isNotBlank)
    }

    override val settings: Flow<AppSettings> = context.materialPreferences.data.map(::settingsFrom)

    override suspend fun setBookmarked(id: String, bookmarked: Boolean) {
        context.materialPreferences.edit { preferences ->
            val current = preferences[Keys.bookmarks].orEmpty()
            preferences[Keys.bookmarks] = if (bookmarked) current + id else current - id
        }
    }

    override suspend fun recordRecent(id: String) {
        context.materialPreferences.edit { preferences ->
            val current = preferences[Keys.recent].orEmpty().split(',').filter(String::isNotBlank)
            preferences[Keys.recent] = updatedRecent(current, id).joinToString(",")
        }
    }

    override suspend fun clearBookmarks() {
        context.materialPreferences.edit { it.remove(Keys.bookmarks) }
    }

    override suspend fun clearRecent() {
        context.materialPreferences.edit { it.remove(Keys.recent) }
    }

    override suspend fun updateThemeMode(mode: ThemeMode) {
        context.materialPreferences.edit { it[Keys.themeMode] = mode.name }
    }

    override suspend fun updateDynamicColor(enabled: Boolean) {
        context.materialPreferences.edit { it[Keys.dynamicColor] = enabled }
    }

    override suspend fun updateMotionMode(mode: MotionMode) {
        context.materialPreferences.edit { it[Keys.motionMode] = mode.name }
    }

    private fun settingsFrom(preferences: Preferences): AppSettings = AppSettings(
        themeMode = preferences[Keys.themeMode]?.let { runCatching { ThemeMode.valueOf(it) }.getOrNull() } ?: ThemeMode.SYSTEM,
        dynamicColor = preferences[Keys.dynamicColor] ?: true,
        motionMode = preferences[Keys.motionMode]?.let { runCatching { MotionMode.valueOf(it) }.getOrNull() } ?: MotionMode.SYSTEM,
    )

    private object Keys {
        val bookmarks = stringSetPreferencesKey("bookmarks")
        val recent = stringPreferencesKey("recent")
        val themeMode = stringPreferencesKey("theme_mode")
        val dynamicColor = booleanPreferencesKey("dynamic_color")
        val motionMode = stringPreferencesKey("motion_mode")
    }

    companion object { const val MAX_RECENT = 20 }
}

internal fun updatedRecent(current: List<String>, id: String): List<String> =
    (listOf(id) + current.filterNot { it == id }).take(DataStoreUserLibraryRepository.MAX_RECENT)
