# Material Design Android Development

Use this skill for implementation and review work inside `material-app/`.

## Product Contract

The installed **Material Design** app is a living, searchable Material 3 component gallery. The running app is the visual and behavioral reference; the modular repository source is the implementation reference.

A user must be able to:

1. Search an official component or foundation name.
2. Interact with relevant variants and states.
3. Read purpose, usage, accessibility, and adaptive guidance.
4. Inspect API availability and stability separately.
5. Copy a real repository-relative source path.

Do not add simulated installs, ratings, popularity, accounts, lesson progress, or catalog entries without working implementations.

## Architecture

- `app/`: activity bootstrap, typed navigation, adaptive application shell, `GalleryViewModel`, dependency container, and demo registry.
- `core/catalog/`: versioned JSON catalog, schema, models, validation, and deterministic search.
- `core/data/`: bookmarks, recent component IDs, appearance settings, and reduced-motion preference through Preferences DataStore.
- `core/designsystem/`: app theme, shared expressive motion, reduced-motion behavior, and cross-cutting Material presentation.
- `feature/`: Explore, Catalog/Search, APIs, Foundations, Detail, and Settings surfaces.
- `samples/`: copyable, state-hoisted implementations grouped by official Material domains.

Add a domain module only when its first working reference exists.

## Component Entry Contract

1. Use the official Material component or foundation name.
2. Keep older terms and abbreviations in searchable aliases.
3. Keep reusable composables independent of navigation, persistence, and feature ViewModels.
4. Hoist state and callbacks.
5. Implement relevant variants, states, semantics, adaptive behavior, and increased-font support.
6. Record exact API artifact and reviewed version.
7. Model artifact availability separately from API stability.
8. Register one demo key and real repository-relative source locations.
9. Add search, source-validation, UI, accessibility, and adaptive light/dark coverage before exposing the entry.

Every visible catalog entry must resolve to real source and exactly one working demo.

## Official Naming

Prefer these names in catalog records and user-facing copy:

- Buttons
- Button groups
- Toggle buttons
- Split buttons
- Floating action buttons
- Progress indicators
- Segmented list items
- Carousels
- Cards
- Lists
- Chips
- Segmented buttons
- Selection controls
- Text fields
- Search
- Dialogs
- Bottom sheets
- Snackbars
- Tooltips
- Menus
- Floating toolbars
- Color, Shape, Motion, Elevation, Layout, Accessibility, and Typography

## Product-Surface Rules

- Use Material components where their actual interaction purpose fits the app.
- “Featured” means curated, “Recently added” comes from catalog metadata, and “Recently viewed” comes from local history.
- Never invent popularity metrics.
- Keep search global and deterministic. Exact names and API symbols outrank aliases, categories, and summaries.
- Adapt from current window space, not device-type assumptions.
- Carousels do not auto-play.
- Label retained custom behavior **Project implementation**; never present it as an official Material API.

## Motion Contract

Use `material-app/core/designsystem/src/main/java/dev/qtremors/material/core/designsystem/ExpressiveMotion.kt` for explicit project spring values.

- Pressed content scales to `0.94`.
- Compact navigation lifts the selected icon by `12.dp`.
- Hold confirmation retains its 1.5-second safety interval.
- The loading example retains its 1.2-second interval.
- Reduced motion removes decorative spatial and opacity movement while preserving state changes, callbacks, haptics, and safety timing.
- Cancel or retarget work safely on release, rapid repeated input, disabled-state changes, recomposition, and disposal.
- Keep official carousel, toolbar, progress-indicator, and navigation physics. Project motion supplements state feedback.

## Verification

From `material-app/`:

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

Also verify:

- Catalog schema and source paths.
- Exactly one registered demo per visible catalog entry.
- Compact, medium, expanded, and relevant height-constrained layouts.
- Increased font size, light/dark appearance, and reduced motion.
- TalkBack semantics and representative interaction flows.

Keep the application label **Material Design**, namespace/application ID `dev.qtremors.materialdesign`, and public version `2.0.4` unless an explicit version change is requested. Update `CHANGELOG.md` for every user-visible change.
