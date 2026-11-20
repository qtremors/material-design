package dev.qtremors.material.core.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.qtremors.material.core.designsystem.AccentSource
import dev.qtremors.material.core.designsystem.ThemeMode
import dev.qtremors.material.core.designsystem.ThemePreset
import dev.qtremors.material.core.designsystem.ThemeState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class AppSettings(
    val themeState: ThemeState = ThemeState(),
)

interface UserLibraryRepository {
    val bookmarks: Flow<Set<String>>
    val recent: Flow<List<String>>
    val settings: Flow<AppSettings>
    suspend fun setBookmarked(id: String, bookmarked: Boolean)
    suspend fun recordRecent(id: String)
    suspend fun clearBookmarks()
    suspend fun clearRecent()
    suspend fun updateThemeState(themeState: ThemeState)

    /** Rewrites stored bookmarks and history so retired entry IDs point at their replacements. */
    suspend fun migrateIds(redirects: Map<String, String>)
}

private val Context.materialPreferences by preferencesDataStore(name = "material_reference")

class DataStoreUserLibraryRepository(private val context: Context) : UserLibraryRepository {
    override val bookmarks: Flow<Set<String>> = context.materialPreferences.data.map { preferences ->
        preferences[Keys.bookmarks].orEmpty()
    }

    override val recent: Flow<List<String>> = context.materialPreferences.data.map { preferences ->
        preferences[Keys.recent].orEmpty().split(',').filter(String::isNotBlank)
    }

    override val settings: Flow<AppSettings> = context.materialPreferences.data.map { preferences ->
        AppSettings(themeState = themeStateFrom(StoredTheme.from(preferences)))
    }

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
            preferences[Keys.pureBlack] = themeState.pureBlack
            preferences[Keys.colorSource] = themeState.accentSource.name
            preferences[Keys.colorPreset] = themeState.preset.name
            preferences[Keys.customSeed] = themeState.customSeedHex
            preferences[Keys.reducedMotion] = themeState.reducedMotion
            preferences[Keys.haptics] = themeState.hapticsEnabled
            preferences[Keys.showBadges] = themeState.showBadges
            // Settings from before 2.1.3 are folded into the keys above the first time they are saved.
            Keys.legacy.forEach { preferences.remove(it) }
        }
    }

    override suspend fun migrateIds(redirects: Map<String, String>) {
        if (redirects.isEmpty()) return
        context.materialPreferences.edit { preferences ->
            val bookmarks = preferences[Keys.bookmarks].orEmpty()
            if (bookmarks.any(redirects::containsKey)) {
                preferences[Keys.bookmarks] = migratedBookmarks(bookmarks, redirects)
            }
            val recent = preferences[Keys.recent].orEmpty().split(',').filter(String::isNotBlank)
            if (recent.any(redirects::containsKey)) {
                preferences[Keys.recent] = migratedRecent(recent, redirects).joinToString(",")
            }
        }
    }

    private object Keys {
        val bookmarks = stringSetPreferencesKey("bookmarks")
        val recent = stringPreferencesKey("recent")
        val themeMode = stringPreferencesKey("theme_mode")
        val pureBlack = booleanPreferencesKey("pure_black")
        val colorSource = stringPreferencesKey("color_source")
        val colorPreset = stringPreferencesKey("color_preset")
        val customSeed = stringPreferencesKey("custom_seed_hex")
        val reducedMotion = booleanPreferencesKey("reduced_motion")
        val haptics = booleanPreferencesKey("vibrations_enabled")
        val showBadges = booleanPreferencesKey("show_badges")

        val legacyAccent = stringPreferencesKey("accent_color")
        val legacyPreset = stringPreferencesKey("theme_preset")
        val legacyCustomPrimary = stringPreferencesKey("custom_primary_hex")
        val legacy: List<Preferences.Key<*>> = listOf(
            legacyAccent,
            legacyPreset,
            legacyCustomPrimary,
            stringPreferencesKey("custom_bg_hex"),
            booleanPreferencesKey("harmonize_colors"),
            booleanPreferencesKey("expandable_app_bar"),
        )
    }

    private fun StoredTheme.Companion.from(preferences: Preferences) = StoredTheme(
        themeMode = preferences[Keys.themeMode],
        pureBlack = preferences[Keys.pureBlack],
        colorSource = preferences[Keys.colorSource],
        colorPreset = preferences[Keys.colorPreset],
        customSeedHex = preferences[Keys.customSeed],
        reducedMotion = preferences[Keys.reducedMotion],
        hapticsEnabled = preferences[Keys.haptics],
        showBadges = preferences[Keys.showBadges],
        legacyAccent = preferences[Keys.legacyAccent],
        legacyPreset = preferences[Keys.legacyPreset],
        legacyCustomPrimary = preferences[Keys.legacyCustomPrimary],
    )

    companion object { const val MAX_RECENT = 20 }
}

/** Raw stored values, including the pre-2.1.3 keys that [themeStateFrom] migrates. */
internal data class StoredTheme(
    val themeMode: String? = null,
    val pureBlack: Boolean? = null,
    val colorSource: String? = null,
    val colorPreset: String? = null,
    val customSeedHex: String? = null,
    val reducedMotion: Boolean? = null,
    val hapticsEnabled: Boolean? = null,
    val showBadges: Boolean? = null,
    val legacyAccent: String? = null,
    val legacyPreset: String? = null,
    val legacyCustomPrimary: String? = null,
) {
    companion object
}

/** Accent colors offered before 2.1.3, kept so a saved choice becomes the same custom seed. */
private val legacyAccentSeeds = mapOf(
    "RED" to "#F44336", "PINK" to "#E91E63", "PURPLE" to "#9C27B0", "DEEP_PURPLE" to "#673AB7",
    "INDIGO" to "#3F51B5", "BLUE" to "#2196F3", "LIGHT_BLUE" to "#03A9F4", "CYAN" to "#00BCD4",
    "TEAL" to "#009688", "GREEN" to "#4CAF50", "LIGHT_GREEN" to "#8BC34A", "LIME" to "#CDDC39",
    "YELLOW" to "#FFEB3B", "AMBER" to "#FFC107", "ORANGE" to "#FF9800", "DEEP_ORANGE" to "#FF5722",
    "BROWN" to "#795548", "BLUE_GREY" to "#607D8B", "GREY" to "#757575", "BLACK" to "#111111",
)

/** Named presets offered before 2.1.3, mapped to their primary color as a custom seed. */
private val legacyPresetSeeds = mapOf("DRACULA" to "#BD93F9", "TOKYO_NIGHT" to "#7AA2F7")

internal fun themeStateFrom(stored: StoredTheme): ThemeState {
    val defaults = ThemeState()
    val legacyOled = stored.themeMode == "OLED"
    val mode = if (legacyOled) ThemeMode.DARK else ThemeMode.entries.find { it.name == stored.themeMode } ?: defaults.themeMode

    val (source, preset, seed) = when {
        stored.colorSource != null -> Triple(
            AccentSource.entries.find { it.name == stored.colorSource } ?: defaults.accentSource,
            ThemePreset.entries.find { it.name == stored.colorPreset } ?: defaults.preset,
            stored.customSeedHex ?: defaults.customSeedHex,
        )
        stored.legacyPreset == "CUSTOM" ->
            Triple(AccentSource.CUSTOM, defaults.preset, stored.legacyCustomPrimary ?: defaults.customSeedHex)
        stored.legacyPreset in legacyPresetSeeds ->
            Triple(AccentSource.CUSTOM, defaults.preset, legacyPresetSeeds.getValue(stored.legacyPreset!!))
        stored.legacyAccent == "MONOCHROME" -> Triple(AccentSource.PRESET, ThemePreset.GRAPHITE, defaults.customSeedHex)
        stored.legacyAccent in legacyAccentSeeds ->
            Triple(AccentSource.CUSTOM, defaults.preset, legacyAccentSeeds.getValue(stored.legacyAccent!!))
        else -> Triple(defaults.accentSource, defaults.preset, defaults.customSeedHex)
    }

    return ThemeState(
        themeMode = mode,
        pureBlack = stored.pureBlack ?: legacyOled,
        accentSource = source,
        preset = preset,
        customSeedHex = seed,
        reducedMotion = stored.reducedMotion ?: defaults.reducedMotion,
        hapticsEnabled = stored.hapticsEnabled ?: defaults.hapticsEnabled,
        showBadges = stored.showBadges ?: defaults.showBadges,
    )
}

internal fun updatedRecent(current: List<String>, id: String): List<String> =
    (listOf(id) + current.filterNot { it == id }).take(DataStoreUserLibraryRepository.MAX_RECENT)

internal fun migratedBookmarks(bookmarks: Set<String>, redirects: Map<String, String>): Set<String> =
    bookmarks.mapTo(linkedSetOf()) { redirects[it] ?: it }

/** Keeps history newest-first and unique after redirecting retired IDs. */
internal fun migratedRecent(recent: List<String>, redirects: Map<String, String>): List<String> =
    recent.map { redirects[it] ?: it }.distinct()
