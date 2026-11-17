package dev.qtremors.materialdesign.shell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ManageSearch
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.designsystem.components.ConnectedItem
import dev.qtremors.material.core.designsystem.components.EmptyState
import dev.qtremors.material.core.designsystem.components.InlineSearchField
import dev.qtremors.material.core.designsystem.components.SectionHeader
import dev.qtremors.material.core.designsystem.spacing
import dev.qtremors.material.core.ui.CatalogEntryRow
import dev.qtremors.materialdesign.R
import dev.qtremors.material.core.designsystem.R as DesignR

/** Queries that show what search understands: an alias, a concept, and an API symbol. */
private val Suggestions = listOf("FAB", "wavy progress", "connected buttons", "SplitButtonLayout", "spring physics", "TalkBack")

/**
 * Full-screen search across official names, aliases, API symbols, and guidance. The keyboard opens
 * once when search opens; returning from a result keeps the results in view instead.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun SearchScreen(
    query: String,
    results: List<CatalogEntry>,
    recent: List<CatalogEntry>,
    onQueryChange: (String) -> Unit,
    onEntryClick: (String) -> Unit,
    onBack: () -> Unit,
) {
    val focus = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    var focusedOnce by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!focusedOnce) {
            focus.requestFocus()
            focusedOnce = true
        }
    }
    val spacing = MaterialTheme.spacing

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = spacing.extraSmall, end = spacing.screenGutter, bottom = spacing.small),
            ) {
                IconButton(onClick = onBack, shapes = IconButtonDefaults.shapes()) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(DesignR.string.action_back))
                }
                InlineSearchField(
                    query = query,
                    onQueryChange = onQueryChange,
                    placeholder = stringResource(R.string.app_search_placeholder),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focus),
                )
            }
        },
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentPadding = PaddingValues(
                start = spacing.screenGutter,
                end = spacing.screenGutter,
                top = inner.calculateTopPadding(),
                bottom = inner.calculateBottomPadding() + spacing.large,
            ),
        ) {
            when {
                query.isBlank() -> {
                    item { SectionHeader(stringResource(R.string.app_search_suggestions)) }
                    item {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(spacing.small),
                            modifier = Modifier.padding(horizontal = spacing.extraSmall),
                        ) {
                            Suggestions.forEach { suggestion ->
                                AssistChip(onClick = { onQueryChange(suggestion) }, label = { Text(suggestion) })
                            }
                        }
                    }
                    if (recent.isEmpty()) {
                        item {
                            EmptyState(
                                icon = Icons.Rounded.ManageSearch,
                                title = stringResource(R.string.app_search_empty_title),
                                message = stringResource(R.string.app_search_empty_body),
                            )
                        }
                    } else {
                        item { SectionHeader(stringResource(R.string.app_search_recent)) }
                        itemsIndexed(recent, key = { _, entry -> "recent-${entry.id}" }) { index, entry ->
                            ConnectedItem(index, recent.size) {
                                CatalogEntryRow(entry, onClick = { onEntryClick(entry.id) }, showSummary = false)
                            }
                        }
                    }
                }
                results.isEmpty() -> item {
                    EmptyState(
                        icon = Icons.Rounded.SearchOff,
                        title = stringResource(R.string.app_search_no_results_title, query.trim()),
                        message = stringResource(R.string.app_search_no_results_body),
                    )
                }
                else -> {
                    item { SectionHeader(stringResource(R.string.app_search_results)) }
                    itemsIndexed(results, key = { _, entry -> entry.id }) { index, entry ->
                        ConnectedItem(index, results.size, modifier = Modifier.animateItem()) {
                            CatalogEntryRow(
                                entry,
                                onClick = {
                                    keyboard?.hide()
                                    onEntryClick(entry.id)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
