package dev.qtremors.material.core.data

import dev.qtremors.material.core.designsystem.AccentSource
import dev.qtremors.material.core.designsystem.ThemeMode
import dev.qtremors.material.core.designsystem.ThemePreset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserLibraryRepositoryTest {
    @Test
    fun recentEntriesAreNewestFirstUniqueAndCappedAtTwenty() {
        val fullHistory = (1..25).map { "component-$it" }

        assertEquals(
            listOf("component-21") + (1..19).map { "component-$it" },
            updatedRecent(fullHistory, "component-21"),
        )
        assertEquals(20, updatedRecent(fullHistory, "new-component").size)
        assertEquals("new-component", updatedRecent(fullHistory, "new-component").first())
    }

    @Test
    fun addingToEmptyHistoryCreatesOneRecentEntry() {
        assertEquals(listOf("buttons"), updatedRecent(emptyList(), "buttons"))
    }

    @Test
    fun retiredIdsMoveToTheirReplacementWithoutDuplicatingHistory() {
        val redirects = mapOf("selection-controls" to "checkbox")

        assertEquals(setOf("checkbox", "buttons"), migratedBookmarks(setOf("selection-controls", "buttons"), redirects))
        assertEquals(
            listOf("checkbox", "buttons"),
            migratedRecent(listOf("selection-controls", "buttons", "checkbox"), redirects),
        )
    }

    @Test
    fun legacyOledModeBecomesDarkWithPureBlack() {
        val theme = themeStateFrom(StoredTheme(themeMode = "OLED"))
        assertEquals(ThemeMode.DARK, theme.themeMode)
        assertTrue(theme.pureBlack)
    }

    @Test
    fun legacyColorChoicesBecomeSeedsWithoutLosingTheColor() {
        assertEquals(AccentSource.WALLPAPER, themeStateFrom(StoredTheme(legacyAccent = "DYNAMIC")).accentSource)
        val red = themeStateFrom(StoredTheme(legacyAccent = "RED"))
        assertEquals(AccentSource.CUSTOM, red.accentSource)
        assertEquals("#F44336", red.customSeedHex)
        assertEquals(ThemePreset.GRAPHITE, themeStateFrom(StoredTheme(legacyAccent = "MONOCHROME")).preset)
        val custom = themeStateFrom(StoredTheme(legacyPreset = "CUSTOM", legacyCustomPrimary = "#123456"))
        assertEquals("#123456", custom.customSeedHex)
    }

    @Test
    fun newKeysTakePrecedenceOverLegacyKeys() {
        val theme = themeStateFrom(
            StoredTheme(colorSource = "PRESET", colorPreset = "CORAL", legacyAccent = "RED", themeMode = "LIGHT"),
        )
        assertEquals(AccentSource.PRESET, theme.accentSource)
        assertEquals(ThemePreset.CORAL, theme.preset)
        assertEquals(ThemeMode.LIGHT, theme.themeMode)
    }
}
