package dev.qtremors.material.feature.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.OpenInNew
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.catalog.ApiAvailability
import dev.qtremors.material.core.catalog.ApiStability
import dev.qtremors.material.core.catalog.CatalogEntry
import dev.qtremors.material.core.catalog.ImplementationKind

enum class DetailSection(val label: String) { PREVIEW("Preview"), GUIDANCE("Guidance"), INSPECT("Inspect"), API("API") }

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(entry.officialName) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { onBookmarkClick(!bookmarked) }) {
                        Icon(if (bookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder, if (bookmarked) "Remove bookmark" else "Bookmark")
                    }
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val isExpressive = entry.collections.contains("expressive") || entry.apiReferences.any { it.optInAnnotation?.contains("Expressive") == true }
                val isExperimental = entry.apiReferences.any { it.stability == ApiStability.EXPERIMENTAL }
                val isCustom = entry.implementation == ImplementationKind.PROJECT_IMPLEMENTATION

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AssistChip(onClick = {}, label = { Text(entry.category) })
                    AssistChip(
                        onClick = {},
                        label = { Text(if (isCustom) "Custom Component" else "Official API") }
                    )
                    if (isExpressive) {
                        AssistChip(onClick = {}, label = { Text("M3 Expressive") })
                    }
                    if (isExperimental) {
                        AssistChip(onClick = {}, label = { Text("Experimental API") })
                    }
                }
                Text(entry.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Also find: ${entry.aliases.joinToString()}", style = MaterialTheme.typography.bodySmall)
            }
            PrimaryTabRow(selectedTabIndex = selectedSection.ordinal) {
                DetailSection.entries.forEach { section ->
                    Tab(selected = selectedSection == section, onClick = { onSectionSelected(section) }, text = { Text(section.label) })
                }
            }
            when (selectedSection) {
                    DetailSection.PREVIEW -> PreviewSection(demo)
                    DetailSection.GUIDANCE -> GuidanceSection(entry)
                    DetailSection.INSPECT -> InspectSection(entry)
                    DetailSection.API -> ApiSection(entry)
            }
        }
    }
}

@Composable
private fun PreviewSection(demo: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Interactive reference", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(20.dp)) { demo() } }
    }
}

@Composable
private fun GuidanceSection(entry: CatalogEntry) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        Text("Design guidance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Purpose", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(entry.guidance.purpose, style = MaterialTheme.typography.titleMedium)
            }
        }
        GuidanceList("Use when", entry.guidance.useWhen)
        GuidanceList("Avoid when", entry.guidance.avoidWhen, caution = true)
        GuidanceList("Behavior and feeling", entry.guidance.behavior)
        GuidanceList("Accessibility", entry.guidance.accessibility)
        GuidanceList("Adaptive layouts", entry.guidance.adaptive)
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
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Implementation reference", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        InspectLine("Stable ID", entry.id)
        InspectLine("Category", entry.category)
        InspectLine("Kind", entry.kind.name.lowercase())
        InspectLine("Implementation", entry.implementation.name.lowercase().replace('_', ' '))
        InspectLine("Reviewed", entry.reviewedOn)
        HorizontalDivider()
        Text("Expected behavior", style = MaterialTheme.typography.titleMedium)
        Text("State is hoisted, touch targets remain accessible, labels are semantic, and layouts must survive compact through expanded windows and increased font scales.")
    }
}

@Composable
private fun ApiSection(entry: CatalogEntry) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text("Official APIs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        entry.apiReferences.forEach { api ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth()) {
                        Text(api.symbol, modifier = Modifier.weight(1f), fontFamily = FontFamily.Monospace)
                        IconButton(onClick = { clipboard.setText(AnnotatedString(api.symbol)) }) { Icon(Icons.Default.ContentCopy, "Copy API symbol") }
                        IconButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(api.url))) }) { Icon(Icons.Default.OpenInNew, "Open official API") }
                    }
                    Text("${api.artifact}:${api.reviewedVersion}")
                    Text(
                        "${if (api.availability == ApiAvailability.STABLE_ARTIFACT) "Stable artifact" else "Alpha only"} · ${if (api.stability == ApiStability.STABLE) "Stable API" else "Experimental API"}",
                        color = MaterialTheme.colorScheme.primary,
                    )
                    api.optInAnnotation?.let { Text("Opt in: $it", fontFamily = FontFamily.Monospace) }
                }
            }
        }
        Text("Repository source", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        entry.sourceLocations.forEach { source ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text(source.path, fontFamily = FontFamily.Monospace)
                        Text(source.symbols.joinToString(), style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { clipboard.setText(AnnotatedString(source.path)) }) { Icon(Icons.Default.ContentCopy, "Copy source path") }
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
