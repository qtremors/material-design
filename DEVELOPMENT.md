# Material Design Development Guide (v1.4.0)

> Comprehensive documentation for developers working on Material Design.

**Version:** 1.4.0 | **Last Updated:** 2026-02-14

---

## Table of Contents

- [Architecture Overview](#architecture-overview)
- [Project Structure](#project-structure)
- [Key Components](#key-components)
- [Configuration](#configuration)
- [Contributing](#contributing)

---

## Architecture Overview

**Material Design** is a **Static Web Application** with no compile step. It follows a component-based architecture where HTML files represent views, and shared logic is injected or imported.

```
┌──────────────────────────────────────────────────────────────┐
│                         HTML Pages                           │
│              (index.html, buttons.html, etc.)                │
└──────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌──────────────────────────────────────────────────────────────┐
│                    JavaScript Modules                        │
│         (Navigation Injection, Theme Engine, Ripples)        │
└──────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌──────────────────────────────────────────────────────────────┐
│                       CSS System                             │
│          (Variables, Design Tokens, Component Styles)        │
└──────────────────────────────────────────────────────────────┘
```

### Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| **Zero Dependencies** | Ensures maximum performance, easy learning curve, and no build toolchain required. |
| **CSS Variables** | Allows for dynamic theming (Light/Dark, Color Seeds) without SASS/LESS preprocessors. |
| **Vanilla JS** | Removes framework overhead; ideal for understanding the underlying mechanics of components. |

---

## Project Structure

```
material-design/
├── src/                  # Source code
│   ├── css/
│   │   ├── components/       # Component-specific styles
│   │   │   ├── buttons.css
│   │   │   ├── cards.css
│   │   │   ├── inputs.css
│   │   │   └── ...
│   │   ├── base.css          # Core foundations (Reset, Typography)
│   │   └── variables.css     # Design tokens (colors, type, elevation)
│   │   └── widgets/          # Widget-specific styles
│   │       ├── structure.css
│   │       ├── music.css
│   │       └── ...
│   ├── js/
│   │   ├── components/       # Component logic
│   │   │   ├── ripples.js
│   │   │   ├── tabs.js
│   │   │   ├── dialogs.js
│   │   │   └── ...
│   │   ├── theme.js          # Theme abstraction and persistence
│   │   ├── navigation.js     # Navigation Rail/Drawer injection
│   │   └── scripts.js        # Main entry point / Orchestrator
│   ├── assets/               # Static images/icons
│   └── *.html                # Individual component showcases
├── index.html            # Entry point
├── README.md             # User-facing documentation
├── DEVELOPMENT.md        # This file
├── CHANGELOG.md          # Version history
└── TASKS.md              # Task tracking
```

---

## Key Components

### Theme Engine (`src/js/theme.js`)

Manages the application state for Theme (Light/Dark) and Color Seeds.

-   **Persistence:** Uses `localStorage` and `window.name` (for session sync).
-   **Application:** Sets `data-theme` and `data-seed` attributes on the `<html>` element.

| Method/Function | Description |
|-----------------|-------------|
| `ThemeEngine.init()` | Loads state and applies it immediately to prevent flash. |
| `ThemeEngine.load()` | Retrieves state from storage or window.name. |
| `ThemeEngine.apply()` | Updates DOM attributes based on current state. |

### Navigation (`src/js/navigation.js`)

Injects the Navigation Rail (desktop) and Drawer (mobile) into the DOM.

| Method/Function | Description |
|-----------------|-------------|
| `renderNavigation()` | Generates HTML for nav rail/drawer and appends to `body`. |

### Ripples (`src/js/scripts.js`)

Custom implementation of the material ink ripple.

| Method/Function | Description |
|-----------------|-------------|
| `initRipples()` | Attaches click listeners to `.ripple-target` elements. |
| `createRipple(e, el)` | Calculates exact position and animates the ripple span. |


---

## Technical Standards & Strict Compliance

All new additions to this library **must** strictly adhere to the following Material Design specifications to maintain a cohesive "feel" and behavior.

### 1. Design Systems & Guidelines
-   **Google Material Design 3 (MD3):** All components **MUST** strictly follow the official [Material Design 3 Guidelines](https://m3.material.io/). This includes layout, spacing, typography, and interaction states.
-   **Material You (Dynamic Color):** All components **MUST** be fully themeable. They must react immediately to `data-seed` changes and support both Light and Dark modes without exception.
-   **Material 3 Expressive:** Adopt "Expressive" traits for high-impact components. Use rounder shapes, playful animations, and unique layouts as defined in the [MD3 Expressive Guidelines](https://m3.material.io/styles/motion/overview).

### 2. Animations & Effects
-   **Duration & Easing:** Use MD3 standard durations (200ms-400ms) and easing functions (standard, emphasized).
-   **Ripples:** Every interactive element must have a ripple effect using the `.ripple-target` class.
-   **Expressive Motion:** For complex transitions (e.g., shape morphing), use `cubic-bezier(0.175, 0.885, 0.32, 1.275)` for a bouncy, elastic feel.

### 3. Theme & Feel
-   **NO HARDCODED COLORS:** Usage of hex codes (e.g., `#FFFFFF`, `#000`) or named colors (e.g., `white`, `black`) is **STRICTLY PROHIBITED** in component styles. You **MUST** always use `--md-sys-color-*` tokens to ensure proper dynamic theming.
-   **State Layers:** Use standard state layer opacities (`.08` for hover, `.12` for pressed) via CSS variables.
-   **Naming:** Follow the MD3 naming schema for variables and classes (e.g., `surface-container-high`, `on-primary-container`).

---

## How to Create New Components

### 1. Create the HTML Page
Create a new file (e.g., `my-component.html`) in the `src/` directory.
> **Tip:** Copy `src/buttons.html` or `src/cards.html` to use as a template. This ensures you have the correct `<head>` logic, viewport meta tags, and script imports.

### 2. Add Styles
Create a new CSS file in `src/css/components/` (e.g., `my-component.css`) and link it in your HTML file.
-   **Strictly use CSS variables** from `src/css/variables.css` for colors (`--md-sys-color-*`), typography (`--md-sys-typescale-*`), and shapes.
-   Do not hardcode hex values or pixel sizes unless absolutely necessary.

### 3. Register Navigation
To make your new page accessible, you must register it in **two places**:

#### A. Navigation Rail & Drawer (`src/js/navigation.js`)
Add a new object to the `NAV_ITEMS` array:
```javascript
const NAV_ITEMS = [
    // ... existing items
    { label: 'New Component', icon: 'extension', url: 'my-component.html' } // Use a valid Material Symbol name for the icon
];
```

#### B. Dashboard Index (`index.html`)
Add a new card to the `.dashboard-grid` container:
```html
<a href="my-component.html" class="md-card nav-card ripple-target">
    <span class="material-symbols-rounded nav-card-icon">extension</span>
    <h3>New Component</h3>
    <p>A brief description of the component.</p>
</a>
```

### 4. Create New Widgets
Widgets are specialized components used in the grid layout (`widgets.html`).

#### A. Structure (`src/widgets.html`)
- **Wrapper**: specific ID + `widget-wrapper` + size class (e.g., `w-2x2`, `w-4x2`).
- **Heading**: `<h3>` with title and `<span class="grid-label">` for size.
- **Content**: A container (often `.md-card`) holding the widget UI.

```html
<!-- Example Widget -->
<div id="widget-example" class="widget-wrapper w-2x2">
    <h3>Title <span class="grid-label">2x2</span></h3>
    <div class="md-card card-filled">
        <!-- Widget Content -->
    </div>
</div>
```

#### B. Styles (`src/css/widgets/`)
-   **Modular CSS**: Styles should be placed in `src/css/widgets/` (e.g., `my-widget.css`).
-   **Import**: Link the new CSS file in `widgets.html`.
-   **Scoping**: All widget styles **MUST** be scoped to the widget's ID.

```css
#widget-example .card-example {
    background: var(--md-sys-color-surface-container);
    /* ... */
}
```

#### C. Interactivity (`src/js/components/widgets.js`)
- If the widget requires JS (e.g., toggles, updates), add a specific initialization block or function in `initWidgets()`.

---

## Configuration

Project configuration is primarily handled via CSS Variables in `src/css/variables.css`.

### Design Tokens

| Token Group | Description |
|-------------|-------------|
| `--md-sys-color-*` | Color system (Primary, Surface, Error, etc.) |
| `--md-sys-typescale-*` | Typography styles (Headline, Body, Label) |
| `--md-sys-shape-*` | Corner radiuses |
| `--md-sys-elevation-*` | Box shadows for elevation levels |

---

## Contributing

### Code Style

-   **HTML:** Semantic HTML5 strictly.
-   **CSS:** **Mandatory** use of CSS Variables for all values. No magic numbers, no hardcoded colors.
-   **JS:** ES6+ syntax. Comment public functions and document animation logic.
-   **Indentation:** 4 spaces.
-   **Compliance:** All code must pass the "MD3 Feel Test"—smooth animations, standard corner radiuses, and correct elevation.

### Pull Request Process

1.  Fork the repository
2.  Create a feature branch (`git checkout -b feature/new-component`)
3.  Implement your changes following the [Technical Standards](#technical-standards--strict-compliance)
4.  **Verify Compliance:**
    -   Test in both Light and Dark modes.
    -   Test across all Color Seeds (Blue, Purple, Green, etc.).
    -   Check that all interactive elements have Ripples.
    -   Ensure animations follow MD3 durations/easing.
5.  Commit with clear, descriptive messages
6.  Push and create a Pull Request

---

<p align="center">
  <a href="README.md">← Back to README</a>
</p>
