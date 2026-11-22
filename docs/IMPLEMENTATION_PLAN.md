# Canteen Mitra — Implementation Plan (as executed)

Phase 1 — Environment: inspected device toolchain. Gradle could not run on-device
(jextractor requires libstdc++.so.6, absent); used the manual pipeline instead:
kotlinc 2.3.0 + compose compiler plugin + D8 + aapt2 + apksigner (all on-device).

Phase 2 — Product docs: this set, written to reflect the build.

Phase 3 — Design system: Theme.kt tokens + components (cards, buttons, empty states,
bottom nav, chips) — done.

Phase 4 — Foundation: models, DemoData (6 canteens, 12 categories, 62 products,
4 banners, notifications, addresses, seeded order), AppStore + persistence,
MainActivity nav stack — done.

Phase 5 — Core experience: onboarding, membership, location, home, explore,
categories, canteens, canteen detail, search, product detail, listing — done.

Phase 6 — Shopping & orders: wishlist, cart (canteen-grouped), checkout, success,
orders list, tracking — done.

Phase 7 — Secondary: notifications, profile, settings, help, about, legal — done.

Phase 8 — Motion & polish: crossfade screens, add-to-cart feedback + haptics,
success animation, pulsing status, reduce-motion toggle, empty states everywhere — done.

Phase 9 — Verification: clean compile (0 errors), D8 success, apksigner verify pass.
No JVM unit-test harness exists in this on-device toolchain, so QA used the manual
checklist in QA_TEST_PLAN.md (documented limitation).

Phase 10 — APK: `ccapp/build/app-debug.apk` (and copy at `ccapp/app-debug.apk`),
13.4 MB (14,064,980 B after the Kotlin-stdlib packaging fix), signed + verified.
