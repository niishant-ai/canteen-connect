# Canteen Mitra — User Flows

Primary flow (first launch):

```mermaid
flowchart TD
  A[Splash] --> B{Onboarding done?}
  B -- no --> C[Onboarding] --> D[Membership gate]
  B -- yes --> D
  D -->|Continue in Demo Mode / Guest| E[Set Location]
  E --> F[Home]
  F --> G[Search / Categories / Canteens]
  G --> H[Product Detail]
  H --> I[Add to Cart]
  I --> J[Cart (grouped by canteen)]
  J --> K[Checkout per canteen]
  K --> L[Order Success]
  L --> M[Orders / Tracking]
  F <--> M
  F <--> N[Wishlist]
  F <--> O[Profile / Settings / Notifications]
```

## Key flows

### First launch
Splash (≈1 s, respects reduce-motion) → Onboarding (3 slides, page dots, Next/Skip)
→ Membership gate (demo ID card visual; "Continue in Demo Mode" or "Explore as Guest")
→ Set Location (city → area; PIN derived; "Use current location" is a labelled demo
mapping that does NOT request GPS) → Home.

### Shopping
Home / Explore / Search → Product Detail (choose canteen, quantity) → Add to Cart.
Cart shows one card per canteen; each card has its own subtotal and "Checkout this
canteen". Multiple canteens ⇒ each is checked out separately (banner explains this).

### Checkout & orders
Fulfilment options reflect canteen capability (Pickup always in demo; Delivery only if
the canteen supports it). Address picker for delivery. Payment is "Pay at Counter
(demo)" — clearly simulated. Placing an order removes that canteen's items from the
cart, assigns a `CC-xxxx` ID, and lands on the animated success screen.

### Tracking
Orders tab lists current / past / cancelled. Status advances automatically every
40 s of elapsed time (demo logic) through: Order Placed → Accepted by Canteen →
Preparing → Ready for Pickup → Out for Delivery → Delivered. Orders can be cancelled
while Preparing or earlier.

### Wishlist & history
Wishlist persists; each saved item offers add-to-cart and remove. Recently viewed and
recent searches surface on Home and Search.
