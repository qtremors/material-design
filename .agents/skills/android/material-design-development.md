# Material Design Android Development

This skill provides architectural guidance, patterns, and best practices for building production Android applications using Material Design 3 and Jetpack Compose.

## Architectural Principles

Structure modern Material 3 Android apps into distinct, well-defined layers:

```text
┌─────────────────────────────────────────────────────────┐
│                        UI Layer                         │
│  - Composable Screens & Components                      │
│  - ViewModels & UI State Holders (Unidirectional Flow)  │
│  - Navigation Destinations & Adaptive Scaffolds         │
└───────────────────────────┬─────────────────────────────┘
                            │ observes UiState / emits Events
┌───────────────────────────▼─────────────────────────────┐
│                      Domain Layer                       │
│  - Use Cases & Business Logic Operations                │
└───────────────────────────┬─────────────────────────────┘
                            │ accesses
┌───────────────────────────▼─────────────────────────────┐
│                       Data Layer                        │
│  - Repositories (Single source of truth)                │
│  - Local Data Sources (DataStore, Room, SQLite)         │
│  - Remote Data Sources (Ktor, Retrofit, WebSockets)     │
└─────────────────────────────────────────────────────────┘
```

### Module Organization
- **`:app`:** Application entry point, dependency injection / composition root, global navigation wiring.
- **`:core:designsystem`:** Design tokens, `MaterialTheme` configuration, color schemes, typography scale, shape definitions, and motion contracts.
- **`:core:data` / `:core:model`:** Data repositories, domain models, and persistence engines.
- **`:feature:<name>`:** Independent feature destinations containing screens, ViewModels, and UI state models.
- **`:samples` or `:ui-components`:** Reusable, state-hoisted UI components independent of business logic.

## State Management and Unidirectional Data Flow (UDF)

Implement state management using immutable UI state and Kotlin Coroutines / Flow:

```kotlin
// 1. Define immutable UI state
data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val errorMessage: String? = null
)

// 2. ViewModel exposes state via StateFlow
class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = userRepository.getUser()
                _uiState.update { it.copy(isLoading = false, user = user) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}
```

### Composable Consumption
In UI composables, collect state lifecycle-safely using `collectAsStateWithLifecycle()`:

```kotlin
@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = viewModel(),
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        uiState = uiState,
        onRefresh = viewModel::loadUserProfile,
        onNavigateToSettings = onNavigateToSettings
    )
}
```

## Type-Safe Navigation with Navigation Compose

Use Kotlinx Serialization with Navigation Compose for type-safe route definitions:

```kotlin
// 1. Declare serializable routes
@Serializable
object HomeDestination

@Serializable
data class DetailDestination(val itemId: String)

// 2. Configure NavHost
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination,
        modifier = modifier
    ) {
        composable<HomeDestination> {
            HomeScreen(
                onItemClick = { id ->
                    navController.navigate(DetailDestination(itemId = id))
                }
            )
        }
        composable<DetailDestination> { backStackEntry ->
            val route: DetailDestination = backStackEntry.toRoute()
            DetailScreen(itemId = route.itemId, onBackClick = { navController.popBackStack() })
        }
    }
}
```

## Material 3 Theme Integration

Wrap your application in a custom `MaterialTheme` wrapper:

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
```

## Component State Hoisting Contract

To ensure UI components are testable, reusable, and previewable:

- Separate **Stateful** (reads ViewModel, handles navigation callbacks) from **Stateless** (accepts plain data and lambda callbacks).
- Use `Modifier` as the first optional parameter.
- Never pass `NavController` or `ViewModel` deep into child composables.

```kotlin
// Stateless component
@Composable
fun ItemCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
```

## Accessibility and Usability Standards

- **Touch Targets:** Maintain minimum interactive touch targets of 48x48 dp (`Modifier.minimumInteractiveComponentSize()`).
- **Content Descriptions:** Provide meaningful `contentDescription` for icon buttons, images, and decorative elements (`null` if purely decorative).
- **Semantics:** Merge semantics for compound items (e.g. list items with text and checkboxes) using `Modifier.semantics(mergeDescendants = true) { ... }`.
- **Dynamic Font Scaling:** Support up to 200% system font scaling without text clipping or truncated critical actions.
- **Edge-to-Edge:** Use `enableEdgeToEdge()` and apply `WindowInsets.safeDrawing` / `WindowInsets.systemBars` padding.

## Verification and Testing

1. **JVM Unit Tests:** Verify ViewModels, repositories, and state transformation logic with `kotlinx-coroutines-test`.
2. **Compose UI Tests:** Test composable states, accessibility semantics, and user interactions with `createComposeRule()`.
3. **Android Lint:** Run `.\gradlew.bat lintDebug` to catch accessibility, performance, and API misuse issues.
4. **Build Verification:** Run `.\gradlew.bat testDebugUnitTest assembleDebug` before committing changes.

