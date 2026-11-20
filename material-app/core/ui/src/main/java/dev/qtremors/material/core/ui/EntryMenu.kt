package dev.qtremors.material.core.ui

import android.content.ClipData
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.designsystem.components.AppMenu
import dev.qtremors.material.core.designsystem.components.MenuAction
import dev.qtremors.material.core.designsystem.components.rememberAppHaptics
import kotlinx.coroutines.launch

/**
 * Gives any entry row the same long-press menu: open it, bookmark it, or copy its source path for
 * an agent or an editor. [row] receives the long-press handler to pass to its row.
 */
@Composable
fun EntryWithMenu(
    entry: CatalogEntry,
    bookmarked: Boolean,
    onOpen: () -> Unit,
    onBookmark: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    row: @Composable (onLongClick: () -> Unit) -> Unit,
) {
    var open by rememberSaveable(entry.id) { mutableStateOf(false) }
    val haptics = rememberAppHaptics()
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val clipLabel = stringResource(R.string.entry_clip_label_path)
    val sourcePath = entry.sourceLocations.firstOrNull()?.path

    Box(modifier) {
        row { open = true }
        AppMenu(
            expanded = open,
            onDismissRequest = { open = false },
            groups = listOf(
                listOf(
                    MenuAction(label = stringResource(R.string.entry_menu_open), icon = Icons.AutoMirrored.Rounded.ArrowForward) {
                        open = false
                        onOpen()
                    },
                    MenuAction(
                        label = stringResource(if (bookmarked) R.string.entry_menu_remove_bookmark else R.string.entry_menu_bookmark),
                        icon = if (bookmarked) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    ) {
                        open = false
                        haptics.toggle(!bookmarked)
                        onBookmark(!bookmarked)
                    },
                ),
                listOfNotNull(
                    sourcePath?.let { path ->
                        MenuAction(
                            label = stringResource(R.string.entry_menu_copy_source),
                            supporting = stringResource(R.string.entry_menu_copy_source_supporting),
                            icon = Icons.Rounded.ContentCopy,
                        ) {
                            open = false
                            haptics.confirm()
                            scope.launch { clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(clipLabel, path))) }
                        }
                    },
                ),
            ).filter { it.isNotEmpty() },
        )
    }
}
