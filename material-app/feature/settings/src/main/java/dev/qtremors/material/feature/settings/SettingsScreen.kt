package dev.qtremors.material.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MotionPhotosOff
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.data.AppSettings
import dev.qtremors.material.core.data.MotionMode
import dev.qtremors.material.core.designsystem.AccentColor
import dev.qtremors.material.core.designsystem.ThemeMode
import dev.qtremors.material.core.designsystem.ThemePreset
import dev.qtremors.material.core.designsystem.ThemeState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    bookmarkCount: Int,
    recentCount: Int,
    onThemeStateChange: (ThemeState) -> Unit,
    onClearBookmarks: () -> Unit,
    onClearRecent: () -> Unit,
    onBack: () -> Unit,
) {
    var confirmAction by remember { mutableStateOf<String?>(null) }
    val theme = settings.themeState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            SettingsSection("Appearance", "Customize theme mode, color palettes, and presets.") {
                ThemeModeSelector(
                    currentMode = theme.themeMode,
                    onModeSelected = { onThemeStateChange(theme.copy(themeMode = it)) },
                )

                ThemePresetSelector(
                    currentPreset = theme.themePreset,
                    onPresetSelected = { onThemeStateChange(theme.copy(themePreset = it)) },
                )

                if (theme.themePreset == ThemePreset.CUSTOM) {
                    CustomThemeCreatorPanel(
                        themeState = theme,
                        onThemeChange = onThemeStateChange,
                    )
                }

                if (theme.themePreset == ThemePreset.NONE) {
                    AccentColorSelector(
                        currentAccent = theme.accentColor,
                        onAccentSelected = { onThemeStateChange(theme.copy(accentColor = it)) },
                    )
                }

                SegmentedListItem(
                    checked = theme.harmonizeColors,
                    onCheckedChange = { onThemeStateChange(theme.copy(harmonizeColors = it)) },
                    shapes = ListItemDefaults.segmentedShapes(0, 1),
                    leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) },
                    supportingContent = { Text("Harmonize palette tones with primary accent") },
                    trailingContent = {
                        Switch(checked = theme.harmonizeColors, onCheckedChange = null)
                    },
                    content = { Text("Harmonize colors") },
                )
            }

            SettingsSection("Motion", "Reduced motion changes presentation, never meaning or safety timing.") {
                SegmentedListItem(
                    checked = theme.reducedMotion,
                    onCheckedChange = {
                        onThemeStateChange(theme.copy(reducedMotion = it))
                    },
                    shapes = ListItemDefaults.segmentedShapes(0, 1),
                    leadingContent = { Icon(Icons.Default.MotionPhotosOff, contentDescription = null) },
                    supportingContent = { Text("Avoid decorative motion while preserving state and confirmation") },
                    trailingContent = {
                        Switch(
                            checked = theme.reducedMotion,
                            onCheckedChange = null,
                        )
                    },
                    content = { Text("Reduce app motion") },
                )
            }

            SettingsSection("Library", "Bookmarks and history stay on this device.") {
                SegmentedListItem(
                    onClick = { confirmAction = "bookmarks" },
                    enabled = bookmarkCount > 0,
                    shapes = ListItemDefaults.segmentedShapes(0, 2),
                    leadingContent = { Icon(Icons.Default.Bookmark, contentDescription = null) },
                    supportingContent = { Text("$bookmarkCount saved components") },
                    trailingContent = { Icon(Icons.Default.DeleteSweep, contentDescription = null) },
                    content = { Text("Clear bookmarks") },
                )
                SegmentedListItem(
                    onClick = { confirmAction = "recent" },
                    enabled = recentCount > 0,
                    shapes = ListItemDefaults.segmentedShapes(1, 2),
                    leadingContent = { Icon(Icons.Default.History, contentDescription = null) },
                    supportingContent = { Text("$recentCount recently viewed components") },
                    trailingContent = { Icon(Icons.Default.DeleteSweep, contentDescription = null) },
                    content = { Text("Clear recent history") },
                )
            }

            SettingsSection("About", "Pinned versions make examples and API metadata reproducible.") {
                SegmentedListItem(
                    shapes = ListItemDefaults.segmentedShapes(0, 3),
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                    supportingContent = { Text("Version 2.0.3") },
                    content = { Text("Material Design") },
                )
                SegmentedListItem(
                    shapes = ListItemDefaults.segmentedShapes(1, 3),
                    leadingContent = { Icon(Icons.Default.Contrast, contentDescription = null) },
                    supportingContent = { Text("Material 3 1.5.0-alpha23 · Compose UI 1.12.0-alpha03") },
                    content = { Text("Catalog snapshot") },
                )
                SegmentedListItem(
                    shapes = ListItemDefaults.segmentedShapes(2, 3),
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                    supportingContent = { Text("MIT") },
                    content = { Text("License") },
                )
            }
        }
    }

    if (confirmAction != null) {
        AlertDialog(
            onDismissRequest = { confirmAction = null },
            title = { Text("Clear ${confirmAction}?") },
            text = { Text("This removes only local app data and cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    if (confirmAction == "bookmarks") onClearBookmarks() else onClearRecent()
                    confirmAction = null
                }) { Text("Clear") }
            },
            dismissButton = { TextButton(onClick = { confirmAction = null }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
            content()
        }
    }
}
