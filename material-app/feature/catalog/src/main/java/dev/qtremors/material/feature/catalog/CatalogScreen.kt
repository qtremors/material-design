package dev.qtremors.material.feature.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.designsystem.components.ConnectedItem
import dev.qtremors.material.core.designsystem.components.EmptyState
import dev.qtremors.material.core.designsystem.components.EntryRow
import dev.qtremors.material.core.designsystem.components.entryBadgeKey
import dev.qtremors.material.core.designsystem.components.SectionHeader
import dev.qtremors.material.core.designsystem.components.categoryIcon
import dev.qtremors.material.core.designsystem.components.rememberAppHaptics
import dev.qtremors.material.core.designsystem.spacing
import dev.qtremors.material.core.ui.EntryWithMenu
import dev.qtremors.material.core.ui.entryTags

/**
 * Every working component, filterable by category and grouped under category headers. Long-press
 * an entry for its menu; bookmarked entries carry a filled bookmark.
 */
@Composable
fun CatalogScreen(
    entries: List<CatalogEntry>,
    selectedCategory: String?,
    bookmarkedIds: Set<String>,
    onCategorySelected: (String?) -> Unit,
    onEntryClick: (String) -> Unit,
    onBookmarkClick: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    allCategories: List<String> = emptyList(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val groups = remember(entries) { entries.groupBy(CatalogEntry::category).toSortedMap() }
    val haptics = rememberAppHaptics()

    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
        item(key = "filters") {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
            ) {
                item {
                    CategoryChip(stringResource(R.string.catalog_filter_all), selected = selectedCategory == null) {
                        haptics.tick()
                        onCategorySelected(null)
                    }
                }
                items(allCategories) { category ->
                    CategoryChip(category, selected = category == selectedCategory) {
                        haptics.tick()
                        onCategorySelected(if (category == selectedCategory) null else category)
                    }
                }
            }
        }

        if (entries.isEmpty()) {
            item(key = "empty") {
                EmptyState(
                    icon = Icons.Rounded.SearchOff,
                    title = stringResource(R.string.catalog_empty_title),
                    message = stringResource(R.string.catalog_empty_body),
                )
            }
        }

        groups.forEach { (category, categoryEntries) ->
            item(key = "header-$category") {
                SectionHeader(
                    text = category,
                    trailing = {
                        Text(
                            pluralStringResource(R.plurals.catalog_count, categoryEntries.size, categoryEntries.size),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    },
                )
            }
            categoryEntries.forEachIndexed { index, entry ->
                item(key = entry.id) {
                    ConnectedItem(index, categoryEntries.size, modifier = Modifier.animateItem()) {
                        CatalogRow(
                            entry = entry,
                            bookmarked = entry.id in bookmarkedIds,
                            onOpen = { onEntryClick(entry.id) },
                            onBookmark = { onBookmarkClick(entry.id, it) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = if (selected) {
            { Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
        } else {
            { Icon(categoryIcon(label), contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
        },
        shape = MaterialTheme.shapes.small,
    )
}

/** One entry with the shared long-press menu; bookmarked entries carry a filled bookmark. */
@Composable
private fun CatalogRow(entry: CatalogEntry, bookmarked: Boolean, onOpen: () -> Unit, onBookmark: (Boolean) -> Unit) {
    EntryWithMenu(entry = entry, bookmarked = bookmarked, onOpen = onOpen, onBookmark = onBookmark) { openMenu ->
        EntryRow(
            title = entry.officialName,
            category = entry.category,
            supporting = entry.summary,
            tags = entryTags(entry),
            onClick = onOpen,
            onLongClick = openMenu,
            sharedKey = entryBadgeKey(entry.id),
            trailing = if (bookmarked) {
                {
                    Icon(
                        Icons.Rounded.Bookmark,
                        contentDescription = stringResource(R.string.catalog_bookmarked),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                null
            },
        )
    }
}
