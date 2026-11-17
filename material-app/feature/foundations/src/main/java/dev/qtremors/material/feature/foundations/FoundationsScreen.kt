package dev.qtremors.material.feature.foundations

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.designsystem.components.ConnectedItem
import dev.qtremors.material.core.designsystem.components.EntryRow
import dev.qtremors.material.core.designsystem.components.entryBadgeKey
import dev.qtremors.material.core.designsystem.spacing
import dev.qtremors.material.core.ui.EntryWithMenu

/** The system under every component: color, type, shape, motion, elevation, layout, and accessibility. */
@Composable
fun FoundationsScreen(
    entries: List<CatalogEntry>,
    onEntryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    bookmarkedIds: Set<String> = emptySet(),
    onBookmarkClick: (String, Boolean) -> Unit = { _, _ -> },
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(modifier = modifier.fillMaxSize(), contentPadding = contentPadding) {
        item(key = "intro") {
            Text(
                stringResource(R.string.foundations_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.extraSmall,
                    end = MaterialTheme.spacing.extraSmall,
                    top = MaterialTheme.spacing.small,
                    bottom = MaterialTheme.spacing.medium,
                ),
            )
        }
        itemsIndexed(entries, key = { _, entry -> entry.id }) { index, entry ->
            ConnectedItem(index, entries.size) {
                EntryWithMenu(
                    entry = entry,
                    bookmarked = entry.id in bookmarkedIds,
                    onOpen = { onEntryClick(entry.id) },
                    onBookmark = { onBookmarkClick(entry.id, it) },
                ) { openMenu ->
                    EntryRow(
                        title = entry.officialName,
                        category = entry.category,
                        supporting = entry.summary,
                        onClick = { onEntryClick(entry.id) },
                        onLongClick = openMenu,
                        sharedKey = entryBadgeKey(entry.id),
                    )
                }
            }
        }
    }
}
