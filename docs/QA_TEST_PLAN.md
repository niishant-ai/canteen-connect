# Canteen Connect — QA Test Plan

## Static / build checks (run in this build)
- [x] Kotlin compile, two passes (core → features): 0 errors
- [x] aapt2 resource link: success
- [x] D8 dex: success (classes.dex + classes2.dex)
- [x] apksigner sign + verify: pass
- [x] aapt2 badging: correct package `canteenconnect.app`, launcher activity, minSdk 26 / targetSdk 35

## Functional checklist (on device)
### Flows
- [ ] First launch lands on Onboarding (splash first, ~1 s)
- [ ] Skip/Get Started both persist onboarding (relaunch starts at membership)
- [ ] Member and Guest both enter demo mode; demo disclaimer visible
- [ ] Location select updates header chip; "use current location" shows demo note
### Discovery
- [ ] Home carousels scroll; banner CTAs navigate
- [ ] Nearby canteens reflect selected city (empty state lists seeded cities)
- [ ] Canteen detail shows only that canteen's catalog; search-within filters
- [ ] Global search returns product + canteen + category groups; recents persist
### Shopping
- [ ] Quick-add on cards adds to the correct canteen group
- [ ] Product detail: qty +/-, canteen selector, wishlist heart toggles & persists
- [ ] Cart: qty steppers, remove at 0, clear-per-canteen, subtotals correct
- [ ] Two-canteen cart shows the "fulfilled separately" banner; checkout switches canteens
- [ ] Checkout: fulfilment options follow canteen capability; delivery shows address + fees
- [ ] Placing order: cart items for that canteen removed, success screen with order ID
- [ ] Orders tab shows the new order; status advances over time; cancel works early only
### Secondary
- [ ] Notifications mark read / mark all; unread chip
- [ ] Settings toggles: haptics, reduce motion, demo badge, clear cart/recents
- [ ] Help/About/Privacy/Terms open and return

## Cart math spot-check
Subtotal = Σ price×qty per group; Delivery adds deliveryFee + packagingFee;
Pickup adds packagingFee only; totals render at group and screen level.

## Accessibility
- [ ] All primary controls ≥ 44 dp; emoji paired with text; demo data labelled DEMO;
      contrast on lime buttons (charcoal on #A8F000).

## Known limitation
JVM unit tests (cart calculation, search) and UI instrumented tests are not runnable
in this on-device toolchain — no Robolectric/Android-test harness is available here.
Compensation: deterministic logic (cart math, liveStatus, search predicates) is small
and was verified by code review + the build; on-device functional pass is pending.

## Crash log & fixes (post-first-build)
- **Startup crash `ClassNotFoundException: kotlin.collections.CollectionsKt`** — the
  first APK (v1, 11.5 MB) shipped without the Kotlin standard library: the whole-jar
  stdlib copy had been removed from `deps.jar` (R8 rejects mixed dex+class jars) but
  was never *extracted* into the class tree. Fix: `build.sh` now extracts every
  runtime jar (kotlin-stdlib, coroutines, annotation, collection, …) into
  `flats/classes` before packaging `deps.jar`, skips compiler-only jars
  (kotlin-reflect, jetbrains-annotations), and hard-fails if `kotlin/` is missing.
  New APK (13 MB) verified: `kotlin/collections/CollectionsKt` now defined in dex.
- **Haptics never wired** — `AppStore.attachView()` is now called from
  `MainActivity.onCreate` with the ComposeView.
