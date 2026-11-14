package canteenconnect.app.features.orders

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.*
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.*
import canteenconnect.app.data.demo.DemoData
import canteenconnect.app.data.models.DemoOrder

/** Animated confirmation shown after a demo order is placed. */
@Composable
fun OrderSuccessScreen(orderId: String, onNav: (String) -> Unit) {
    val order = AppStore.state().orders.firstOrNull { it.id == orderId }
    if (order == null) {
        Column(Modifier.fillMaxSize().background(O.Bg)) {
            TopBar("Order", onBack = { onNav(Nav.ORDERS) })
            EmptyState(title = "Order not found", body = "We could not find that order.",
                glyph = { IcReceipt(size = 30.dp, tint = O.Mute) },
                actionLabel = "Go to Orders", onAction = { onNav(Nav.ORDERS) })
        }
        return
    }

    // success check that scales in
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (!AppStore.stateSnapshot.settingsReducedMotion) {
            progress.animateTo(1f, tween(700, 0, EaseOut))
        } else progress.snapTo(1f)
    }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Order placed", subtitle = "Confirmation", onBack = { onNav(Nav.ORDERS) })
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = Sp.s5, vertical = Sp.s4)
        ) {
            // animated success check
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .background(O.SuccessSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(Modifier.scale(progress.value.coerceAtLeast(0.001f))) {
                        IcCheckCircle(size = 72.dp, tint = O.Success, strokeWidth = 1.6f)
                    }
                }
            }
            Spacer(Modifier.height(Sp.s4))
            Text("Thank you!", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                textAlign = TextAlign.Center)
            Text("Your order is on its way.", fontSize = 14.sp, color = O.Slate,
                textAlign = TextAlign.Center, modifier = Modifier.padding(top = Sp.s1))

            Spacer(Modifier.height(Sp.s5))
            // order card
            Surface(
                shape = RoundedCornerShape(Radius.lg),
                color = O.Card,
                border = androidx.compose.foundation.BorderStroke(1.dp, O.LineSoft),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(Sp.s4)) {
                    DetailRow("Order ID", order.id, bold = true)
                    Spacer(Modifier.height(Sp.s2))
                    DetailRow("Canteen", order.canteenName)
                    DetailRow("Fulfilment", order.fulfilment + " · " + order.estimated)
                    DetailRow("Payment", order.payment)
                    DetailRow("Items", "${order.items.sumOf { it.qty }} · ${order.items.joinToString(", ") { it.name }}")
                    Spacer(Modifier.height(Sp.s2))
                    Box(Modifier.fillMaxWidth().height(1.dp).background(O.LineSoft))
                    Spacer(Modifier.height(Sp.s2))
                    DetailRow("Total paid", "₹${order.total}", bold = true)
                }
            }

            PickupPass(order)

            Spacer(Modifier.height(Sp.s5))
            PrimaryButton("View Order", modifier = Modifier.fillMaxWidth()) {
                onNav(Nav.order(order.id))
            }
            Spacer(Modifier.height(Sp.s3))
            GhostButton("Continue Shopping", modifier = Modifier.fillMaxWidth()) { onNav(Nav.HOME) }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth()) {
        Text(label, fontSize = 13.sp, color = O.Slate,
            modifier = Modifier.weight(0.45f))
        Text(value, fontSize = if (bold) 15.sp else 13.5.sp,
            fontWeight = if (bold) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = O.Ink,
            modifier = Modifier.weight(0.55f), textAlign = TextAlign.End)
    }
}

/**
 * AURA Step 6 — verified digital pickup pass: identity, ACTIVE badge, a
 * deterministic barcode and an active pickup checklist.
 */
@Composable
private fun PickupPass(order: DemoOrder) {
    Spacer(Modifier.height(Sp.s4))
    Surface(
        shape = RoundedCornerShape(Radius.lg),
        color = O.Card,
        border = androidx.compose.foundation.BorderStroke(1.dp, O.Line),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(Sp.s4)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(Brand.NAME + " Pickup Pass", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                    Text(order.id, fontSize = 11.sp, color = O.Slate, modifier = Modifier.padding(top = 1.dp))
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(Radius.pill))
                        .background(O.SuccessSoft, RoundedCornerShape(Radius.pill))
                        .border(1.dp, O.Success.copy(alpha = 0.35f), RoundedCornerShape(Radius.pill))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(6.dp).clip(CircleShape).background(O.Success))
                        Spacer(Modifier.width(5.dp))
                        Text("ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = O.Success)
                    }
                }
            }

            Spacer(Modifier.height(Sp.s3))
            // barcode
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Radius.md))
                    .background(O.Surface, RoundedCornerShape(Radius.md))
                    .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.md)),
                contentAlignment = Alignment.Center
            ) {
                Column(Modifier.padding(vertical = Sp.s3)) {
                    Barcode(order.id)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        order.id.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = O.Ink,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(Sp.s4))
            Text("Pickup checklist", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = O.BlueDeep)
            Spacer(Modifier.height(Sp.s2))
            ChecklistRow("Order placed", order.id, done = true)
            ChecklistRow("Ready at counter", order.canteenName, done = order.fulfilment == "Pickup")
            ChecklistRow("Scan pass to handover", order.estimated, done = false)
        }
    }
}

/** Deterministic Code-39-ish barcode drawn from the order id. */
@Composable
private fun Barcode(seed: String) {
    val bars = remember(seed) {
        val sb = StringBuilder()
        var x = 0
        for (c in seed) {
            val bits = c.code % 16
            repeat(3) { i ->
                val on = ((bits shr i) and 1) == 1
                sb.append(if (on) "#" else ".")
            }
            sb.append(".")
            x += 4
        }
        sb.toString()
    }
    Canvas(Modifier.fillMaxWidth().height(56.dp)) {
        val w = size.width
        val h = size.height
        val step = w / bars.length.toFloat()
        bars.forEachIndexed { i, c ->
            if (c == '#') {
                drawRect(
                    topLeft = androidx.compose.ui.geometry.Offset(i * step, 0f),
                    size = androidx.compose.ui.geometry.Size(step * 0.85f, h),
                    color = O.Ink
                )
            }
        }
    }
}

@Composable
private fun ChecklistRow(label: String, detail: String, done: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(if (done) O.Success else O.BlueFaint, CircleShape)
                .border(1.dp, if (done) O.Success else O.Line, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (done) IcCheck(size = 11.dp, tint = Color.White, strokeWidth = 3f)
        }
        Spacer(Modifier.width(Sp.s2))
        Column(Modifier.weight(1f)) {
            Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = O.Ink)
            Text(detail, fontSize = 10.5.sp, color = O.Mute, modifier = Modifier.padding(top = 1.dp))
        }
        Text(
            if (done) "DONE" else "PENDING",
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp,
            color = if (done) O.Success else O.Mute
        )
    }
}
