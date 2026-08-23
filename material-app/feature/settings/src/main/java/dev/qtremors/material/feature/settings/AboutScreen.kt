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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.expressiveSegmentedShapes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AboutScreen(
    appVersion: String,
    material3Version: String,
    composeUiVersion: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val copiedToClipboardTemplate = stringResource(R.string.settings_copied_to_clipboard)
    val versionLabel = stringResource(R.string.settings_version_label)
    val deviceLabel = stringResource(R.string.settings_device_label)
    val m3ComposeLabel = stringResource(R.string.settings_m3_compose_label)
    val composeUiLabel = stringResource(R.string.settings_compose_ui_label)

    val copyToClipboard: (String, String) -> Unit = { label, text ->
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.setPrimaryClip(ClipData.newPlainText(label, text))
        Toast.makeText(context, copiedToClipboardTemplate.format(label), Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_about_title),
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
                                contentDescription = stringResource(R.string.settings_back),
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
                        contentDescription = stringResource(R.string.settings_app_name),
                        modifier = Modifier.size(96.dp),
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.settings_app_name),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.settings_about_tagline),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            item {
                AboutSection(title = stringResource(R.string.settings_about_section_app_info)) {
                    SegmentedListItem(
                        onClick = { copyToClipboard(versionLabel, appVersion) },
                        shapes = expressiveSegmentedShapes(index = 0, count = 4),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text(versionLabel) },
                        supportingContent = { Text(appVersion) },
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
                        content = { Text(stringResource(R.string.settings_developer)) },
                        supportingContent = { Text(stringResource(R.string.settings_developer_name)) },
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
                        content = { Text(stringResource(R.string.settings_repository)) },
                        supportingContent = { Text(stringResource(R.string.settings_repository_url)) },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    val deviceText = stringResource(
                        R.string.settings_device_format,
                        Build.MANUFACTURER,
                        Build.MODEL,
                        Build.VERSION.RELEASE,
                    )
                    SegmentedListItem(
                        onClick = { copyToClipboard(deviceLabel, deviceText) },
                        shapes = expressiveSegmentedShapes(index = 3, count = 4),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text(deviceLabel) },
                        supportingContent = { Text(deviceText) },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                }
            }

            item {
                AboutSection(title = stringResource(R.string.settings_about_section_catalog)) {
                    SegmentedListItem(
                        onClick = { copyToClipboard(m3ComposeLabel, material3Version) },
                        shapes = expressiveSegmentedShapes(index = 0, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Layers, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text(m3ComposeLabel) },
                        supportingContent = { Text(material3Version) },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                    SegmentedListItem(
                        onClick = { copyToClipboard(composeUiLabel, composeUiVersion) },
                        shapes = expressiveSegmentedShapes(index = 1, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Layers, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text(stringResource(R.string.settings_compose_ui_version_label)) },
                        supportingContent = { Text(composeUiVersion) },
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
                        content = { Text(stringResource(R.string.settings_components)) },
                        supportingContent = { Text(stringResource(R.string.settings_components_description)) },
                        colors = ListItemDefaults.segmentedColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.height(IntrinsicSize.Min),
                    )
                }
            }

            item {
                AboutSection(title = stringResource(R.string.settings_about_section_community)) {
                    SegmentedListItem(
                        onClick = { uriHandler.openUri("https://github.com/qtremors/material-design/releases") },
                        shapes = expressiveSegmentedShapes(index = 0, count = 3),
                        leadingContent = {
                            Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        },
                        content = { Text(stringResource(R.string.settings_releases_changelog)) },
                        supportingContent = { Text(stringResource(R.string.settings_releases_changelog_description)) },
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
                        content = { Text(stringResource(R.string.settings_report_issue)) },
                        supportingContent = { Text(stringResource(R.string.settings_report_issue_description)) },
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
                        content = { Text(stringResource(R.string.settings_open_source_license)) },
                        supportingContent = { Text(stringResource(R.string.settings_license_name)) },
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
