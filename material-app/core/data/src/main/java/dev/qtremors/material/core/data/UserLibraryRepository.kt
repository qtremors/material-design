package dev.qtremors.material.core.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.qtremors.material.core.designsystem.AccentColor
import dev.qtremors.material.core.designsystem.ThemeMode
import dev.qtremors.material.core.designsystem.ThemePreset
import dev.qtremors.material.core.designsystem.ThemeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class MotionMode { SYSTEM, REDUCED }

data class AppSettings(
    val themeState: ThemeState = ThemeState(),
) {
    // Backward compatibility helpers
    val themeMode: ThemeMode get() = themeState.themeMode
    val dynamicColor: Boolean get() = themeState.accentColor == AccentColor.DYNAMIC
    val motionMode: MotionMode get() = if (themeState.reducedMotion) MotionMode.REDUCED else MotionMode.SYSTEM
}

interface UserLibraryRepository {
    val bookmarks: Flow<Set<String>>
    val recent: Flow<List<String>>
    val settings: Flow<AppSettings>
    suspend fun setBookmarked(id: String, bookmarked: Boolean)
    suspend fun recordRecent(id: String)
    suspend fun clearBookmarks()
    suspend fun clearRecent()
    suspend fun updateThemeState(themeState: ThemeState)
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

    override suspend fun updateThemeState(themeState: ThemeState) {
        context.materialPreferences.edit { preferences ->
            preferences[Keys.themeMode] = themeState.themeMode.name
            preferences[Keys.accentColor] = themeState.accentColor.name
            preferences[Keys.themePreset] = themeState.themePreset.name
            preferences[Keys.customPrimary] = themeState.customPrimaryColorHex
            preferences[Keys.customBg] = themeState.customBackgroundColorHex
            preferences[Keys.harmonizeColors] = themeState.harmonizeColors
            preferences[Keys.reducedMotion] = themeState.reducedMotion
        }
    }

    override suspend fun updateThemeMode(mode: ThemeMode) {
        context.materialPreferences.edit { it[Keys.themeMode] = mode.name }
    }

    override suspend fun updateDynamicColor(enabled: Boolean) {
        context.materialPreferences.edit {
            it[Keys.accentColor] = if (enabled) AccentColor.DYNAMIC.name else AccentColor.BLUE.name
        }
    }

    override suspend fun updateMotionMode(mode: MotionMode) {
        context.materialPreferences.edit {
            it[Keys.reducedMotion] = (mode == MotionMode.REDUCED)
        }
    }

    private fun settingsFrom(preferences: Preferences): AppSettings {
        val themeModeStr = preferences[Keys.themeMode] ?: ThemeMode.SYSTEM.name
        val accentColorStr = preferences[Keys.accentColor] ?: AccentColor.DYNAMIC.name
        val themePresetStr = preferences[Keys.themePreset] ?: ThemePreset.NONE.name
        val customPrimary = preferences[Keys.customPrimary] ?: "#BD93F9"
        val customBg = preferences[Keys.customBg] ?: "#282A36"
        val harmonize = preferences[Keys.harmonizeColors] ?: true
        val reducedMotion = preferences[Keys.reducedMotion] ?: false

        val themeState = ThemeState(
            themeMode = ThemeMode.entries.find { it.name == themeModeStr } ?: ThemeMode.SYSTEM,
            accentColor = AccentColor.entries.find { it.name == accentColorStr } ?: AccentColor.DYNAMIC,
            themePreset = ThemePreset.entries.find { it.name == themePresetStr } ?: ThemePreset.NONE,
            customPrimaryColorHex = customPrimary,
            customBackgroundColorHex = customBg,
            harmonizeColors = harmonize,
            reducedMotion = reducedMotion,
        )
        return AppSettings(themeState = themeState)
    }

    private object Keys {
        val bookmarks = stringSetPreferencesKey("bookmarks")
        val recent = stringPreferencesKey("recent")
        val themeMode = stringPreferencesKey("theme_mode")
        val accentColor = stringPreferencesKey("accent_color")
        val themePreset = stringPreferencesKey("theme_preset")
        val customPrimary = stringPreferencesKey("custom_primary_hex")
        val customBg = stringPreferencesKey("custom_bg_hex")
        val harmonizeColors = booleanPreferencesKey("harmonize_colors")
        val reducedMotion = booleanPreferencesKey("reduced_motion")
    }

    companion object { const val MAX_RECENT = 20 }
}

internal fun updatedRecent(current: List<String>, id: String): List<String> =
    (listOf(id) + current.filterNot { it == id }).take(DataStoreUserLibraryRepository.MAX_RECENT)
