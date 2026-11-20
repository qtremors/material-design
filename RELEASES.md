# Material Design - Releases

> **Project:** Material Design
> **Last Updated:** 2026-11-20

| Version | Release Date | Key Focus |
| :--- | :--- | :--- |
| [v2.1.5](#v215) | 2026-11-20 | A full Material 3 Expressive redesign, Material 3 1.5.0-rc01, adaptive layouts for large screens, and measured official API coverage |
| [v2.1.0](#v210) | 2026-08-30 | First public Android app release with an interactive gallery, validated catalog, practical guidance, and local personalization |

---

# v2.1.5

**Release Date:** November 20, 2026

**Previous public release:** [v2.1.0](#v210)

**Development range included:** v2.1.1 through v2.1.5.

**Known issues and roadmap:** Track active work in [TASKS.md](TASKS.md) and the measured API backlog in [COVERAGE.md](material-app/core/catalog/api/COVERAGE.md).

Material Design v2.1.5 rebuilds the whole app on Material 3 Expressive, so the app around the demos is now a reference in its own right. It moves to Material 3 1.5.0-rc01, adapts to tablets and foldables, and makes the catalog accountable to the complete official API.

## Highlights

- **A Material 3 Expressive app, not only demos:** Google Sans Flex typography, large flexible top bars that collapse on scroll, connected groups and toggle buttons, expressive switches and menus, and loading indicators across every screen.
- **Colors generated from a seed:** follow your wallpaper, pick one of six presets, or enter any color; Material Color Utilities builds a complete scheme with proper contrast in light, dark, and pure black.
- **Fluid navigation:** a floating tab bar that morphs into Back and the page title, transitions that match predictive back, and shared-element transitions that carry an entry's badge into its page.
- **Built for large screens:** tablets, unfolded foldables, and desktop windows get the wide navigation rail and the list beside the open entry.
- **Material 3 1.5.0-rc01:** Expressive APIs that graduated now show as stable, and every API reference was re-reviewed against the library sources.
- **Measured official API coverage:** every public symbol in Material 3 and Material 3 Adaptive is tracked, so the catalog shows exactly which official APIs it demonstrates.

## What's New

### Design and Navigation

- Rebuilt Explore as a hub with counts, featured references in the official carousel, bookmarks, recently viewed, categories, and what each release added.
- Rebuilt Catalog, APIs, and Foundations on connected rows, category filters, and maturity tags that state both stability and availability.
- Redesigned the component page around Preview, Guidance, Inspect, and API as connected toggle buttons, with icon-led use and avoid guidance and copyable source paths.
- Moved search to its own full-screen page with suggestions, recently viewed entries, and tagged results.
- Added one long-press menu to every list of entries: open, bookmark, or copy the source path for an agent or an editor.

### Settings and Personalization

- Redesigned Settings with theme mode, pure black, color source with preset swatches or a custom seed, a live preview of color roles, haptics, reduced motion, and maturity badges.
- Redesigned About with project links, versions, credits, and device details you can copy for a bug report.
- Existing theme choices carry over: OLED mode becomes Dark with pure black, and accent colors and named presets become the same color as a custom seed.
- Removed two settings that had no effect, and Haptics now controls every vibration in the app.

### Catalog and Reference Data

- Split Selection controls into Checkbox, Radio button, and Switch references; bookmarks of the old entry move to Checkbox automatically.
- Corrected 26 availability values, two API references, and several categories, so Divider, Search, Text fields, and Scaffold sit where Material groups them.
- Standardized entry names, so each reference has one official name with older names kept for search.

### Engineering and Reliability

- Upgraded to Material 3 1.5.0-rc01, Android Gradle Plugin 9.5.1, Kotlin 2.4.21, Gradle 9.8.2, and current AndroidX libraries.
- Added a generated API inventory, a coverage ledger, and tests that check every catalog reference for existence, stability, availability, and reviewed version.
- Version checks moved from app startup into build-time tests, so a metadata mismatch can never stop the app from opening.
- A splash screen holds until your saved theme loads, so the app never opens in the wrong colors.
- The back gesture now slides and fades like the Back button instead of shrinking the page into a sharp rectangle.
- The instrumented test suite runs again and passes on a physical device.

## Requirements and Installation

- Android 7.0 or newer.
- Download the APK from [GitHub Releases](https://github.com/qtremors/material-design/releases).
- No account, network permission, or online service is required by the Android app.

For source builds, testing commands, and signing configuration, see [DEVELOPMENT.md](DEVELOPMENT.md).

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
