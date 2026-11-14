# Canteen Connect — Product Requirements Document

## 1. Vision
A premium, calm, mobile-first shopping experience for India's government canteen
ecosystem. Users discover registered canteens near them, shop everyday products,
place demo orders and track fulfilment — with the fluid feel of leading quick-commerce
apps but a distinct brand identity of its own.

## 2. Problem Statement
Registered government canteens serve eligible members but have little discoverability:
no unified catalogue, no cart, no order visibility. Canteen Connect defines the product
experience for that ecosystem and, in this milestone, proves it end-to-end on fully
local, demo data.

## 3. Target Audience & Personas
- **Demo Member** — an eligible canteen member who shops at their workplace canteen
  weekly and wants to see stock and place orders ahead of arrival.
- **Office Administrator** — wants to know what is available across canteens nearby.
- **Casual Browser** — guest, exploring the catalogue and offers.

## 4. Business Objectives
- Define the golden path (discover → shop → order → track).
- Prove UI/UX quality and state management on real devices.
- Keep the door open to backend + membership verification (architecture boundary).

## 5. Scope (MVP, this milestone)
Included: onboarding, demo membership gate, manual location, home, explore/categories,
nearby canteens + detail, search, product listing & detail, wishlist, canteen-grouped
cart, simulated checkout, order success, order history + live demo tracking,
notifications, profile, settings/help/about/privacy/terms, floating bottom navigation,
persistence of cart/wishlist/orders/preferences.

Excluded (documented, not faked): real authentication/OTP, identity-document handling,
real payment, live geolocation, push notifications, real canteen directory.

## 6. Functional Requirements (implemented)
- RF-1 Onboarding (3 slides, persisted completion). ✅
- RF-2 Demo membership gate (member / guest, clearly labelled). ✅
- RF-3 Manual city/area/PIN selection + demo "use current location". ✅
- RF-4 Home: hero carousel, category shortcuts, nearby canteens, product rails
  (popular, best sellers, offers, new, essentials, recommended, recently viewed). ✅
- RF-5 Canteen discovery: cards with status/hours/services/distance; filters (open now,
  sort by distance/popularity) via Explore + canteen scope. ✅
- RF-6 Canteen detail: banner, about, search-within-canteen, canteen-only catalog. ✅
- RF-7 Search: live product/canteen/category results, recent & popular chips. ✅
- RF-8 Product detail: gallery tile, price/discount, quantity, canteen choice,
  wishlist, related products, add-to-cart feedback. ✅
- RF-9 Wishlist with persistence. ✅
- RF-10 Cart grouped by canteen; items from different canteens are never merged;
  quantity/remove/clear-per-canteen; totals. ✅
- RF-11 Checkout: fulfilment by canteen capability, addresses (delivery), demo
  "Pay at Counter (demo)", price breakdown. ✅
- RF-12 Order success with animation; unique local order IDs (CC-xxxx). ✅
- RF-13 Orders list (current/past/cancelled) + detail with animated demo status
  timeline (auto-advances ~every 40 s), cancel while preparing. ✅
- RF-14 Notifications with read/unread, mark read / mark all. ✅
- RF-15 Profile (demo member card, location, menu), settings (haptics, reduced
  motion, demo badges, clear data), help, about, privacy/terms placeholders. ✅
- RF-16 Persistence: cart, wishlist, orders, location, recent searches,
  viewed products, notification read state, settings — via SharedPreferences JSON. ✅

## 7. Non-Functional Requirements
- Visual: consistent token system, no default-Material screens, premium density.
- Motion: fade/scale transitions, animated add-to-cart, success animation, pulsing
  status dots; respects "reduce motion" setting.
- Performance: single-process Compose UI, small local dataset, target 60 fps.
- Accessibility: ≥44 dp touch targets on primary controls, labelled demo data,
  readable contrast, emoji used alongside text (never icon-only).
- Integrity: every interactive element changes real application state; demo data is
  visibly labelled; the app never claims government endorsement.

## 8. User Journeys
See USER_FLOWS.md.

## 9. Future Roadmap
Real membership verification (Canteen ID), canteen directory API, payments,
live geolocation, push notifications, multi-city expansion, canteen owner console.

## 10. Assumptions & Constraints
- Demo dataset is seeded and fictional; prices are plausible INR values.
- Build environment is an on-device toolchain (no Gradle daemon); see BUILD_AND_RUN.md.
- Package name is `canteenconnect.app`: the requested `in.canteenconnect.app` is not
  a valid Kotlin package (`in` is a reserved keyword) — a documented deviation.
