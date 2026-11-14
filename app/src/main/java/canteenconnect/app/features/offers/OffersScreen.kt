package canteenconnect.app.features.offers

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.*
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp
import canteenconnect.app.data.demo.DemoData
import canteenconnect.app.data.models.Offer

/**
 * Canteen Offers: hero banner, discount cards with copyable coupon codes and
 * minimum-order requirements (spec L). Copying writes to the clipboard.
 */
@Composable
fun OffersScreen(onNav: (String) -> Unit, onBack: () -> Unit) {
    val s = AppStore.state()
    var copied by remember { mutableStateOf<String?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current

    fun copy(code: String) {
        val cm = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
            as android.content.ClipboardManager
        cm.setPrimaryClip(android.content.ClipData.newPlainText("Opshar coupon", code))
        copied = code
        AppStore.haptic()
    }
    // auto-clear the "copied" affordance after 1.6s
    LaunchedEffect(copied) {
        if (copied != null) { kotlinx.coroutines.delay(1600); copied = null }
    }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Canteen Offers", subtitle = "Deals & coupon codes", onBack = onBack)

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = Sp.s5)) {
            // hero banner
            HeroBanner(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                onNav(Nav.EXPLORE)
            }

            // filter chips: All + categories with offers
            var filter by remember { mutableStateOf<String?>(null) }
            Row(
                Modifier.fillMaxWidth().padding(horizontal = Sp.s5, vertical = Sp.s2),
                horizontalArrangement = Arrangement.spacedBy(Sp.s2)
            ) {
                FilterChip("All Offers", active = filter == null) { filter = null }
                Spacer(Modifier.width(Sp.s1))
                DemoData.offers.distinctBy { it.category }
                    .filter { it.category != null }
                    .forEach { off ->
                        val categoryId = off.category ?: return@forEach
                        val label = DemoData.category(categoryId).name
                        FilterChip(label, active = filter == off.category) {
                            filter = if (filter == off.category) null else off.category
                        }
                        Spacer(Modifier.width(Sp.s1))
                    }
            }

            // offer cards
            val shown = DemoData.offers.filter { filter == null || it.category == filter }
            shown.forEach { off ->
                OfferCard(off, modifier = Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2),
                    applied = s.coupon?.equals(off.code, true) == true,
                    onCopy = { copy(off.code) },
                    onApply = {
                        AppStore.setCoupon(off.code)
                        AppStore.haptic()
                    },
                    copied = copied == off.code)
            }

            Spacer(Modifier.height(Sp.s2))
            // canteen-rate note
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Sp.s5, vertical = Sp.s2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IcInfo(size = 16.dp, tint = O.Slate, strokeWidth = 2f)
                Spacer(Modifier.width(Sp.s2))
                Text(
                    "Coupon codes apply in the demo cart/checkout. Savings are computed against each canteen's subtotal; category offers apply to that category's lines.",
                    fontSize = 11.5.sp, color = O.Slate, lineHeight = 16.sp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(Sp.s3))
        }
    }
}

/** Top promotional banner (reference: "Monthly Canteen Deals / Up to 30% OFF"). */
@Composable
private fun HeroBanner(modifier: Modifier, onShop: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Radius.xl))
            .background(O.Ink, RoundedCornerShape(Radius.xl))
            .clickable(onClick = onShop)
            .padding(Sp.s5),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text("Monthly Canteen Deals", fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold, color = O.BlueSoft)
            Spacer(Modifier.height(2.dp))
            Text("Up to 30% OFF", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold,
                color = Color.White)
            Text("On essential canteen products", fontSize = 12.5.sp,
                color = O.BlueSoft, modifier = Modifier.padding(top = 2.dp))
            Spacer(Modifier.height(Sp.s3))
            Row(Modifier
                .clip(RoundedCornerShape(Radius.pill))
                .background(O.Blue, RoundedCornerShape(Radius.pill))
                .padding(horizontal = 14.dp, vertical = 8.dp)) {
                Text("Shop the deals", fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold,
                    color = Color.White)
                Spacer(Modifier.width(4.dp))
                IcArrowRight(size = 13.dp, tint = Color.White, strokeWidth = 2.4f)
            }
        }
        Spacer(Modifier.width(Sp.s3))
        Box(
            Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(O.Blue.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IcBadgePercent(size = 36.dp, tint = O.BlueSoft, strokeWidth = 1.6f)
        }
    }
}

/** One discount card: code pill, min order, copy + apply actions. */
@Composable
fun OfferCard(
    off: Offer,
    modifier: Modifier = Modifier,
    applied: Boolean = false,
    copied: Boolean = false,
    onCopy: () -> Unit,
    onApply: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.card))
            .border(1.dp, if (applied) O.Success else O.LineSoft, RoundedCornerShape(Radius.card))
            .padding(Sp.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // code tile
        Box(
            Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(Radius.md))
                .background(if (applied) O.SuccessSoft else O.BlueSoft, RoundedCornerShape(Radius.md)),
            contentAlignment = Alignment.Center
        ) {
            if (applied) IcCheck(size = 24.dp, tint = O.Success, strokeWidth = 2.4f)
            else IcTag(size = 24.dp, tint = O.BlueDeep, strokeWidth = 1.8f)
        }
        Spacer(Modifier.width(Sp.s3))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(off.title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                    maxLines = 1)
                off.category?.let { catId ->
                    Chip(DemoData.category(catId).name, O.BlueFaint, O.Slate)
                }
            }
            Text(off.subtitle, fontSize = 11.5.sp, color = O.Slate,
                modifier = Modifier.padding(top = 1.dp))
            // min order / free delivery requirement
            val req = buildList {
                if (off.minOrder > 0) add("Min order ₹${off.minOrder}")
                if (off.freeDeliveryAbove > 0) add("Free delivery above ₹${off.freeDeliveryAbove}")
                if (off.isPercent && off.cap > 0) add("Cap ₹${off.cap}")
            }
            if (req.isNotEmpty()) {
                Text(req.joinToString(" · "), fontSize = 10.5.sp, color = O.Mute,
                    modifier = Modifier.padding(top = 2.dp))
            }
        }
        Spacer(Modifier.width(Sp.s3))
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.width(84.dp)) {
            // code chip
            Box(
                Modifier
                    .clip(RoundedCornerShape(Radius.pill))
                    .background(O.BlueFaint, RoundedCornerShape(Radius.pill))
                    .border(1.dp, O.Line, RoundedCornerShape(Radius.pill))
                    .clickable { onCopy() }
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(off.code, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                        color = O.BlueDeep)
                    Spacer(Modifier.width(4.dp))
                    if (copied) IcCheck(size = 11.dp, tint = O.Success, strokeWidth = 3f)
                    else IcCopy(size = 11.dp, tint = O.Slate, strokeWidth = 2f)
                }
            }
            Spacer(Modifier.height(5.dp))
            Text(
                if (copied) "Copied!" else if (applied) "Applied" else "Apply",
                fontSize = 12.sp, fontWeight = FontWeight.ExtraBold,
                color = if (copied) O.Success else if (applied) O.Success else O.Blue,
                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                modifier = Modifier.fillMaxWidth()
                    .clickable(enabled = !applied) { onApply() }
                    .padding(vertical = 3.dp)
            )
        }
    }
}
