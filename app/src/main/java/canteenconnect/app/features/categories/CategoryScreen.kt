package canteenconnect.app.features.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.*
import canteenconnect.app.core.icons.IcSliders
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp
import canteenconnect.app.data.demo.DemoData
import canteenconnect.app.data.models.Category

/** Category listing: two-column product grid scoped to the category, with a
 *  sort control wired to [ProductListingScreen] behaviour. */
@Composable
fun CategoryScreen(categoryId: String, onNav: (String) -> Unit, onBack: () -> Unit) {
    val category: Category = DemoData.category(categoryId)
    val s = AppStore.state()
    val scoping = AppStore.cityCanteens(s.city)
    val pool = if (scoping.isNotEmpty()) scoping else DemoData.canteens
    val seen = LinkedHashSet<String>()
    var sort by remember { mutableStateOf("popular") }
    var items = pool.flatMap { c -> DemoData.productsFor(c.id) }
        .filter { it.categoryId == categoryId }
        .distinctBy { p -> seen.add(p.id) }
        .take(40)
    when (sort) {
        "price-asc" -> items = items.sortedBy { it.price }
        "price-desc" -> items = items.sortedByDescending { it.price }
        else -> items = items.sortedByDescending { it.sold }
    }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar(category.name, subtitle = "${items.size} products", onBack = onBack)
        // filter + sort row
        Row(
            Modifier.fillMaxWidth().padding(horizontal = Sp.s5, vertical = Sp.s2),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            FilterChip("Popularity", sort == "popular") { sort = "popular" }
            Spacer(Modifier.width(Sp.s2))
            FilterChip("Price: Low→High", sort == "price-asc") { sort = "price-asc" }
            Spacer(Modifier.width(Sp.s2))
            FilterChip("Price: High→Low", sort == "price-desc") { sort = "price-desc" }
        }
        if (items.isEmpty()) {
            EmptyState(
                title = "Nothing here yet",
                body = "No ${category.name.lowercase()} stocked at canteens in ${s.city}. Try another city or category.",
                glyph = { IcSliders(size = 30.dp, tint = O.Mute) },
                actionLabel = "Back to Explore",
                onAction = { onNav(Nav.EXPLORE) }
            )
        } else {
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Sp.s5, vertical = Sp.s3)
                    .padding(bottom = 132.dp)
            ) {
                items.chunked(2).forEach { rowItems ->
                    Row(Modifier.fillMaxWidth().padding(bottom = Sp.s3),
                        horizontalArrangement = Arrangement.spacedBy(Sp.s3),
                        verticalAlignment = Alignment.Top) {
                        rowItems.forEach { p ->
                            ProductCard(p, modifier = Modifier.weight(1f),
                                onProduct = { onNav(Nav.productDetail(p.id)); AppStore.recordViewed(p.id) })
                        }
                        if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
