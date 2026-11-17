package dev.qtremors.material.feature.detail

import android.content.ClipData
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Accessibility
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.ApiReference
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.catalog.CatalogKind
import dev.qtremors.material.core.catalog.ImplementationKind
import dev.qtremors.material.core.catalog.SourceLocation
import dev.qtremors.material.core.designsystem.CodeTextStyle
import dev.qtremors.material.core.designsystem.LocalReducedMotion
import dev.qtremors.material.core.designsystem.components.AppScaffold
import dev.qtremors.material.core.designsystem.components.ConnectedChoices
import dev.qtremors.material.core.designsystem.components.ConnectedGroup
import dev.qtremors.material.core.designsystem.components.ConnectedRow
import dev.qtremors.material.core.designsystem.components.IconBadge
import dev.qtremors.material.core.designsystem.components.SectionHeader
import dev.qtremors.material.core.designsystem.components.TagRow
import dev.qtremors.material.core.designsystem.components.rememberAppHaptics
import dev.qtremors.material.core.designsystem.spacing
import dev.qtremors.material.core.ui.apiTags
import dev.qtremors.material.core.ui.entryTags
import kotlinx.coroutines.launch

enum class DetailSection { PREVIEW, GUIDANCE, INSPECT, API }

@Composable
private fun DetailSection.label(): String = when (this) {
    DetailSection.PREVIEW -> stringResource(R.string.detail_tab_preview)
    DetailSection.GUIDANCE -> stringResource(R.string.detail_tab_guidance)
    DetailSection.INSPECT -> stringResource(R.string.detail_tab_inspect)
    DetailSection.API -> stringResource(R.string.detail_tab_api)
}

/**
 * One reference, four jobs: Preview (the working demo), Guidance (when and how to use it),
 * Inspect (what it is and where its source lives), and API (exact symbols and maturity).
 */
@Composable
fun ComponentDetailScreen(
    entry: CatalogEntry,
    selectedSection: DetailSection,
    bookmarked: Boolean,
    onSectionSelected: (DetailSection) -> Unit,
    onBookmarkClick: (Boolean) -> Unit,
    demo: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = rememberAppHaptics()
    val reducedMotion = LocalReducedMotion.current
    val effectsIn = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
    val effectsOut = MaterialTheme.motionScheme.fastEffectsSpec<Float>()

    AppScaffold(
        title = entry.officialName,
        subtitle = entry.category,
        modifier = modifier,
        actions = {
            IconToggleButton(
                checked = bookmarked,
                onCheckedChange = { checked ->
                    haptics.toggle(checked)
                    onBookmarkClick(checked)
                },
                shapes = IconButtonDefaults.toggleableShapes(),
            ) {
                Icon(
                    if (bookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    contentDescription = stringResource(
                        if (bookmarked) R.string.detail_remove_bookmark_content_description else R.string.detail_add_bookmark_content_description,
                    ),
                )
            }
        },
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            item(key = "summary") { Summary(entry) }
            item(key = "sections") {
                ConnectedChoices(
                    options = DetailSection.entries,
                    selected = selectedSection,
                    label = { it.label() },
                    onSelect = onSectionSelected,
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium),
                )
            }
            item(key = "content") {
                AnimatedContent(
                    targetState = selectedSection,
                    transitionSpec = {
                        if (reducedMotion) {
                            EnterTransition.None togetherWith ExitTransition.None
                        } else {
                            fadeIn(effectsIn) togetherWith fadeOut(effectsOut)
                        }
                    },
                    label = "detailSection",
                ) { section ->
                    when (section) {
                        DetailSection.PREVIEW -> PreviewSection(demo)
                        DetailSection.GUIDANCE -> GuidanceSection(entry)
                        DetailSection.INSPECT -> InspectSection(entry)
                        DetailSection.API -> ApiSection(entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun Summary(entry: CatalogEntry) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space12),
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
    ) {
        Text(entry.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        TagRow(entryTags(entry))
        val aliases = entry.aliases.filterNot { it.equals(entry.officialName, ignoreCase = true) }
        if (aliases.isNotEmpty()) {
            Text(
                stringResource(R.string.detail_aliases, aliases.joinToString(", ")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PreviewSection(demo: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.space12)) {
        Text(
            stringResource(R.string.detail_preview_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
        )
        Surface(
            color = MaterialTheme.colorScheme.surfaceBright,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(Modifier.padding(MaterialTheme.spacing.space20)) { demo() }
        }
    }
}

@Composable
private fun GuidanceSection(entry: CatalogEntry) {
    val colors = MaterialTheme.colorScheme
    val guidance = entry.guidance
    Column {
        Surface(color = colors.primaryContainer, contentColor = colors.onPrimaryContainer, shape = MaterialTheme.shapes.extraLarge) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.space20),
            ) {
                Text(stringResource(R.string.detail_guidance_purpose_label), style = MaterialTheme.typography.labelLarge)
                Text(guidance.purpose, style = MaterialTheme.typography.titleMedium)
            }
        }
        GuidanceGroup(R.string.detail_guidance_use_when_title, guidance.useWhen, Icons.Rounded.Check, colors.primary, colors.onPrimary)
        GuidanceGroup(R.string.detail_guidance_avoid_when_title, guidance.avoidWhen, Icons.Rounded.Close, colors.error, colors.onError)
        GuidanceGroup(R.string.detail_guidance_behavior_title, guidance.behavior, Icons.Rounded.Animation, colors.secondaryContainer, colors.onSecondaryContainer)
        GuidanceGroup(R.string.detail_guidance_accessibility_title, guidance.accessibility, Icons.Rounded.Accessibility, colors.secondaryContainer, colors.onSecondaryContainer)
        GuidanceGroup(R.string.detail_guidance_adaptive_title, guidance.adaptive, Icons.Rounded.Devices, colors.secondaryContainer, colors.onSecondaryContainer)
    }
}

/**
 * A titled group of guidance lines, each led by the same small icon. Do and don't read through
 * the icon and the heading together, never through color alone.
 */
@Composable
private fun GuidanceGroup(title: Int, lines: List<String>, icon: ImageVector, iconContainer: Color, iconContent: Color) {
    SectionHeader(stringResource(title))
    ConnectedGroup {
        lines.forEach { line ->
            Surface(color = MaterialTheme.colorScheme.surfaceBright, modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.spacing.rowHorizontal,
                        vertical = MaterialTheme.spacing.rowVertical,
                    ),
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(24.dp)
                            .background(iconContainer, CircleShape),
                    ) {
                        Icon(icon, contentDescription = null, tint = iconContent, modifier = Modifier.size(16.dp))
                    }
                    Spacer(Modifier.width(MaterialTheme.spacing.space12))
                    Text(line, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun InspectSection(entry: CatalogEntry) {
    val clipboard = LocalClipboard.current
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    val haptics = rememberAppHaptics()
    val pathLabel = stringResource(R.string.detail_clip_label_path)
    Column {
        SectionHeader(stringResource(R.string.detail_inspect_classification_title))
        ConnectedGroup {
            ConnectedRow(
                title = stringResource(
                    if (entry.implementation == ImplementationKind.OFFICIAL_API) R.string.detail_inspect_official_api else R.string.detail_inspect_project_implementation,
                ),
                supporting = stringResource(R.string.detail_inspect_implementation_label),
                leading = { IconBadge(Icons.Rounded.MenuBook, key = entry.implementation.name) },
            )
            FactRow(
                R.string.detail_inspect_kind_label,
                stringResource(if (entry.kind == CatalogKind.COMPONENT) R.string.detail_inspect_kind_component else R.string.detail_inspect_kind_foundation),
            )
            FactRow(R.string.detail_inspect_category_label, entry.category)
            FactRow(R.string.detail_inspect_added_label, entry.addedIn)
            FactRow(R.string.detail_inspect_reviewed_label, entry.reviewedOn)
            FactRow(R.string.detail_inspect_id_label, entry.id, code = true)
        }

        SectionHeader(stringResource(R.string.detail_inspect_source_title))
        ConnectedGroup {
            entry.sourceLocations.forEach { source ->
                SourceRow(source) {
                    haptics.confirm()
                    scope.launch { clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(pathLabel, source.path))) }
                }
            }
        }

        if (entry.officialUrls.isNotEmpty()) {
            SectionHeader(stringResource(R.string.detail_inspect_links_title))
            ConnectedGroup {
                entry.officialUrls.forEach { url ->
                    ConnectedRow(
                        title = url.removePrefix("https://").substringBefore('?'),
                        leading = { IconBadge(Icons.AutoMirrored.Rounded.OpenInNew, key = "link") },
                        onClick = { uriHandler.openUri(url) },
                    )
                }
            }
        }
    }
}

/** A label above a value, for facts about the entry. */
@Composable
private fun FactRow(label: Int, value: String, code: Boolean = false) {
    Surface(color = MaterialTheme.colorScheme.surfaceBright, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.rowHorizontal, vertical = MaterialTheme.spacing.space12),
        ) {
            Text(stringResource(label), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = if (code) CodeTextStyle else MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun SourceRow(source: SourceLocation, onCopy: () -> Unit) {
    ConnectedRow(
        title = source.path.substringAfterLast('/'),
        leading = { IconBadge(Icons.Rounded.FolderOpen, key = "source") },
        onClick = onCopy,
        tapHaptic = false,
        trailing = { Icon(Icons.Rounded.ContentCopy, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        supportingContent = {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(source.path, style = CodeTextStyle, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    source.symbols.joinToString(" · "),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    stringResource(R.string.detail_inspect_source_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Composable
private fun ApiSection(entry: CatalogEntry) {
    Column {
        Text(
            stringResource(R.string.detail_api_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
        )
        Spacer(Modifier.height(MaterialTheme.spacing.space12))
        ConnectedGroup {
            entry.apiReferences.forEach { api -> ApiRow(api) }
        }
    }
}

@Composable
private fun ApiRow(api: ApiReference) {
    val clipboard = LocalClipboard.current
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    val haptics = rememberAppHaptics()
    val symbolLabel = stringResource(R.string.detail_clip_label_symbol)
    val simpleName = api.symbol.substringAfterLast('.')
    Surface(color = MaterialTheme.colorScheme.surfaceBright, modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(
                start = MaterialTheme.spacing.rowHorizontal,
                end = MaterialTheme.spacing.extraSmall,
                top = MaterialTheme.spacing.space12,
                bottom = MaterialTheme.spacing.space12,
            ),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall), modifier = Modifier.weight(1f)) {
                Text(simpleName, style = CodeTextStyle.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                Text(
                    stringResource(R.string.detail_artifact_version, api.artifact, api.reviewedVersion),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TagRow(apiTags(api), force = true)
                api.optInAnnotation?.let { optIn ->
                    Text(
                        stringResource(R.string.detail_opt_in_label, optIn.substringAfterLast('.')),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            IconButton(
                onClick = {
                    haptics.confirm()
                    scope.launch { clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(symbolLabel, api.symbol))) }
                },
                shapes = IconButtonDefaults.shapes(),
            ) {
                Icon(Icons.Rounded.ContentCopy, contentDescription = stringResource(R.string.detail_copy_symbol_content_description, simpleName))
            }
            IconButton(onClick = { uriHandler.openUri(api.url) }, shapes = IconButtonDefaults.shapes()) {
                Icon(
                    Icons.AutoMirrored.Rounded.OpenInNew,
                    contentDescription = stringResource(R.string.detail_open_official_api_content_description, simpleName),
                )
            }
        }
    }
}
