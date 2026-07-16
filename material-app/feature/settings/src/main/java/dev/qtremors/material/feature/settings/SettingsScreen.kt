package dev.qtremors.material.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.data.AppSettings
import dev.qtremors.material.core.data.MotionMode
import dev.qtremors.material.core.data.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    bookmarkCount: Int,
    recentCount: Int,
    onThemeModeChange: (ThemeMode) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onMotionModeChange: (MotionMode) -> Unit,
    onClearBookmarks: () -> Unit,
    onClearRecent: () -> Unit,
    onBack: () -> Unit,
) {
    var confirmAction by remember { mutableStateOf<String?>(null) }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } }) },
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(bottom = 24.dp),
        ) {
            SectionTitle("Appearance")
            ThemeMode.entries.forEach { mode ->
                ListItem(
                    headlineContent = { Text(mode.name.lowercase().replaceFirstChar(Char::uppercase)) },
                    leadingContent = { RadioButton(selected = settings.themeMode == mode, onClick = { onThemeModeChange(mode) }) },
                )
            }
            ListItem(
                headlineContent = { Text("Dynamic color") },
                supportingContent = { Text("Use the device color scheme on Android 12 and newer") },
                trailingContent = { Switch(checked = settings.dynamicColor, onCheckedChange = onDynamicColorChange) },
            )
            HorizontalDivider()
            SectionTitle("Motion")
            ListItem(
                headlineContent = { Text("Reduce app motion") },
                supportingContent = { Text("Demos should avoid decorative motion when enabled") },
                trailingContent = { Switch(checked = settings.motionMode == MotionMode.REDUCED, onCheckedChange = { onMotionModeChange(if (it) MotionMode.REDUCED else MotionMode.SYSTEM) }) },
            )
            HorizontalDivider()
            SectionTitle("Library")
            ListItem(
                headlineContent = { Text("Clear bookmarks") },
                supportingContent = { Text("$bookmarkCount saved components") },
                modifier = Modifier.fillMaxWidth(),
                trailingContent = { TextButton(onClick = { confirmAction = "bookmarks" }, enabled = bookmarkCount > 0) { Text("Clear") } },
            )
            ListItem(
                headlineContent = { Text("Clear recent history") },
                supportingContent = { Text("$recentCount recently viewed components") },
                trailingContent = { TextButton(onClick = { confirmAction = "recent" }, enabled = recentCount > 0) { Text("Clear") } },
            )
            HorizontalDivider()
            SectionTitle("About")
            ListItem(headlineContent = { Text("Material Design") }, supportingContent = { Text("Version 2.0.0") })
            ListItem(headlineContent = { Text("Catalog snapshot") }, supportingContent = { Text("Material 3 1.5.0-alpha23 · stable baseline 1.4.0") })
            ListItem(headlineContent = { Text("License") }, supportingContent = { Text("MIT") })
        }
    }
    if (confirmAction != null) {
        AlertDialog(
            onDismissRequest = { confirmAction = null },
            title = { Text("Clear ${confirmAction}?") },
            text = { Text("This removes only local app data and cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    if (confirmAction == "bookmarks") onClearBookmarks() else onClearRecent()
                    confirmAction = null
                }) { Text("Clear") }
            },
            dismissButton = { TextButton(onClick = { confirmAction = null }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
    )
}
