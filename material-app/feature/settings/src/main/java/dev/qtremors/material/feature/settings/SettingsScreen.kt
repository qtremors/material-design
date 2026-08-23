package dev.qtremors.material.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MotionPhotosOff
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.data.AppSettings
import dev.qtremors.material.core.designsystem.MaterialListSurface
import dev.qtremors.material.core.designsystem.ThemePreset
import dev.qtremors.material.core.designsystem.ThemeState
import dev.qtremors.material.core.designsystem.expressiveSegmentedShapes

/** Destructive library actions that require confirmation. */
private enum class ConfirmAction { BOOKMARKS, RECENT }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    appVersion: String,
    material3Version: String,
    bookmarkCount: Int,
    recentCount: Int,
    onThemeStateChange: (ThemeState) -> Unit,
    onClearBookmarks: () -> Unit,
    onClearRecent: () -> Unit,
    onNavigateToAbout: () -> Unit = {},
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var confirmAction by rememberSaveable { mutableStateOf<ConfirmAction?>(null) }
    val theme = settings.themeState
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp),
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.95f),
                ),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Appearance Section
            item {
                SettingsSection(title = "Appearance") {
                    MaterialListSurface {
                        ThemeModeSelector(
                            currentMode = theme.themeMode,
                            onModeSelected = { onThemeStateChange(theme.copy(themeMode = it)) },
                        )
                    }

                    MaterialListSurface {
                        ThemePresetSelector(
                            currentPreset = theme.themePreset,
                            onPresetSelected = { onThemeStateChange(theme.copy(themePreset = it)) },
                        )
                    }

                    if (theme.themePreset == ThemePreset.CUSTOM) {
                        MaterialListSurface {
                            CustomThemeCreatorPanel(
                                themeState = theme,
                                onThemeChange = onThemeStateChange,
                            )
                        }
                    }

                    if (theme.themePreset == ThemePreset.NONE) {
                        MaterialListSurface {
                            AccentColorSelector(
                                currentAccent = theme.accentColor,
                                onAccentSelected = { onThemeStateChange(theme.copy(accentColor = it)) },
                            )
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
                        SettingsSwitchRow(
                            index = 0,
                            count = 2,
                            title = "Harmonize Colors",
                            description = "Blend category and status colors with the active theme accent color.",
                            checked = theme.harmonizeColors,
                            leadingIcon = Icons.Default.Palette,
                            onCheckedChange = { onThemeStateChange(theme.copy(harmonizeColors = it)) },
                        )

                        SettingsSwitchRow(
                            index = 1,
                            count = 2,
                            title = "Vibrations",
                            description = "Enable haptic feedback on actions and gestures.",
                            checked = theme.vibrationsEnabled,
                            leadingIcon = Icons.Default.Vibration,
                            onCheckedChange = { onThemeStateChange(theme.copy(vibrationsEnabled = it)) },
                        )
                    }
                }
            }

            // Browsing & Catalog Section
            item {
                SettingsSection(title = "Browsing & Layout") {
                    Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
                        SettingsSwitchRow(
                            index = 0,
                            count = 2,
                            title = "Expandable App Bars",
                            description = "Expand and collapse large top app bars smoothly during scroll.",
                            checked = theme.expandableAppBar,
                            leadingIcon = Icons.Default.Expand,
                            onCheckedChange = { onThemeStateChange(theme.copy(expandableAppBar = it)) },
                        )

                        SettingsSwitchRow(
                            index = 1,
                            count = 2,
                            title = "Catalog Badges",
                            description = "Display M3 Expressive, experimental, and API maturity chips on component cards.",
                            checked = theme.showBadges,
                            leadingIcon = Icons.Default.AutoAwesome,
                            onCheckedChange = { onThemeStateChange(theme.copy(showBadges = it)) },
                        )
                    }
                }
            }

            // Motion Section
            item {
                SettingsSection(title = "Motion") {
                    Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
                        SettingsSwitchRow(
                            index = 0,
                            count = 1,
                            title = "Reduce App Motion",
                            description = "Use instantaneous snap transitions instead of bouncy spring physics.",
                            checked = theme.reducedMotion,
                            leadingIcon = Icons.Default.MotionPhotosOff,
                            onCheckedChange = { onThemeStateChange(theme.copy(reducedMotion = it)) },
                        )
                    }
                }
            }

            // Library & History Section
            item {
                SettingsSection(title = "Library & History") {
                    Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
                        SegmentedListItem(
                            onClick = { confirmAction = ConfirmAction.BOOKMARKS },
                            enabled = bookmarkCount > 0,
                            shapes = expressiveSegmentedShapes(index = 0, count = 2),
                            leadingContent = {
                                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = if (bookmarkCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                    )
                                }
                            },
                            supportingContent = { Text("$bookmarkCount saved components") },
                            trailingContent = {
                                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.DeleteSweep,
                                        contentDescription = null,
                                        tint = if (bookmarkCount > 0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                                    )
                                }
                            },
                            colors = ListItemDefaults.segmentedColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            ),
                            content = { Text("Clear bookmarks") },
                            modifier = Modifier.height(IntrinsicSize.Min),
                        )
                        SegmentedListItem(
                            onClick = { confirmAction = ConfirmAction.RECENT },
                            enabled = recentCount > 0,
                            shapes = expressiveSegmentedShapes(index = 1, count = 2),
                            leadingContent = {
                                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.History,
                                        contentDescription = null,
                                        tint = if (recentCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                    )
                                }
                            },
                            supportingContent = { Text("$recentCount recently viewed components") },
                            trailingContent = {
                                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.DeleteSweep,
                                        contentDescription = null,
                                        tint = if (recentCount > 0) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                                    )
                                }
                            },
                            colors = ListItemDefaults.segmentedColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            ),
                            content = { Text("Clear recent history") },
                            modifier = Modifier.height(IntrinsicSize.Min),
                        )
                    }
                }
            }

            // Info Section
            item {
                SettingsSection(title = "Info") {
                    Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
                        SegmentedListItem(
                            onClick = onNavigateToAbout,
                            shapes = expressiveSegmentedShapes(index = 0, count = 1),
                            leadingContent = {
                                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            supportingContent = { Text("Version $appVersion · M3 Compose $material3Version · Repository") },
                            colors = ListItemDefaults.segmentedColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            ),
                            content = { Text("About Material Design") },
                            modifier = Modifier.height(IntrinsicSize.Min),
                        )
                    }
                }
            }
        }
    }

    if (confirmAction != null) {
        val isBookmarksAction = confirmAction == ConfirmAction.BOOKMARKS
        AlertDialog(
            onDismissRequest = { confirmAction = null },
            title = { Text("Clear ${if (isBookmarksAction) "bookmarks" else "recent history"}?") },
            text = { Text("This removes only local app data and cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    if (isBookmarksAction) onClearBookmarks() else onClearRecent()
                    confirmAction = null
                }) { Text("Clear") }
            },
            dismissButton = { TextButton(onClick = { confirmAction = null }) { Text("Cancel") } },
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SettingsSwitchRow(
    index: Int = 0,
    count: Int = 1,
    title: String,
    description: String,
    checked: Boolean,
    leadingIcon: ImageVector? = null,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
) {
    SegmentedListItem(
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        shapes = expressiveSegmentedShapes(index = index, count = count),
        leadingContent = if (leadingIcon != null) {
            {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        } else null,
        content = { Text(title) },
        supportingContent = { Text(description) },
        trailingContent = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                dev.qtremors.material.core.designsystem.ExpressiveSwitch(
                    checked = checked,
                    onCheckedChange = null,
                    enabled = enabled,
                )
            }
        },
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = Modifier.height(IntrinsicSize.Min),
    )
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp, top = 4.dp),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = { content() },
        )
    }
}
