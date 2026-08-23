package dev.qtremors.material.feature.settings

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.AccentColor
import dev.qtremors.material.core.designsystem.buildMonochromeScheme
import dev.qtremors.material.core.designsystem.buildScheme

fun displayedAccentColors(): List<AccentColor> = buildList {
    add(AccentColor.DYNAMIC)
    add(AccentColor.MONOCHROME)
    AccentColor.entries
        .filter { it != AccentColor.DYNAMIC && it != AccentColor.MONOCHROME }
        .forEach(::add)
}

fun accentLabelRes(accent: AccentColor): Int = when (accent) {
    AccentColor.DYNAMIC -> R.string.settings_accent_dynamic
    AccentColor.MONOCHROME -> R.string.settings_accent_monochrome
    AccentColor.RED -> R.string.settings_accent_red
    AccentColor.PINK -> R.string.settings_accent_pink
    AccentColor.PURPLE -> R.string.settings_accent_purple
    AccentColor.DEEP_PURPLE -> R.string.settings_accent_deep_purple
    AccentColor.CYAN -> R.string.settings_accent_cyan
    AccentColor.LIGHT_BLUE -> R.string.settings_accent_light_blue
    AccentColor.BLUE -> R.string.settings_accent_blue
    AccentColor.INDIGO -> R.string.settings_accent_indigo
    AccentColor.TEAL -> R.string.settings_accent_teal
    AccentColor.GREEN -> R.string.settings_accent_green
    AccentColor.LIGHT_GREEN -> R.string.settings_accent_light_green
    AccentColor.LIME -> R.string.settings_accent_lime
    AccentColor.DEEP_ORANGE -> R.string.settings_accent_deep_orange
    AccentColor.ORANGE -> R.string.settings_accent_orange
    AccentColor.AMBER -> R.string.settings_accent_amber
    AccentColor.YELLOW -> R.string.settings_accent_yellow
    AccentColor.BROWN -> R.string.settings_accent_brown
    AccentColor.BLUE_GREY -> R.string.settings_accent_blue_grey
    AccentColor.GREY -> R.string.settings_accent_grey
    AccentColor.BLACK -> R.string.settings_accent_black
}

@Composable
fun accentLabel(accent: AccentColor): String = stringResource(accentLabelRes(accent))

@Composable
private fun getAccentDisplayColor(accent: AccentColor): Color {
    val isDark = isSystemInDarkTheme()
    return when (accent) {
        AccentColor.DYNAMIC -> MaterialTheme.colorScheme.primary
        AccentColor.MONOCHROME -> if (isDark) Color.White else Color.Black
        else -> accent.color ?: Color.Gray
    }
}

@Composable
private fun resolvePreviewColorScheme(
    accent: AccentColor,
    currentAccent: AccentColor,
): ColorScheme {
    val currentScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()

    if (accent == currentAccent) {
        return currentScheme
    }

    return remember(accent, isDark) {
        when {
            accent == AccentColor.MONOCHROME -> buildMonochromeScheme(isDark = isDark, isOled = false)
            accent == AccentColor.DYNAMIC && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            accent != AccentColor.DYNAMIC -> {
                val primaryColor = accent.color ?: Color(0xFF2196F3)
                buildScheme(primary = primaryColor, isDark = isDark)
            }
            else -> currentScheme
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccentColorSelector(
    currentAccent: AccentColor,
    onAccentSelected: (AccentColor) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPicker by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }
    val allAccents = remember { displayedAccentColors() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.settings_accent_color_header),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            IconButton(
                onClick = { showPicker = true },
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = stringResource(R.string.settings_select_accent_color_content_description),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(allAccents, key = { it.name }) { accent ->
                val isSelected = currentAccent == accent
                val displayColor = getAccentDisplayColor(accent)
                val label = accentLabel(accent)

                val animatedCornerRadius by animateDpAsState(
                    targetValue = if (isSelected) 14.dp else 26.dp,
                    animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
                    label = "inlineCornerRadius",
                )
                val animatedScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.08f else 1f,
                    animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
                    label = "inlineScale",
                )

                val isDark = isSystemInDarkTheme()
                val animatedBorderColor by animateColorAsState(
                    targetValue = if (isSelected) {
                        if (isDark) {
                            if (displayColor.luminance() > 0.6f) Color.Black.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.4f)
                        } else {
                            if (displayColor.luminance() > 0.6f) Color.Black.copy(alpha = 0.6f) else displayColor
                        }
                    } else {
                        displayColor.copy(alpha = 0.15f)
                    },
                    animationSpec = spring(stiffness = 300f),
                    label = "inlineBorder",
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .scale(animatedScale)
                        .size(50.dp)
                        .clip(RoundedCornerShape(animatedCornerRadius))
                        .background(displayColor)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = animatedBorderColor,
                            shape = RoundedCornerShape(animatedCornerRadius),
                        )
                        .combinedClickable(
                            onClick = { onAccentSelected(accent) },
                            onLongClick = { showPicker = true },
                        )
                        .semantics {
                            selected = isSelected
                            contentDescription = label
                        },
                ) {
                    when (accent) {
                        AccentColor.DYNAMIC -> {
                            Icon(
                                Icons.Default.ColorLens,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                        AccentColor.MONOCHROME -> {
                            Icon(
                                Icons.Default.Contrast,
                                contentDescription = null,
                                tint = if (isSystemInDarkTheme()) Color.Black else Color.White,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                        else -> {
                            if (isSelected) {
                                val iconTint = if (displayColor.luminance() > 0.5f) Color.Black else Color.White
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = iconTint,
                                    modifier = Modifier.size(22.dp),
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showPicker) {
            AccentColorPickerSheet(
                currentAccent = currentAccent,
                onAccentSelected = onAccentSelected,
                onDismiss = { showPicker = false },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccentColorPickerSheet(
    currentAccent: AccentColor,
    onAccentSelected: (AccentColor) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.settings_select_accent_color_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.settings_action_done), style = MaterialTheme.typography.labelLarge)
                }
            }

            LiveAccentPreviewCard(
                accent = currentAccent,
                currentAccent = currentAccent,
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(R.string.settings_system_themes),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SpecialAccentItem(
                    title = stringResource(R.string.settings_accent_dynamic_short),
                    subtitle = stringResource(R.string.settings_accent_dynamic_subtitle),
                    icon = Icons.Default.ColorLens,
                    isSelected = currentAccent == AccentColor.DYNAMIC,
                    onClick = { onAccentSelected(AccentColor.DYNAMIC) },
                    modifier = Modifier.weight(1f),
                )
                SpecialAccentItem(
                    title = stringResource(R.string.settings_accent_monochrome),
                    subtitle = stringResource(R.string.settings_accent_monochrome_subtitle),
                    icon = Icons.Default.Contrast,
                    isSelected = currentAccent == AccentColor.MONOCHROME,
                    onClick = { onAccentSelected(AccentColor.MONOCHROME) },
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(12.dp))

            AccentCategorySection(
                title = stringResource(R.string.settings_accent_category_warm),
                colors = listOf(
                    AccentColor.RED, AccentColor.PINK, AccentColor.DEEP_ORANGE,
                    AccentColor.ORANGE, AccentColor.AMBER, AccentColor.YELLOW,
                ),
                currentAccent = currentAccent,
                onSelect = onAccentSelected,
            )

            Spacer(modifier = Modifier.height(12.dp))

            AccentCategorySection(
                title = stringResource(R.string.settings_accent_category_cool),
                colors = listOf(
                    AccentColor.BLUE, AccentColor.LIGHT_BLUE, AccentColor.CYAN,
                    AccentColor.INDIGO, AccentColor.PURPLE, AccentColor.DEEP_PURPLE,
                ),
                currentAccent = currentAccent,
                onSelect = onAccentSelected,
            )

            Spacer(modifier = Modifier.height(12.dp))

            AccentCategorySection(
                title = stringResource(R.string.settings_accent_category_nature),
                colors = listOf(
                    AccentColor.TEAL, AccentColor.GREEN, AccentColor.LIGHT_GREEN, AccentColor.LIME,
                ),
                currentAccent = currentAccent,
                onSelect = onAccentSelected,
            )

            Spacer(modifier = Modifier.height(12.dp))

            AccentCategorySection(
                title = stringResource(R.string.settings_accent_category_earth),
                colors = listOf(
                    AccentColor.BROWN, AccentColor.BLUE_GREY, AccentColor.GREY, AccentColor.BLACK,
                ),
                currentAccent = currentAccent,
                onSelect = onAccentSelected,
            )
        }
    }
}

@Composable
fun LiveAccentPreviewCard(
    accent: AccentColor,
    currentAccent: AccentColor,
    modifier: Modifier = Modifier,
) {
    val scheme = resolvePreviewColorScheme(accent = accent, currentAccent = currentAccent)

    val primary by animateColorAsState(scheme.primary, spring(stiffness = 300f), label = "prevPrimary")
    val onPrimary by animateColorAsState(scheme.onPrimary, spring(stiffness = 300f), label = "prevOnPrimary")
    val primaryContainer by animateColorAsState(scheme.primaryContainer, spring(stiffness = 300f), label = "prevPrimaryContainer")
    val onPrimaryContainer by animateColorAsState(scheme.onPrimaryContainer, spring(stiffness = 300f), label = "prevOnPrimaryContainer")
    val secondaryContainer by animateColorAsState(scheme.secondaryContainer, spring(stiffness = 300f), label = "prevSecondaryContainer")
    val surfaceContainer by animateColorAsState(scheme.surfaceContainer, spring(stiffness = 300f), label = "prevSurfaceContainer")
    val surfaceContainerHigh by animateColorAsState(scheme.surfaceContainerHigh, spring(stiffness = 300f), label = "prevSurfaceContainerHigh")
    val onSurface by animateColorAsState(scheme.onSurface, spring(stiffness = 300f), label = "prevOnSurface")

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = onPrimary,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.settings_preview_gallery),
                        style = MaterialTheme.typography.titleSmall,
                        color = onSurface,
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = primaryContainer,
                ) {
                    Text(
                        text = accentLabel(accent),
                        style = MaterialTheme.typography.labelSmall,
                        color = onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                color = surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(primaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = onPrimaryContainer,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.settings_preview_components_controls),
                            style = MaterialTheme.typography.labelMedium,
                            color = onSurface,
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = primary,
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = onPrimary,
                                modifier = Modifier.size(12.dp),
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp)),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(primary))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(primaryContainer))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(secondaryContainer))
                Box(modifier = Modifier.weight(1f).fillMaxHeight().background(surfaceContainerHigh))
            }
        }
    }
}

@Composable
private fun AccentCategorySection(
    title: String,
    colors: List<AccentColor>,
    currentAccent: AccentColor,
    onSelect: (AccentColor) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            colors.forEach { accent ->
                ExpressiveColorSwatch(
                    accent = accent,
                    isSelected = currentAccent == accent,
                    onSelect = { onSelect(accent) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun ExpressiveColorSwatch(
    accent: AccentColor,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayColor = accent.color ?: Color.Gray
    val label = accentLabel(accent)
    val isDark = isSystemInDarkTheme()

    val cornerRadius by animateDpAsState(
        targetValue = if (isSelected) 12.dp else 22.dp,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "swatchCornerRadius",
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "swatchScale",
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            if (isDark) {
                if (displayColor.luminance() > 0.6f) Color.Black.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.4f)
            } else {
                if (displayColor.luminance() > 0.6f) Color.Black.copy(alpha = 0.6f) else displayColor
            }
        } else {
            displayColor.copy(alpha = 0.2f)
        },
        animationSpec = spring(stiffness = 300f),
        label = "swatchBorder",
    )

    val shape = RoundedCornerShape(cornerRadius)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(shape)
            .background(displayColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = shape,
            )
            .clickable { onSelect() }
            .semantics {
                contentDescription = label
                selected = isSelected
            },
    ) {
        AnimatedVisibility(
            visible = isSelected,
            enter = scaleIn(spring(dampingRatio = 0.6f, stiffness = 500f)) + fadeIn(),
            exit = scaleOut() + fadeOut(),
        ) {
            val iconTint = if (displayColor.luminance() > 0.5f) Color.Black else Color.White
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (displayColor.luminance() > 0.6f) Color.Black.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@Composable
fun SpecialAccentItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedBg by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
        animationSpec = spring(stiffness = 300f),
        label = "specialBg",
    )

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = animatedBg,
        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)) else null,
        modifier = modifier.clickable { onClick() },
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }
    }
}
