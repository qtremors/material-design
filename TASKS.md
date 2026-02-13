# Material Design - Tasks

> **Project:** Material Design  
> **Version:** 1.1.0  
> **Last Updated:** 2026-02-13

---

## High Priority

- [x] **Refactoring & Security**
    - [x] Hardware navigation.js for path traversal/XSS
    - [x] Refactor inline `onclick` handlers to `addEventListener`
    - [x] Fix accessibility: Contrast issues in Dark Mode (Buttons)
    - [x] Fix bug: Ripple effect positioning on Nav Rail items
    - [x] Fix accessibility: Mobile navigation menu trigger missing on inner pages
    - [x] Fix UI: Misalignment between Hamburger Menu and Page Titlebuttons.html`) to use `addEventListener`.
    - [x] Sanitize or validate input for `NAV_ITEMS` in `navigation.js` if dynamic sources are ever introduced.

## Medium Priority

- [ ] **Code Quality**
    - [ ] Refactor `navigation.js` path handling to be less brittle (currently relies on `/src/` string check).
    - [ ] Remove unused CSS rules if found (e.g., check for unused `z-index` layers).
    - [] Review color contrast ratios for text on `primary` and `secondary` containers.
