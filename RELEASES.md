# Material Design - Releases

> **Project:** Material Design
> **Last Updated:** 2026-08-30

| Version | Release Date | Key Focus |
| :--- | :--- | :--- |
| [v2.1.0](#v210) | 2026-08-30 | First public Android app release with an interactive gallery, validated catalog, practical guidance, and local personalization |

---

# v2.1.0

**Release Date:** August 30, 2026

**Previous public release:** None. This is the first public release.

**Development range included:** Initial project history through v2.1.0.

**Known issues and roadmap:** Track active work in [TASKS.md](TASKS.md).

Material Design v2.1.0 is the first public release of the Android app. It combines an interactive Compose gallery, reviewed component and API metadata, practical design guidance, and local personalization in an offline reference for Android 7.0 and newer.

## Highlights

- **Interactive Material 3 Gallery:** Search, browse, and exercise working component references across actions, communication, containment, navigation, selection, and foundations.
- **Validated Offline Catalog:** Every installed entry has searchable aliases, implementation sources, guidance, official links, and reviewed API metadata aligned with the pinned dependencies.
- **Expressive and Adaptive Product Shell:** The gallery dogfoods Material 3 Expressive styling, motion, responsive navigation, search, details, settings, and accessibility behavior.
- **Practical Material Guidance:** Purpose, use and avoid rules, behavior, accessibility, adaptive considerations, and source locations accompany the demos.
- **Local-First Personalization:** Bookmarks, recent references, themes, dynamic color, accent choices, and reduced-motion preferences stay on the device.

## What's Included

### Android Reference App

- Explore, Catalog, APIs, and Foundations destinations inside an adaptive phone, tablet, foldable, and resizable-window shell.
- Interactive references for buttons, button groups, icon buttons, floating actions, toolbars, progress, pull to refresh, dialogs, sheets, snackbars, tooltips, menus, carousels, lists, search bars, navigation, tabs, sliders, pickers, text fields, and related foundations.
- Search across official names, aliases, descriptions, categories, guidance, and API symbols.
- Stable and experimental API views with artifact versions, opt-in annotations, official URLs, and repository source paths.
- Bookmarks, recent history, category filtering, theme modes, OLED mode, dynamic color, custom accents, and reduced motion.

### Material Guidance and Metadata

- A validated bundled catalog with 55 working entries and no placeholder demonstrations.
- Guidance for purpose, appropriate and inappropriate use, expected behavior, accessibility, and adaptive layout.
- Explicit separation between official Material APIs and project implementations.
- Reviewed Material 3, Compose UI, and stable-baseline metadata enforced by repository tests.

### Engineering and Reliability

- Modular Android architecture separating the application shell, catalog and data layers, design system, feature surfaces, and component samples.
- Convention plugins for Android, Compose, serialization, namespaces, SDK levels, Java/Kotlin targets, and packaging behavior.
- Unit, catalog, motion, backup-rule, navigation, and build-logic coverage.
- Release checks for catalog structure, version metadata, production strings, lint, and optimized APK generation.
- DataStore preferences excluded from cloud backup and device-transfer restoration.

## Requirements and Installation

- Android 7.0 or newer.
- Download the APK from [GitHub Releases](https://github.com/qtremors/material-design/releases).
- Allow installation from the selected browser or file manager when Android requests it.
- No account, network permission, or online service is required by the Android app.

For source builds, testing commands, and signing configuration, see [DEVELOPMENT.md](DEVELOPMENT.md).
