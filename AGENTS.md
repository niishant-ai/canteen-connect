# AGENTS — Canteen Mitra development rules

- **Language/stack**: Kotlin, Jetpack Compose (1.7.6), material3 1.3.0,
  coroutines 1.10.1. Single Activity + hand-rolled nav stack (`MainActivity`, `Nav.kt`).
  Do not add dependencies: the on-device build uses pinned jars in
  `ccapp/build/deps` (see BUILD_AND_RUN.md).
- **Package**: `canteenconnect.app` (the requested `in.` prefix is a reserved-word
  collision in Kotlin; keep it this way; do not "fix" it back).
- **State**: one source of truth — `AppStore`. Mutate via its functions, read via
  `AppStore.state()`. No global mutable singletons elsewhere, no passing raw
  `MutableState` between screens.
- **Components**: shared UI lives in `core/components`; screens in
  `features/<screen>`. Action lambdas are LAST parameters in component signatures
  (`Composable(...) { ... }` with `modifier = …` named) — keep this convention;
  chaining `.padding()`/`.weight()` onto Unit-returning composables is a compile error.
- **Data**: seed via `DemoData`; never hardcode screen-level data. Demo data must
  stay visibly labelled (DEMO badges, copy).
- **Build**: `sh ccapp/build/build.sh`. Do not use Gradle on-device (verified
  infeasible: jextractor needs libstdc++.so.6).
- **UI**: ivory background, charcoal text, lime CTAs, pastel accent tiles
  (`CC.DemoSurface`), 8 dp spacing (`Sp`), radii in `Radius`. No rainbow gradients.
