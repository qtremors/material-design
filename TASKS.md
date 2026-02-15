# Material Design Tasks (v1.4.0)

> **Project:** Material Design  
> **Version:** 1.4.0
> **Last Updated:** 2026-02-15

---

## Maintenance & Refactoring (Deep Analysis)

### 🟡 Architecture & Logic
- [ ] **Consolidate Theme Logic**: 
    - [ ] Move fallback logic from `scripts.js` (lines 30-39) to `theme.js` to create a single source of truth.
    - [ ] **Logic Conflict**: Resolve duplicate `keydown` listeners between `scripts.js` (lines 96-110) and `interactions.js` (lines 210-218). Both listen for 'Enter'/'Space' on similar elements.
- [ ] **Automate Seeds & Navigation**: 
    - [ ] Create a `layout.js` to inject `color-swatch` HTML (currently hardcoded in `index.html` lines 112-117) via JS.
    - [ ] Move `NAV_ITEMS` to a JSON config.
- [ ] **Widget Grid Robustness**: 
    - [ ] Refactor `widgets.js` `snapToGrid` (lines 141-171) and `packWidgets`.
    - [ ] Remove hardcoded `100` divisor for columns. Use `ResizeObserver` to determine cell size dynamically based on container width.

#### ⚪ Code Quality & Style
- [ ] **CSS Scoping**: 
    - [ ] Move global animations from `buttons.css` to `motion.css`.
- [ ] **Linting**: Add `.eslintrc`.

### Documentation & Consistency Gaps
- [ ] **Missing Interactions Doc**: `src/js/components/interactions.js` is the core event delegate but is completely missing from `DEVELOPMENT.md` "Key Components".
- [ ] **Incorrect References**: `DEVELOPMENT.md` implies `initRipples` is in `scripts.js`. It is actually in `src/js/components/ripples.js`.
- [ ] **Widgets Documentation**: The bin-packing logic in `widgets.js` is complex and undocumented in code or markdown.
- [ ] **Orchestration Clarity**: Clarify in docs that `scripts.js` is purely an orchestrator and does not contain component logic.
