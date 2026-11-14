package canteenconnect.app.features.products

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.graphicsLayer
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

/**
 * Product Details — reference-faithful PDP. Large vector image stage with slide
 * dots, brand + name, description, variant (pack size) selector, price block
 * with MRP strikethrough + discount, quantity stepper, canteen picker, details
 * accordions, and a sticky Add to Cart bar.
 */
@Composable
fun ProductDetailScreen(productId: String, onNav: (String) -> Unit, onBack: () -> Unit) {
    val p = DemoData.product(productId)
    if (p == null) {
        Column(Modifier.fillMaxSize().background(O.Bg)) {
            TopBar("Product", onBack = onBack)
            EmptyState(title = "Product not found", body = "This demo product does not exist.",
                glyph = { IcBag(size = 30.dp, tint = O.Mute) },
                actionLabel = "Back to Home", onAction = { onNav(Nav.HOME) })
        }
        return
    }

    val s = AppStore.state()
    val scoping = AppStore.cityCanteens(s.city)
    val canteens = (if (scoping.isNotEmpty()) scoping else DemoData.canteens)
        .filter { c -> DemoData.stock(c.id).contains(p.id) }
    var selectedCanteen by remember { mutableStateOf(canteens.firstOrNull()?.id ?: DemoData.canteens.first().id) }
    var qty by remember { mutableIntStateOf(1) }
    var showAdded by remember { mutableStateOf(false) }
    LaunchedEffect(selectedCanteen, showAdded) {
        if (showAdded) {
            kotlinx.coroutines.delay(1600)
            showAdded = false
        }
    }
    val inWish = AppStore.isWishlisted(p.id)
    val pct = discountPct(p)

    // variant selector (package sizes = distinct units in the same category)
    val variants = DemoData.products.filter { it.categoryId == p.categoryId }.map { it.unit }.distinct().take(5)
    var selectedVariant by remember { mutableStateOf(p.unit) }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Product Details", onBack = onBack) {
            Box(
                Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(O.Card, CircleShape)
                    .border(1.dp, O.Line, CircleShape)
                    .clickable { AppStore.toggleWishlist(p.id); AppStore.haptic() },
                contentAlignment = Alignment.Center
            ) { WishlistHeart(inWish, iconSize = 20.dp) }
        }

        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp)
        ) {
            // ---- image stage: real product photo + slide dots ----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Sp.s5)
                    .aspectRatio(0.92f)
                    .clip(RoundedCornerShape(Radius.xl))
                    .background(O.surfaceOf(p.accent), RoundedCornerShape(Radius.xl))
                    .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.xl)),
                contentAlignment = Alignment.Center
            ) {
                ProductImage(p, modifier = Modifier.matchParentSize())
                // slide dots (bottom-center, over the stage)
                Row(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(Radius.pill))
                        .background(O.Card.copy(alpha = 0.9f), RoundedCornerShape(Radius.pill))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.width(4.dp))
                    Box(Modifier.size(7.dp).clip(CircleShape).background(O.Blue))
                    Spacer(Modifier.width(4.dp))
                    Box(Modifier.size(7.dp).clip(CircleShape).background(O.Line))
                    Spacer(Modifier.width(4.dp))
                    Box(Modifier.size(7.dp).clip(CircleShape).background(O.Line))
                }
            }
            Spacer(Modifier.height(Sp.s4))

            // brand + name
            Row(Modifier.padding(horizontal = Sp.s5), verticalAlignment = Alignment.CenterVertically) {
                Text(p.brand, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = O.Slate)
                if (p.rating >= 4.0) {
                    Spacer(Modifier.width(Sp.s2))
                    Chip("★ ${p.rating.toString()}", O.AmberSoft, O.Amber)
                }
                Spacer(Modifier.weight(1f))
                if (p.isNew) Chip("New", O.SuccessSoft, O.Success)
            }
            Spacer(Modifier.height(4.dp))
            Text(p.name + " (${p.unit})", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold,
                color = O.Ink, modifier = Modifier.padding(horizontal = Sp.s5), lineHeight = 27.sp)
            Spacer(Modifier.height(Sp.s2))
            Text(p.description, fontSize = 13.5.sp, color = O.Slate, lineHeight = 19.sp,
                modifier = Modifier.padding(horizontal = Sp.s5))

            Spacer(Modifier.height(Sp.s4))

            // variant selector (package sizes)
            if (variants.size > 1) {
                SectionHeader("Pack size", modifier = Modifier.padding(horizontal = Sp.s5, vertical = 0.dp))
                Row(
                    Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = Sp.s5)
                ) {
                    variants.forEach { v ->
                        val on = v == selectedVariant
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Radius.md))
                                .background(if (on) O.BlueSoft else O.Surface, RoundedCornerShape(Radius.md))
                                .border(1.dp, if (on) O.Blue else O.Line, RoundedCornerShape(Radius.md))
                                .clickable { selectedVariant = v; AppStore.haptic() }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(v, fontSize = 13.sp, fontWeight = if (on) FontWeight.ExtraBold else FontWeight.SemiBold,
                                color = if (on) O.BlueDeep else O.Slate)
                        }
                        Spacer(Modifier.width(Sp.s2))
                    }
                }
                Spacer(Modifier.height(Sp.s3))
            }

            // canteen availability
            if (canteens.size > 1) {
                Text("Available at", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = O.Slate,
                    modifier = Modifier.padding(horizontal = Sp.s5, vertical = 4.dp))
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = Sp.s5)) {
                    canteens.forEach { c ->
                        val on = c.id == selectedCanteen
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Radius.pill))
                                .background(if (on) O.Ink else Color.White, RoundedCornerShape(Radius.pill))
                                .border(1.dp, if (on) O.Ink else O.Line, RoundedCornerShape(Radius.pill))
                                .clickable { selectedCanteen = c.id; AppStore.haptic() }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(c.name, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold,
                                color = if (on) Color.White else O.Ink)
                        }
                        Spacer(Modifier.width(Sp.s2))
                    }
                }
                Spacer(Modifier.height(Sp.s4))
            }

            // price + quantity row
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(O.Card, RoundedCornerShape(Radius.card))
                    .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))
                    .padding(horizontal = Sp.s5, vertical = Sp.s4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("₹${p.price}", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                        if (pct > 0) {
                            Text("  ₹${p.originalPrice}", fontSize = 14.sp, color = O.Mute,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                                modifier = Modifier.padding(bottom = 4.dp))
                            Box(
                                Modifier
                                    .padding(start = 8.dp, bottom = 6.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(O.DangerSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("-$pct%", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = O.Danger,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                    Text("Canteen price · ${pct}+ savings where applicable", fontSize = 11.5.sp, color = O.Slate,
                        modifier = Modifier.padding(top = 2.dp))
                }
                Spacer(Modifier.width(Sp.s3))
                // quantity stepper
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(Radius.md))
                        .background(O.BlueSoft, RoundedCornerShape(Radius.md))
                        .padding(horizontal = 6.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QtyBtn(onClick = { if (qty > 1) qty -= 1 }, glyph = { IcMinus(size = 15.dp, tint = O.BlueDeep, strokeWidth = 2.6f) })
                    Box(Modifier.width(36.dp)) {
                        Text(qty.toString(), fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,
                            color = O.BlueDeep, modifier = Modifier.align(Alignment.Center))
                    }
                    QtyBtn(onClick = { qty += 1 }, glyph = { IcPlus(size = 15.dp, tint = O.BlueDeep, strokeWidth = 2.6f) })
                }
            }
            Spacer(Modifier.height(Sp.s4))

            // accordions
            AccordionRow(title = "Product Details", openDefault = true) {
                DetailLine("Brand", p.brand)
                DetailLine("Package", p.unit)
                DetailLine("Category", DemoData.category(p.categoryId).name)
                DetailLine("Units sold", "${p.sold}")
            }
            AccordionRow(title = "Nutritional Information", openDefault = false) {
                DetailLine("Basis", "Per 100 g")
                DetailLine("Energy", "When available, shown from the label (demo)")
                DetailLine("Allergens", "See label for full allergen info")
            }

            Spacer(Modifier.height(Sp.s6))
        }

        // ---- sticky Add to Cart bar — anchored above the system nav bar so
        // the button is never tucked under the gesture area (edge-to-edge).
        Row(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .background(O.Card)
                .border(1.dp, O.LineSoft, RoundedCornerShape(24.dp))
                .padding(horizontal = Sp.s5, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(Radius.md), color = O.Blue,
                modifier = Modifier.weight(1f).height(52.dp).clickable {
                    AppStore.addToCart(p.id, selectedCanteen, qty)
                    AppStore.recordViewed(p.id)
                    AppStore.haptic()
                    showAdded = true
                }
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Center)) {
                        if (showAdded) {
                            IcCheck(size = 17.dp, tint = Color.White, strokeWidth = 2.6f)
                            Spacer(Modifier.width(6.dp))
                        }
                        Text(if (showAdded) "Added to Cart" else "Add to Cart",
                            fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun QtyBtn(onClick: () -> Unit, modifier: Modifier = Modifier, glyph: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(O.Card, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) { glyph() }
}

@Composable
private fun DetailLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(label, fontSize = 13.sp, color = O.Slate, modifier = Modifier.weight(0.42f))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = O.Ink,
            modifier = Modifier.weight(0.58f), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun AccordionRow(title: String, openDefault: Boolean, content: @Composable () -> Unit) {
    var open by remember { mutableStateOf(openDefault) }
    Column(
        Modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.card))
            .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))
            .padding(horizontal = Sp.s5)
    ) {
        Row(
            Modifier.fillMaxWidth().clickable { open = !open }.padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink, modifier = Modifier.weight(1f))
            val rot = animateFloatAsState(if (open) 180f else 0f, label = "chev")
            Box(Modifier.graphicsLayer { rotationZ = rot.value }) {
                IcChevronDown(size = 18.dp, tint = O.Slate, strokeWidth = 2.2f)
            }
        }
        androidx.compose.animation.AnimatedVisibility(visible = open) {
            Column(Modifier.padding(bottom = Sp.s4)) {
                content()
            }
        }
    }
}
