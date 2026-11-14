package canteenconnect.app.features.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.*
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.*
import canteenconnect.app.data.demo.DemoData
import canteenconnect.app.data.models.*

/**
 * The third capsule destination: a combined Orders & Wishlist hub with a
 * segmented switch. One destination holds both (reference behaviour).
 */
@Composable
fun OrdersScreen(onNav: (String) -> Unit) {
    val s = AppStore.state()
    var tab by remember { mutableIntStateOf(0) } // 0 orders, 1 wishlist
    val orders = s.orders
    var tick by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) { kotlinx.coroutines.delay(2000); tick = tick + 1 }
    }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Orders & Wishlist", subtitle = when (tab) {
            0 -> "${orders.size} ${if (orders.size == 1) "order" else "orders"}"
            1 -> "${s.wishlist.size} saved"
            else -> ""
        })

        // segmented switch
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Sp.s5, vertical = Sp.s2)
                .background(O.BlueFaint, RoundedCornerShape(Radius.pill))
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Seg("My Orders", 0, tab, Modifier.weight(1f)) { tab = 0 }
            Seg("Wishlist", 1, tab, Modifier.weight(1f)) { tab = 1 }
        }

        when (tab) {
            0 -> OrdersList(orders, Modifier.weight(1f), onNav)
            else -> WishlistList(Modifier.weight(1f), onNav)
        }
    }
}

@Composable
private fun Seg(label: String, index: Int, current: Int, modifier: Modifier, onClick: () -> Unit) {
    val on = current == index
    Row(
        modifier
            .height(44.dp)
            .clip(RoundedCornerShape(Radius.pill))
            .background(if (on) O.Card else Color.Transparent, RoundedCornerShape(Radius.pill))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(label, fontSize = 14.sp, fontWeight = if (on) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = if (on) O.Ink else O.Slate)
    }
}

/** Order history with live demo status, view + reorder. */
@Composable
private fun OrdersList(orders: List<DemoOrder>, modifier: Modifier, onNav: (String) -> Unit) {
    if (orders.isEmpty()) {
        Box(modifier.fillMaxSize()) {
            EmptyState(
                modifier = Modifier.align(Alignment.Center),
                title = "No orders yet",
                body = "Your placed orders will appear here with live status. Browse products to make your first order.",
                glyph = { IcBox(size = 30.dp, tint = O.Mute) },
                actionLabel = "Shop now",
                onAction = { onNav(Nav.HOME) }
            )
        }
        return
    }
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Sp.s5, vertical = Sp.s3)
            .padding(bottom = 132.dp)
    ) {
        orders.forEach { o ->
            val live = AppStore.liveStatus(o)
            OrderCard(o, live, modifier = Modifier.padding(vertical = Sp.s2),
                onOpen = { onNav(Nav.order(o.id)) },
                onReorder = {
                    AppStore.reorder(o.id); AppStore.haptic()
                    onNav(Nav.CART)
                })
        }
    }
}

/** Wishlist two-column product grid with add-to-cart + remove. */
@Composable
private fun WishlistList(modifier: Modifier, onNav: (String) -> Unit) {
    val s = AppStore.state()
    val items = s.wishlist.mapNotNull { DemoData.product(it) }
    if (items.isEmpty()) {
        Box(modifier.fillMaxSize()) {
            EmptyState(
                modifier = Modifier.align(Alignment.Center),
                title = "Nothing saved yet",
                body = "Tap the heart on any product to keep it here. You can add saved items to your cart whenever you like.",
                glyph = { IcHeart(size = 30.dp, tint = O.Mute) },
                actionLabel = "Browse products",
                onAction = { onNav(Nav.EXPLORE) }
            )
        }
        return
    }
    Column(
        modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Sp.s5, vertical = Sp.s3)
            .padding(bottom = 132.dp)
    ) {
        items.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth().padding(bottom = Sp.s3),
                horizontalArrangement = Arrangement.spacedBy(Sp.s3),
                verticalAlignment = Alignment.Top) {
                row.forEach { p ->
                    WishCard(p, Modifier.weight(1f),
                        onOpen = { onNav(Nav.productDetail(p.id)); AppStore.recordViewed(p.id) },
                        onRemove = { AppStore.toggleWishlist(p.id); AppStore.haptic() },
                        onAdd = {
                            AppStore.addToCart(p.id, anyStockingCanteen(p.id))
                            AppStore.haptic()
                        })
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

/** Compact wishlist product card (reference "Favorite Products"). */
@Composable
private fun WishCard(
    product: Product,
    modifier: Modifier,
    onOpen: () -> Unit,
    onRemove: () -> Unit,
    onAdd: () -> Unit
) {
    val pct = discountPct(product)
    Column(
        modifier = modifier
            .background(O.Card, RoundedCornerShape(Radius.card))
            .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))
            .clickable(onClick = onOpen)
            .padding(Sp.s3)
    ) {
        Box(Modifier.fillMaxWidth()) {
            ProductTile(product, size = 76.dp, glyphSize = 34.dp)
            if (product.rating >= 4.0) {
                Box(
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(O.Card.copy(alpha = 0.94f), RoundedCornerShape(7.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Text("★", fontSize = 10.sp, color = O.Amber, fontWeight = FontWeight.ExtraBold)
                        Text(product.rating.toString(), fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                            color = O.Amber, modifier = Modifier.padding(start = 2.dp))
                    }
                }
            }
            if (pct > 0) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(O.Danger, RoundedCornerShape(7.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("-$pct%", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                }
            }
        }
        Spacer(Modifier.height(Sp.s2))
        Text(product.name, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(product.brand, fontSize = 11.sp, color = O.Slate, maxLines = 1,
            overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(3.dp))
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
            Text("₹${product.price}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
            if (pct > 0) {
                Text("₹${product.originalPrice}", fontSize = 10.5.sp, color = O.Mute,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                    modifier = Modifier.padding(start = 4.dp, bottom = 1.dp))
            }
            Spacer(Modifier.weight(1f))
        }
        Row(
            Modifier.fillMaxWidth().padding(top = Sp.s2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(O.BlueSoft, RoundedCornerShape(10.dp))
                    .clickable(onClick = onAdd)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IcPlus(size = 13.dp, tint = O.BlueDeep, strokeWidth = 2.6f)
                    Text(" Add", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = O.BlueDeep,
                        modifier = Modifier.padding(start = 3.dp))
                }
            }
            Spacer(Modifier.weight(1f))
            Box(
                    Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(O.Surface, CircleShape)
                        .clickable(onClick = onRemove),
                    contentAlignment = Alignment.Center
                ) {
                IcTrash(size = 14.dp, tint = O.Danger, strokeWidth = 2f)
            }
        }
    }
}

/** One order row (used by [OrdersList]). */
@Composable
fun OrderCard(
    o: DemoOrder,
    live: Int,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit,
    onReorder: (() -> Unit)? = null
) {
    val statusLabel = when {
        o.cancelled -> "Cancelled"
        else -> ORDER_STATUSES[live.coerceIn(0, ORDER_STATUSES.lastIndex)]
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.card))
            .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))
            .clickable(onClick = onOpen)
            .padding(Sp.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(Radius.md))
                .background(O.BlueSoft, RoundedCornerShape(Radius.md)),
            contentAlignment = Alignment.Center
        ) {
            if (o.cancelled) IcCircleX(size = 22.dp, tint = O.Danger, strokeWidth = 2f)
            else if (live >= 5) IcCheckCircle(size = 22.dp, tint = O.Success, strokeWidth = 2f)
            else IcBox(size = 22.dp, tint = O.BlueDeep, strokeWidth = 2f)
        }
        Spacer(Modifier.width(Sp.s3))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(o.id, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                Spacer(Modifier.width(Sp.s2))
                StatusChip(statusLabel, active = !o.cancelled && live < 5)
            }
            Text(o.canteenName, fontSize = 12.5.sp, color = O.Slate,
                maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
            Text(
                "${o.items.sumOf { it.qty }} ${if (o.items.sumOf { it.qty } == 1) "item" else "items"} · ${o.fulfilment}",
                fontSize = 11.5.sp, color = O.Mute, modifier = Modifier.padding(top = 1.dp)
            )
        }
        Spacer(Modifier.width(Sp.s3))
        Column(horizontalAlignment = Alignment.End) {
            Text("₹${o.total}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
            if (onReorder != null && !o.cancelled) {
                Row(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clip(RoundedCornerShape(Radius.pill))
                        .background(O.BlueSoft, RoundedCornerShape(Radius.pill))
                        .clickable { onReorder() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Reorder", fontSize = 11.sp, color = O.BlueDeep, fontWeight = FontWeight.ExtraBold)
                }
            } else {
                Text("View", fontSize = 11.sp, color = O.BlueDeep, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 3.dp))
            }
        }
    }
}

@Composable
private fun StatusChip(label: String, active: Boolean) {
    Box(
        Modifier
            .clip(RoundedCornerShape(7.dp))
            .background(if (active) O.BlueSoft else if (label == "Cancelled") O.DangerSoft else O.SuccessSoft),
    ) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
            color = if (active) O.BlueDeep else if (label == "Cancelled") O.Danger else O.Success,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp))
    }
}
