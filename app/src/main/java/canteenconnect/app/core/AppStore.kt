package canteenconnect.app.core

import android.content.Context
import android.view.HapticFeedbackConstants
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import canteenconnect.app.data.demo.DemoData
import canteenconnect.app.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

/**
 * Global demo application state. Screens read [state] and call mutation
 * functions; changes are pushed to Compose through [state] (a StateFlow) and
 * persisted to SharedPreferences as JSON so the app does not reset on restart.
 *
 * The shape mirrors what a real repository/service layer would expose, so a
 * future backend can replace the seeded sources behind the same API.
 */
object AppStore {

    data class State(
        val onboardingDone: Boolean = false,
        val entered: Boolean = false,       // passed the membership/demo gate
        val guest: Boolean = false,         // entered as guest
        val locationSet: Boolean = false,   // has explicitly chosen a location
        val city: String = "New Delhi",
        val area: String = "Vikas Sadan",
        val pin: String = "110090",
        var cart: List<CartItem> = emptyList(),
        var wishlist: List<String> = emptyList(),
        var orders: List<DemoOrder> = emptyList(),
        var notifications: List<DemoNotification> = DemoData.notifications,
        var recent: List<String> = emptyList(),
        var viewed: List<String> = emptyList(),
        var lastOrder: String? = null,
        var coupon: String? = null,
        var settingsHaptics: Boolean = true,
        var settingsReducedMotion: Boolean = false,
        var demoBadgeVisible: Boolean = false
    )

    private var store = MutableStateFlow(State())

    @Composable
    fun state(): State = store.collectAsState().value

    val stateSnapshot: State get() = store.value

    /** Persisted key inside SharedPreferences. */
    private const val PREF = "cc_state"
    private const val KEY = "state_v1"

    private var initialized = false

    fun init(context: Context) {
        if (initialized) return
        initialized = true
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY, null)
        if (raw != null) {
            runCatching { store.value = restore(JSONObject(raw)) }
        }
        if (store.value.orders.isEmpty()) {
            store.value = store.value.copy(orders = listOf(DemoData.seededOrder()))
        }
        val scope = kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main)
        scope.launch {
            store.collect { s -> prefs.edit().putString(KEY, s.toJson().toString()).apply() }
        }
    }

    // ---------- persistence ----------
    private fun State.toJson() = JSONObject().apply {
        put("onboardingDone", onboardingDone); put("entered", entered); put("guest", guest)
        put("locationSet", locationSet)
        put("city", city); put("area", area); put("pin", pin)
        put("cart", JSONArray(cart.map {
            JSONObject().put("p", it.productId).put("c", it.canteenId).put("q", it.qty)
        }))
        put("wishlist", JSONArray(wishlist))
        put("recent", JSONArray(recent))
        put("viewed", JSONArray(viewed))
        put("settingsHaptics", settingsHaptics)
        put("settingsReducedMotion", settingsReducedMotion)
        put("orders", JSONArray(orders.map { o -> JSONObject().apply {
            put("id", o.id); put("cid", o.canteenId); put("cname", o.canteenName)
            put("items", JSONArray(o.items.map { JSONObject().apply {
                put("p", it.productId); put("name", it.name); put("e", it.emoji)
                put("a", it.accent); put("unit", it.unit); put("price", it.price); put("q", it.qty)
            }}))
            put("sub", o.subtotal); put("fees", o.fees); put("total", o.total)
            put("f", o.fulfilment); put("st", o.status); put("at", o.placedAt); put("est", o.estimated); put("pay", o.payment)
        }}))
        put("notifRead", JSONArray(notifications.map { it.read }))
        put("lastOrder", lastOrder ?: "")
        put("coupon", coupon ?: "")
    }

    private fun restore(j: JSONObject): State {
        val s = store.value
        val cart = j.getJSONArray("cart").let { a ->
            (0 until a.length()).mapNotNull { i ->
                val o = a.getJSONObject(i)
                val p = DemoData.product(o.getString("p")) ?: return@mapNotNull null
                val c = DemoData.canteen(o.getString("c")) ?: return@mapNotNull null
                CartItem(p.id, p.name, p.brand, p.emoji, p.accent, p.unit, p.price, c.id, c.name, o.getInt("q"))
            }
        }
        val orders = j.getJSONArray("orders").let { a ->
            (0 until a.length()).map { i ->
                val o = a.getJSONObject(i)
                val items = o.getJSONArray("items").let { ia ->
                    (0 until ia.length()).map { j2 ->
                        val it = ia.getJSONObject(j2)
                        OrderItem(it.getString("p"), it.getString("name"), it.getString("e"),
                            it.getInt("a"), it.getString("unit"), it.getInt("price"), it.getInt("q"))
                    }
                }
                DemoOrder(o.getString("id"), o.getString("cid"), o.getString("cname"), items,
                    o.getInt("sub"), o.getInt("fees"), o.getInt("total"), o.getString("f"),
                    o.getInt("st"), o.getLong("at"), o.getString("est"), o.getString("pay"))
            }
        }
        val notifRead = j.optJSONArray("notifRead")
        val notifs = DemoData.notifications.mapIndexed { i, n ->
            n.copy(read = notifRead?.let { it.optBoolean(i) } ?: false)
        }
        return s.copy(
            onboardingDone = j.optBoolean("onboardingDone"),
            entered = j.optBoolean("entered"),
            guest = j.optBoolean("guest"),
            locationSet = j.optBoolean("locationSet"),
            city = j.optString("city", s.city),
            area = j.optString("area", s.area),
            pin = j.optString("pin", s.pin),
            cart = cart,
            wishlist = j.optJSONArray("wishlist")?.let { a -> (0 until a.length()).map { a.getString(it) } } ?: emptyList(),
            recent = j.optJSONArray("recent")?.let { a -> (0 until a.length()).map { a.getString(it) } } ?: emptyList(),
            viewed = j.optJSONArray("viewed")?.let { a -> (0 until a.length()).map { a.getString(it) } } ?: emptyList(),
            settingsHaptics = j.optBoolean("settingsHaptics", true),
            settingsReducedMotion = j.optBoolean("settingsReducedMotion", false),
            orders = orders,
            notifications = notifs,
            lastOrder = j.optString("lastOrder", "").takeIf { it.isNotEmpty() },
            coupon = j.optString("coupon", "").takeIf { it.isNotEmpty() }
        )
    }

    // ---------- mutations (each emits + persists via the collector) ----------
    private fun update(block: (State) -> State) { store.value = block(store.value) }

    fun completeOnboarding() = update { it.copy(onboardingDone = true) }
    fun enterAsMember() = update { it.copy(entered = true, guest = false) }
    fun enterAsGuest() = update { it.copy(entered = true, guest = true) }

    fun setLocation(city: String, area: String, pin: String) =
        update { it.copy(city = city, area = area, pin = pin, locationSet = true) }

    /** Canteens available in the selected city (demo scoping). */
    fun cityCanteens(city: String): List<Canteen> =
        DemoData.canteens.filter { it.city == city }

    fun cityCanteens(): List<Canteen> = cityCanteens(store.value.city)

    fun unreadNotifications(): Int = store.value.notifications.count { !it.read }

    fun cityAreas(): List<String> {
        val c = DemoData.cities.firstOrNull { it.city == store.value.city } ?: DemoData.cities.first()
        return c.areas
    }

    fun pinForArea(area: String, city: String): String {
        val c = DemoData.cities.firstOrNull { it.city == city } ?: return store.value.pin
        val i = c.areas.indexOf(area)
        return if (i in c.pins.indices) c.pins[i] else (store.value.pin.takeIf { it.isNotEmpty() } ?: c.pins.first())
    }

    // ----- cart -----
    fun addToCart(productId: String, canteenId: String, qty: Int = 1) = update { s ->
        val p = DemoData.product(productId) ?: return@update s
        val c = DemoData.canteen(canteenId) ?: return@update s
        val key = "$productId@$canteenId"
        val existing = s.cart.firstOrNull { it.key == key }
        val cart = if (existing != null) {
            s.cart.map { if (it.key == key) it.copy(qty = it.qty + qty) else it }
        } else {
            s.cart + CartItem(p.id, p.name, p.brand, p.emoji, p.accent, p.unit, p.price, c.id, c.name, qty)
        }
        s.copy(cart = cart)
    }

    fun setQty(key: String, qty: Int) = update { s ->
        s.copy(cart = s.cart.map { if (it.key == key) it.copy(qty = qty) else it }.filter { it.qty > 0 })
    }

    fun removeItem(key: String) = update { s -> s.copy(cart = s.cart.filterNot { it.key == key }) }

    fun clearCart() = update { s -> s.copy(cart = emptyList()) }

    fun cartCount(): Int = store.value.cart.sumOf { it.qty }

    /** Group cart items by canteen (never merged across canteens). */
    fun groups(): List<CartGroup> = store.value.cart.groupBy { it.canteenId }
        .filter { DemoData.canteen(it.key) != null }
        .mapNotNull { (cid, items) ->
            val c = DemoData.canteen(cid)!!
            CartGroup(c, items)
        }

    fun clearCartFor(canteenId: String) =
        update { s -> s.copy(cart = s.cart.filterNot { it.canteenId == canteenId }) }

    // ----- offers / coupons (demo) -----
    fun coupon(code: String): canteenconnect.app.data.models.Offer? =
        DemoData.offers.firstOrNull { it.code.equals(code, ignoreCase = true) }

    fun setCoupon(code: String?) = update { s -> s.copy(coupon = code?.takeIf { it.isNotEmpty() }) }

    /** Whether a coupon is usable for a given canteen subtotal/fulfilment. */
    fun couponUsable(o: canteenconnect.app.data.models.Offer, subtotal: Int, fulfilment: String): Boolean {
        if (subtotal < o.minOrder) return false
        if (o.freeDeliveryAbove >= 0 && fulfilment != "Delivery") return false
        return true
    }

    /** Rupee savings of [code] for a canteen subtotal; 0 when not applicable. */
    fun couponSavings(code: String?, subtotal: Int, fulfilment: String): Int {
        val o = coupon(code ?: "") ?: return 0
        if (!couponUsable(o, subtotal, fulfilment)) return 0
        return if (o.isPercent) {
            val amt = subtotal * o.value / 100
            if (o.cap > 0) minOf(amt, o.cap) else amt
        } else o.value
    }

    /** Re-add every item of a past order to the cart (same canteen, same qty).
     *  Returns how many units were added. */
    fun reorder(orderId: String): Int {
        val o = store.value.orders.firstOrNull { it.id == orderId } ?: return 0
        var added = 0
        for (it in o.items) {
            if (DemoData.product(it.productId) != null) {
                addToCart(it.productId, o.canteenId, it.qty)
                added += it.qty
            }
        }
        recordViewed(o.items.firstOrNull()?.productId ?: "")
        return added
    }

    // ----- orders -----
    fun placeOrder(group: CartGroup, fulfilment: String, addressLabel: String?): DemoOrder {
        val c = group.canteen
        val items = group.items.map { OrderItem(it.productId, it.name, it.emoji, it.accent, it.unit, it.price, it.qty) }
        val sub = group.subtotal
        val fees = if (fulfilment == "Delivery") c.deliveryFee + c.packagingFee else c.packagingFee
        val total = sub + fees
        val id = "CC-" + (1000 + Random.nextInt(8000))
        val est = if (fulfilment == "Delivery") "Arrives in ~35–45 min" else "Ready in ~15–20 min"
        val order = DemoOrder(id, c.id, c.name, items, sub, fees, total, fulfilment, 0,
            System.currentTimeMillis(), est, "Pay at Counter (demo)")
        update { s -> s.copy(orders = listOf(order) + s.orders, lastOrder = id, cart = s.cart.filterNot { it.canteenId == c.id }) }
        return order
    }

    /** Demo status progression: one stage every 40s of real time since placed. */
    fun liveStatus(o: DemoOrder): Int {
        if (o.cancelled) return -1
        val elapsed = (System.currentTimeMillis() - o.placedAt) / 1000L
        return minOf(ORDER_STATUSES.lastIndex, (elapsed / 40).toInt())
    }

    fun cancelOrder(id: String) = update { s ->
        s.copy(orders = s.orders.map { if (it.id == id && it.status < 2) it.copy(status = -1) else it })
    }

    // ----- wishlist -----
    fun toggleWishlist(productId: String) = update { s ->
        val has = s.wishlist.contains(productId)
        s.copy(wishlist = if (has) s.wishlist.filterNot { it == productId } else listOf(productId) + s.wishlist)
    }
    fun isWishlisted(id: String): Boolean = store.value.wishlist.contains(id)

    // ----- search & browsing history -----
    fun recordSearch(q: String) {
        val q2 = q.trim().takeIf { it.isNotEmpty() } ?: return
        update { s -> s.copy(recent = (listOf(q2) + s.recent.filterNot { it == q2 }).take(8)) }
    }
    fun clearRecent() = update { s -> s.copy(recent = emptyList()) }

    fun recordViewed(productId: String) = update { s ->
        s.copy(viewed = (listOf(productId) + s.viewed.filterNot { it == productId }).take(12))
    }

    fun markAllRead() = update { s -> s.copy(notifications = s.notifications.map { it.copy(read = true) }) }
    fun markRead(id: String) = update { s -> s.copy(notifications = s.notifications.map { if (it.id == id) it.copy(read = true) else it }) }

    fun setHaptics(v: Boolean) = update { it.copy(settingsHaptics = v) }
    fun setReducedMotion(v: Boolean) = update { it.copy(settingsReducedMotion = v) }
    fun setDemoBadge(v: Boolean) = update { it.copy(demoBadgeVisible = v) }

    /** Reset to a fresh logged-out state (clears cart/wishlist/orders/coupons). */
    fun logout() = update { s ->
        s.copy(
            cart = emptyList(), coupon = null, lastOrder = null,
            orders = emptyList(), entered = false, guest = false
        )
    }

    // ----- haptics helper -----
    private var hapticView: android.view.View? = null

    fun attachView(view: android.view.View) { hapticView = view }

    fun haptic() {
        val v = hapticView ?: return
        val s = store.value
        if (s.settingsHaptics && !s.settingsReducedMotion) {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }
}
