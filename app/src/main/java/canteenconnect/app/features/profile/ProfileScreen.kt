package canteenconnect.app.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import canteenconnect.app.core.theme.Brand
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp

@Composable
fun ProfileScreen(onNav: (String) -> Unit) {
    val s = AppStore.state()
    val name = if (s.guest) "Guest shopper" else Brand.USER_NAME
    val sub = if (s.guest) "Browsing without a membership" else "Canteen ID · ${Brand.CANTEEN_ID}"

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("My Account") {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(O.Card, CircleShape)
                    .border(1.dp, O.Line, CircleShape)
                    .clickable { onNav(Nav.Settings) },
                contentAlignment = Alignment.Center
            ) { IcSettings(size = 19.dp, tint = O.Ink) }
        }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 40.dp)) {
            // identity card — aligned to the same 20dp gutter as the menu below
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(Sp.s4, Sp.s5, 0.dp, Sp.s5),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(O.BlueSoft, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (s.guest) "G" else "NK", fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold, color = O.BlueDeep)
                }
                Spacer(Modifier.width(Sp.s4))
                Column(Modifier.weight(1f)) {
                    Text("Hi, $name", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                    Text(sub, fontSize = 13.sp, color = O.Slate, modifier = Modifier.padding(top = 2.dp))
                }
                Box(
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(O.Card, CircleShape)
                        .border(1.dp, O.Line, CircleShape)
                        .clickable { onNav(Nav.PROFILE); AppStore.haptic() },
                    contentAlignment = Alignment.Center
                ) { IcChevronRight(size = 20.dp, tint = O.Ink, strokeWidth = 2.2f) }
            }

            Spacer(Modifier.height(Sp.s3))

            // menu
            SectionHeader("Menu", modifier = Modifier.padding(horizontal = Sp.s5, vertical = 0.dp))
            Column(Modifier.padding(horizontal = Sp.s5)) {
                ProfileRow(
                    { m, t -> IcBox(m, t, 20.dp, 2f) }, "My Orders",
                    "${s.orders.size} order${if (s.orders.size == 1) "" else "s"}", O.BlueSoft, O.BlueDeep
                ) { onNav(Nav.ORDERS) }
                ProfileRow(
                    { m, t -> canteenconnect.app.core.components.WishlistHeart(active = true, modifier = m, iconSize = 20.dp) },
                    "Wishlist", "${s.wishlist.size} saved", O.DangerSoft, O.Danger
                ) { onNav(Nav.ORDERS) }
                ProfileRow(
                    { m, t -> IcBadgePercent(m, t, 20.dp, 2f) }, "Canteen Offers",
                    "Coupons & category deals", O.AmberSoft, O.Amber
                ) { onNav(Nav.OFFERS) }
                ProfileRow(
                    { m, t -> IcMapPin(m, t, 20.dp, 2f) }, "Addresses",
                    "Home · Office · Gym", O.TealSoft, O.Teal
                ) { onNav(Nav.LOCATION) }
                ProfileRow(
                    { m, t -> IcCreditCard(m, t, 20.dp, 2f) }, "Payment Methods",
                    "Pay at counter (demo)", O.BlueSoft, O.BlueDeep
                ) { onNav(Nav.Settings) }
                ProfileRow(
                    { m, t -> IcHeadphones(m, t, 20.dp, 2f) }, "Help & Support",
                    "FAQs and contact", O.SuccessSoft, O.Success
                ) { onNav(Nav.Help) }
            }

            Spacer(Modifier.height(Sp.s4))

            // logout — single-layer padding, warm surface, consistent card shape
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Sp.s5)
                    .background(O.Card, RoundedCornerShape(Radius.md))
                    .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.md))
                    .clickable {
                        AppStore.logout()
                        AppStore.haptic()
                    }
                    .padding(horizontal = Sp.s4, vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IcLogout(size = 18.dp, tint = O.Danger, strokeWidth = 2f)
                    Spacer(Modifier.width(Sp.s3))
                    Text("Logout", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Danger,
                        modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ProfileRow(
    glyph: @Composable (Modifier, Color) -> Unit,
    title: String,
    sub: String,
    bg: Color,
    fg: Color,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.md))
            .clickable(onClick = onClick)
            .padding(horizontal = Sp.s4, vertical = Sp.s3)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bg, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            glyph(Modifier, fg)
        }
        Spacer(Modifier.width(Sp.s3))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(sub, fontSize = 12.sp, color = O.Slate)
        }
        IcChevronRight(size = 18.dp, tint = O.Mute, strokeWidth = 2.2f)
    }
}
