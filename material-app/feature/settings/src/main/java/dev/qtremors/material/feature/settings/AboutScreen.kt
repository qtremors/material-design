package dev.qtremors.material.feature.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Source
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.expressiveSegmentedShapes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val copyToClipboard: (String, String) -> Unit = { label, text ->
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
        Toast.makeText(context, "Copied $label to clipboard", Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "About",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(40.dp),
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.95f),
                ),
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_material_logo),
                        contentDescription = "Material Design",
                        modifier = Modifier.size(96.dp),
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Material Design",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Interactive Material 3 Expressive Showcase",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            item {
                AboutSection(title = "App Info") {
                    SegmentedListItem(
                        onClick = { copyToClipboard("Version", "2.0.6") },
                        shapes = expressiveSegmentedShapes(index = 0, count = 4),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Version") },
                        supportingContent = { Text("2.0.6") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    SegmentedListItem(
                        onClick = { uriHandler.openUri("https://github.com/qtremors") },
                        shapes = expressiveSegmentedShapes(index = 1, count = 4),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Code, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Developer") },
                        supportingContent = { Text("Tremors (@qtremors)") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    SegmentedListItem(
                        onClick = { uriHandler.openUri("https://github.com/qtremors/material-design") },
                        shapes = expressiveSegmentedShapes(index = 2, count = 4),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Source, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Repository") },
                        supportingContent = { Text("github.com/qtremors/material-design") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    val deviceText = "${Build.MANUFACTURER} ${Build.MODEL} (Android ${Build.VERSION.RELEASE})"
                    SegmentedListItem(
                        onClick = { copyToClipboard("Device", deviceText) },
                        shapes = expressiveSegmentedShapes(index = 3, count = 4),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Device") },
                        supportingContent = { Text(deviceText) },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                }
            }

            item {
                AboutSection(title = "Material 3 Catalog") {
                    SegmentedListItem(
                        onClick = { copyToClipboard("Material 3 Compose", "1.5.0-alpha23") },
                        shapes = expressiveSegmentedShapes(index = 0, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Layers, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Material 3 Compose") },
                        supportingContent = { Text("1.5.0-alpha23") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    SegmentedListItem(
                        onClick = { copyToClipboard("Compose UI", "1.12.0-alpha03") },
                        shapes = expressiveSegmentedShapes(index = 1, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Layers, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Compose UI Version") },
                        supportingContent = { Text("1.12.0-alpha03") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    SegmentedListItem(
                        onClick = {},
                        shapes = expressiveSegmentedShapes(index = 2, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Components") },
                        supportingContent = { Text("40+ official & expressive interactive references") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                }
            }

            item {
                AboutSection(title = "Community & Source") {
                    SegmentedListItem(
                        onClick = { uriHandler.openUri("https://github.com/qtremors/material-design/releases") },
                        shapes = expressiveSegmentedShapes(index = 0, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Releases & Changelog") },
                        supportingContent = { Text("View project release notes and updates") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    SegmentedListItem(
                        onClick = { uriHandler.openUri("https://github.com/qtremors/material-design/issues") },
                        shapes = expressiveSegmentedShapes(index = 1, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.BugReport, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Report an Issue") },
                        supportingContent = { Text("Submit bug reports or feature requests") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    SegmentedListItem(
                        onClick = { uriHandler.openUri("https://github.com/qtremors/material-design/blob/main/LICENSE") },
                        shapes = expressiveSegmentedShapes(index = 2, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text("Open Source License") },
                        supportingContent = { Text("MIT License") },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                }
            }
        }
    }
}

@Composable
private fun AboutSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
            content()
        }
    }
}
