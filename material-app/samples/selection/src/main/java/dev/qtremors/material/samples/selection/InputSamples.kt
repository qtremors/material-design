package dev.qtremors.material.samples.selection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun TextFieldsSample(modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("material@example") }
    var password by remember { mutableStateOf("expressive") }
    var passwordVisible by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf("") }
    val emailValid = email.isBlank() || ("@" in email && "." in email.substringAfter('@', ""))

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        InputReferenceSection(
            title = "Filled and outlined styles",
            description = "Filled fields group closely with a surface. Outlined fields establish a stronger boundary. Labels persist after input.",
        ) {
            TextField(
                value = name,
                onValueChange = { name = it.take(40) },
                label = { Text("Reference name") },
                placeholder = { Text("Button behavior audit") },
                supportingText = { Text("${name.length}/40 characters") },
                leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it.take(160) },
                label = { Text("Implementation notes") },
                placeholder = { Text("Describe behavior, constraints, and edge states") },
                supportingText = { Text("${notes.length}/160") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        InputReferenceSection(
            title = "Validation",
            description = "Validation appears after meaningful input, states the problem, and keeps the correction path available.",
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Review email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                trailingIcon = if (email.isNotEmpty()) {
                    {
                        IconButton(onClick = { email = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear email")
                        }
                    }
                } else {
                    null
                },
                supportingText = {
                    Text(if (emailValid) "Used only for this example" else "Enter a complete address such as name@example.com")
                },
                isError = !emailValid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        InputReferenceSection(
            title = "Password and visibility",
            description = "The visibility action has its own label, and changing visibility never changes the stored value.",
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Preview password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        InputReferenceSection(
            title = "Prefix, suffix, read-only, and disabled",
            description = "Affixes clarify format. Read-only values remain selectable; disabled values communicate temporary unavailability.",
        ) {
            OutlinedTextField(
                value = "material-design",
                onValueChange = {},
                label = { Text("Repository") },
                prefix = { Text("github.com/qtremors/") },
                readOnly = true,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = "1.5.0-alpha23",
                onValueChange = {},
                label = { Text("Material dependency") },
                supportingText = { Text("Pinned by the version catalog") },
                enabled = false,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSample(modifier: Modifier = Modifier) {
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val scope = rememberCoroutineScope()
    val entries = remember {
        listOf(
            "Buttons" to "Actions",
            "Button groups" to "Actions",
            "Cards" to "Containment",
            "Color" to "Foundation",
            "Motion" to "Foundation",
            "Accessibility" to "Foundation",
        )
    }
    val query = textFieldState.text.toString().trim()
    val results = entries.filter { (name, category) ->
        query.isBlank() || name.contains(query, ignoreCase = true) || category.contains(query, ignoreCase = true)
    }
    val inputField = @Composable {
        SearchBarDefaults.InputField(
            textFieldState = textFieldState,
            searchBarState = searchBarState,
            onSearch = { scope.launch { searchBarState.animateToCollapsed() } },
            placeholder = { Text("Search working references", Modifier.clearAndSetSemantics {}) },
            leadingIcon = {
                IconButton(
                    onClick = {
                        if (searchBarState.currentValue == androidx.compose.material3.SearchBarValue.Expanded) {
                            scope.launch { searchBarState.animateToCollapsed() }
                        }
                    },
                ) {
                    Icon(
                        if (searchBarState.currentValue == androidx.compose.material3.SearchBarValue.Expanded) {
                            Icons.AutoMirrored.Filled.ArrowBack
                        } else {
                            Icons.Default.Search
                        },
                        contentDescription = if (searchBarState.currentValue == androidx.compose.material3.SearchBarValue.Expanded) "Close search" else "Search",
                    )
                }
            },
            trailingIcon = if (query.isNotEmpty()) {
                {
                    IconButton(onClick = { textFieldState.edit { replace(0, length, "") } }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            } else {
                null
            },
        )
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(24.dp)) {
        InputReferenceSection(
            title = "Search entry and expansion",
            description = "Search expands from a stable entry point, moves focus into input, and keeps a clear route back.",
        ) {
            SearchBar(
                state = searchBarState,
                inputField = inputField,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                "Tap the search field to inspect live results, empty results, clear, and collapse behavior.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        InputReferenceSection(
            title = "Search is deterministic",
            description = "Exact official names and API symbols should outrank aliases, categories, summaries, and guidance text.",
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Suggested queries", fontWeight = FontWeight.Bold)
                    listOf("fab", "spring physics", "talkback", "connected buttons").forEach { suggestion ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                textFieldState.edit { replace(0, length, suggestion) }
                            }.padding(vertical = 8.dp),
                        ) {
                            Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(20.dp))
                            Text(suggestion, Modifier.padding(start = 12.dp))
                        }
                    }
                }
            }
        }
    }

    ExpandedFullScreenSearchBar(
        state = searchBarState,
        inputField = inputField,
    ) {
        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            if (results.isEmpty()) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("No working references", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Try an official name, API symbol, alias, category, or design behavior.")
                }
            } else {
                results.forEach { (name, category) ->
                    ListItem(
                        onClick = {
                            textFieldState.edit { replace(0, length, name) }
                            scope.launch { searchBarState.animateToCollapsed() }
                        },
                        leadingContent = { Icon(Icons.Default.Search, contentDescription = null) },
                        supportingContent = { Text(category) },
                    ) { Text(name) }
                }
            }
        }
    }
}

@Composable
private fun InputReferenceSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        content()
    }
}
