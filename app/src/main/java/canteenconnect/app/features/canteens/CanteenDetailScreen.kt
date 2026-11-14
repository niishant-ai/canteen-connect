package canteenconnect.app.features.canteens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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

@Composable
fun CanteenDetailScreen(canteenId: String, onNav: (String) -> Unit, onBack: () -> Unit) {
    val c = DemoData.canteen(canteenId)
    if (c == null) {
        Column(Modifier.fillMaxSize().background(O.Bg)) {
            TopBar("Canteen", onBack = onBack)
            EmptyState(title = "Canteen not found", body = "This demo canteen does not exist.",
                glyph = { IcStore(size = 30.dp, tint = O.Mute) },
                actionLabel = "Back to Explore", onAction = { onNav(Nav.EXPLORE) })
        }
        return
    }
    var query by remember { mutableStateOf("") }
    val all = DemoData.productsFor(c.id)
    val shown = all.filter { p ->
        query.isBlank() ||
            p.name.contains(query, true) ||
            p.brand.contains(query, true)
    }

    val tfState = rememberTextFieldState("")
    LaunchedEffect(query) { tfState.setTextAndPlaceCursorAtEnd(query) }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        // Hero banner: accent panel + store glyph, floating back / open-now
        Box(
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .statusBarsPadding()
                .clip(RoundedCornerShape(bottomStart = Radius.xl, bottomEnd = Radius.xl))
                .background(O.surfaceOf(c.accent), RoundedCornerShape(bottomStart = Radius.xl, bottomEnd = Radius.xl))
        ) {
            Box(
                Modifier.align(Alignment.Center).size(96.dp).clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.75f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IcStore(size = 48.dp, tint = O.deepOf(c.accent), strokeWidth = 1.5f)
            }
            // back button
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(Sp.s4)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(O.Card, CircleShape)
                    .border(BorderStroke(1.dp, O.LineSoft), CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                IcChevronLeft(size = 20.dp, tint = O.Ink, strokeWidth = 2.2f)
            }
            // open/closed pill
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(Sp.s4)
                    .clip(RoundedCornerShape(Radius.pill))
                    .background(if (c.openNow) O.Success else O.Danger, RoundedCornerShape(Radius.pill)),
                contentAlignment = Alignment.Center
            ) {
                Row(Modifier.padding(horizontal = 12.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(O.Card, CircleShape))
                    Spacer(Modifier.width(6.dp))
                    Text(if (c.openNow) "Open now" else "Closed", fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
        }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 132.dp)) {
            // name + meta
            Column(Modifier.padding(Sp.s5)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(c.name, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                        modifier = Modifier.weight(1f), maxLines = 2,
                        overflow = TextOverflow.Ellipsis, lineHeight = 28.sp)
                    DemoBadge(Modifier.padding(start = Sp.s2))
                }
                Spacer(Modifier.height(Sp.s2))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("★ ${c.rating.toString()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = O.Amber)
                    Spacer(Modifier.width(Sp.s2))
                    Text("· ${fmtDistance(c.distanceM)} away", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = O.Slate)
                }
                Spacer(Modifier.height(Sp.s3))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IcMapPin(size = 15.dp, tint = O.Slate, strokeWidth = 2f)
                    Spacer(Modifier.width(Sp.s2))
                    Text("${c.area}, ${c.city} — PIN ${c.pin}", fontSize = 13.sp, color = O.Slate,
                        modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(Sp.s2))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IcClock(size = 15.dp, tint = O.Slate, strokeWidth = 2f)
                    Spacer(Modifier.width(Sp.s2))
                    Text("${c.openFrom} – ${c.openTo}", fontSize = 13.sp, color = O.Slate,
                        modifier = Modifier.weight(1f))
                }
            }

            // service chips
            Row(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2),
                horizontalArrangement = Arrangement.spacedBy(Sp.s2)) {
                if (c.pickup) Chip("Pickup", O.BlueSoft, O.BlueDeep)
                if (c.delivery) Chip("Delivery · ₹${c.deliveryFee}", O.TealSoft, O.Teal)
                else Chip("No delivery", O.LineSoft, O.Slate)
                Chip("Packaging ₹${c.packagingFee}", O.BlueFaint, O.Slate)
            }

            // About
            Spacer(Modifier.height(Sp.s3))
            SectionHeader("About this canteen")
            Text(c.about, fontSize = 13.5.sp, color = O.Slate, lineHeight = 19.sp,
                modifier = Modifier.padding(horizontal = Sp.s5))
            Spacer(Modifier.height(Sp.s2))
            Row(
                Modifier.fillMaxWidth().padding(horizontal = Sp.s5),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IcInfo(size = 14.dp, tint = O.Slate, strokeWidth = 2f)
                Spacer(Modifier.width(Sp.s1))
                Text("Demonstration data — not a real registered canteen.",
                    fontSize = 11.sp, color = O.Slate, modifier = Modifier.weight(1f))
            }

            // In-canteen search
            Spacer(Modifier.height(Sp.s4))
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Sp.s5)
                    .background(O.Card, RoundedCornerShape(Radius.pill))
                    .border(BorderStroke(1.dp, O.Line), RoundedCornerShape(Radius.pill))
                    .padding(horizontal = Sp.s4, vertical = Sp.s3)
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IcSearch(size = 16.dp, tint = O.Slate, strokeWidth = 2f)
                    Spacer(Modifier.width(Sp.s3))
                    BasicTextField(
                        state = tfState,
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )
                    if (query.isNotEmpty()) {
                        Box(
                            Modifier.size(26.dp).clickable {
                                query = ""
                                tfState.setTextAndPlaceCursorAtEnd("")
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            IcX(size = 15.dp, tint = O.Slate, strokeWidth = 2.2f)
                        }
                    }
                }
            }
            LaunchedEffect(tfState) {
                snapshotFlow { tfState.text.toString() }.collect { query = it }
            }

            Spacer(Modifier.height(Sp.s2))
            SectionHeader("Available products", action = "(${shown.size})")
            if (shown.isEmpty()) {
                EmptyState(
                    title = "No matches",
                    body = "No products match “${query}”.",
                    glyph = { IcSearch(size = 30.dp, tint = O.Mute) },
                    actionLabel = "Clear",
                    onAction = {
                        query = ""
                        tfState.setTextAndPlaceCursorAtEnd("")
                    }
                )
            } else {
                Column(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                    shown.chunked(2).forEach { rowItems ->
                        Row(Modifier.fillMaxWidth().padding(bottom = Sp.s3),
                            horizontalArrangement = Arrangement.spacedBy(Sp.s3)) {
                            rowItems.forEach { p ->
                                ProductCard(p, canteenId = c.id,
                                    modifier = Modifier.weight(1f),
                                    onProduct = {
                                        onNav(Nav.productDetail(p.id))
                                        AppStore.recordViewed(p.id)
                                    })
                            }
                            if (rowItems.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
