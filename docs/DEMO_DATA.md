# Canteen Connect — Demo Data

Everything is seeded in `data/demo/DemoData.kt` and is **fictional**: names,
addresses, prices and ratings do not represent real registered canteens.

| Dataset | Count | Notes |
|---|---|---|
| Cities | 6 | New Delhi, Bengaluru, Pune, Hyderabad, Chandigarh, Lucknow (areas + PINs) |
| Canteens | 6 | One per city; distance, hours, open/close, pickup/delivery capability, fees, demo rating, popularity |
| Categories | 12 | Groceries → Other, each with emoji + accent + blurb |
| Products | 62 | INR pricing, brands, units, demo ratings, sold counts; discounts where originalPrice > price |
| Stock matrix | per-canteen | `stock(canteenId)` maps canteen → product IDs; sparse (0) / broad (1) / full (2) stock levels |
| Banners | 4 | Hero carousel content with target routes |
| Notifications | 5 | order update, new canteen, stock alert, offer, tip |
| Addresses | 3 | Home / Office / Gym (demo) |
| Seeded order | 1 | `CC-1042`, status 3 (Ready for Pickup) |

Prices are plausible Indian canteen rates (₹15–₹999). Each product's `accent` picks a
pastel surface so the UI reads premium even without photography.

New product IDs are `p-`prefixed free strings; a new canteen only needs a row in
`canteens` plus a `canteenStock` entry.
