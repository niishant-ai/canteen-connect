package canteenconnect.app.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.*
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.Brand
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp

/* ---------------- Settings ---------------- */
@Composable
fun SettingsScreen(onNav: (String) -> Unit, onBack: () -> Unit) {
    val s = AppStore.state()
    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Settings", onBack = onBack)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = Sp.s5)) {
            SectionHeader("Preferences")
            SwitchRow("Haptic feedback", s.settingsHaptics) { AppStore.setHaptics(it) }
            SwitchRow("Reduce motion", s.settingsReducedMotion) { AppStore.setReducedMotion(it) }
            SwitchRow("Show DEMO data badges", s.demoBadgeVisible) { AppStore.setDemoBadge(it) }

            Spacer(Modifier.height(Sp.s3))
            SectionHeader("Account")
            SettingsRow({ m, t -> IcRotateCcw(m, t, 18.dp, 2f) }, "Sign out of demo mode") {
                AppStore.logout()
            }
            SettingsRow({ m, t -> IcTrash(m, t, 18.dp, 2f) }, "Clear cart") { AppStore.clearCart() }
            SettingsRow({ m, t -> IcClock(m, t, 18.dp, 2f) }, "Clear recent searches") { AppStore.clearRecent() }

            Spacer(Modifier.height(Sp.s3))
            SectionHeader("Legal")
            SettingsRow({ m, t -> IcShieldCheck(m, t, 18.dp, 2f) }, "Privacy Information") { onNav(Nav.Privacy) }
            SettingsRow({ m, t -> IcList(m, t, 18.dp, 2f) }, "Terms & Conditions") { onNav(Nav.Terms) }
            SettingsRow({ m, t -> IcInfo(m, t, 18.dp, 2f) }, "About ${Brand.NAME}") { onNav(Nav.About) }

            Spacer(Modifier.height(Sp.s5))
            Text("Preferences are stored on this device. No data leaves your phone in this demo build.",
                fontSize = 11.5.sp, color = O.Slate, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Sp.s5))
        }
    }
}

@Composable
private fun SwitchRow(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = Sp.s5, vertical = Sp.s3),
        verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold, color = O.Ink,
            modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = { onToggle(it); AppStore.haptic() })
    }
}

@Composable
private fun SettingsRow(icon: @Composable (Modifier, Color) -> Unit, label: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.md))
            .clickable(onClick = onClick)
            .padding(horizontal = Sp.s4, vertical = Sp.s3)
            .padding(horizontal = Sp.s5, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(O.BlueSoft, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            icon(Modifier, O.BlueDeep)
        }
        Spacer(Modifier.width(Sp.s3))
        Text(label, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold, color = O.Ink,
            modifier = Modifier.weight(1f))
        IcChevronRight(size = 16.dp, tint = O.Mute, strokeWidth = 2.4f)
    }
}

/* ---------------- Help & Support ---------------- */
@Composable
fun HelpScreen(onNav: (String) -> Unit, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Help & Support", onBack = onBack)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = Sp.s5)) {
            SectionHeader("Frequently asked")
            Faq("How do I add an item to my cart?",
                "Open a product and tap ＋ on its card, or use the Add to Cart bar on the product page. The item is tied to the canteen you selected.")
            Faq("Why are my items split by canteen?",
                "Each registered canteen fulfils its own orders, so the cart keeps items grouped by canteen and never merges them into one fulfilment.")
            Faq("Is this a real government service?",
                "No. This is a demo using locally seeded data. It is not an official or government-endorsed application.")
            Faq("Do payments actually go through?",
                "No. Checkout uses a simulated “Pay at Counter (demo)” step and no real payment details are collected.")
            Faq("How do order statuses advance?",
                "Demo orders progress automatically over time so you can watch the tracking timeline without a backend.")
            Spacer(Modifier.height(Sp.s3))
            SectionHeader("Contact")
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(O.SuccessSoft, RoundedCornerShape(Radius.lg))
                    .padding(horizontal = Sp.s5, vertical = Sp.s3)
            ) {
                Box(
                    Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(Radius.md))
                        .background(O.Card, RoundedCornerShape(Radius.md)),
                    contentAlignment = Alignment.Center
                ) { IcHeadphones(size = 20.dp, tint = O.Success, strokeWidth = 2f) }
                Spacer(Modifier.width(Sp.s3))
                Column(Modifier.weight(1f)) {
                    Text("demo-support@opshar.example", fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold, color = O.Success)
                    Text("This is a fictional support address used for the demo.",
                        fontSize = 12.sp, color = O.Slate, modifier = Modifier.padding(top = 2.dp))
                }
            }
        }
    }
}

@Composable
private fun Faq(q: String, a: String) {
    var open by remember { mutableStateOf(false) }
    Column(Modifier.padding(horizontal = Sp.s5, vertical = 3.dp)) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(O.Card, RoundedCornerShape(Radius.md))
                .clickable { open = !open; AppStore.haptic() }
                .padding(Sp.s3)
        ) {
            Text(q, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = O.Ink,
                modifier = Modifier.weight(1f))
            if (open) IcMinus(size = 16.dp, tint = O.Blue, strokeWidth = 2.4f)
            else IcPlus(size = 16.dp, tint = O.Blue, strokeWidth = 2.4f)
        }
        if (open) {
            Text(a, fontSize = 12.5.sp, color = O.Slate, lineHeight = 17.sp,
                modifier = Modifier.padding(start = Sp.s3, end = Sp.s3, top = Sp.s2, bottom = Sp.s2))
        }
    }
}

/* ---------------- About ---------------- */
@Composable
fun AboutScreen(onNav: (String) -> Unit, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("About", onBack = onBack)
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = Sp.s5),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(Sp.s4))
            Box(
                Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(Radius.xl))
                    .background(O.Blue, RoundedCornerShape(Radius.xl)),
                contentAlignment = Alignment.Center
            ) {
                MitraMark(size = 52.dp)
            }
            Spacer(Modifier.height(Sp.s3))
            Text(Brand.NAME, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
            Text(Brand.TAGLINE, fontSize = 13.sp, color = O.Slate)
            Spacer(Modifier.height(Sp.s4))
            Text("Version 1.0.0 · Demo build", fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold, color = O.Slate)
            Spacer(Modifier.height(Sp.s5))
            Text(
                "${Brand.NAME} is a premium canteen-shopping experience designed around India's " +
                    "canteen ecosystem. In this development milestone it runs entirely on locally " +
                    "seeded demonstration data: canteen names, addresses, prices and ratings are " +
                    "fictional and are not affiliated with, or endorsed by, any government body. " +
                    "Authentication, real payments and live canteen integrations are planned for a " +
                    "future release; the architecture already isolates them behind a store layer.",
                fontSize = 13.5.sp, color = O.Slate, lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Sp.s5),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Sp.s5))
            Row(Modifier.fillMaxWidth().padding(horizontal = Sp.s5),
                horizontalArrangement = Arrangement.spacedBy(Sp.s2)) {
                Row(
                    Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(Radius.pill))
                        .background(O.Card, RoundedCornerShape(Radius.pill))
                        .border(BorderStroke(1.dp, O.Line), RoundedCornerShape(Radius.pill))
                        .clickable { onNav(Nav.Privacy) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Privacy", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                }
                Row(
                    Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(Radius.pill))
                        .background(O.Card, RoundedCornerShape(Radius.pill))
                        .border(BorderStroke(1.dp, O.Line), RoundedCornerShape(Radius.pill))
                        .clickable { onNav(Nav.Terms) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Terms", fontSize = 13.5.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                }
            }
        }
    }
}

/* ---------------- Privacy & Terms placeholders ---------------- */
@Composable
fun PrivacyScreen(onBack: () -> Unit) = LegalPlaceholder(
    title = "Privacy Information",
    body = listOf(
        "This demo build stores preferences, cart, wishlist, browsing history and demo orders only on your device.",
        "No account is created, no personal data is collected, and nothing is sent to a server.",
        "Location is set manually in the demo; the “use current location” option does not request real GPS.",
        "In a future version we will describe how any live services handle personal data here."
    ),
    onBack = onBack
)

@Composable
fun TermsScreen(onBack: () -> Unit) = LegalPlaceholder(
    title = "Terms & Conditions",
    body = listOf(
        "${Brand.NAME} is demonstration software and is not an official government application.",
        "All canteens, products, prices, ratings and orders shown are fictional sample data.",
        "Demo checkout does not create a real order or payment.",
        "The terms will be updated when a production service is launched."
    ),
    onBack = onBack
)

@Composable
private fun LegalPlaceholder(title: String, body: List<String>, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar(title, onBack = onBack)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(Sp.s5)) {
            Spacer(Modifier.height(Sp.s2))
            body.forEach { para ->
                Text(para, fontSize = 13.5.sp, color = O.Slate,
                    lineHeight = 20.sp, modifier = Modifier.fillMaxWidth().padding(vertical = Sp.s2))
            }
        }
    }
}
