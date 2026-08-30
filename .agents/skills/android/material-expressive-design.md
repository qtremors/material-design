# Material 3 Expressive Design for Android

This skill provides comprehensive guidance on designing and implementing **Material 3 Expressive** and **Adaptive** user interfaces with Jetpack Compose.

## 1. Color System and Dynamic Color

Material 3 uses semantic color roles rather than arbitrary color values.

### Core Color Roles
- **Primary / On Primary / Primary Container / On Primary Container:** For high-emphasis actions and key components.
- **Secondary / Secondary Container:** For less prominent UI elements, filter chips, and accents.
- **Tertiary / Tertiary Container:** For contrasting accents, creative expression, or balanced visual interest.
- **Surface / Surface Container (Lowest, Low, Container, High, Highest):** Provides tonal elevation and hierarchical depth without heavy drop shadows.
- **Outline / Outline Variant:** For subtle borders and structural dividers.
- **Error / Error Container:** For warnings, validation errors, and destructive actions.

### Dynamic Theming Configuration
```kotlin
val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
}
```

## 2. Typography and Variable Fonts

Material 3 defines a 15-token type scale across 5 roles: `Display`, `Headline`, `Title`, `Body`, and `Label` (each with `Large`, `Medium`, and `Small` sizes).

### Variable Font Axes Handling
When using variable fonts (such as Roboto Flex):
- Clamp font variation axes (`opsz`, `wght`, `wdth`, `GRAD`) to the font's supported ranges.
- Treat animated font axes as progressive enhancement.
- Ensure layouts never clip text when system font scaling is increased up to 200%.

## 3. Shape, Elevation, and Surface Grouping

- **Tonal Elevation:** Surfaces change color shade (using `SurfaceContainerLow`, `SurfaceContainer`, `SurfaceContainerHigh`) rather than casting strong drop shadows.
- **Expressive Shapes:** Use asymmetric or rounded container shapes to communicate grouping (e.g. connected card items, segmented button sets).
- **Interactive State Layers:** Visual feedback for hover, focus, press, and drag is applied via standardized alpha overlays over the container color.

## 4. Expressive Motion and Spring Physics

Material 3 Expressive replaces rigid duration curves with natural spring physics.

### Motion Springs Contract
```kotlin
object ExpressiveMotion {
    // Fast, responsive spring for press feedback
    val PressSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    // Smooth spatial reflow and container expansion
    val SpatialSpring = spring<IntOffset>(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMediumLow
    )

    // Shape morphing and settling
    val ShapeSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMediumLow
    )
}
```

### Reduced Motion
Respect `prefers-reduced-motion` settings. When reduced motion is enabled:
- Remove decorative spatial movement, bouncing, and scale oscillations.
- Preserve instantaneous state changes, haptic feedback, and safety timing (e.g. hold-to-confirm).

## 5. Adaptive Layouts with Material 3 Adaptive

Design for window space rather than specific device categories:

| Size Class | Window Width | Navigation Pattern | Layout Structure |
| --- | --- | --- | --- |
| **Compact** | `< 600 dp` | `NavigationBar` or `BottomAppBar` | Single Pane |
| **Medium** | `600 - 839 dp` | `NavigationRail` | Single Pane or Supporting Pane |
| **Expanded** | `840 - 1199 dp` | `NavigationRail` / `NavigationDrawer` | List-Detail or Supporting Pane |
| **Large / XL** | `>= 1200 dp` | Permanent `NavigationDrawer` | Multi-Pane with Max Reading Width |

### NavigationSuiteScaffold
```kotlin
@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
@Composable
fun AppShell(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit,
    content: @Composable () -> Unit
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestination.entries.forEach { dest ->
                item(
                    selected = currentDestination == dest,
                    onClick = { onNavigate(dest) },
                    icon = { Icon(dest.icon, contentDescription = dest.label) },
                    label = { Text(dest.label) }
                )
            }
        }
    ) {
        content()
    }
}
```

## 6. Expressive Component Blueprints

### Split Button
Combines a primary action with a contextual dropdown menu:
```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveSplitButton(
    onPrimaryClick: () -> Unit,
    onMenuClick: () -> Unit,
    menuExpanded: Boolean,
    onDismissMenu: () -> Unit
) {
    SplitButtonLayout(
        leadingButton = {
            Button(onClick = onPrimaryClick) {
                Text("Save")
            }
        },
        trailingButton = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More save options")
            }
        }
    )
}
```

### Floating Toolbar
Floating action surface that adapts between horizontal and vertical orientations:
```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveFloatingToolbar(
    actions: List<ToolbarAction>,
    modifier: Modifier = Modifier
) {
    HorizontalFloatingToolbar(
        modifier = modifier,
        shape = FloatingToolbarDefaults.ContainerShape
    ) {
        actions.forEach { action ->
            IconButton(onClick = action.onClick) {
                Icon(action.icon, contentDescription = action.title)
            }
        }
    }
}
```

### Wavy Progress Indicator
Expressive progress representation for determinate or indeterminate operations:
```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExpressiveWavyProgress(
    progress: Float,
    modifier: Modifier = Modifier
) {
    WavyLinearProgressIndicator(
        progress = { progress },
        modifier = modifier.fillMaxWidth()
    )
}
```

## 7. Quality and Verification Checklist

- [ ] All components use semantic `MaterialTheme.colorScheme` and `MaterialTheme.typography` tokens.
- [ ] Interactive touch targets satisfy the minimum 48x48 dp standard.
- [ ] Dynamic color works gracefully in both light and dark themes.
- [ ] Responsive navigation adapts seamlessly between compact, medium, and expanded window sizes.
- [ ] All motion transitions support a reduced-motion fallback.
- [ ] Screen readers (TalkBack) correctly announce component roles, labels, and state changes.
