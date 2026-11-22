package canteenconnect.app.data.models

/** Core demo data models for Canteen Mitra.
 *  All records are demonstration data: names, addresses and prices are fictional
 *  and the app is not an officially endorsed government service. */

data class Category(
    val id: String,
    val name: String,
    val emoji: String,
    val accent: Int,      // index into palette of product surfaces
    val blurb: String = ""
)

data class Banner(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val accent: Int,
    val route: String     // top-level destination to open
)

data class Canteen(
    val id: String,
    val name: String,
    val area: String,
    val city: String,
    val pin: String,
    val distanceM: Int,
    val openFrom: String,     // "08:00"
    val openTo: String,       // "19:30"
    val openNow: Boolean,     // demo status
    val pickup: Boolean,
    val delivery: Boolean,
    val deliveryFee: Int,
    val packagingFee: Int,
    val popularity: Int,      // demo popularity score
    val rating: Double,       // demo rating
    val about: String,
    val emoji: String,
    val accent: Int,
    val stockLevel: Int      // 0 sparse, 1 broad, 2 full
)

data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val unit: String,
    val price: Int,
    val originalPrice: Int,
    val categoryId: String,
    val tagline: String,
    val description: String,
    val emoji: String,
    val accent: Int,
    val rating: Double,      // demo rating
    val sold: Int,           // demo units sold
    val isNew: Boolean = false
)

data class CityOption(
    val city: String,
    val areas: List<String>,
    val pins: List<String>
)

/** An item in the cart, tied to one canteen (items from different canteens are
 *  never merged into one fulfilment). */
data class CartItem(
    val productId: String,
    val name: String,
    val brand: String,
    val emoji: String,
    val accent: Int,
    val unit: String,
    val price: Int,
    val canteenId: String,
    val canteenName: String,
    var qty: Int
) {
    val key get() = "$productId@$canteenId"
    val lineTotal get() = price * qty
}

data class OrderItem(
    val productId: String,
    val name: String,
    val emoji: String,
    val accent: Int,
    val unit: String,
    val price: Int,
    val qty: Int
)

data class DemoOrder(
    val id: String,
    val canteenId: String,
    val canteenName: String,
    val items: List<OrderItem>,
    val subtotal: Int,
    val fees: Int,
    val total: Int,
    val fulfilment: String,      // "Pickup" | "Delivery"
    val status: Int,            // index into ORDER_STATUSES
    val placedAt: Long,
    val estimated: String,
    val payment: String = "Pay at Counter (demo)"
) {
    val cancelled get() = status == -1
}

val ORDER_STATUSES = listOf(
    "Order Placed",
    "Accepted by Canteen",
    "Preparing",
    "Ready for Pickup",
    "Out for Delivery",
    "Delivered"
)

data class DemoNotification(
    val id: String,
    val emoji: String,
    val title: String,
    val body: String,
    val ts: Long,
    var read: Boolean = false
)

/** A canteen offer / coupon code. All figures are demo values. */
data class Offer(
    val id: String,
    val code: String,
    val title: String,          // "Flat 10% OFF"
    val subtitle: String,       // "On groceries above \u20b9299"
    val isPercent: Boolean,
    val value: Int,            // percent points OR flat rupees
    val cap: Int = 0,          // max rupee savings (percent offers)
    val minOrder: Int = 0,     // minimum cart value in rupees
    val freeDeliveryAbove: Int = -1,  // -1 = no fee waiver; else rupee threshold
    val category: String? = null,     // optional category limit ("grocery" etc.)
    val accent: Int = 0
)

data class SavedAddress(
    val id: String,
    val label: String,
    val line: String,
    val city: String
)

/** A grouped canteen view for the cart. */
data class CartGroup(
    val canteen: Canteen,
    val items: List<CartItem>
) {
    val subtotal get() = items.sumOf { it.lineTotal }
    val itemCount get() = items.sumOf { it.qty }
}
