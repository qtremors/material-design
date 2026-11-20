package dev.qtremors.material.feature.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.catalog.CatalogKind
import dev.qtremors.material.core.designsystem.MetricTextStyle
import dev.qtremors.material.core.designsystem.components.BadgePalette
import dev.qtremors.material.core.designsystem.components.ConnectedGroup
import dev.qtremors.material.core.designsystem.components.ConnectedItem
import dev.qtremors.material.core.designsystem.components.NavigationRow
import dev.qtremors.material.core.designsystem.components.SectionHeader
import dev.qtremors.material.core.designsystem.components.categoryIcon
import dev.qtremors.material.core.designsystem.components.rememberAppHaptics
import dev.qtremors.material.core.designsystem.spacing
import dev.qtremors.material.core.ui.CatalogEntryRow
import dev.qtremors.material.core.ui.EntryWithMenu
import dev.qtremors.material.core.ui.isExpressive

private data class ExploreSections(
    val featured: List<CatalogEntry>,
    val bookmarked: List<CatalogEntry>,
    val recent: List<CatalogEntry>,
    val categories: List<Pair<String, Int>>,
    val componentCount: Int,
    val foundationCount: Int,
    val apiSymbolCount: Int,
    val expressiveCount: Int,
    val latestRelease: String?,
    val recentlyAdded: List<CatalogEntry>,
)

/** Compares `x.y.z` versions numerically, so 2.0.10 sorts after 2.0.9. */
private val VersionOrder = compareBy<String>({ it.split('.').getOrNull(0)?.toIntOrNull() ?: 0 }, { it.split('.').getOrNull(1)?.toIntOrNull() ?: 0 }, { it.split('.').getOrNull(2)?.toIntOrNull() ?: 0 })

/**
 * The hub: what the reference holds, featured references in the official Expressive carousel,
 * the person's bookmarks and history, categories, and what the latest release added.
 */
@Composable
fun ExploreScreen(
    entries: List<CatalogEntry>,
    bookmarkedIds: Set<String>,
    recentIds: List<String>,
    onEntryClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit = {},
    onAllComponentsClick: () -> Unit = {},
    onExpressiveClick: () -> Unit = {},
    onFoundationsClick: () -> Unit = {},
    onApisClick: () -> Unit = {},
    onBookmarkClick: (String, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val sections = remember(entries, bookmarkedIds, recentIds) {
        val byId = entries.associateBy(CatalogEntry::id)
        val components = entries.filter { it.kind == CatalogKind.COMPONENT }
        val latest = entries.map(CatalogEntry::addedIn).maxWithOrNull(VersionOrder)
        ExploreSections(
            featured = entries.filter { "featured" in it.collections },
            bookmarked = entries.filter { it.id in bookmarkedIds },
            recent = recentIds.mapNotNull(byId::get).take(5),
            categories = components.groupingBy(CatalogEntry::category).eachCount().toList().sortedBy { it.first },
            componentCount = components.size,
            foundationCount = entries.size - components.size,
            apiSymbolCount = entries.flatMap { entry -> entry.apiReferences.map { it.symbol } }.distinct().size,
            expressiveCount = entries.count { it.isExpressive },
            latestRelease = latest,
            recentlyAdded = entries.filter { it.addedIn == latest },
        )
    }

    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
        item(key = "hero") {
            Hero(
                sections = sections,
                onComponentsClick = onAllComponentsClick,
                onFoundationsClick = onFoundationsClick,
                onApisClick = onApisClick,
            )
        }

        if (sections.featured.isNotEmpty()) {
            item(key = "featured-header") { SectionHeader(stringResource(R.string.explore_section_featured)) }
            item(key = "featured") { FeaturedCarousel(sections.featured, onEntryClick) }
        }

        if (sections.bookmarked.isNotEmpty()) {
            item(key = "bookmarks-header") { SectionHeader(stringResource(R.string.explore_section_bookmarks)) }
            itemsIndexed(sections.bookmarked, key = { _, entry -> "bookmark-${entry.id}" }) { index, entry ->
                ConnectedItem(index, sections.bookmarked.size) {
                    EntryWithMenu(
                        entry = entry,
                        bookmarked = entry.id in bookmarkedIds,
                        onOpen = { onEntryClick(entry.id) },
                        onBookmark = { onBookmarkClick(entry.id, it) },
                    ) { openMenu ->
                        CatalogEntryRow(entry, onClick = { onEntryClick(entry.id) }, onLongClick = openMenu, showSummary = false, shareBadge = false)
                    }
                }
            }
        }

        if (sections.recent.isNotEmpty()) {
            item(key = "recent-header") { SectionHeader(stringResource(R.string.explore_section_recently_viewed)) }
            itemsIndexed(sections.recent, key = { _, entry -> "recent-${entry.id}" }) { index, entry ->
                ConnectedItem(index, sections.recent.size) {
                    EntryWithMenu(
                        entry = entry,
                        bookmarked = entry.id in bookmarkedIds,
                        onOpen = { onEntryClick(entry.id) },
                        onBookmark = { onBookmarkClick(entry.id, it) },
                    ) { openMenu ->
                        CatalogEntryRow(entry, onClick = { onEntryClick(entry.id) }, onLongClick = openMenu, showSummary = false, shareBadge = false)
                    }
                }
            }
        }

        item(key = "categories-header") { SectionHeader(stringResource(R.string.explore_section_categories)) }
        item(key = "categories") {
            ConnectedGroup {
                NavigationRow(
                    title = stringResource(R.string.explore_section_expressive),
                    supporting = stringResource(R.string.explore_expressive_supporting),
                    icon = Icons.Rounded.AutoAwesome,
                    badgeKey = "expressive",
                    trailingText = pluralStringResource(R.plurals.explore_expressive_count, sections.expressiveCount, sections.expressiveCount),
                    onClick = onExpressiveClick,
                )
                sections.categories.forEach { (category, count) ->
                    NavigationRow(
                        title = category,
                        icon = categoryIcon(category),
                        badgeKey = category,
                        trailingText = pluralStringResource(R.plurals.explore_category_count, count, count),
                        onClick = { onCategoryClick(category) },
                    )
                }
            }
        }

        if (sections.latestRelease != null && sections.recentlyAdded.isNotEmpty()) {
            item(key = "added-header") {
                SectionHeader(stringResource(R.string.explore_section_recently_added, sections.latestRelease))
            }
            itemsIndexed(sections.recentlyAdded, key = { _, entry -> "added-${entry.id}" }) { index, entry ->
                ConnectedItem(index, sections.recentlyAdded.size) {
                    EntryWithMenu(
                        entry = entry,
                        bookmarked = entry.id in bookmarkedIds,
                        onOpen = { onEntryClick(entry.id) },
                        onBookmark = { onBookmarkClick(entry.id, it) },
                    ) { openMenu ->
                        CatalogEntryRow(entry, onClick = { onEntryClick(entry.id) }, onLongClick = openMenu, shareBadge = false)
                    }
                }
            }
        }
    }
}

/** What the reference holds, as three tappable measures under a one-line promise. */
@Composable
private fun Hero(
    sections: ExploreSections,
    onComponentsClick: () -> Unit,
    onFoundationsClick: () -> Unit,
    onApisClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    Surface(
        color = colors.primaryContainer,
        contentColor = colors.onPrimaryContainer,
        shape = MaterialTheme.shapes.extraLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = MaterialTheme.spacing.small),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            modifier = Modifier.padding(MaterialTheme.spacing.space20),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .background(colors.primary, MaterialShapes.Cookie9Sided.toShape()),
                ) {
                    Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = colors.onPrimary)
                }
                Spacer(Modifier.size(MaterialTheme.spacing.space12))
                Text(stringResource(R.string.explore_intro), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
                Metric(sections.componentCount, stringResource(R.string.explore_metric_components), onComponentsClick, Modifier.weight(1f))
                Metric(sections.foundationCount, stringResource(R.string.explore_metric_foundations), onFoundationsClick, Modifier.weight(1f))
                Metric(sections.apiSymbolCount, stringResource(R.string.explore_metric_apis), onApisClick, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun Metric(value: Int, label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val haptics = rememberAppHaptics()
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceBright.copy(alpha = 0.55f), MaterialTheme.shapes.large)
            .clickable(role = Role.Button, onClickLabel = label) {
                haptics.tap()
                onClick()
            }
            .padding(horizontal = MaterialTheme.spacing.space12, vertical = MaterialTheme.spacing.small),
    ) {
        Text(value.toString(), style = MetricTextStyle, color = MaterialTheme.colorScheme.onSurface)
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
    }
}

/**
 * Featured references in Material's multi-browse carousel: one large item, smaller ones beside
 * it. Item text fades with the item's width so it never gets cut mid-word.
 */
@Composable
private fun FeaturedCarousel(entries: List<CatalogEntry>, onEntryClick: (String) -> Unit) {
    val dark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val state = rememberCarouselState { entries.size }
    val haptics = rememberAppHaptics()
    HorizontalMultiBrowseCarousel(
        state = state,
        preferredItemWidth = 216.dp,
        itemSpacing = MaterialTheme.spacing.small,
        modifier = Modifier
            .fillMaxWidth()
            .height(184.dp),
    ) { index ->
        val entry = entries[index]
        // Keyed by entry, so neighboring cards differ even when they share a category.
        val badge = remember(entry.id, dark) { BadgePalette.forKey(entry.id, dark) }
        val description = stringResource(R.string.explore_open_entry_content_description, entry.officialName)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .maskClip(MaterialTheme.shapes.extraLarge)
                .background(badge.container)
                .clickable(role = Role.Button) {
                    haptics.tap()
                    onEntryClick(entry.id)
                }
                .semantics { contentDescription = description }
                .padding(MaterialTheme.spacing.medium),
        ) {
            Icon(categoryIcon(entry.category), contentDescription = null, tint = badge.content, modifier = Modifier.size(28.dp))
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .graphicsLayer {
                        // Text shows only on items near full width, so it never appears cut mid-word.
                        val info = carouselItemDrawInfo
                        val fadeStart = info.maxSize * 0.8f
                        val range = (info.maxSize - fadeStart).coerceAtLeast(1f)
                        alpha = ((info.size - fadeStart) / range).coerceIn(0f, 1f)
                    },
            ) {
                Text(
                    entry.officialName,
                    style = MaterialTheme.typography.titleLarge,
                    color = badge.content,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
                Text(
                    entry.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = badge.content.copy(alpha = 0.8f),
                    maxLines = 1,
                )
            }
        }
    }
}
