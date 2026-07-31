# Material 3 Expressive Web Development

Use this skill for implementation work inside `material-web/`.

Read `skills/web/material-design-ui.md`, `docs/material-3-expressive-development.html`, and `docs/parity.html` before starting a component.

## Preconditions

1. The corresponding Android entry is marked **Android Refined**.
2. Its accepted states, interaction intent, accessibility behavior, and adaptive changes are documented.
3. Current official Material guidance has been checked.
4. The web API and implementation plan are independent; Compose signatures are not treated as browser APIs.

## Implementation Contract

- Use semantic HTML, modern CSS, and vanilla JavaScript.
- Keep web tokens, components, state, tests, and releases independent from Android.
- Express color, type, shape, spacing, elevation, and motion through web-owned custom properties.
- Prefer native elements and behaviors before adding ARIA.
- Support keyboard, pointer, touch, assistive technology, high contrast, forced colors, reduced motion, and zoom.
- Adapt to the available viewport rather than device labels.
- Preserve user state, focus, and action outcomes when layout or input mode changes.
- Avoid inline styles in reusable component implementations.

## Component Workflow

1. Write a state table covering every applicable state.
2. Choose semantic HTML and define the accessible name, role, value, state, and keyboard model.
3. Define the public JavaScript interface without copying Compose signatures.
4. Implement structure, layout, and tokens before decorative motion.
5. Add expressive typography, shape, and motion without hiding state changes or delaying core actions.
6. Test compact, medium, expanded, and compact-height layouts where relevant.
7. Verify keyboard-only use, screen-reader output, 200% zoom, long text, forced colors, and reduced motion.
8. Add focused automated tests and run the complete web validation available in the repository.
9. Update `TASKS.md`, `CHANGELOG.md`, public HTML guidance, and the matching Markdown skill.

## Pattern Requirements

### Split Buttons

- Use separate focusable controls for the primary action and menu trigger.
- Keep the visual group clear without merging semantics.
- Give the trigger an accessible name and expose expanded state.

### Wavy Progress Indicators

- Preserve determinate versus indeterminate meaning.
- Use native or ARIA progress semantics as appropriate.
- Clamp determinate values and expose status text when users need it.
- Reduce or remove wave motion under `prefers-reduced-motion`.

### Dynamic Typography

- Treat variable-font axes as progressive enhancement.
- Test reflow across every supported axis extreme.
- Keep semantic heading levels independent of visual styling.

### Morphing and Hold Confirmation

- Keep press morphing brief, reversible, and interruptible.
- Hold confirmation provides visible progress and cancellation.
- Preserve safety timing with reduced motion.
- Provide a non-hold alternative when required.

### Segmented Controls and Sections

- Use the correct single-select or multi-select model.
- Preserve readable boundaries and visible focus across joined shapes.
- Support arrow-key behavior only when the selected ARIA pattern requires it.

### Floating Navigation and Toolbars

- Do not obscure focused or primary content.
- Account for safe areas and compact-height layouts.
- Keep every action keyboard reachable.
- Preserve state when changing orientation or navigation presentation.

## Completion Checklist

- The implementation matches accepted intent without sharing Android code.
- Compact, medium, expanded, zoomed, and long-text layouts are verified.
- Keyboard and screen-reader semantics are verified.
- Focus remains visible and unobscured.
- Forced colors and reduced motion remain usable.
- Intentional platform differences are documented.
- Status, tasks, HTML documentation, Markdown skills, and changelog entries are current.
