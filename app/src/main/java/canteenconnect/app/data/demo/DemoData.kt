package canteenconnect.app.data.demo

import canteenconnect.app.data.models.*
import kotlin.random.Random

/**
 * Locally seeded demo dataset. Everything here is fictional demonstration data:
 * canteen names, addresses, prices and ratings are invented for the demo and do
 * not represent actual registered government canteens.
 */
object DemoData {

    // ---- accent surfaces (index -> (surfaceHex, deepHex)) ----
    // 0 Sky Blue, 1 Lavender, 2 Mint, 3 Peach, 4 Butter, 5 Rose, 6 Sand
    val SURFACES = mapOf(
        0 to Pair(0xFFE3F1FF, 0xFF4C8DF5),
        1 to Pair(0xFFEEE3FF, 0xFF7B5BE0),
        2 to Pair(0xFFDCFBEE, 0xFF1F9E7A),
        3 to Pair(0xFFFFECDD, 0xFFE88B45),
        4 to Pair(0xFFFDF6D8, 0xFFB9902A),
        5 to Pair(0xFFFFE4EA, 0xFFE0568A),
        6 to Pair(0xFFF3EFE2, 0xFF8A7A55)
    )

    val categories = listOf(
        Category("grocery", "Groceries", "🌾", 6, "Staples, grains & daily essentials"),
        Category("snacks", "Snacks & Namkeen", "🥜", 3, "Biscuits, nuts & crunchy picks"),
        Category("beverages", "Beverages", "🥤", 0, "Tea, coffee, juices & water"),
        Category("dairy", "Dairy", "🥛", 5, "Milk, curd, paneer & ghee"),
        Category("packaged", "Packaged Foods", "🍱", 4, "Ready-to-eat & shelf-stable meals"),
        Category("produce", "Fruits & Veggies", "🥕", 2, "Fresh fruits & vegetables"),
        Category("personal", "Personal Care", "🧴", 1, "Soaps, shampoo & care"),
        Category("household", "Household Essentials", "🧺", 6, "Cleaning & home staples"),
        Category("stationery", "Stationery", "📓", 4, "Notebooks, pens & office supplies"),
        Category("kitchen", "Kitchen Essentials", "🍳", 0, "Cooking gear & pantry items"),
        Category("cleaning", "Cleaning Supplies", "🧽", 2, "Detergents, dusters & care"),
        Category("other", "Other Canteen Items", "🛍️", 1, "Gifts, misc & more")
    )

    val banners = listOf(
        Banner("b1", "Everyday Essentials", "Groceries & staples, stocked fresh daily", "🌾", 6, "categories"),
        Banner("b2", "Fresh Stock Arrived", "Fruits & veggies just in at nearby canteens", "🥕", 2, "produce"),
        Banner("b3", "Discover Nearby Canteens", "Browse registered canteens close to you", "🏛️", 0, "canteens"),
        Banner("b4", "Your Daily Needs, One Place", "Shop, add to cart & track your order", "🛒", 3, "home")
    )

    val cities = listOf(
        CityOption("New Delhi", listOf("Vikas Sadan", "Rajendra Nagar", "Gokulpuri", "Chanakyapuri"),
            listOf("110090", "110064", "110094", "110021")),
        CityOption("Bengaluru", listOf("Hebbal", "Koramangala", "Whitefield", "HSR Layout"),
            listOf("560024", "560034", "560048", "560049")),
        CityOption("Pune", listOf("Kothrud", "Aundh", "Baner", "Viman Nagar"),
            listOf("411038", "411007", "411045", "411014")),
        CityOption("Hyderabad", listOf("Banjara Hills", "Gachibowli", "Secunderabad", "Madhapur"),
            listOf("500034", "500032", "500003", "500081")),
        CityOption("Chandigarh", listOf("Sector 35", "Sector 21", "Sector 9", "Mohali"),
            listOf("160035", "160021", "160009", "140306")),
        CityOption("Lucknow", listOf("Aligunj", "Hazratganj", "Gomti Nagar", "Aminabad"),
            listOf("226005", "226001", "226016", "226002"))
    )

    val canteens = listOf(
        Canteen("c1", "Sadar Canteen", "Vikas Sadan", "New Delhi", "110090", 480,
            "08:00", "19:30", true, true, true, 40, 5, 92, 4.7,
            "A long-standing staff canteen offering a broad range of groceries, snacks and hot beverages. Serves government offices nearby.",
            "🏛️", 0, 2),
        Canteen("c2", "Hebbal Hub Canteen", "Hebbal", "Bengaluru", "560024", 950,
            "07:30", "21:00", true, false, true, 0, 5, 88, 4.5,
            "Focused on fresh produce, dairy and beverages. No delivery in this demo; pick up at the counter.",
            "🏢", 2, 1),
        Canteen("c3", "Kothrud Court Canteen", "Kothrud", "Pune", "411038", 320,
            "09:00", "18:00", false, true, false, 30, 5, 76, 4.2,
            "Compact canteen near the courts. Great for stationery, household and personal care.",
            "🏬", 4, 0),
        Canteen("c4", "Gachibowli IT Park Canteen", "Gachibowli", "Hyderabad", "500032", 1500,
            "08:00", "22:00", true, true, true, 60, 8, 90, 4.8,
            "Full-service canteen serving the IT park. Wide stocked range across all categories.",
            "🏢", 1, 2),
        Canteen("c5", "Sector 35 Civic Canteen", "Sector 35", "Chandigarh", "160035", 620,
            "08:30", "19:00", true, true, true, 45, 5, 84, 4.4,
            "Balanced catalogue of groceries, packaged foods and beverages with reliable delivery.",
            "🏛️", 3, 1),
        Canteen("c6", "Hazratganj Central Canteen", "Hazratganj", "Lucknow", "226001", 780,
            "09:00", "20:00", true, false, false, 35, 0, 81, 4.3,
            "Neighbourhood canteen near the market. Pickup only in this demo with curated snacks & produce.",
            "🏬", 5, 0)
    )

    private fun prod(id: String, name: String, brand: String, unit: String, price: Int,
                     orig: Int, cat: String, tagline: String, emoji: String, accent: Int,
                     rating: Double, sold: Int, isNew: Boolean = false) =
        Product(id, name, brand, unit, price, orig, cat, tagline,
            "$tagline — $brand $name, $unit, served at registered canteens. Demo data.",
            emoji, accent, rating, sold, isNew)

    val products = listOf(
        // Groceries
        prod("g1", "Basmati Rice", "Amritdhara", "5 kg", 999, 1249, "grocery", "Aromatic long-grain rice", "🌾", 6, 4.6, 820),
        prod("g2", "Whole Wheat Atta", "Kisan", "1 kg", 89, 110, "grocery", "Stone-milled chakki atta", "🥣", 6, 4.4, 340),
        prod("g3", "Toor Dal", "Kisan", "1 kg", 159, 189, "grocery", "Protein-rich split pigeon pea", "🫘", 6, 4.5, 610),
        prod("g4", "Moong Dal", "Patanjali", "500 g", 95, 0, "grocery", "Light and quick-cooking dal", "🫘", 6, 4.3, 420),
        prod("g5", "Sooji (Rava)", "Aashirvaad", "1 kg", 65, 0, "grocery", "Fine semolina for upma", "🥣", 6, 4.2, 280),
        prod("g6", "Maida", "Aashirvaad", "500 g", 45, 0, "grocery", "Refined flour for baking", "🌾", 6, 4.0, 200),
        prod("g7", "Ghee (Clarified Butter)", "Mata", "500 ml", 320, 375, "grocery", "Pure 1A grade desi ghee", "🧈", 4, 4.8, 540),
        prod("g8", "Sesame Oil", "Suguna", "1 L", 240, 275, "grocery", "Cold-pressed kalu neni", "🫗", 4, 4.4, 300),
        prod("g9", "Black Salt", "Eva", "200 g", 40, 0, "grocery", "Kala namak for chutneys", "🧂", 6, 4.1, 150),
        prod("g10", "Turmeric Powder", "Marudher", "100 g", 55, 0, "grocery", "Fresh-ground lal mirch-free", "🟡", 4, 4.5, 460),
        // Snacks & Namkeen
        prod("s1", "Butter Biscuits", "Britannia", "250 g", 42, 0, "snacks", "Golden Marie biscuits", "🍪", 3, 4.7, 900),
        prod("s2", "Green Peanuts", "Haldiram", "200 g", 60, 0, "snacks", "Roasted namkeen mungfali", "🥜", 3, 4.4, 640),
        prod("s3", "Poha (Flattened Rice)", "MTR", "500 g", 45, 0, "snacks", "Soft thin poha for breakfast", "🍚", 3, 4.3, 380),
        prod("s4", "Cheese Crackers", "Britannia", "300 g", 85, 99, "snacks", "Crispy cheddar biscuits", "🧀", 3, 4.5, 410),
        prod("s5", "Roasted Chana", "Haldiram", "250 g", 70, 0, "snacks", "Protein chana namkeen", "🥜", 3, 4.2, 350),
        prod("s6", "Samosa (Frozen)", "Nissin", "4 pcs", 90, 0, "snacks", "Air-fried ready samosa", "🥟", 3, 4.0, 260, true),
        // Beverages
        prod("b1", "Green Tea", "Lipton", "500 g", 250, 299, "beverages", "Classic leaf green tea", "🍵", 0, 4.6, 720),
        prod("b2", "Instant Coffee", "Nescafe", "100 g", 399, 465, "beverages", "Smooth rich brew", "☕", 0, 4.7, 500),
        prod("b3", "Bottled Water", "Bisleri", "1 L × 6", 108, 0, "beverages", "Sealed mineral water pack", "💧", 0, 4.3, 1200),
        prod("b4", "Mango Juice", "Real", "1 L", 120, 0, "beverages", "Alphonso mango nectar", "🥭", 0, 4.4, 430),
        prod("b5", "Badam Milk (Amal", "Amal", "250 ml", 45, 0, "beverages", "Nutrient milk carton", "🥛", 0, 4.1, 300),
        prod("b6", "Coca Cola", "Coca-Cola", "750 ml", 65, 0, "beverages", "Chilled fizzy classic", "🥤", 0, 4.0, 800),
        // Dairy
        prod("d1", "Full Cream Milk", "Nandini", "1 L", 78, 0, "dairy", "Toned-down full cream", "🥛", 5, 4.6, 1400),
        prod("d2", "Curd (Dahi)", "Verite", "200 g", 30, 0, "dairy", "Thick set curd", "🥣", 5, 4.3, 600),
        prod("d3", "Paneer", "Amul", "200 g", 95, 0, "dairy", "Fresh chhana cubes", "🧆", 5, 4.5, 480),
        prod("d4", "Cheddar Cheese", "Britannia", "200 g", 210, 0, "dairy", "Sliced cheddar", "🧀", 5, 4.2, 350),
        prod("d5", "Butter (Salted)", "Amul", "200 g", 140, 0, "dairy", "Table butter", "🧈", 5, 4.6, 400),
        // Packaged
        prod("p1", "Ready Soup", "Knorr", "4 × 25 g", 96, 0, "packaged", "Instant masala soup", "🍲", 4, 4.0, 260),
        prod("p2", "Chips (Masala)", "Lays", "130 g", 90, 100, "packaged", "Crinkle-cut masala", "🥔", 4, 4.5, 1500),
        prod("p3", "Instant Noodles", "Maggi", "500 g", 115, 0, "packaged", "2-minute masala noodles", "🍜", 4, 4.6, 1100),
        prod("p4", "Ready Maida Paratha", "MTR", "500 g", 75, 0, "packaged", "Instant poha packs", "🥘", 4, 3.9, 240, true),
        prod("p5", "Honey Jar", "Apico", "300 g", 340, 400, "packaged", "Raw wildflower honey", "🍯", 4, 4.7, 380),
        // Produce
        prod("v1", "Bananas (Bunch)", "Fresh", "6 pcs", 60, 0, "produce", "Nendran bananas", "🍌", 2, 4.4, 900),
        prod("v2", "Tomatoes", "Fresh", "1 kg", 45, 0, "produce", "Ripe tamarind tomatoes", "🍅", 2, 4.2, 700),
        prod("v3", "Onion", "Fresh", "1 kg", 40, 0, "produce", "Red-skinned onions", "🧅", 2, 4.3, 820),
        prod("v4", "Apples", "Fresh", "4 pcs", 180, 0, "produce", "Shimla apples", "🍎", 2, 4.5, 460),
        prod("v5", "Spinach", "Fresh", "1 bunch", 30, 0, "produce", "Fresh palak", "🥬", 2, 4.1, 520),
        prod("v6", "Almonds", "Fresh", "250 g", 480, 560, "produce", "Raw blanched badam", "🌰", 2, 4.8, 340),
        // Personal
        prod("pc1", "Bath Soap", "Lux", "4 × 100 g", 100, 0, "personal", "Fragrant bath bar", "🧼", 1, 4.4, 1300),
        prod("pc2", "Shampoo", "Dove", "360 ml", 260, 300, "personal", "Nourishing shampoo", "🧴", 1, 4.5, 500),
        prod("pc3", "Toothpaste", "Colgate", "200 g", 95, 0, "personal", "Gum care paste", "🪥", 1, 4.6, 900),
        prod("pc4", "Face Wash", "Himalaya", "150 ml", 150, 0, "personal", "Herbal face wash", "🧖", 1, 4.3, 420),
        // Household
        prod("h1", "Broom (Jhoola)", "Home", "1 pc", 80, 0, "household", "Palm leaf broom", "🧹", 6, 4.0, 300),
        prod("h2", "Sweeper", "Home", "1 pc", 60, 0, "household", "Plastic sweeper", "🧹", 6, 3.9, 280),
        prod("h3", "Garbage Bags", "Home", "50 pcs", 99, 0, "household", "Trash bag roll", "🛍️", 6, 4.1, 400),
        prod("h4", "Matchbox", "Match", "2 × 40", 24, 0, "household", "Safety matchboxes", "🔥", 6, 4.2, 260),
        // Stationery
        prod("st1", "Notebook 200pg", "Camlin", "1 pc", 55, 0, "stationery", "A5 hard cover", "📓", 4, 4.4, 700),
        prod("st2", "Ballpoint Pens", "Pentel", "10 pcs", 90, 0, "stationery", "0.7 mm blue pens", "🖊️", 4, 4.3, 820),
        prod("st3", "A4 Ream", "Kraft", "500 sht", 260, 0, "stationery", "80 GSM paper", "📄", 4, 4.2, 300),
        prod("st4", "Geometry Box", "Stationery", "1 pc", 120, 0, "stationery", "Set of 16 tools", "📐", 4, 4.1, 240, true),
        // Kitchen
        prod("k1", "Masala Chai Cup", "Steel", "6 pcs", 180, 0, "kitchen", "Steel chai set", "🫖", 0, 4.3, 320),
        prod("k2", "Steel Tawa", "Pigeon", "10 inch", 240, 0, "kitchen", "Chapati tawa", "🍳", 0, 4.5, 400),
        prod("k3", "Glass Bottle", "Kich", "1 L", 120, 0, "kitchen", "Water storage bottle", "🍶", 0, 4.2, 260),
        prod("k4", "Steel Glass Set", "Pigeon", "6 pcs", 220, 0, "kitchen", "Thermacold cups", "🥛", 0, 4.4, 380),
        // Cleaning
        prod("cl1", "Dish Bar Soap", "Harpic", "250 g", 48, 0, "cleaning", "Dish soap bar", "🧽", 2, 4.3, 1000),
        prod("cl2", "Liquid Detergent", "Surf", "1 L", 190, 0, "cleaning", "Stain-removing wash", "🧺", 2, 4.5, 600),
        prod("cl3", "Floor Cleaner", "Surf", "1 L", 160, 0, "cleaning", "Shine & sanitize", "🧴", 2, 4.2, 300),
        prod("cl4", "Sponge Pack", "Home", "4 pcs", 50, 0, "cleaning", "Scrub sponges", "🧽", 2, 4.0, 380),
        // Other
        prod("o1", "Gift Hamper", "CC Gifts", "1 box", 499, 0, "other", "Festive assorted hamper", "🎁", 1, 4.6, 120, true),
        prod("o2", "Lamp (Rechargeable)", "Eureka", "1 pc", 210, 0, "other", "Emergency torch", "🔦", 1, 4.1, 180),
        prod("o3", "Earphone (Wired)", "JBL", "1 pc", 299, 399, "other", "In-ear audio", "🎧", 1, 4.3, 260)
    )

    /** Which canteen stocks which product (id sets). */
    private val canteenStock: Map<String, Set<String>> = mapOf(
        "c1" to hashSetOf("g1","g2","g3","g4","g7","s1","s2","b1","b2","b3","d1","d2","d5","p3","pc1","pc2","st1","st2","k2","cl1","cl2"),
        "c2" to hashSetOf("v1","v2","v3","v4","v5","v6","d1","d2","d3","b3","b4","g7","s1","pc1"),
        "c3" to hashSetOf("st1","st2","st3","st4","h1","h2","h3","h4","pc1","pc2","pc3","pc4","k1","cl2","o2","o3","p4"),
        "c4" to hashSetOf("g1","g2","g3","g5","g6","g8","g9","g10","s1","s2","s3","s4","s5","s6","b1","b2","b6","d1","d4","d5","p1","p2","p3","p5","pc1","pc3","k1","k2","k3","k4","cl1","cl2","cl3","o1","o3"),
        "c5" to hashSetOf("g1","g3","g4","s1","s3","b1","b5","d1","d2","d3","p2","p3","pc1","pc4","k3","cl1","cl4","h3"),
        "c6" to hashSetOf("v1","v2","v3","v5","s2","s3","s5","b3","b4","p2","pc1","pc2","k1","cl1","h2","h4")
    )

    fun stock(canteenId: String): Set<String> = canteenStock[canteenId] ?: emptySet()

    /** Products available at a given canteen. */
    fun productsFor(canteenId: String): List<Product> {
        val ids = canteenStock[canteenId] ?: emptySet()
        return products.filter { it.id in ids }
    }

    val notifications = buildList {
        fun t(i: Long, d: Long) = System.currentTimeMillis() - d
        add(DemoNotification("n1", "📦", "Order #CC-1042 updated", "Your pickup order from Sadar Canteen is now Ready for Pickup.", t(1, 0)))
        add(DemoNotification("n2", "🏪", "New canteen near you", "Gachibowli IT Park Canteen just joined Canteen Mitra.", t(1, 3600000 * 5)))
        add(DemoNotification("n3", "🎯", "Fresh stock alert", "Apples & Almonds are in stock at Hebbal Hub Canteen today.", t(1, 3600000 * 26)))
        add(DemoNotification("n4", "🎁", "Weekend offer", "On us: free delivery on orders above ₹300 this weekend (demo).", t(1, 3600000 * 48)))
        add(DemoNotification("n5", "ℹ️", "Canteen Mitra tips", "Use the Wishlist tab to save products and add them to your cart later.", t(1, 3600000 * 72)))
    }

    val addresses = listOf(
        SavedAddress("a1", "Home", "42 Vikas Sadan B Block", "New Delhi"),
        SavedAddress("a2", "Office", "Tower B, 3rd Floor", "New Delhi"),
        SavedAddress("a3", "Gym", "Sports Arena, Ground", "New Delhi")
    )

    /** Canteen offers / coupon codes (demo). */
    val offers = listOf(
        Offer("o1", "CANTEEN10", "Flat 10% OFF",
            "On groceries above \u20b9299", true, 10, cap = 100, minOrder = 299, category = "grocery", accent = 0),
        Offer("o2", "SNACK25", "Up to 25% OFF",
            "On beverages & snacks", true, 25, cap = 150, minOrder = 0, category = "snacks", accent = 1),
        Offer("o3", "SAVE50", "Flat \u20b950 OFF",
            "On orders above \u20b9499", false, 50, minOrder = 499, accent = 2),
        Offer("o4", "FRESH5", "5% off produce",
            "On fruits & veggies above \u20b9199", true, 5, cap = 60, minOrder = 199, category = "produce", accent = 3),
        Offer("o5", "CARE15", "15% OFF personal care",
            "On soaps, shampoo & more", true, 15, cap = 120, minOrder = 199, category = "personal", accent = 4),
        Offer("o6", "FREEDEL", "Free delivery",
            "On delivery orders above \u20b9399", false, 0, minOrder = 399, freeDeliveryAbove = 399, accent = 5)
    )

    /** An example past order for the "My Orders" demo. */
    fun seededOrder(): DemoOrder {
        val c = canteens[0]
        val items = listOf(
            OrderItem("b1", "Green Tea", "🍵", 0, "500 g", 250, 2),
            OrderItem("s1", "Butter Biscuits", "🍪", 3, "250 g", 42, 4),
            OrderItem("d1", "Full Cream Milk", "🥛", 5, "1 L", 78, 6)
        )
        val sub = items.sumOf { it.price * it.qty }
        val fees = c.deliveryFee + c.packagingFee
        return DemoOrder("CC-1042", c.id, c.name, items, sub, fees, sub + fees,
            "Pickup", 3, System.currentTimeMillis() - 3600000L * 3, "Ready in ~20 min")
    }

    fun category(id: String): Category = categories.firstOrNull { it.id == id } ?: categories.first()
    fun canteen(id: String): Canteen? = canteens.firstOrNull { it.id == id }
    fun product(id: String): Product? = products.firstOrNull { it.id == id }
}
