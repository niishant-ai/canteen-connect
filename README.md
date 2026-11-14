# 🥣 Canteen Connect

A premium, fully working **demo** shopping app for India's government canteen
ecosystem. Kotlin + Jetpack Compose, single-activity architecture, local seeded
data, canteen-grouped cart, simulated checkout, animated order tracking.

> **Demo mode** — all canteens, products, prices and ratings are fictional and are
> not affiliated with, or endorsed by, any government body. No authentication, no
> real payments, no network.

## Features
- Onboarding → demo membership gate → manual location
- Home with hero carousel, category shortcuts, nearby canteens & product rails
- Canteen discovery + detail with in-canteen search & canteen-only catalog
- Live search (products / canteens / categories) with recent searches
- Product detail: quantity, canteen choice, wishlist, related products
- Canteen-grouped cart (never merges canteens) → simulated checkout
- Animated order success + auto-advancing demo status timeline
- Notifications, profile, settings, help, about, privacy/terms
- Cart, wishlist, orders, location & preferences persist across restarts
- Floating pill bottom nav, haptics, reduce-motion support, light & dark theme

## Screenshots
(To be captured on device: Home, Canteen Detail, Product Detail, Cart, Checkout,
Order Tracking. Add images under `docs/screenshots/`.)

## Build (on-device)
```sh
sh ccapp/build/build.sh
```
→ `ccapp/build/app-debug.apk` (signed, apksigner-verified, 13.4 MB)

Install (privileged shell):
```sh
pm install ccapp/app-debug.apk
am start -n canteenconnect.app/.MainActivity
```
Full details: `docs/BUILD_AND_RUN.md`.

## Docs
`docs/PRD.md` · `docs/USER_FLOWS.md` · `docs/DESIGN_SYSTEM.md` ·
`docs/SCREEN_INVENTORY.md` · `docs/ARCHITECTURE.md` · `docs/IMPLEMENTATION_PLAN.md` ·
`docs/QA_TEST_PLAN.md` · `docs/BUILD_AND_RUN.md` · `docs/DEMO_DATA.md` · `AGENTS.md`

## Repo layout
`ccapp/app/src/main/java/canteenconnect/app/{core,data,features}` +
`ccapp/app/src/main/{AndroidManifest.xml,res}` + `ccapp/build/` (toolchain).
