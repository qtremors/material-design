package dev.qtremors.material.samples.communication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BadgesSample(modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Badges on Icons", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Row(
                Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dot Badge
                BadgedBox(badge = { Badge() }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }

                // Numeric Badge (Single Digit)
                BadgedBox(badge = { Badge { Text("5") } }) {
                    Icon(Icons.Default.Email, contentDescription = "Email")
                }

                // Large Count Badge
                BadgedBox(badge = { Badge { Text("99+") } }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            }
        }

        Text("Badges in Navigation Drawer Items", style = MaterialTheme.typography.titleMedium)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Email, contentDescription = null) },
                    label = { Text("Messages") },
                    badge = { Badge { Text("12") } },
                    selected = true,
                    onClick = {}
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    label = { Text("Updates") },
                    badge = { Badge { Text("3") } },
                    selected = false,
                    onClick = {}
                )
            }
        }
    }
}
