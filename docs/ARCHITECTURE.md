# Canteen Connect — Architecture

## Shape
Single-module Kotlin/Compose app. MVVM-ish, with a single observable application
store instead of many ViewModels (fewer moving parts, one source of truth, trivial
persistence).

```
canteenconnect/app/
├─ MainActivity.kt          Activity + nav stack + host ComposeView
├─ core/
│  ├─ AppStore.kt           StateFlow<State> + mutations + JSON persistence
│  ├─ navigation/Nav.kt     route constants, bottom tabs
│  ├─ theme/Theme.kt        tokens (Brand, CC, Sp, Radius, typography, schemes)
│  └─ components/          shared UI: Components.kt, Chromes.kt
├─ data/
│  ├─ models/Models.kt      Product, Canteen, CartItem, DemoOrder, …
│  └─ demo/DemoData.kt      seed data + stock matrix + helpers
└─ features/<screen>/       one package per screen
```

## State
`AppStore.State` is a single data class held in a `MutableStateFlow`; screens read it
via `AppStore.state()` (`collectAsState`) and call mutation functions
(`addToCart`, `setQty`, `toggleWishlist`, `placeOrder`, …). Each mutation emits a new
state and a background collector serializes it to SharedPreferences (JSON), so cart,
wishlist, orders, location, recents and settings survive restarts.

## Repository boundary (future backend)
All data access flows through `AppStore` + `DemoData`. To integrate a real backend:
swap `DemoData` sources for a `CanteenRepository`/`ProductRepository` backed by a
Retrofit/RPC layer, and replace `placeOrder` with an order service; `State` shape stays
stable. Membership verification would slot in behind `MembershipScreen` via a
`MembershipService` interface (demo implementation returns the fixed demo card).

## Persistence
SharedPreferences `cc_state` / key `state_v1` (JSON via org.json, a framework class —
no extra dependency). Orders also seed one sample past order on first run.

## Images
Emoji-on-pastel-tiles ("EmojiTile") instead of bundling photography: zero asset
weight, impossible broken images, works offline. Swappable for Coil/AsyncImage.

## Error handling
No remote calls ⇒ no network errors. Guards: unknown product/canteen IDs render
empty states; empty cart/checkout/others all have designed empties; JSON restore is
wrapped in `runCatching` so corrupt state falls back to fresh state.

## Why no Room/DataStore
For a demo dataset, SharedPreferences JSON is the smallest robust persistence that
still survives restarts. Room is the planned upgrade path for catalogue/orders at
scale; the boundary above is where it would plug in.
