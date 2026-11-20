package dev.qtremors.material.feature.settings

import android.content.ClipData
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material.icons.rounded.Code
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Gavel
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.components.AppScaffold
import dev.qtremors.material.core.designsystem.components.ConnectedGroup
import dev.qtremors.material.core.designsystem.components.ConnectedRow
import dev.qtremors.material.core.designsystem.components.IconBadge
import dev.qtremors.material.core.designsystem.components.NavigationRow
import dev.qtremors.material.core.designsystem.components.SectionHeader
import dev.qtremors.material.core.designsystem.components.rememberAppHaptics
import dev.qtremors.material.core.designsystem.spacing
import kotlinx.coroutines.launch
import dev.qtremors.material.core.designsystem.R as DesignR

private const val RepositoryUrl = "https://github.com/qtremors/material-design"

@Composable
fun AboutScreen(
    appVersion: String,
    material3Version: String,
    composeUiVersion: String,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val haptics = rememberAppHaptics()
    val device = stringResource(
        R.string.about_device_format,
        Build.MANUFACTURER.replaceFirstChar(Char::uppercaseChar),
        Build.MODEL,
        Build.VERSION.RELEASE,
        Build.VERSION.SDK_INT,
    )
    val deviceClipLabel = stringResource(R.string.about_device_copied_label)

    AppScaffold(title = stringResource(R.string.about_title), modifier = modifier) { padding ->
        LazyColumn(contentPadding = padding) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = MaterialTheme.spacing.large),
                ) {
                    Image(
                        painter = painterResource(DesignR.drawable.material_design),
                        contentDescription = null,
                        modifier = Modifier.size(88.dp),
                    )
                    Text(stringResource(R.string.about_app_name), style = MaterialTheme.typography.headlineMedium)
                    Text(
                        stringResource(R.string.about_version, appVersion),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        stringResource(R.string.about_tagline),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                    )
                }
            }

            item { SectionHeader(stringResource(R.string.about_section_project)) }
            item {
                ConnectedGroup {
                    NavigationRow(
                        title = stringResource(R.string.about_developer),
                        supporting = stringResource(R.string.about_developer_name),
                        icon = Icons.Rounded.Person,
                        badgeKey = "developer",
                        onClick = { uriHandler.openUri("https://github.com/qtremors") },
                    )
                    NavigationRow(
                        title = stringResource(R.string.about_source),
                        supporting = stringResource(R.string.about_source_supporting),
                        icon = Icons.Rounded.Code,
                        badgeKey = "source",
                        onClick = { uriHandler.openUri(RepositoryUrl) },
                    )
                    NavigationRow(
                        title = stringResource(R.string.about_releases),
                        supporting = stringResource(R.string.about_releases_supporting),
                        icon = Icons.Rounded.Download,
                        badgeKey = "releases",
                        onClick = { uriHandler.openUri("$RepositoryUrl/releases") },
                    )
                    NavigationRow(
                        title = stringResource(R.string.about_issues),
                        supporting = stringResource(R.string.about_issues_supporting),
                        icon = Icons.Rounded.BugReport,
                        badgeKey = "issues",
                        onClick = { uriHandler.openUri("$RepositoryUrl/issues") },
                    )
                    NavigationRow(
                        title = stringResource(R.string.about_license),
                        supporting = stringResource(R.string.about_license_supporting),
                        icon = Icons.Rounded.Gavel,
                        badgeKey = "license",
                        onClick = { uriHandler.openUri("$RepositoryUrl/blob/main/LICENSE.md") },
                    )
                }
            }

            item { SectionHeader(stringResource(R.string.about_section_built_on)) }
            item {
                ConnectedGroup {
                    ConnectedRow(
                        title = stringResource(R.string.about_material3),
                        supporting = material3Version,
                        leading = { IconBadge(Icons.Rounded.Widgets, key = "material3") },
                    )
                    ConnectedRow(
                        title = stringResource(R.string.about_compose_ui),
                        supporting = composeUiVersion,
                        leading = { IconBadge(Icons.Rounded.Layers, key = "compose") },
                    )
                }
            }

            item { SectionHeader(stringResource(R.string.about_section_credits)) }
            item {
                ConnectedGroup {
                    NavigationRow(
                        title = stringResource(R.string.about_credit_material),
                        supporting = stringResource(R.string.about_credit_material_supporting),
                        icon = Icons.Rounded.Palette,
                        badgeKey = "credit-material",
                        onClick = { uriHandler.openUri("https://m3.material.io/") },
                    )
                    NavigationRow(
                        title = stringResource(R.string.about_credit_compose),
                        supporting = stringResource(R.string.about_credit_compose_supporting),
                        icon = Icons.Rounded.Layers,
                        badgeKey = "credit-compose",
                        onClick = { uriHandler.openUri("https://developer.android.com/compose") },
                    )
                    NavigationRow(
                        title = stringResource(R.string.about_credit_font),
                        supporting = stringResource(R.string.about_credit_font_supporting),
                        icon = Icons.Rounded.TextFields,
                        badgeKey = "credit-font",
                        onClick = { uriHandler.openUri("https://fonts.google.com/specimen/Google+Sans+Flex") },
                    )
                    NavigationRow(
                        title = stringResource(R.string.about_credit_kolor),
                        supporting = stringResource(R.string.about_credit_kolor_supporting),
                        icon = Icons.Rounded.Palette,
                        badgeKey = "credit-kolor",
                        onClick = { uriHandler.openUri("https://github.com/jordond/MaterialKolor") },
                    )
                }
            }

            item { SectionHeader(stringResource(R.string.about_section_device)) }
            item {
                ConnectedGroup {
                    ConnectedRow(
                        title = device,
                        supporting = stringResource(R.string.about_device_copy_hint),
                        leading = { IconBadge(Icons.Rounded.PhoneAndroid, key = "device") },
                        tapHaptic = false,
                        onClick = {
                            haptics.confirm()
                            scope.launch {
                                val report = "Material Design $appVersion · Material 3 $material3Version · $device"
                                clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(deviceClipLabel, report)))
                            }
                        },
                    )
                }
            }
        }
    }
}
