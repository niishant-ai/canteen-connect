package canteenconnect.app.features.orders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
 * Order tracking: vertical progress timeline (Placed → Accepted → Preparing →
 * Ready/Out-for-delivery → Delivered), order details, items, and support.
 * Status advances live in the demo (one stage per 40s).
 */
@Composable
fun OrderDetailScreen(orderId: String, onNav: (String) -> Unit, onBack: () -> Unit) {
    val s = AppStore.state()
    val order = s.orders.firstOrNull { it.id == orderId }
    if (order == null) {
        Column(Modifier.fillMaxSize().background(O.Bg)) {
            TopBar("Order", onBack = onBack)
            EmptyState(title = "Order not found", body = "This demo order does not exist.",
                glyph = { IcReceipt(size = 30.dp, tint = O.Mute) },
                actionLabel = "Go to Orders", onAction = { onNav(Nav.ORDERS) })
        }
        return
    }
    // periodic tick so the demo status advances over time
    var tick by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) { tick++; kotlinx.coroutines.delay(5000) }
    }
    val live = AppStore.liveStatus(order)

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar(order.id, subtitle = order.canteenName + " · " + order.fulfilment, onBack = onBack)

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 132.dp)) {
            // ---- status tracker ----
            SectionHeader("Status")
            Column(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                if (order.cancelled) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(O.DangerSoft, RoundedCornerShape(Radius.md))
                            .border(BorderStroke(1.dp, O.Danger.copy(alpha = 0.4f)), RoundedCornerShape(Radius.md))
                            .padding(Sp.s4),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(38.dp).clip(CircleShape).background(O.Card, CircleShape),
                            contentAlignment = Alignment.Center
                        ) { IcCircleX(size = 20.dp, tint = O.Danger, strokeWidth = 2f) }
                        Spacer(Modifier.width(Sp.s3))
                        Column(Modifier.weight(1f)) {
                            Text("Order cancelled", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Danger)
                            Text("This demo order was cancelled by you.", fontSize = 12.5.sp, color = O.Slate,
                                modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                } else {
                    StatusTracker(live)
                }
            }

            // delivery info
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(O.Card, RoundedCornerShape(Radius.md))
                    .border(BorderStroke(1.dp, O.LineSoft), RoundedCornerShape(Radius.md))
                    .padding(horizontal = Sp.s5, vertical = Sp.s2)
            ) {
                Box(
                    Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(Radius.md))
                        .background(O.BlueSoft, RoundedCornerShape(Radius.md)),
                    contentAlignment = Alignment.Center
                ) {
                    if (order.fulfilment == "Delivery") IcTruck(size = 18.dp, tint = O.BlueDeep, strokeWidth = 2f)
                    else IcStore(size = 18.dp, tint = O.BlueDeep, strokeWidth = 2f)
                }
                Spacer(Modifier.width(Sp.s3))
                Column(Modifier.weight(1f)) {
                    Text(order.fulfilment, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                    Text(order.estimated, fontSize = 12.sp, color = O.Slate)
                }
                Chip("On track", O.SuccessSoft, O.Success)
            }

            // Details
            SectionHeader("Order details")
            Surface(shape = RoundedCornerShape(Radius.lg), color = Color.White,
                border = BorderStroke(1.dp, O.LineSoft),
                modifier = Modifier.fillMaxWidth().padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                Column(Modifier.padding(Sp.s4)) {
                    DetailLine("Placed", formatTime(order.placedAt))
                    DetailLine("Fulfilment", order.fulfilment + " · " + order.estimated)
                    DetailLine("Payment", order.payment)
                    DetailLine("Subtotal", "₹${order.subtotal}")
                    DetailLine("Fees", "₹${order.fees}")
                    DetailLine("Total", "₹${order.total}", bold = true)
                }
            }

            // items
            Spacer(Modifier.height(Sp.s3))
            SectionHeader("Items (${order.items.sumOf { it.qty }})")
            Column(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                order.items.forEach { it ->
                    Row(Modifier.fillMaxWidth().padding(vertical = Sp.s2),
                        verticalAlignment = Alignment.CenterVertically) {
                        ProductTileFor(it, size = 44.dp)
                        Spacer(Modifier.width(Sp.s3))
                        Column(Modifier.weight(1f)) {
                            Text(it.name, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = O.Ink,
                                maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(it.unit + " · ₹${it.price}", fontSize = 11.5.sp, color = O.Slate)
                        }
                        Text("×${it.qty}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                    }
                }
            }

            // actions
            Spacer(Modifier.height(Sp.s4))
            if (!order.cancelled && live < 2) {
                GhostButton("Cancel order", modifier = Modifier.fillMaxWidth().padding(horizontal = Sp.s5)) {
                    AppStore.cancelOrder(order.id); AppStore.haptic()
                }
                Spacer(Modifier.height(Sp.s3))
            }
            Row(Modifier.fillMaxWidth().padding(horizontal = Sp.s5),
                horizontalArrangement = Arrangement.spacedBy(Sp.s2)) {
                GhostButton("Reorder", modifier = Modifier.weight(1f)) {
                    AppStore.reorder(order.id); AppStore.haptic(); onNav(Nav.CART)
                }
                GhostButton("Help & Support", modifier = Modifier.weight(1f)) { onNav(Nav.Help) }
            }
        }
    }
}

/** Vertical status timeline with done / active / pending states. */
@Composable
private fun StatusTracker(live: Int) {
    Column(Modifier.fillMaxWidth()) {
        ORDER_STATUSES.forEachIndexed { i, label ->
            val done = i < live
            val active = i == live
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                // marker + connector
                Column(Modifier.align(Alignment.Top).width(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    done -> O.Success
                                    active -> O.Blue
                                    else -> Color.White
                                }, CircleShape
                            )
                            .border(
                                2.dp,
                                when {
                                    done -> O.Success
                                    active -> O.Blue
                                    else -> O.Line
                                }, CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            done -> IcCheck(size = 13.dp, tint = Color.White, strokeWidth = 3f)
                            active -> Box(Modifier.size(8.dp).clip(CircleShape).background(O.Card, CircleShape))
                            else -> Unit
                        }
                    }
                    if (i != ORDER_STATUSES.lastIndex) {
                        Box(
                            Modifier
                                .padding(top = 2.dp)
                                .width(2.dp)
                                .height(28.dp)
                                .background(if (i < live) O.Success else O.LineSoft)
                        )
                    }
                }
                Spacer(Modifier.width(Sp.s3))
                Column(Modifier.weight(1f)) {
                    Text(label, fontSize = 14.sp,
                        fontWeight = if (active) FontWeight.ExtraBold else FontWeight.SemiBold,
                        color = if (active) O.Ink else O.Slate)
                    if (active) {
                        Text("In progress — status updates live", fontSize = 11.sp,
                            color = O.Amber, modifier = Modifier.padding(top = 1.dp))
                    }
                    if (i != ORDER_STATUSES.lastIndex) Spacer(Modifier.height(Sp.s3))
                }
            }
        }
    }
}

/** Vector tile for an order item (falls back to accent tile if product missing). */
@Composable
private fun ProductTileFor(it: OrderItem, size: androidx.compose.ui.unit.Dp) {
    val p = DemoData.product(it.productId)
    val glyph = (size.value * 0.5f).dp
    val rad = (size.value * 0.28f).dp
    if (p != null) ProductTile(p, size = size, glyphSize = glyph, radius = rad)
    else {
        Box(
            Modifier
                .size(size)
                .clip(RoundedCornerShape(rad))
                .background(O.surfaceOf(it.accent), RoundedCornerShape(rad)),
            contentAlignment = Alignment.Center
        ) { }
    }
}

@Composable
private fun DetailLine(label: String, value: String, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, fontSize = 13.sp, color = O.Slate, modifier = Modifier.weight(1f))
        Text(value, fontSize = 13.5.sp, fontWeight = if (bold) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = O.Ink,
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
            textAlign = TextAlign.End,
            maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

private fun formatTime(ms: Long): String {
    val mins = ((System.currentTimeMillis() - ms) / 60000L)
    return when {
        mins < 1 -> "Just now"
        mins < 60 -> "$mins min ago"
        else -> "${mins / 60} h ago"
    }
}
