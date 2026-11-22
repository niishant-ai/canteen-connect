# Canteen Mitra — Design System

Implemented in `core/theme/Theme.kt` (`Brand`, `CC`, `Sp`, `Radius`, typography,
light/dark color schemes) and `core/components/`.

## Color tokens
| Token | Light value | Use |
|---|---|---|
| Ivory | `#F7F8F4` | app background |
| Charcoal | `#171717` | primary text / dark surfaces |
| Slate | `#777D83` | secondary text |
| White | `#FFFFFF` | card surface |
| Lime | `#A8F000` | primary accent (CTA, active tab) |
| Lime Deep | `#79B800` | accent text / marks |
| Lime Soft | `#EFF8CC` | accent tint surfaces |
| Product accent pairs | 0 Sky `#E3F1FF/#4C8DF5` · 1 Lavender `#EEE3FF/#7B5BE0` · 2 Mint `#DCFBEE/#1F9E7A` · 3 Peach `#FFECDD/#E88B45` · 4 Butter `#FDF6D8/#B9902A` · 5 Rose `#FFE4EA/#E0568A` · 6 Sand `#F3EFE2/#8A7A55` | pastel product/canteen tiles (emoji "photography") |

Dark mode maps background/surfaces to charcoal neutrals; Lime stays the accent.

## Typography
Extrabold for display/heading (−letter-spacing feel via 0 tracking + weight), semibold
titles, medium body, grey small labels. Sizes: 30/22/20/18/16/15/14/13/12 sp tiers
defined in `ccTypography`.

## Spacing & radii
8 dp system: `s1=4 … s7=48`. Radii: `md 14, lg 18, xl 26, card 22, pill 999`.

## Components (all in `core/components`)
- `PrimaryButton` / `GhostButton` — 52 dp tall, 14 dp radius, lime fill / bordered.
- `ProductCard` — pastel emoji tile, discount ribbon, wishlist heart, price row,
  quick-add ＋ (30 dp).
- `CanteenCard` — emoji logo, status chips (Open/Closed, Pickup, Delivery), distance,
  demo rating, "View" affordance.
- `TopBar` — optional back chevron, title/subtitle, trailing slot.
- `FloatingBottomNav` — pill, white, 1 dp border, soft shadow; active tab gets a lime
  pill behind its emoji with 55% alpha when inactive; system inset aware.
- `SectionHeader`, `Chip`, `EmptyState` (emoji in lime circle + copy + action),
  `ShimmerCard`, `DemoBadge`, `EmojiTile`, `LocationChip`.

## Imagery policy
This build ships no remote images: products/canteens render as emoji on pastel accent
tiles (consistent, offline, never broken). When real photography is added later,
replace `EmojiTile` with an `AsyncImage` surface of identical dimensions.

## Motion guidelines
- Screen changes: AnimatedContent crossfade (220 ms in / 140 ms out).
- Add-to-cart: "Added ✓" label swap for 900 ms + haptic (if enabled).
- Order success: ✓ scales in (700 ms EaseOut).
- Live status: pulsing dot (`rememberInfiniteTransition`).
- Everything above is skipped/damped when Settings → Reduce motion is on.
