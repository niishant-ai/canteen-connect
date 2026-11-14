package canteenconnect.app.features.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
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
import canteenconnect.app.data.models.CartItem

@Composable
fun CartScreen(onNav: (String) -> Unit, onBack: () -> Unit) {
    val s = AppStore.state()
    val groups = AppStore.groups()
    val totalItems = s.cart.sumOf { it.qty }
    val grandTotal = groups.sumOf { it.subtotal }
    // coupon (applied to the whole cart for the demo; checkout applies it per canteen)
    val coupon = AppStore.coupon(s.coupon ?: "")

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Your Cart", subtitle = if (groups.isEmpty()) "Nothing here yet" else "$totalItems items",
            onBack = onBack)

        if (groups.isNotEmpty()) MemberPassStrip(s.city)

        if (groups.isEmpty()) {
            EmptyState(
                title = "Your cart is empty",
                body = "Add products from a canteen to get started.",
                glyph = { IcCart(size = 30.dp, tint = O.Mute) },
                actionLabel = "Start shopping",
                onAction = { onNav(Nav.HOME) }
            )
        } else {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 140.dp)) {
                if (groups.size > 1) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(O.AmberSoft, RoundedCornerShape(Radius.md))
                            .border(1.dp, O.Amber.copy(alpha = 0.4f), RoundedCornerShape(Radius.md))
                            .padding(Sp.s5, Sp.s5, Sp.s5, Sp.s3)
                    ) {
                        IcInfo(size = 18.dp, tint = O.Amber, strokeWidth = 2f)
                        Spacer(Modifier.width(Sp.s3))
                        Column(Modifier.weight(1f)) {
                            Text("Multiple canteens", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                            Text("Each canteen fulfils its own order. Proceed to checkout to place each one separately — they are never merged.",
                                fontSize = 12.sp, color = O.Slate, lineHeight = 16.sp, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                    Spacer(Modifier.height(Sp.s3))
                }

                groups.forEach { g ->
                    CartGroupCard(g, modifier = Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2))
                }

                // coupon section
                SectionHeader("Apply coupon", modifier = Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2))
                CouponCard(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2),
                    onApply = { code ->
                        AppStore.setCoupon(code); AppStore.haptic()
                    })

                Spacer(Modifier.height(Sp.s4))
            }
        }

        // Sticky checkout bar — anchored above the system nav bar and offset to
        // clear the floating capsule (12dp offset + ~56dp expanded height + gap),
        // so the total and button are never tucked under the nav on edge-to-edge.
        AnimatedVisibility(
            visible = groups.isNotEmpty(),
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 72.dp)
        ) {
            Surface(color = O.Card, shadowElevation = 10.dp,
                modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(Sp.s4)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Total (${totalItems} items)", fontSize = 13.sp, color = O.Slate)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("₹$grandTotal", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                                if (coupon != null) {
                                    val save = AppStore.couponSavings(coupon.code, grandTotal, "Delivery")
                                    if (save > 0) {
                                        Text("− ₹$save", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold,
                                            color = O.Success, modifier = Modifier.padding(start = 8.dp, bottom = 2.dp))
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.width(Sp.s3))
                        PrimaryButton(
                            if (groups.size > 1) "Checkout ${groups.size} orders" else "Proceed to Checkout",
                            modifier = Modifier.weight(1f)
                        ) {
                            onNav(Nav.Checkout)
                        }
                    }
                }
            }
        }
    }
}

/** Verified-member pass strip: restates identity + pickup canteen. */
@Composable
private fun MemberPassStrip(location: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Sp.s5, vertical = Sp.s2)
            .clip(RoundedCornerShape(Radius.card))
            .background(O.SageGlass, RoundedCornerShape(Radius.card))
            .border(1.dp, O.Line, RoundedCornerShape(Radius.card))
            .padding(Sp.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(Radius.md))
                .background(O.Card, RoundedCornerShape(Radius.md))
                .border(1.dp, O.Line, RoundedCornerShape(Radius.md)),
            contentAlignment = Alignment.Center
        ) { IcUser(size = 20.dp, tint = O.BlueDeep, strokeWidth = 1.8f) }
        Spacer(Modifier.width(Sp.s3))
        Column(Modifier.weight(1f)) {
            Text(Brand.NAME + " · " + Brand.MEMBER_CODE, fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
            Text(location + " · counter pickup", fontSize = 11.sp, color = O.Slate, modifier = Modifier.padding(top = 1.dp))
        }
        Box(
            Modifier
                .clip(RoundedCornerShape(Radius.pill))
                .background(O.SuccessSoft, RoundedCornerShape(Radius.pill))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(6.dp).clip(CircleShape).background(O.Success))
                Spacer(Modifier.width(5.dp))
                Text("Verified", fontSize = 10.5.sp, fontWeight = FontWeight.ExtraBold, color = O.Success)
            }
        }
    }
}

/** One canteen group card with item rows + qty steppers. */
@Composable
fun CartGroupCard(g: CartGroup, modifier: Modifier = Modifier, onCheckout: () -> Unit = {}) {
    val open = g.canteen.openNow
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.card))
            .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))
            .padding(Sp.s4)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CanteenGlyph(g.canteen.accent, 44.dp)
            Spacer(Modifier.width(Sp.s3))
            Column(Modifier.weight(1f)) {
                Text(g.canteen.name, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (open) {
                    Row(Modifier.padding(top = 2.dp)) {
                        Chip("Open · Fulfilled here", O.SuccessSoft, O.Success)
                    }
                } else {
                    Row(Modifier.padding(top = 2.dp)) {
                        Chip("Closed · order when open", O.DangerSoft, O.Danger)
                    }
                }
            }
            Text("₹${g.subtotal}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
        }
        Spacer(Modifier.height(Sp.s3))
        g.items.forEach { it ->
            CartRow(it)
            Spacer(Modifier.height(Sp.s2))
        }
        Row(Modifier.fillMaxWidth().padding(top = Sp.s1), verticalAlignment = Alignment.CenterVertically) {
            Text("Clear canteen", fontSize = 12.sp, color = O.Danger,
                modifier = Modifier.clickable { AppStore.clearCartFor(g.canteen.id); AppStore.haptic() })
            Spacer(Modifier.weight(1f))
            Text("Subtotal ₹${g.subtotal}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = O.Ink)
        }
    }
}

/** A single cart row: thumbnail, name/size/price, qty stepper, line total. */
@Composable
fun CartRow(item: CartItem) {
    val product = DemoData.product(item.productId)
    Row(
        Modifier.fillMaxWidth().padding(horizontal = Sp.s1, vertical = Sp.s1),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (product != null) {
            ProductTile(product, size = 52.dp, glyphSize = 24.dp, radius = 14.dp)
        } else {
            CanteenGlyph(item.accent, 52.dp)
        }
        Spacer(Modifier.width(Sp.s3))
        Column(Modifier.weight(1f)) {
            Text(item.name, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold, color = O.Ink,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("₹${item.price} · ${item.unit}", fontSize = 11.5.sp, color = O.Slate)
        }
        // quantity controls
        Row(
            modifier = Modifier
                .background(O.BlueSoft, RoundedCornerShape(10.dp))
                .padding(vertical = 4.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(26.dp).clip(CircleShape).background(O.Card, CircleShape).clickable {
                if (item.qty <= 1) AppStore.removeItem(item.key) else AppStore.setQty(item.key, item.qty - 1)
                AppStore.haptic()
            }, contentAlignment = Alignment.Center) {
                IcMinus(size = 14.dp, tint = O.BlueDeep, strokeWidth = 2.6f)
            }
            Text(item.qty.toString(), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold,
                color = O.Ink, modifier = Modifier.padding(horizontal = 8.dp))
            Box(Modifier.size(26.dp).clip(CircleShape).background(O.Card, CircleShape).clickable {
                AppStore.setQty(item.key, item.qty + 1); AppStore.haptic()
            }, contentAlignment = Alignment.Center) {
                IcPlus(size = 14.dp, tint = O.BlueDeep, strokeWidth = 2.6f)
            }
        }
        Spacer(Modifier.width(Sp.s2))
        Text("₹${item.lineTotal}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
            modifier = Modifier.width(56.dp), textAlign = TextAlign.End)
    }
}

/** Coupon picker: show the active code with apply/remove + tappable suggestions. */
@Composable
fun CouponCard(modifier: Modifier = Modifier, onApply: (String?) -> Unit) {
    val s = AppStore.state()
    val active = AppStore.coupon(s.coupon ?: "")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.card))
            .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))
            .padding(Sp.s4)
    ) {
        if (active != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(Radius.md))
                        .background(O.SuccessSoft, RoundedCornerShape(Radius.md)),
                    contentAlignment = Alignment.Center
                ) { IcTag(size = 20.dp, tint = O.Success, strokeWidth = 2f) }
                Spacer(Modifier.width(Sp.s3))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Applied · ${active.code}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                        if (active.isPercent && active.cap > 0) {
                            Text(" (up to ₹${active.cap})", fontSize = 11.sp, color = O.Slate, modifier = Modifier.padding(start = 4.dp))
                        }
                    }
                    Text(active.title + " · " + active.subtitle, fontSize = 11.5.sp, color = O.Slate)
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(O.DangerSoft, RoundedCornerShape(8.dp))
                        .clickable { onApply(null) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Remove", fontSize = 11.5.sp, fontWeight = FontWeight.ExtraBold, color = O.Danger)
                }
            }
            Spacer(Modifier.height(Sp.s3))
        } else {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(Radius.md))
                        .background(O.BlueSoft, RoundedCornerShape(Radius.md)),
                    contentAlignment = Alignment.Center
                ) { IcTag(size = 20.dp, tint = O.BlueDeep, strokeWidth = 2f) }
                Spacer(Modifier.width(Sp.s3))
                Column(Modifier.weight(1f)) {
                    Text("Have a coupon code?", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                    Text("Tap an offer below or enter your code.", fontSize = 11.5.sp, color = O.Slate)
                }
            }
            // manual coupon input — the "enter your code" promise, now wired up
            var code by remember { mutableStateOf("") }
            Row(
                Modifier.fillMaxWidth().padding(top = Sp.s3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    placeholder = { Text("Enter coupon code", fontSize = 13.sp, color = O.Mute) },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(Sp.s2))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.md))
                        .background(if (code.isBlank()) O.BlueSoft else O.Blue)
                        .clickable {
                            val c = code.trim()
                            if (c.isNotEmpty()) { onApply(c); code = ""; AppStore.haptic() }
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Apply", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold,
                        color = if (code.isBlank()) O.Slate else Color.White)
                }
            }
        }
        // quick-apply offers
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(top = Sp.s3)
        ) {
            DemoData.offers.forEach { off ->
                val on = active?.code == off.code
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (on) O.SuccessSoft else O.BlueFaint, RoundedCornerShape(10.dp))
                        .border(1.dp, if (on) O.Success else O.Line, RoundedCornerShape(10.dp))
                        .clickable {
                            if (on) onApply(null) else { onApply(off.code); AppStore.haptic() }
                        }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(off.code, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold,
                            color = if (on) O.Success else O.BlueDeep)
                        if (on) {
                            IcCheck(size = 13.dp, tint = O.Success, strokeWidth = 2.8f)
                            Spacer(Modifier.width(2.dp))
                        }
                    }
                }
                Spacer(Modifier.width(Sp.s2))
            }
        }
    }
}
