package canteenconnect.app.features.products

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import canteenconnect.app.core.icons.IcBasket
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.*
import canteenconnect.app.data.demo.DemoData

enum class SortMode { Relevance, PriceAsc, PriceDesc, Rating, Popularity }

/**
 * Generic product listing with live filtering: category, price band, rating,
 * canteen and sort — all against the local demo dataset.
 */
@Composable
fun ProductListingScreen(onNav: (String) -> Unit, onBack: () -> Unit,
                         fixedCategoryId: String? = null, canteenId: String? = null) {
    var cat by remember { mutableStateOf(fixedCategoryId ?: "all") }
    var sort by remember { mutableStateOf(SortMode.Relevance) }
    var maxPrice by remember { mutableIntStateOf(0) } // 0 = any
    var ratingFilter by remember { mutableIntStateOf(0) } // 0 = any, else >=
    var canteenFilter by remember { mutableStateOf(canteenId ?: "all") }

    var base: List<canteenconnect.app.data.models.Product> = remember { DemoData.products }
    base = base.filter { p ->
        (cat == "all" || p.categoryId == cat) &&
            (maxPrice == 0 || p.price <= maxPrice) &&
            (ratingFilter == 0 || p.rating >= ratingFilter)
    }
    if (canteenFilter != "all") {
        val stock = DemoData.stock(canteenFilter)
        base = base.filter { it.id in stock }
    }
    val shown = when (sort) {
        SortMode.PriceAsc -> base.sortedBy { it.price }
        SortMode.PriceDesc -> base.sortedByDescending { it.price }
        SortMode.Rating -> base.sortedByDescending { it.rating }
        SortMode.Popularity -> base.sortedByDescending { it.sold }
        SortMode.Relevance -> base
    }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar(
            if (fixedCategoryId != null) DemoData.category(fixedCategoryId).name
            else canteenFilter.let { if (it != "all") DemoData.canteen(it)?.name ?: "Products" else "All products" },
            subtitle = "${shown.size} products",
            onBack = onBack
        )
        // filter + sort chips
        Row(Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Sp.s5, vertical = Sp.s2),
            horizontalArrangement = Arrangement.spacedBy(Sp.s2)
        ) {
            FilterChip("All", active = cat == "all") { cat = "all" }
            Spacer(Modifier.width(Sp.s1))
            DemoData.categories.forEach { c ->
                FilterChip(c.name, active = cat == c.id) { cat = c.id }
                Spacer(Modifier.width(Sp.s2))
            }
            FilterChip(if (maxPrice == 0) "Price" else "≤₹$maxPrice", active = maxPrice != 0) {
                maxPrice = when (maxPrice) { 0 -> 200; 200 -> 500; else -> 0 }
            }
            Spacer(Modifier.width(Sp.s1))
            FilterChip(if (ratingFilter == 0) "4.0+" else "Any rating", active = ratingFilter == 4) {
                ratingFilter = if (ratingFilter == 4) 0 else 4
            }
            Spacer(Modifier.width(Sp.s1))
            FilterChip("Popular", active = sort == SortMode.Popularity) {
                sort = if (sort == SortMode.Popularity) SortMode.Relevance else SortMode.Popularity
            }
            Spacer(Modifier.width(Sp.s1))
            FilterChip("Price ↑", active = sort == SortMode.PriceAsc) {
                sort = if (sort == SortMode.PriceAsc) SortMode.Relevance else SortMode.PriceAsc
            }
            Spacer(Modifier.width(Sp.s1))
            FilterChip("Price ↓", active = sort == SortMode.PriceDesc) {
                sort = if (sort == SortMode.PriceDesc) SortMode.Relevance else SortMode.PriceDesc
            }
            Spacer(Modifier.width(Sp.s1))
            FilterChip("Top rated", active = sort == SortMode.Rating) {
                sort = if (sort == SortMode.Rating) SortMode.Relevance else SortMode.Rating
            }
            Spacer(Modifier.width(Sp.s5))
        }
        if (shown.isEmpty()) {
            EmptyState(
                title = "No products match",
                body = "Try loosening a filter or choosing a different canteen.",
                glyph = { IcBasket(size = 30.dp, tint = O.Mute) },
                actionLabel = "Clear filters",
                onAction = {
                    cat = "all"; maxPrice = 0; ratingFilter = 0; sort = SortMode.Relevance; canteenFilter = "all"
                }
            )
        } else {
            Column(Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Sp.s5, vertical = Sp.s3)
                .padding(bottom = 132.dp)) {
                shown.chunked(2).forEach { rowItems ->
                    Row(Modifier.fillMaxWidth().padding(bottom = Sp.s3),
                        horizontalArrangement = Arrangement.spacedBy(Sp.s3),
                        verticalAlignment = Alignment.Top) {
                        rowItems.forEach { p ->
                            ProductCard(
                                p,
                                canteenId = canteenFilter.takeIf { it != "all" },
                                modifier = Modifier.weight(1f),
                                onProduct = { onNav(Nav.productDetail(p.id)); AppStore.recordViewed(p.id) }
                            )
                        }
                        if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
