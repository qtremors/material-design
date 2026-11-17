package dev.qtremors.material.feature.settings

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.data.AppSettings
import dev.qtremors.material.core.designsystem.AccentSource
import dev.qtremors.material.core.designsystem.FieldShape
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.ThemeMode
import dev.qtremors.material.core.designsystem.ThemePreset
import dev.qtremors.material.core.designsystem.ThemeState
import dev.qtremors.material.core.designsystem.components.AppScaffold
import dev.qtremors.material.core.designsystem.components.ChoiceRow
import dev.qtremors.material.core.designsystem.components.ConnectedGroup
import dev.qtremors.material.core.designsystem.components.ConnectedRow
import dev.qtremors.material.core.designsystem.components.IconBadge
import dev.qtremors.material.core.designsystem.components.NavigationRow
import dev.qtremors.material.core.designsystem.components.SectionHeader
import dev.qtremors.material.core.designsystem.components.ToggleRow
import dev.qtremors.material.core.designsystem.components.rememberAppHaptics
import dev.qtremors.material.core.designsystem.parseHexColor
import dev.qtremors.material.core.designsystem.seedColorScheme
import dev.qtremors.material.core.designsystem.spacing

/** Seeds offered next to the hex field, so a custom scheme is one tap away. */
private val QuickSeeds = listOf(
    "#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
    "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFC107", "#FF9800", "#FF5722", "#795548",
)

private enum class PendingClear { BOOKMARKS, HISTORY }

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
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = settings.themeState
    val update: (ThemeState.() -> ThemeState) -> Unit = { change -> onThemeStateChange(theme.change()) }
    var pendingClear by rememberSaveable { mutableStateOf<PendingClear?>(null) }
    val wallpaperAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val sources = if (wallpaperAvailable) AccentSource.entries else AccentSource.entries - AccentSource.WALLPAPER
    val source = if (!wallpaperAvailable && theme.accentSource == AccentSource.WALLPAPER) AccentSource.PRESET else theme.accentSource

    AppScaffold(title = stringResource(R.string.settings_title), modifier = modifier) { padding ->
        LazyColumn(contentPadding = padding) {
            item { SectionHeader(stringResource(R.string.settings_section_appearance)) }
            item {
                ConnectedGroup {
                    ChoiceRow(
                        title = stringResource(R.string.settings_theme),
                        options = ThemeMode.entries,
                        selected = theme.themeMode,
                        label = { stringResource(it.labelRes) },
                        onSelect = { mode -> update { copy(themeMode = mode) } },
                        icon = Icons.Rounded.Contrast,
                    )
                    ToggleRow(
                        title = stringResource(R.string.settings_pure_black),
                        supporting = stringResource(R.string.settings_pure_black_supporting),
                        checked = theme.pureBlack,
                        onCheckedChange = { on -> update { copy(pureBlack = on) } },
                        icon = Icons.Rounded.DarkMode,
                    )
                    ChoiceRow(
                        title = stringResource(R.string.settings_colors),
                        supporting = stringResource(source.supportingRes),
                        options = sources,
                        selected = source,
                        label = { stringResource(it.labelRes) },
                        onSelect = { picked -> update { copy(accentSource = picked) } },
                        icon = Icons.Rounded.Palette,
                    )
                    AnimatedVisibility(
                        visible = source == AccentSource.PRESET,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut(),
                    ) {
                        PresetPicker(selected = theme.preset, onSelect = { preset -> update { copy(preset = preset) } })
                    }
                    AnimatedVisibility(
                        visible = source == AccentSource.CUSTOM,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut(),
                    ) {
                        CustomSeedEditor(seedHex = theme.customSeedHex, onSeedChange = { hex -> update { copy(customSeedHex = hex) } })
                    }
                    SchemePreview()
                }
            }

            item { SectionHeader(stringResource(R.string.settings_section_behavior)) }
            item {
                ConnectedGroup {
                    ToggleRow(
                        title = stringResource(R.string.settings_haptics),
                        supporting = stringResource(R.string.settings_haptics_supporting),
                        checked = theme.hapticsEnabled,
                        onCheckedChange = { on -> update { copy(hapticsEnabled = on) } },
                        icon = Icons.Rounded.Vibration,
                    )
                    ToggleRow(
                        title = stringResource(R.string.settings_reduce_motion),
                        supporting = stringResource(R.string.settings_reduce_motion_supporting),
                        checked = theme.reducedMotion,
                        onCheckedChange = { on -> update { copy(reducedMotion = on) } },
                        icon = Icons.Rounded.Animation,
                    )
                    ToggleRow(
                        title = stringResource(R.string.settings_badges),
                        supporting = stringResource(R.string.settings_badges_supporting),
                        checked = theme.showBadges,
                        onCheckedChange = { on -> update { copy(showBadges = on) } },
                        icon = Icons.Rounded.Sell,
                    )
                }
            }

            item { SectionHeader(stringResource(R.string.settings_section_library)) }
            item {
                ConnectedGroup {
                    ConnectedRow(
                        title = stringResource(R.string.settings_clear_bookmarks),
                        supporting = pluralStringResource(R.plurals.settings_bookmark_count, bookmarkCount, bookmarkCount),
                        leading = { IconBadge(Icons.Rounded.Bookmarks, key = "bookmarks") },
                        onClick = { pendingClear = PendingClear.BOOKMARKS },
                        enabled = bookmarkCount > 0,
                    )
                    ConnectedRow(
                        title = stringResource(R.string.settings_clear_history),
                        supporting = pluralStringResource(R.plurals.settings_history_count, recentCount, recentCount),
                        leading = { IconBadge(Icons.Rounded.History, key = "history") },
                        onClick = { pendingClear = PendingClear.HISTORY },
                        enabled = recentCount > 0,
                    )
                }
            }

            item { SectionHeader(stringResource(R.string.settings_section_about)) }
            item {
                ConnectedGroup {
                    NavigationRow(
                        title = stringResource(R.string.settings_about_row),
                        supporting = stringResource(R.string.settings_about_supporting, appVersion, material3Version),
                        icon = Icons.Rounded.Info,
                        badgeKey = "about",
                        onClick = onNavigateToAbout,
                    )
                }
            }
        }
    }

    pendingClear?.let { target ->
        ClearDialog(
            title = stringResource(
                if (target == PendingClear.BOOKMARKS) R.string.settings_clear_bookmarks_title else R.string.settings_clear_history_title,
            ),
            confirmLabel = stringResource(
                if (target == PendingClear.BOOKMARKS) R.string.settings_clear_bookmarks else R.string.settings_clear_history,
            ),
            onConfirm = {
                if (target == PendingClear.BOOKMARKS) onClearBookmarks() else onClearRecent()
                pendingClear = null
            },
            onDismiss = { pendingClear = null },
        )
    }
}

private val ThemeMode.labelRes: Int
    get() = when (this) {
        ThemeMode.SYSTEM -> R.string.settings_theme_system
        ThemeMode.LIGHT -> R.string.settings_theme_light
        ThemeMode.DARK -> R.string.settings_theme_dark
    }

private val AccentSource.labelRes: Int
    get() = when (this) {
        AccentSource.WALLPAPER -> R.string.settings_colors_wallpaper
        AccentSource.PRESET -> R.string.settings_colors_preset
        AccentSource.CUSTOM -> R.string.settings_colors_custom
    }

private val AccentSource.supportingRes: Int
    get() = when (this) {
        AccentSource.WALLPAPER -> R.string.settings_colors_wallpaper_supporting
        AccentSource.PRESET -> R.string.settings_colors_preset_supporting
        AccentSource.CUSTOM -> R.string.settings_colors_custom_supporting
    }

private val ThemePreset.labelRes: Int
    get() = when (this) {
        ThemePreset.BASELINE -> R.string.settings_preset_baseline
        ThemePreset.BLUE -> R.string.settings_preset_blue
        ThemePreset.GREEN -> R.string.settings_preset_green
        ThemePreset.AMBER -> R.string.settings_preset_amber
        ThemePreset.CORAL -> R.string.settings_preset_coral
        ThemePreset.GRAPHITE -> R.string.settings_preset_graphite
    }

/** One row of preset swatches, each drawn from the scheme its seed generates. */
@Composable
private fun PresetPicker(selected: ThemePreset, onSelect: (ThemePreset) -> Unit) {
    val dark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    Surface(color = MaterialTheme.colorScheme.surfaceBright, modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space12),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = MaterialTheme.spacing.rowHorizontal,
                vertical = MaterialTheme.spacing.rowVertical,
            ),
        ) {
            items(ThemePreset.entries) { preset ->
                val scheme = remember(preset, dark) { seedColorScheme(preset.seed, dark, preset.monochrome) }
                SchemeSwatch(
                    scheme = scheme,
                    label = stringResource(preset.labelRes),
                    selected = preset == selected,
                    onClick = { onSelect(preset) },
                )
            }
        }
    }
}

/**
 * A swatch showing a scheme's primary, secondary, and tertiary roles. The selected swatch morphs
 * into a soft Expressive shape and carries a check, so selection never relies on color alone.
 */
@Composable
private fun SchemeSwatch(scheme: ColorScheme, label: String, selected: Boolean, onClick: () -> Unit) {
    val haptics = rememberAppHaptics()
    val reduced = LocalReducedMotion.current
    val ringWidth by animateDpAsState(
        targetValue = if (selected) 3.dp else 0.dp,
        animationSpec = if (reduced) androidx.compose.animation.core.snap() else MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "swatchRing",
    )
    val shape = if (selected) MaterialShapes.Cookie9Sided.toShape() else CircleShape
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(role = Role.RadioButton) {
                haptics.tick()
                onClick()
            }
            .semantics {
                this.selected = selected
                contentDescription = label
            }
            .padding(MaterialTheme.spacing.extraSmall),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .border(ringWidth.coerceAtLeast(0.dp), MaterialTheme.colorScheme.onSurface, shape)
                .padding(5.dp)
                .clip(shape),
        ) {
            Column(Modifier.size(56.dp)) {
                Box(Modifier.fillMaxWidth().weight(1f).background(scheme.primary))
                Row(Modifier.fillMaxWidth().weight(1f)) {
                    Box(Modifier.weight(1f).size(28.dp).background(scheme.secondary))
                    Box(Modifier.weight(1f).size(28.dp).background(scheme.tertiary))
                }
            }
            if (selected) {
                Icon(
                    Icons.Rounded.Check,
                    contentDescription = null,
                    tint = scheme.onPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .background(scheme.primary, CircleShape)
                        .padding(3.dp),
                )
            }
        }
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** A hex field with quick seeds; invalid input shows a hint and leaves the scheme unchanged. */
@Composable
private fun CustomSeedEditor(seedHex: String, onSeedChange: (String) -> Unit) {
    var text by rememberSaveable(seedHex) { mutableStateOf(seedHex) }
    val valid = parseHexColor(text) != null && text.removePrefix("#").length == 6
    val haptics = rememberAppHaptics()
    Surface(color = MaterialTheme.colorScheme.surfaceBright, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.rowHorizontal,
                vertical = MaterialTheme.spacing.rowVertical,
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space12),
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { input ->
                    val normalized = "#" + input.removePrefix("#").filter { it.isLetterOrDigit() }.take(6).uppercase()
                    text = normalized
                    if (parseHexColor(normalized) != null && normalized.length == 7) onSeedChange(normalized)
                },
                label = { Text(stringResource(R.string.settings_seed_label)) },
                supportingText = {
                    Text(stringResource(if (valid) R.string.settings_seed_supporting else R.string.settings_seed_invalid))
                },
                isError = !valid,
                singleLine = true,
                leadingIcon = {
                    Box(
                        Modifier
                            .size(24.dp)
                            .background(parseHexColor(seedHex) ?: Color.Transparent, CircleShape),
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done,
                ),
                shape = FieldShape,
                modifier = Modifier.fillMaxWidth(),
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                items(QuickSeeds) { hex ->
                    val color = parseHexColor(hex) ?: Color.Transparent
                    val chosen = hex.equals(seedHex, ignoreCase = true)
                    val description = stringResource(R.string.settings_seed_choice, hex)
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(if (chosen) MaterialShapes.Cookie9Sided.toShape() else CircleShape)
                            .background(color)
                            .clickable(role = Role.RadioButton) {
                                haptics.tick()
                                text = hex
                                onSeedChange(hex)
                            }
                            .semantics {
                                selected = chosen
                                contentDescription = description
                            },
                    ) {
                        if (chosen) Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White)
                    }
                }
            }
        }
    }
}

/** The live scheme's key roles, so a color change is visible before leaving Settings. */
@Composable
private fun SchemePreview() {
    val colors = MaterialTheme.colorScheme
    val roles = listOf(
        Triple(R.string.settings_role_primary, colors.primary, colors.onPrimary),
        Triple(R.string.settings_role_secondary, colors.secondaryContainer, colors.onSecondaryContainer),
        Triple(R.string.settings_role_tertiary, colors.tertiaryContainer, colors.onTertiaryContainer),
        Triple(R.string.settings_role_container, colors.surfaceContainerHighest, colors.onSurface),
    )
    Surface(color = colors.surfaceBright, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.rowHorizontal,
                vertical = MaterialTheme.spacing.rowVertical,
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            Text(
                stringResource(R.string.settings_scheme_preview),
                style = MaterialTheme.typography.labelLarge,
                color = colors.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                roles.forEach { (label, container, content) ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(MaterialTheme.shapes.medium)
                            .background(container)
                            .padding(vertical = MaterialTheme.spacing.space12),
                    ) {
                        Text(
                            stringResource(label),
                            style = MaterialTheme.typography.labelMedium,
                            color = content,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClearDialog(title: String, confirmLabel: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Rounded.DeleteSweep, contentDescription = null) },
        title = { Text(title) },
        text = { Text(stringResource(R.string.settings_clear_message)) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shapes = ButtonDefaults.shapes(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) { Text(confirmLabel) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shapes = ButtonDefaults.shapes()) {
                Text(stringResource(R.string.settings_cancel))
            }
        },
    )
}
