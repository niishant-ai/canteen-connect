package canteenconnect.app.features.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.*
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.*
import canteenconnect.app.data.demo.DemoData
import canteenconnect.app.data.models.*

@Composable
fun HomeScreen(onNav: (String) -> Unit) {
    val s = AppStore.state()
    // scope demo canteens to the selected city
    val canteens = AppStore.cityCanteens(s.city)
    LaunchedEffect(s.city) { AppStoreStateHolder.setCityCanteens(canteens) }

    val products = DemoData.products
    val popular = products.sortedByDescending { it.sold }.take(8)
    val bestsellers = products.filter { it.rating >= 4.5 }.sortedByDescending { it.sold }.take(8)
    val offers = products.filter { it.originalPrice > it.price }.take(8)
    val newItems = products.filter { it.isNew }.take(6)
    val recommended = products.filter { it.id in s.viewed }
        .ifEmpty { products.shuffled().take(6) }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        // ---- header: delivery address (tap → location) + profile icon ----
        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = Sp.s5, vertical = Sp.s2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(Radius.pill))
                    .background(
                        Brush.linearGradient(
                            listOf(O.Card.copy(alpha = 0.9f), O.Sand.copy(alpha = 0.55f))
                        ),
                        RoundedCornerShape(Radius.pill)
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(Radius.pill))
                    .clickable { onNav(Nav.LOCATION) }
                    .padding(start = 8.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(O.BlueSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) { IcMapPin(size = 17.dp, tint = O.BlueDeep, strokeWidth = 2f) }
                Spacer(Modifier.width(Sp.s2))
                Column(Modifier.weight(1f)) {
                    Text("Deliver to", fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = O.Mute)
                    Text("${s.area}, ${s.city}",
                        fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                IcChevronDown(size = 15.dp, tint = O.Slate, strokeWidth = 2.4f)
            }
            Spacer(Modifier.width(Sp.s2))
            HeaderIconButton({ m, t -> IcUser(m, t, 20.dp, 2f) }) { onNav(Nav.PROFILE) }
        }

        // search + filter
        Spacer(Modifier.height(Sp.s2))
        Row(
            Modifier.fillMaxWidth().padding(horizontal = Sp.s5),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(onTap = { onNav(Nav.Search) }, modifier = Modifier.weight(1f))
            Spacer(Modifier.width(Sp.s3))
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .glass(RoundedCornerShape(Radius.md))
                    .clickable { onNav(Nav.EXPLORE); AppStore.haptic() },
                contentAlignment = Alignment.Center
            ) { IcSliders(size = 20.dp, tint = O.Ink) }
        }
        Spacer(Modifier.height(Sp.s3))

        // ---- scrollable content ----
        // Bottom clearance must cover the floating nav (capsule height 56dp +
        // 12dp offset + system nav bar); 120dp left the last product row's
        // price/name clipped under the glass capsule. Match sibling screens.
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 132.dp)
        ) {
            // Top Categories
            SectionHeader("Top Categories", action = "See All", onAction = { onNav(Nav.EXPLORE) })
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = Sp.s5, top = Sp.s1, end = Sp.s5, bottom = Sp.s1)
            ) {
                DemoData.categories.take(8).forEach { c ->
                    CategoryTile(c) {
                        onNav(Nav.category(c.id)); AppStore.haptic()
                    }
                }
                Spacer(Modifier.width(Sp.s5))
            }

            // Trending banner carousel
            val pagerState = rememberPagerState { DemoData.banners.size }
            Column(Modifier.fillMaxWidth().padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                ) { i ->
                    BannerCard(DemoData.banners[i]) {
                        onNav(when (DemoData.banners[i].route) {
                            "produce" -> Nav.category("produce")
                            "canteens" -> Nav.EXPLORE
                            "categories" -> Nav.EXPLORE
                            else -> Nav.HOME
                        })
                    }
                }
                // page dots
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.height(Sp.s3))
                    DemoData.banners.indices.forEach { i ->
                        val activeDot = pagerState.currentPage == i
                        Spacer(Modifier.width(4.dp))
                        Box(
                            Modifier
                                .width(if (activeDot) 18.dp else 7.dp)
                                .height(7.dp)
                                .clip(RoundedCornerShape(99.dp))
                                .background(if (activeDot) O.Blue else O.Line)
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                }
            }

            // Special Deals rail
            if (offers.isNotEmpty()) {
                SectionHeader("Special Deals", action = "See All", onAction = { onNav(Nav.OFFERS) })
                ProductRail(offers) { pid -> onNav(Nav.productDetail(pid)) }
            }

            // Nearby canteens
            if (canteens.isNotEmpty()) {
                SectionHeader("Nearby canteens", action = "See all", onAction = { onNav(Nav.EXPLORE) })
                canteens.take(3).forEach { c ->
                    CanteenCard(
                        c,
                        modifier = Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2),
                        onView = { onNav(Nav.canteen(c.id)) }
                    )
                }
            } else {
                SectionHeader("Nearby canteens")
                EmptyState(
                    title = "No canteens in ${s.city}",
                    body = "This demo seeds canteens for New Delhi, Bengaluru, Pune, Hyderabad, Chandigarh and Lucknow. Try changing city.",
                    glyph = { IcStore(size = 30.dp, tint = O.Mute) },
                    actionLabel = "Change city",
                    onAction = { onNav(Nav.LOCATION) }
                )
            }

            // Popular products
            ProductRailSection("Popular", popular) { pid -> onNav(Nav.productDetail(pid)) }

            // New arrivals
            if (newItems.isNotEmpty()) {
                ProductRailSection("New Arrivals", newItems) { pid -> onNav(Nav.productDetail(pid)) }
            }

            // Recommended / recently viewed
            if (s.viewed.isNotEmpty()) {
                ProductRailSection("Recently Viewed", recommended) { pid -> onNav(Nav.productDetail(pid)) }
            }
        }
    }
}

/** Header circular icon button, optional count badge. */
@Composable
private fun HeaderIconButton(
    icon: @Composable (Modifier, Color) -> Unit,
    badge: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(O.Card.copy(alpha = 0.95f), O.Sand.copy(alpha = 0.7f))
                ),
                CircleShape
            )
            .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        icon(Modifier, O.Ink)
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 3.dp, y = 1.dp)
                    .clip(CircleShape)
                    .background(O.Danger)
                    .border(1.dp, O.Bg, CircleShape)
                    .padding(horizontal = 4.dp, vertical = 1.5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(badge, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 10.sp)
            }
        }
    }
}

/** Pastel category tile (reference "Top Categories"). */
@Composable
fun CategoryTile(
    c: Category,
    modifier: Modifier = Modifier,
    width: Dp = 108.dp,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .width(width)
            .clickable(onClick = onClick)
            .padding(end = Sp.s3),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(74.dp)
                .clip(RoundedCornerShape(24.dp)),
        ) {
            CategoryImage(
                c,
                modifier = Modifier.matchParentSize(),
                fallback = { m ->
                    Box(
                        m.background(O.surfaceOf(c.accent), RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) { CategoryGlyph(c) }
                }
            )
        }
        Spacer(Modifier.height(Sp.s2))
        // Two-line label slot keeps every tile the same height and fits long
        // names ("Snacks & Namkeen") without ellipsis truncation.
        Box(
            Modifier
                .fillMaxWidth()
                .height(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(c.name, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                maxLines = 2, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun CategoryGlyph(c: Category) {
    when (c.id) {
        "grocery" -> IcWheat(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "snacks" -> IcCookie(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "beverages" -> IcCoffee(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "dairy" -> IcMilk(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "packaged" -> IcUtensils(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "produce" -> IcCarrot(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "personal" -> IcDroplets(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "household" -> IcBasket(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "stationery" -> IcPencil(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "kitchen" -> IcCookingPot(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        "cleaning" -> IcSprayCan(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
        else -> IcGift(Modifier, O.deepOf(c.accent), 30.dp, 1.6f)
    }
}

/** Promotional banner card (reference "Daily Essentials"). */
@Composable
fun BannerCard(b: Banner, onShop: () -> Unit) {
    val surface = O.surfaceOf(b.accent)
    val deep = O.deepOf(b.accent)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(Radius.lg))
            .background(
                Brush.linearGradient(
                    listOf(O.BlueFaint, O.BlueSoft.copy(alpha = 0.55f), O.Sand)
                ),
                RoundedCornerShape(Radius.lg)
            )
            .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.lg))
            .clickable(onClick = onShop)
    ) {
        // soft radial glow, top-right — subtle depth without a blob
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .size(180.dp)
                .background(
                    Brush.radialGradient(
                        listOf(surface.copy(alpha = 0.9f), Color.Transparent),
                        radius = 90f
                    )
                )
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Sp.s5),
            verticalAlignment = Alignment.CenterVertically
        ) {
        // Left column: chip, title, subtitle, CTA. Takes the remaining row
        // space; the CTA pill wraps its content so "Shop now" never collapses
        // to an ellipsis on narrow (e.g. split-view) widths.
        Column(Modifier.weight(1f)) {
            Chip("Canteen Special", O.Card, O.BlueDeep)
            Spacer(Modifier.height(Sp.s3))
            Text(b.title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                lineHeight = 24.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(Sp.s1))
            Text(b.subtitle, fontSize = 13.sp, color = O.Slate, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(Sp.s3))
            Row(
                modifier = Modifier
                    .height(44.dp)
                    .clip(RoundedCornerShape(Radius.pill))
                    .background(O.Charcoal, RoundedCornerShape(Radius.pill))
                    .clickable(onClick = onShop)
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Shop now", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold,
                    color = Color.White)
                Spacer(Modifier.width(6.dp))
                IcArrowRight(size = 15.dp, tint = Color.White, strokeWidth = 2.4f)
            }
        }
        Spacer(Modifier.width(Sp.s4))
        // Illustration tile, right edge — no longer cropped by the card boundary
        Box(
            Modifier
                .align(Alignment.Bottom)
                .size(84.dp)
                .clip(RoundedCornerShape(Radius.lg))
                .background(surface, RoundedCornerShape(Radius.lg)),
            contentAlignment = Alignment.Center
        ) {
            BannerGlyph(b, deep)
        }
        }
    }
}

@Composable
private fun BannerGlyph(b: Banner, deep: Color, modifier: Modifier = Modifier) {
    when (b.id) {
        "b1" -> IcWheat(modifier, deep, 40.dp, 1.5f)
        "b2" -> IcCarrot(modifier, deep, 40.dp, 1.5f)
        "b3" -> IcStore(modifier, deep, 40.dp, 1.5f)
        else -> IcBag(modifier, deep, 40.dp, 1.5f)
    }
}

/** Section + horizontal product rail. */
@Composable
private fun ProductRailSection(title: String, products: List<Product>, onProduct: (String) -> Unit) {
    if (products.isEmpty()) return
    SectionHeader(title)
    ProductRail(products) { onProduct(it) }
}

@Composable
private fun ProductRail(products: List<Product>, onProduct: (String) -> Unit) {
    if (products.isEmpty()) return
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = Sp.s5, vertical = Sp.s2)
    ) {
        products.take(12).forEach { p ->
            ProductCard(p, modifier = Modifier.width(150.dp).padding(end = Sp.s3),
                onProduct = { onProduct(p.id) })
        }
        Spacer(Modifier.width(Sp.s5))
    }
}
