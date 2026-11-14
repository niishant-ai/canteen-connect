# Canteen Connect — Screen Inventory

| Route (in `core/navigation/Nav.kt`) | Composable | File | Purpose / nav |
|---|---|---|---|
| `onboarding` | `OnboardingScreen` | `features/onboarding/` | 3-slide intro, page dots, Get Started; persists completion |
| `membership` | `MembershipScreen` | `features/membership/` | Demo ID card, member/guest entry, demo notice |
| `location` | `LocationScreen` | `features/location/` | City/area select, demo "use current location", set + proceed |
| `home` | `HomeScreen` | `features/home/` | Header (greeting, notifications/cart badges, profile), location chip, search, hero pager, category shortcuts, nearby canteens, product rails |
| `explore` | `ExploreScreen` | `features/explore/` | Category grid (two columns) + canteen list scoped to city |
| `orders` | `OrdersScreen` | `features/orders/` | Current/past/cancelled demo orders, live status, tap → detail |
| `wishlist` | `WishlistScreen` | `features/wishlist/` | Saved products: open, add-to-cart, remove |
| `profile` | `ProfileScreen` | `features/profile/` | Demo member card, location, menu into all secondary screens |
| `search` | `SearchScreen` | `features/search/` | Live product/canteen/category results, recent + popular chips |
| `cart` | `CartScreen` | `features/cart/` | Canteen-grouped cards, qty controls, clear-per-canteen, sticky checkout |
| `checkout` | `CheckoutScreen` | `features/checkout/` | Canteen tab switch, fulfilment, address, breakdown, demo payment |
| `ordersuccess/{id}` | `OrderSuccessScreen` | `features/orders/` | Animated ✓, order card, View Order / Continue |
| `order/{id}` | `OrderDetailScreen` | `features/orders/` | Animated status timeline, details, items, cancel while early |
| `canteen/{id}` | `CanteenDetailScreen` | `features/canteens/` | Banner, about, in-canteen search, canteen-only product grid |
| `category/{id}` | `CategoryScreen` | `features/categories/` | Category product grid scoped to city |
| `productdetail/{id}` | `ProductDetailScreen` | `features/products/` | Gallery tile, price/discount, qty, canteen selector, related, sticky add |
| `notifications` | `NotificationsScreen` | `features/notifications/` | Read/unread, mark read/all |
| `settings` | `SettingsScreen` | `features/profile/SettingsScreens.kt` | Haptics, reduce motion, demo badges, clear cart/recents, legal links |
| `help` | `HelpScreen` | same | FAQ accordions + fictional contact |
| `about` | `AboutScreen` | same | Brand, version, demo disclaimer, legal links |
| `privacy` / `terms` | `PrivacyScreen` / `TermsScreen` | same | Placeholder legal copy, clearly demo |

Navigation model: a hand-rolled stack in `MainActivity` (single Activity,
`ComposeView`). Bottom tabs replace the stack; detail screens push; system back pops
via `NavBridge`. No external navigation library is used (keeps the dependency set
minimal on the on-device toolchain).
