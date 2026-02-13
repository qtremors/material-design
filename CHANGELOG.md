# Material Design Changelog

> **Project:** Material Design  
> **Version:** 1.2.1  
> **Last Updated:** 2026-02-13

---


## [1.2.1] - 2026-02-13

### Technical Compliance
-   **Tokens:** Standardized the design token system with complete MD3 roles including `surface-variant`, `inverse-surface`, and `inverse-primary`.
-   **RGB Variants:** Added `--md-sys-color-*-rgb` tokens to support high-fidelity interaction effects like slider halos.
-   **States:** Replaced hardcoded interaction layers with a standardized `::before` state layer system using MD3 opacities.
-   **Elevation:** Implemented the "Tonal Elevation" logic where elevated surfaces receive a primary-tinted container overlay.
-   **Motion:** Standardized component transitions (Labels, Drawers, Speed Dials) using MD3-standard cubic-bezier curves.

## [1.2.0] - 2026-02-13

### Added
-   **Expressive Components:** Implemented Speed Dial (Expandable FAB) with staggered entry animations.
-   **Expressive Components:** Added Horizontal Drawer component with smooth right-side expansion.
-   **Expressive Components:** Implemented Wavy and Segmented progress bars for high-fidelity loading states.
-   **Guidelines:** Added strict technical standards and compliance requirements for MD3, Material You, and Expressive traits in `DEVELOPMENT.md`.

### Changed
-   **Layout:** Reorganized the FAB section into a flexible 3-column grid for better demonstration of expressive components.
-   **Theming:** Fully integrated MD3 theme variables across all expressive components, enabling dynamic color switching and dark mode support.
-   **Alignment:** Shifted component demos to left-alignment within columns to prevent collisions during expansion.
-   **Refactor:** Streamlined `buttons.html` by removing redundant standard FAB size demos.

---

## [1.1.1] - 2026-02-13

### Fixed
-   **Security:** Replaced inline `onclick` handlers with `data-action` attributes in `navigation.html`.
-   **Settings:** Fixed duplicated DOM nesting in `settings.html` and improved reset functionality to only clear relevant keys.
-   **Tabs:** Scoped tab panel toggling to the parent container to prevent conflicts.

### Changed
-   **Refactor:** Updated `navigation.js` path detection to be environment-agnostic using script tag inspection.

---

## [1.1.0] - 2026-02-13

### Security
-   **Refactor:** Removed inline `onclick` attributes from all HTML files to improve CSP compliance and security.
-   **Hardening:** Implemented `escapeHtml` and `isValidUrl` in `navigation.js` to prevent XSS and Open Redirect vulnerabilities.

### Added
-   **Mobile Navigation:** Added `.menu-trigger` buttons to all content pages to allow Drawer access on mobile devices.
-   **Interactions:** Centralized event delegation in `scripts.js` for handling Dialogs, Snackbars, Sheets, and Theme Toggles.

### Fixed
-   **UI:** Fixed vertical misalignment between the Hamburger Menu icon and Page Title in the top app bar.
-   **Visual:** Fixed Ripple Effect positioning bug on Navigation Rail items (was appearing in top-left).
-   **Accessibility:** Fixed poor text contrast on "Filled" buttons in Dark Mode by removing forced white text.

### Changed
-   **Architecture:** Moved from inline JavaScript to a global Event Delegation model using `data-action` attributes.

---

## [1.0.0] - 2026-02-13

### Added
-   **Core:** Initial release of Material Design Showcase.
-   **Components:** Buttons, Cards, Inputs, Checkboxes, Switches, Chips, Dialogs, Bottom Sheets.
-   **Theming:** Dynamic Theme Engine with Light/Dark mode and 5+ color seeds.
-   **Navigation:** Responsive Navigation Rail and Drawer.
-   **Documentation:** README, DEVELOPMENT, CHANGELOG, and TASKS.
