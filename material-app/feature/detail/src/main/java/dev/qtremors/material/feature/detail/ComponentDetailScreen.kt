package dev.qtremors.material.feature.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.ApiAvailability
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.catalog.ImplementationKind
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class DetailSection { PREVIEW, GUIDANCE, INSPECT, API }

/** Enum properties cannot call composables, so tab labels resolve here. */
@Composable
private fun DetailSection.label(): String = when (this) {
    DetailSection.PREVIEW -> stringResource(R.string.detail_tab_preview)
    DetailSection.GUIDANCE -> stringResource(R.string.detail_tab_guidance)
    DetailSection.INSPECT -> stringResource(R.string.detail_tab_inspect)
    DetailSection.API -> stringResource(R.string.detail_tab_api)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ComponentDetailScreen(
    entry: CatalogEntry,
    selectedSection: DetailSection,
    bookmarked: Boolean,
    onSectionSelected: (DetailSection) -> Unit,
    onBookmarkClick: (Boolean) -> Unit,
    onBack: () -> Unit,
    demo: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = selectedSection.ordinal) { DetailSection.entries.size }

    LaunchedEffect(pagerState.currentPage) {
        val currentSection = DetailSection.entries[pagerState.currentPage]
        if (currentSection != selectedSection) {
            onSectionSelected(currentSection)
        }
    }

    LaunchedEffect(selectedSection) {
        if (pagerState.currentPage != selectedSection.ordinal) {
            pagerState.animateScrollToPage(selectedSection.ordinal)
        }
    }

    // Collapse offsets survive configuration change and process recreation.
    var headerHeightPx by rememberSaveable { mutableFloatStateOf(0f) }
    var headerOffsetPx by rememberSaveable { mutableFloatStateOf(0f) }

    val nestedScrollConnection = remember(headerHeightPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta < 0f && headerHeightPx > 0f) {
                    val oldOffset = headerOffsetPx
                    val newOffset = (headerOffsetPx + delta).coerceIn(-headerHeightPx, 0f)
                    headerOffsetPx = newOffset
                    return Offset(0f, newOffset - oldOffset)
                }
                return Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta > 0f && headerHeightPx > 0f) {
                    val oldOffset = headerOffsetPx
                    val newOffset = (headerOffsetPx + delta).coerceIn(-headerHeightPx, 0f)
                    headerOffsetPx = newOffset
                    return Offset(0f, newOffset - oldOffset)
                }
                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(entry.officialName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.detail_back_content_description))
                    }
                },
                actions = {
                    IconButton(onClick = { onBookmarkClick(!bookmarked) }) {
                        Icon(
                            imageVector = if (bookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = stringResource(
                                if (bookmarked) R.string.detail_remove_bookmark_content_description
                                else R.string.detail_add_bookmark_content_description,
                            ),
                        )
                    }
                },
            )
        },
    ) { padding ->
        val density = LocalDensity.current
        val headerHeightDp = with(density) { headerHeightPx.toDp() }
        val headerOffsetDp = with(density) { headerOffsetPx.toDp() }
        val tabRowHeightDp = 48.dp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .nestedScroll(nestedScrollConnection),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = (headerHeightDp + headerOffsetDp + tabRowHeightDp).coerceAtLeast(tabRowHeightDp)),
            ) { page ->
                when (DetailSection.entries[page]) {
                    DetailSection.PREVIEW -> PreviewSection(demo)
                    DetailSection.GUIDANCE -> GuidanceSection(entry)
                    DetailSection.INSPECT -> InspectSection(entry)
                    DetailSection.API -> ApiSection(entry)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, headerOffsetPx.roundToInt()) }
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { size ->
                            if (headerHeightPx != size.height.toFloat()) {
                                headerHeightPx = size.height.toFloat()
                            }
                        }
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val isExpressive = entry.collections.contains("expressive") || entry.apiReferences.any { it.optInAnnotation?.contains("Expressive") == true }
                    val isExperimental = entry.apiReferences.any { it.stability == ApiStability.EXPERIMENTAL }
                    val isCustom = entry.implementation == ImplementationKind.PROJECT_IMPLEMENTATION

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        AssistChip(onClick = {}, label = { Text(entry.category, maxLines = 1, overflow = TextOverflow.Ellipsis) })
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = stringResource(
                                        if (isCustom) R.string.detail_badge_custom_component
                                        else R.string.detail_badge_official_api,
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            },
                        )
                        if (isExpressive) {
                            AssistChip(onClick = {}, label = { Text(stringResource(R.string.detail_badge_m3_expressive), maxLines = 1, overflow = TextOverflow.Ellipsis) })
                        }
                        if (isExperimental) {
                            AssistChip(onClick = {}, label = { Text(stringResource(R.string.detail_badge_experimental_api), maxLines = 1, overflow = TextOverflow.Ellipsis) })
                        }
                    }
                    Text(entry.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(stringResource(R.string.detail_aliases, entry.aliases.joinToString()), style = MaterialTheme.typography.bodySmall)
                }

                PrimaryTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    DetailSection.entries.forEachIndexed { index, section ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = { Text(section.label()) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewSection(demo: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(stringResource(R.string.detail_preview_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(20.dp)) { demo() } }
    }
}

@Composable
private fun GuidanceSection(entry: CatalogEntry) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text(stringResource(R.string.detail_guidance_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(stringResource(R.string.detail_guidance_purpose_label), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(entry.guidance.purpose, style = MaterialTheme.typography.titleMedium)
            }
        }
        GuidanceList(stringResource(R.string.detail_guidance_use_when_title), entry.guidance.useWhen)
        GuidanceList(stringResource(R.string.detail_guidance_avoid_when_title), entry.guidance.avoidWhen, caution = true)
        GuidanceList(stringResource(R.string.detail_guidance_behavior_title), entry.guidance.behavior)
        GuidanceList(stringResource(R.string.detail_guidance_accessibility_title), entry.guidance.accessibility)
        GuidanceList(stringResource(R.string.detail_guidance_adaptive_title), entry.guidance.adaptive)
    }
}

@Composable
private fun GuidanceList(title: String, items: List<String>, caution: Boolean = false) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (caution) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (caution) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLow
                },
            ),
        ) {
            Column(Modifier.padding(horizontal = 18.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items.forEach { item ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("•", fontWeight = FontWeight.Bold)
                        Text(item, modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun InspectSection(entry: CatalogEntry) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(stringResource(R.string.detail_inspect_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        InspectLine(stringResource(R.string.detail_inspect_stable_id_label), entry.id)
        InspectLine(stringResource(R.string.detail_inspect_category_label), entry.category)
        InspectLine(stringResource(R.string.detail_inspect_kind_label), entry.kind.name.lowercase())
        InspectLine(stringResource(R.string.detail_inspect_implementation_label), entry.implementation.name.lowercase().replace('_', ' '))
        InspectLine(stringResource(R.string.detail_inspect_reviewed_label), entry.reviewedOn)
        HorizontalDivider()
        Text(stringResource(R.string.detail_expected_behavior_title), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(R.string.detail_expected_behavior_body))
    }
}

@Composable
private fun ApiSection(entry: CatalogEntry) {
    val clipboard = LocalClipboard.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(stringResource(R.string.detail_api_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        entry.apiReferences.forEach { api ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth()) {
                        Text(api.symbol, modifier = Modifier.weight(1f), fontFamily = FontFamily.Monospace)
                        IconButton(onClick = {
                            coroutineScope.launch {
                                clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(context.getString(R.string.detail_clip_label_symbol), api.symbol)))
                            }
                        }) {
                            Icon(Icons.Default.ContentCopy, stringResource(R.string.detail_copy_symbol_content_description))
                        }
                        IconButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(api.url))) }) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, stringResource(R.string.detail_open_official_api_content_description))
                        }
                    }
                    Text(stringResource(R.string.detail_artifact_version, api.artifact, api.reviewedVersion))
                    val availabilityLabel = if (api.availability == ApiAvailability.STABLE_ARTIFACT) {
                        stringResource(R.string.detail_availability_stable_artifact)
                    } else {
                        stringResource(R.string.detail_availability_alpha_only)
                    }
                    val stabilityLabel = if (api.stability == ApiStability.STABLE) {
                        stringResource(R.string.detail_stability_stable)
                    } else {
                        stringResource(R.string.detail_stability_experimental)
                    }
                    Text(
                        stringResource(R.string.detail_availability_and_stability, availabilityLabel, stabilityLabel),
                        color = MaterialTheme.colorScheme.primary,
                    )
                    api.optInAnnotation?.let { Text(stringResource(R.string.detail_opt_in_label, it), fontFamily = FontFamily.Monospace) }
                }
            }
        }
        Text(stringResource(R.string.detail_repository_source_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        entry.sourceLocations.forEach { source ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(source.path, fontFamily = FontFamily.Monospace)
                        Text(source.symbols.joinToString(), style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(context.getString(R.string.detail_clip_label_path), source.path)))
                        }
                    }) {
                        Icon(Icons.Default.ContentCopy, stringResource(R.string.detail_copy_path_content_description))
                    }
                }
            }
        }
    }
}

@Composable
private fun InspectLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
