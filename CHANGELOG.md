# Material Design Changelog

> **Project:** Material Design  
> **Version:** 1.0.0  
> **Last Updated:** 2026-02-13



## [1.1.1] - 2026-02-13

### Fixed
-   **Security:** Replaced inline `onclick` handlers with `data-action` attributes in `navigation.html`.
-   **Settings:** Fixed duplicated DOM nesting in `settings.html` and improved reset functionality to only clear relevant keys.
-   **Tabs:** Scoped tab panel toggling to the parent container to prevent conflicts.

### Changed
-   **Refactor:** Updated `navigation.js` path detection to be environment-agnostic using script tag inspection.

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
