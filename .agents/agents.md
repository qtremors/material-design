# Material Design Repository Agent Guide

This file is the durable operating context for any human, LLM, or coding agent working in this repository. Read it before planning or changing the project. Then inspect the current working tree, `CHANGELOG.md`, `TASKS.md`, `README.md`, and the relevant source files instead of relying on memory.

## Repository Mission

Material Design is a canonical, working Material 3 Expressive reference for humans and coding agents. It exists to show what a complete Material app looks like, how it behaves, how it feels, why its design decisions work, and how those decisions are implemented.

The repository should let someone:

1. Search an official Material component or foundation name.
2. Experience its real states and behavior in the Android app.
3. Read concise guidance explaining when to use it, when not to use it, its behavior, accessibility, and adaptive layout requirements.
4. Inspect reviewed Material and Compose API maturity.
5. Follow a validated repository-relative path to an original, working implementation.
6. Use that combined evidence to build a coherent Material product and customize it later.

This is not merely a component inventory, screenshot collection, code dump, generic app template, or imitation of another project. A component reference is incomplete if the surrounding product shell, hierarchy, motion, accessibility, adaptation, and guidance are generic or inconsistent.

## Sources of Truth

Use these sources together:

- The installed Android app is the visual and behavioral truth.
- The Kotlin and Jetpack Compose source is the implementation truth.
- The validated catalog is the naming, search, API, source-location, and design-guidance truth.
- The documentation site is the human- and agent-readable entry point.
- Official Material Design and Android/Compose documentation are the authority for official terminology and API claims.

Never present a project experiment as an official Material API. The catalog's `implementation` field must distinguish official API usage from project implementations.

## External Reference Projects: Learning Only

The projects under `X:\0\refs\m3ex`, including projects such as Zenith and Lune, are read-only learning references. They may be studied to understand broad qualities such as hierarchy, composition, interaction pacing, motion character, adaptation, accessibility decisions, or how a complete expressive product feels.

Never copy from a referenced project in any way or form. This includes:

- Source code or snippets.
- Assets, icons, illustrations, screenshots, or generated media.
- Text, labels, documentation, comments, or metadata.
- Tokens, constants, dimensions, animation values, or component structures.
- File organization, architecture, or distinctive interaction sequences reproduced as a close translation.
- Code rewritten with renamed symbols, reordered statements, syntax conversion, or other superficial changes.

Every implementation in this repository must be independently designed and written using official platform APIs, repository conventions, and original reasoning. Reference projects can reveal questions worth solving; they cannot provide the solution to transplant.

When describing work, do not imply that referenced projects supplied implementation code. If provenance is uncertain, stop and investigate before adding it.

## Project Surfaces

### Active Android reference

`material-app/` is the evolving Kotlin and Jetpack Compose reference app. It is a living, searchable gallery and a real Material product.

### Active documentation

`docs/` explains the project, its development contracts, platform status, and Material guidance. It must remain responsive and readable for people and agents.

### Browser showcase

`docs/material-web-legacy/` is the responsive framework-free 1.5.0 browser showcase. Keep its established UI and behavior coherent. Make showcase-specific changes only when they are in scope.

### Independent Material Web workspace

`material-web/` is the dedicated independent vanilla HTML/CSS/JavaScript implementation workspace. Develop it only when the task explicitly includes Material Web. Android and web may share design intent, but never implementation code, tokens, APIs, state management, architecture, or release schedules.

## Android Architecture

Keep module ownership strict:

- `material-app/app/`: application bootstrap, dependency composition, typed navigation, adaptive shell, and demo registration.
- `material-app/core/catalog/`: bundled catalog JSON, schema, typed models, validation, and deterministic search.
- `material-app/core/data/`: local application state and persistence.
- `material-app/core/designsystem/`: shared theme, system-level UI, and expressive motion contracts.
- `material-app/feature/`: product destinations such as Explore, Catalog, APIs, Foundations, Detail, and Settings.
- `material-app/samples/`: independent, state-hoisted implementations grouped by official Material domains.

Sample implementations must not depend on feature navigation, app persistence, or product-specific state. Feature modules consume the catalog and sample registry; they do not own component implementations.

The Gradle version catalog is the dependency source of truth. Catalog API metadata must match the versions actually resolved by the build.

## Catalog Contract

The bundled catalog contains only working references. Do not add placeholder, speculative, disabled, or "coming soon" catalog cards. Track unfinished work in `TASKS.md` instead.

Every catalog entry must include:

- A unique canonical ID and official Material name.
- Useful aliases, including established abbreviations or older project terms only when they improve search.
- The correct component or foundation kind and category.
- A concise, accurate summary.
- Complete guidance covering purpose, appropriate use, inappropriate use, behavior, accessibility, and adaptive layout.
- A registered working `demoKey`.
- Existing repository-relative source paths and exact symbols.
- Reviewed API symbols, artifacts, versions, availability, stability, opt-in annotations when required, and official URLs.
- A truthful implementation classification.
- The exact release in which the entry was added and the review date.

Search must remain deterministic. Exact official names and API symbols should outrank aliases, categories, summaries, and guidance text.

API availability and API stability are separate concepts:

- Availability records whether the API exists in a stable artifact or only in the pinned alpha line.
- Stability records whether the symbol itself is stable or experimental.

Do not infer either value from the other.

## Component Detail Contract

The detail experience uses four distinct jobs:

- **Preview:** the working interactive reference and meaningful states.
- **Guidance:** purpose, use and avoid rules, behavior and feeling, accessibility, and adaptation.
- **Inspect:** implementation classification, metadata, and source locations.
- **API:** exact official symbols, artifacts, versions, maturity, opt-ins, and links.

Do not restore a separate Variants tab that duplicates Preview. Variants belong inside the interactive reference when they materially differ; design reasoning belongs in Guidance.

## Material 3 Expressive Quality Bar

Treat Material 3 Expressive as a complete UI/UX system, not a recipe for rounded containers.

- Use official component names and components according to purpose, hierarchy, frequency, consequence, platform convention, available space, and accessibility.
- Dogfood completed components in the app when their interaction purpose fits. Do not confine high-quality Material work to demo surfaces while real product controls remain generic.
- Design complete journeys, including navigation, search, settings, details, loading, empty, error, disabled, focused, hovered, pressed, confirmation, and completion states.
- Preserve readable hierarchy through color, type, shape, spacing, containment, elevation, and motion rather than applying the same treatment everywhere.
- Support compact, medium, expanded, resizable, split-screen, large-text, translated-text, keyboard, pointer, and touch contexts.
- Never use color, shape, motion, gesture, or position as the sole carrier of meaning.
- Keep touch targets, semantics, focus order, announcements, contrast, reduced motion, interruption behavior, and alternative action paths part of the implementation—not a final audit.

## Motion Contract

All explicit shared app-shell and project-reference springs belong in `material-app/core/designsystem/ExpressiveMotion.kt`. Do not scatter anonymous replacement constants across features and samples.

Different motion has different jobs. Preserve the repository's distinctions between press feedback, spatial navigation and reflow, shape or state settling, progress smoothing, and rejected-action feedback. Do not force every transition onto one universal spring.

Reduced motion must remove or snap decorative spatial and opacity transitions while preserving state changes, callbacks, semantic meaning, haptics where appropriate, and safety timing such as hold-to-confirm.

Animation code must tolerate interruption, rapid repeated input, disposal, disabled-state changes, and zero-duration behavior without leaving stale jobs, progress, selection, or transformed content.

Official components such as carousels, progress indicators, toolbars, sheets, and navigation retain their own Material physics. Project motion may complement their state feedback but must not impersonate or overwrite official behavior.

## Product Language and Honesty

- Use precise, neutral status language: working, refined, experimental, established, scoped, or verified.
- Never invent popularity, download, ranking, engagement, or store metrics.
- "Featured" is curated catalog metadata.
- "Recently added" comes from release metadata.
- "Recently viewed" is local history.
- Do not recreate store fiction, accounts, install flows, or unrelated product chrome.
- Use concise labels that describe actions and outcomes.

## Change Workflow

Before editing:

1. Read the applicable instructions and relevant project docs completely.
2. Inspect `git status` and preserve unrelated user changes.
3. Locate the real architecture, source, registry, catalog, and tests involved.
4. If external references are useful, study only high-level principles and keep the implementation independent.

For a new working reference:

1. Implement or integrate it in the correct domain sample module.
2. Demonstrate meaningful variants, state transitions, recovery, and edge cases.
3. Register its demo key in the app composition root.
4. Add complete catalog metadata and guidance.
5. Add deterministic alias, symbol, source-path, guidance, and registry coverage where relevant.
6. Dogfood it only where its purpose fits the actual product.
7. Update current documentation, `TASKS.md`, and `CHANGELOG.md` as applicable.
8. Verify before committing.

For bug fixes, identify all plausible root causes within scope and fix every relevant cause that could produce the same regression. Do not stop at the first visible symptom and do not make unrelated changes.

## Version and Release Discipline

The current release is recorded in the source and documentation; verify it rather than trusting this prose alone.

- Never bump the app version unless the user explicitly requests it.
- When a version bump is requested, keep the project version and Android `versionName` identical.
- Derive Android `versionCode` by removing dots from the version: `2.0.3` becomes `203`.
- Every sequential release commit advances to its own version. Do not create two release commits with the same version.
- Give every release its own concise changelog section. Never merge a later release's changes into the previous release section.
- Preserve all historical changelog sections. Update the current section without deleting or rewriting history.
- Catalog `addedIn` values must identify the release that actually introduced each entry.
- Update every current-version surface consistently when—and only when—a bump is explicitly requested.

Use Title Case in release commit summaries:

```text
vX.Y.Z: <One-Line Summary Based On The Changelog>
```

Checkpoint and commit when the diff reaches approximately 1000–1500 changed lines, as required by the repository instructions. Keep each checkpoint coherent and independently buildable. Do not push, create a pull request, or rewrite already shared history unless explicitly requested.

## Changelog Discipline

Update `CHANGELOG.md` for every user-visible change. Keep the active release section concise and user-relevant:

- Describe outcomes people or agents can observe or use.
- Combine related work into a small number of bullets.
- Do not list internal refactors, experiments, failed attempts, or every intermediate commit.
- Do not remove historical releases.
- Do not change the project version merely because the changelog changed.

## Verification

Use checks proportional to the change. The standard Android quality gate is:

```powershell
cd material-app
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

During development, compile or test the smallest affected module first for faster feedback. Before a release checkpoint, run the complete quality gate.

Also verify as applicable:

- `git diff --check` before staging and on the staged diff.
- Catalog schema decoding and validation.
- Exact dependency-version alignment.
- Every source location exists.
- Every demo key is registered.
- Alias, official-name, API-symbol, and guidance search behavior.
- Adaptive behavior and representative compact/expanded layouts.
- Light, dark, dynamic-color, and reduced-motion behavior.
- TalkBack semantics, keyboard focus, large text, touch targets, and non-gesture alternatives.
- Manual interaction for motion, timing, interruption, sheets, dialogs, menus, tooltips, and other behavior that unit tests cannot prove.

Do not claim that Android instrumentation tests ran unless a device or emulator actually executed them. Compilation and linting of instrumentation sources are not the same as running them.

## Current Direction

Android is the leading active implementation. The catalog already covers actions, selection, text input, search, containment, progress, dialogs, bottom sheets, snackbars, tooltips, menus, floating toolbars, typography, and inspectable system foundations.

Use `TASKS.md` as the live backlog. At the time this guide was created, navigation bars, rails, drawers, and tabs were the next incomplete Android component family. Re-read `TASKS.md` before acting because this statement will age.

## Definition of Done

A change is done only when:

- Its behavior and design intent are coherent with the full product.
- The implementation is original and correctly placed in the architecture.
- Catalog metadata and guidance are truthful and complete when applicable.
- Accessibility, adaptation, and reduced-motion behavior are considered.
- Documentation and the changelog accurately describe the user-visible outcome.
- Required tests, lint, compilation, and build checks pass.
- The version and changelog history obey the release rules.
- The working tree contains no accidental generated files or unrelated modifications.
