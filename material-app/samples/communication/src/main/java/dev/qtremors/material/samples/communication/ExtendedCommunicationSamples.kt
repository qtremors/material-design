package dev.qtremors.material.samples.communication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenuGroupSample(modifier: Modifier = Modifier) {
    var menuOpen by remember { mutableStateOf(false) }
    var lastAction by remember { mutableStateOf("No group action chosen yet.") }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Dropdown menu group", style = MaterialTheme.typography.titleMedium)
        Text(lastAction, style = MaterialTheme.typography.bodyMedium)
        Button(onClick = { menuOpen = true }) { Text("Open grouped menu") }
        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
            DropdownMenuGroup(shapes = MenuDefaults.groupShape(index = 0, count = 2)) {
                DropdownMenuItem(
                    text = { Text("Rename") },
                    onClick = {
                        lastAction = "Group 1: Rename"
                        menuOpen = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Duplicate") },
                    onClick = {
                        lastAction = "Group 1: Duplicate"
                        menuOpen = false
                    }
                )
            }
            DropdownMenuGroup(
                shapes = MenuDefaults.groupShape(index = 1, count = 2),
                containerColor = MenuDefaults.groupVibrantContainerColor,
            ) {
                DropdownMenuItem(
                    text = { Text("Move to archive") },
                    onClick = {
                        lastAction = "Vibrant group: Archive"
                        menuOpen = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Delete permanently") },
                    onClick = {
                        lastAction = "Vibrant group: Delete"
                        menuOpen = false
                    }
                )
            }
        }
    }
}

@Composable
fun GroupedDropdownMenusSample(modifier: Modifier = Modifier) = DropdownMenuGroupSample(modifier)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicAlertDialogSample(modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Basic alert dialog", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { showDialog = true }) { Text("Open basic alert dialog") }
    }

    if (showDialog) {
        BasicAlertDialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = 6.dp,
                modifier = Modifier.padding(16.dp),
            ) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Custom alert layout", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "BasicAlertDialog provides only the dialog window; you compose the surface, text, and actions.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(onClick = { showDialog = false }) { Text("Close") }
                }
            }
        }
    }
}

@Composable
fun BasicAlertDialogsSample(modifier: Modifier = Modifier) = BasicAlertDialogSample(modifier)
