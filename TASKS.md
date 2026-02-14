# Material Design Tasks (v1.4.0)

> **Project:** Material Design  
> **Version:** 1.4.0
> **Last Updated:** 2026-02-14

---

## 🚀 v1.4 Planned Roadmap: "Selling the System"
Focus on maturing the showcase from a component list to a professional design system portal.

### 1. Developer Mode & DX Tools
- [ ] **Implementation**: Add a "Developer Mode" toggle in `settings.html` (persisted in `localStorage`).
- [ ] **Copy-to-Code**: 
    - [ ] Add "Copy HTML" buttons to all component demo blocks.
    - [ ] These buttons should only be visible when "Developer Mode" is ON.
    - [ ] Implement global clipboard logic in `scripts.js`.
- [ ] **Dependency Stats**: Show a small dashboard/badge showing 0 dependencies and total CSS/JS byte size (only in Dev Mode).

### 2. Layouts & Templates
- [ ] **Architecture**: Create a `/templates` folder in the root.
- [ ] **Mock Shell - Inbox**: Rail + Fixed Top Bar + Adaptive List + FAB.
- [ ] **Mock Shell - Dashboard**: Grid Layout + Cards + Filter Chips + Search.
- [ ] **Mock Shell - Settings**: Hierarchical settings list + responsive preview area.

### 3. Navigation & Discovery
- [ ] **Command Search (Ctrl+K)**: 
    - [ ] Build a modal overlay for site-wide search.
    - [ ] Index existing component pages via `navigation.js`.
- [ ] **Navigation Refinement**: Consolidate rail and drawer interactions for better template support.

### 4. Documentation & Education
- [ ] **Get Started Page**: 
    - [ ] Implementation of content for `index.html` (the button exists but target is empty/missing).
    - [ ] Instructions for linking `styles.css` and `scripts.js`.
    - [ ] Explanation of CSS variable theming (`data-seed`, `data-theme`).
- [ ] **Shapes & Motion Showcase**:
    - [ ] New page dedicated to the "Feel" of the system.
    - [ ] Interactive demos for Spring Easing and Corner Radius tokens.

---

## 🧠 Technical Context & Specs (For Future Reference)
*Keep this section updated to prevent context loss between sessions.*

### Theming Architecture
- **Attributes**: The system is driven by `data-*` attributes on `<html>`:
    - `data-theme`: `light` | `dark`
    - `data-seed`: `purple` | `teal` | `red` | `green` | `pink` | `orange` | `yellow` | `cyan`
    - `data-radius`: `small` | `medium` | `large` | `full`
    - `data-style`: `standard` | `expressive`

### Navigation Data
- **Source**: `navigation.js` contains the source-of-truth for the Rail and Drawer items.
- **Search Indexing**: The Ctrl+K search should consume the `items` array from this file.

### Component Requirements
- **Interactive Elements**: Must use `ripple-target` class for the vanilla ripple effect.
- **Pathing**: `navigation.js` contains deterministic path detection logic to work across different subfolder depths.
- **Safety**: Always use null-guards for DOM queries as the system is used across multiple disparate HTML pages.

### Developer Mode (v1.3 Specs)
- **State**: `localStorage.getItem('devMode') === 'true'`.
- **Visibility**: Use a CSS class (e.g., `.dev-only`) controlled by the body attribute `data-dev-mode="true"` to toggle snippet buttons.
