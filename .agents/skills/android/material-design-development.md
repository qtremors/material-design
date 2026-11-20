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
- **`:core:designsystem`:** The Expressive design system: theme, generated color schemes, typography, shape and spacing tokens, motion, haptics, and shared components (scaffold, floating tab bar, connected rows, connected choices, switches, menus, state views).
- **`:core:ui`:** Shared UI that needs domain models, such as list rows and status tags for catalog entries, so the design system itself stays model-free.
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

Wrap the application in one theme composable that builds on `MaterialExpressiveTheme` and reads every appearance choice from a single settings model:

```kotlin
@Composable
fun AppTheme(state: ThemeState, content: @Composable () -> Unit) {
    val dark = when (state.mode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val context = LocalContext.current
    val scheme = remember(state, dark) {
        val base = when {
            state.source == AccentSource.WALLPAPER && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
                if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            state.source == AccentSource.CUSTOM -> seedColorScheme(state.customSeed, dark)
            else -> seedColorScheme(state.preset.seed, dark)
        }
        if (dark && state.pureBlack) base.toPureBlack() else base
    }
    CompositionLocalProvider(LocalReducedMotion provides state.reducedMotion) {
        MaterialExpressiveTheme(
            colorScheme = scheme,
            typography = AppTypography, // One variable font, weights as instances.
            shapes = AppShapes,
            motionScheme = if (state.reducedMotion) ReducedMotionScheme else MotionScheme.expressive(),
            content = content,
        )
    }
}
```

- **Generated color:** `seedColorScheme` builds the full scheme from one seed with Material Color Utilities (for example MaterialKolor's `SchemeTonalSpot`), so presets and custom colors keep proper contrast. Never hand-pick a whole scheme.
- **Stored settings migrate:** when a settings model changes shape, read the old keys once and map them onto the new values, so nobody loses a choice.
- **No wrong first frame:** keep the splash screen until stored settings arrive, with a short ceiling.

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
5. **Instrumented Tests:** Run `.\gradlew.bat :app:connectedDebugAndroidTest` on a device or emulator; compiling them is not the same as running them.

## Official API Coverage

A reference app is accountable for the whole official API of the libraries it documents, not only the components someone remembered to add.

1. A Gradle task reads the pinned sources jars (and the newest stable release) and writes a checked-in inventory of every public symbol with its stability.
2. A ledger lists every symbol no catalog entry cites yet, as `planned` or `excluded` with a reason, and a generated report shows coverage per artifact.
3. Unit tests check every catalog reference against the inventory (existence, deprecation, stability, opt-in, availability, reviewed version) and fail when a symbol is untracked.

After any Material dependency bump:

```powershell
.\gradlew.bat :app:generateMaterialApiInventory
.\gradlew.bat :app:updateApiCoverageLedger
```

Newly published symbols arrive as `new` and fail the tests until each is triaged as `planned` or `excluded`. Fix whatever the tests report in the catalog, then rerun the ledger task to refresh the report.

