package canteenconnect.app.core.navigation

/** All navigable destinations in Opshar.
 *  The four capsule destinations (Home, Explore, Orders & Wishlist, Cart) are
 *  switched via the floating bottom navigation; Profile and detail/flow screens
 *  are pushed on a back stack. */
object Nav {
    const val ONBOARDING = "onboarding"
    const val MEMBERSHIP = "membership"
    const val LOCATION = "location"

    const val HOME = "home"
    const val EXPLORE = "explore"
    /** Combined Orders + Wishlist hub (third capsule destination). */
    const val ORDERS = "orders"
    const val CART = "cart"

    const val PROFILE = "profile"
    const val OFFERS = "offers"
    const val CanteenDetail = "canteen/{id}"
    const val Category = "category/{id}"
    const val Product = "product/{id}"
    const val ProductDetail = "productdetail/{id}"
    const val Search = "search"
    const val Checkout = "checkout"
    const val OrderSuccess = "ordersuccess/{id}"
    const val OrderDetail = "order/{id}"
    const val Notifications = "notifications"
    const val Settings = "settings"
    const val Help = "help"
    const val About = "about"
    const val Privacy = "privacy"
    const val Terms = "terms"

    fun canteen(id: String) = "canteen/$id"
    fun category(id: String) = "category/$id"
    fun productDetail(id: String) = "productdetail/$id"
    fun orderSuccess(id: String) = "ordersuccess/$id"
    fun order(id: String) = "order/$id"

    /** Four capsule destinations. Labels here drive the pill expansion. */
    val bottomTabs = listOf(
        Tab(Nav.HOME, "Home"),
        Tab(Nav.EXPLORE, "Explore"),
        Tab(Nav.ORDERS, "Orders"),
        Tab(Nav.CART, "Cart")
    )

    data class Tab(val route: String, val label: String)
}
