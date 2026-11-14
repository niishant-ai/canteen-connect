package canteenconnect.app.features.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import canteenconnect.app.data.models.CartGroup

@Composable
fun CheckoutScreen(onNav: (String) -> Unit, onBack: () -> Unit) {
    val s = AppStore.state()
    val groups = AppStore.groups()
    var activeIndex by remember { mutableIntStateOf(0) }
    // clamp when the cart shrinks
    LaunchedEffect(groups.size) {
        if (activeIndex >= groups.size) activeIndex = 0
    }

    if (groups.isEmpty()) {
        Column(Modifier.fillMaxSize().background(O.Bg)) {
            TopBar("Checkout", onBack = onBack)
            EmptyState(title = "Nothing to check out", body = "Your cart is empty. Add a few items first.",
                glyph = { IcCart(size = 30.dp, tint = O.Mute) },
                actionLabel = "Go to cart", onAction = { onNav(Nav.CART) })
        }
        return
    }

    val group: CartGroup = groups[activeIndex.coerceIn(0, groups.size - 1)]
    val c = group.canteen
    var fulfilment by remember(c.id) {
        mutableStateOf(if (c.delivery && c.pickup) "Pickup" else if (c.delivery) "Delivery" else "Pickup")
    }
    var addressId by remember(c.id, fulfilment) {
        mutableStateOf(if (fulfilment == "Delivery") DemoData.addresses.first().id else null)
    }

    // coupon: savings against this canteen's subtotal at the selected fulfilment
    val coupon = AppStore.coupon(s.coupon ?: "")
    val savings = AppStore.couponSavings(s.coupon ?: "", group.subtotal, fulfilment)

    val deliveryFees = if (fulfilment == "Delivery") c.deliveryFee + c.packagingFee else c.packagingFee
    val total = (group.subtotal + deliveryFees - savings).coerceAtLeast(0)

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Checkout", subtitle = "Place a demo order", onBack = onBack)

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 132.dp)) {
            // canteen selector if multiple
            if (groups.size > 1) {
                Spacer(Modifier.height(Sp.s3))
                Text("Checking out from:", fontSize = 12.sp, color = O.Slate,
                    modifier = Modifier.padding(horizontal = Sp.s5))
                Row(
                    Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = Sp.s5, vertical = Sp.s2),
                    horizontalArrangement = Arrangement.spacedBy(Sp.s2)
                ) {
                    groups.forEachIndexed { i, g ->
                        val on = i == activeIndex
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Radius.pill))
                                .background(if (on) O.Ink else Color.White, RoundedCornerShape(Radius.pill))
                                .border(1.dp, if (on) O.Ink else O.Line, RoundedCornerShape(Radius.pill))
                                .clickable { activeIndex = i }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(g.canteen.name, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold,
                                color = if (on) Color.White else O.Slate)
                        }
                    }
                }
            }

            // canteen summary
            Row(
                Modifier.fillMaxWidth().padding(Sp.s5, Sp.s5, Sp.s5, Sp.s2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CanteenGlyph(c.accent, 52.dp)
                Spacer(Modifier.width(Sp.s3))
                Column(Modifier.weight(1f)) {
                    Text(c.name, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                    Text("Fulfilled by this canteen · ${fmtDistance(c.distanceM)}",
                        fontSize = 11.5.sp, color = O.Slate)
                }
            }

            // Fulfilment options
            SectionHeader("Fulfilment method")
            Row(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2),
                horizontalArrangement = Arrangement.spacedBy(Sp.s2)) {
                FulfilmentOption(
                    "Pickup at counter",
                    enabled = c.pickup,
                    selected = fulfilment == "Pickup",
                    glyph = { m, t -> IcStore(m, t, 22.dp, 1.8f) },
                    modifier = Modifier.weight(1f)
                ) { fulfilment = "Pickup"; AppStore.haptic() }
                FulfilmentOption(
                    "Home delivery",
                    enabled = c.delivery,
                    selected = fulfilment == "Delivery",
                    glyph = { m, t -> IcTruck(m, t, 22.dp, 1.8f) },
                    modifier = Modifier.weight(1f)
                ) { fulfilment = "Delivery"; AppStore.haptic() }
            }

            // Address (delivery) or pickup details
            if (fulfilment == "Delivery") {
                SectionHeader("Deliver to")
                Column(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                    DemoData.addresses.forEach { a ->
                        val sel = a.id == addressId
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(if (sel) O.BlueSoft else Color.White, RoundedCornerShape(Radius.md))
                                .border(BorderStroke(1.dp, if (sel) O.Blue else O.Line), RoundedCornerShape(Radius.md))
                                .clickable { addressId = a.id }
                                .padding(Sp.s3),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(18.dp).clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (sel) {
                                    Box(
                                        Modifier.size(18.dp).clip(CircleShape).background(O.Blue, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) { IcCheck(size = 11.dp, tint = Color.White, strokeWidth = 3f) }
                                } else {
                                    Box(
                                        Modifier.size(18.dp).clip(CircleShape)
                                            .border(1.5.dp, O.Line, CircleShape)
                                    )
                                }
                            }
                            Spacer(Modifier.width(Sp.s3))
                            Column(Modifier.weight(1f)) {
                                Text(a.label, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                                Text("${a.line}, ${a.city}", fontSize = 12.sp, color = O.Slate)
                            }
                        }
                        Spacer(Modifier.height(Sp.s2))
                    }
                }
            } else {
                SectionHeader("Pickup details")
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(O.Card, RoundedCornerShape(Radius.md))
                        .border(BorderStroke(1.dp, O.LineSoft), RoundedCornerShape(Radius.md))
                        .padding(horizontal = Sp.s5, vertical = Sp.s2)
                ) {
                    Box(
                        Modifier.size(38.dp).clip(CircleShape).background(O.SuccessSoft, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { IcBox(size = 18.dp, tint = O.Success, strokeWidth = 2f) }
                    Spacer(Modifier.width(Sp.s3))
                    Column(Modifier.weight(1f)) {
                        Text("Ready for pickup at the counter", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                        Text("Collect once the canteen marks your order ready. Counter pickup is free.",
                            fontSize = 12.sp, color = O.Slate, lineHeight = 16.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            // coupon state
            if (coupon != null && savings > 0) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(O.SuccessSoft, RoundedCornerShape(Radius.md))
                        .padding(horizontal = Sp.s5, vertical = Sp.s2)
                ) {
                    Box(
                        Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(O.Card, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { IcCheck(size = 18.dp, tint = O.Success, strokeWidth = 2.6f) }
                    Spacer(Modifier.width(Sp.s3))
                    Column(Modifier.weight(1f)) {
                        Text("Coupon ${coupon.code} applied", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = O.Success)
                        Text("You save ₹$savings on this order", fontSize = 11.5.sp, color = O.Slate)
                    }
                }
            }

            // Order summary
            SectionHeader("Order summary")
            Column(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                group.items.forEach { it ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("${it.qty}×", fontSize = 13.sp, color = O.Slate,
                            modifier = Modifier.width(36.dp))
                        Text(it.name, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = O.Ink,
                            modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text("₹${it.lineTotal}", fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = O.Ink)
                    }
                }
                Spacer(Modifier.height(Sp.s2))
                Box(Modifier.fillMaxWidth().height(1.dp).background(O.LineSoft))
                Spacer(Modifier.height(Sp.s2))
                PriceRow("Items subtotal", group.subtotal)
                if (fulfilment == "Delivery") {
                    PriceRow("Delivery fee", c.deliveryFee)
                    PriceRow("Packaging fee", c.packagingFee)
                } else {
                    PriceRow("Packaging fee", c.packagingFee)
                    PriceRow("Pickup fee", 0)
                }
                if (savings > 0) {
                    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
                        Text("Canteen discount (${coupon?.code})", fontSize = 13.sp, color = O.Success,
                            fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        Text("− ₹$savings", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = O.Success)
                    }
                }
                Spacer(Modifier.height(Sp.s1))
                Box(Modifier.fillMaxWidth().height(1.dp).background(O.LineSoft))
                Spacer(Modifier.height(Sp.s2))
                Row(Modifier.fillMaxWidth()) {
                    Text("Total payable", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                        modifier = Modifier.weight(1f))
                    Text("₹$total", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.BlueDeep)
                }
            }

            // Payment
            SectionHeader("Payment")
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(O.BlueSoft, RoundedCornerShape(Radius.md))
                    .padding(horizontal = Sp.s5, vertical = Sp.s2)
            ) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(O.Card, CircleShape),
                    contentAlignment = Alignment.Center
                ) { IcCreditCard(size = 18.dp, tint = O.BlueDeep, strokeWidth = 2f) }
                Spacer(Modifier.width(Sp.s3))
                Column(Modifier.weight(1f)) {
                    Text("Pay at Counter", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                    Text("Paid at the canteen counter on pickup. No online payment needed.",
                        fontSize = 11.5.sp, color = O.Slate, lineHeight = 15.sp)
                }
                IcCheck(size = 20.dp, tint = O.BlueDeep, strokeWidth = 2.4f)
            }

            // disclaimer
            Spacer(Modifier.height(Sp.s3))
            Text("This is a demo order. No money moves and no real canteen is involved.",
                fontSize = 11.sp, color = O.Slate, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Sp.s5))
        }

        // sticky place-order bar
        Surface(color = Color.White, shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
            Row(Modifier.padding(Sp.s4), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Total payable", fontSize = 12.sp, color = O.Slate)
                    Text("₹$total", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                }
                PrimaryButton("Place Demo Order", modifier = Modifier.width(180.dp)) {
                    val order = AppStore.placeOrder(
                        group,
                        fulfilment,
                        DemoData.addresses.firstOrNull { it.id == addressId }?.label
                    )
                    AppStore.setCoupon(null) // single-use in the demo
                    AppStore.haptic()
                    onNav(Nav.orderSuccess(order.id))
                }
            }
        }
    }
}

@Composable
private fun FulfilmentOption(
    label: String,
    enabled: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier,
    glyph: @Composable (androidx.compose.ui.Modifier, Color) -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(Radius.md),
        color = if (selected) O.BlueSoft else if (enabled) Color.White else Color(0xFFEFF1F7),
        border = if (selected) BorderStroke(1.dp, O.Blue) else BorderStroke(1.dp, O.Line),
        modifier = modifier.height(66.dp).clickable(enabled = enabled, onClick = onClick)
    ) {
        Row(Modifier.fillMaxSize().padding(Sp.s3), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(if (selected) O.Blue else O.BlueFaint, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                glyph(Modifier, if (selected) Color.White else if (enabled) O.BlueDeep else O.Mute)
            }
            Spacer(Modifier.width(Sp.s3))
            Column {
                Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold,
                    color = if (enabled) O.Ink else O.Mute, maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (!enabled) Text("Not available", fontSize = 9.5.sp, color = O.Mute)
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, amount: Int) {
    Row(Modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Text(label, fontSize = 13.sp, color = O.Slate, modifier = Modifier.weight(1f))
        Text(
            if (amount > 0) "₹$amount" else "Free",
            fontSize = 13.sp,
            fontWeight = if (amount > 0) FontWeight.SemiBold else FontWeight.ExtraBold,
            color = if (amount > 0) O.Ink else O.Success
        )
    }
}
