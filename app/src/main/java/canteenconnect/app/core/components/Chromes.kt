package canteenconnect.app.core.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.navigation.Nav
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp

/** Standard top bar with title, optional back chevron and trailing slots.
 *  AURA: the band is always warm sand (O.Bg) — never a dark surface. */
@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    transparent: Boolean = false,
    right: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(if (transparent) Modifier else Modifier.glassBand())
            .then(if (transparent) Modifier else Modifier.statusBarsPadding())
            .padding(horizontal = Sp.s4, vertical = Sp.s3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(O.Card.copy(alpha = 0.95f), O.Sand.copy(alpha = 0.7f))
                        ),
                        CircleShape
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                IcChevronLeft(size = 20.dp, tint = O.Ink, strokeWidth = 2.2f)
            }
            Spacer(Modifier.width(Sp.s3))
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (subtitle != null) {
                Text(subtitle, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        right?.invoke(this)
    }
}

/** Search pill — tappable to open full search. */
@Composable
fun SearchBar(onTap: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .height(50.dp)
            .glass(RoundedCornerShape(Radius.pill))
            .clickable(onClick = onTap)
            .padding(horizontal = Sp.s4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IcSearch(size = 18.dp, tint = O.Slate, strokeWidth = 2f)
        Spacer(Modifier.width(Sp.s3))
        Text("Search products, brands…", fontSize = 14.5.sp, color = O.Slate)
    }
}

/** A pill "location chip" shown in headers. */
@Composable
fun LocationChip(onTap: () -> Unit, modifier: Modifier = Modifier) {
    val s = AppStore.state()
    Row(
        modifier = modifier
            .background(O.BlueSoft, RoundedCornerShape(Radius.pill))
            .clickable(onClick = onTap)
            .padding(horizontal = Sp.s4, vertical = Sp.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IcMapPin(size = 15.dp, tint = O.BlueDeep, strokeWidth = 2f)
        Spacer(Modifier.width(Sp.s2))
        Column {
            Text("${s.area}, ${s.city}", fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = O.BlueDeep)
            Text("PIN ${s.pin} · Change", fontSize = 10.sp, color = O.Slate)
        }
        Spacer(Modifier.width(Sp.s1))
        IcChevronDown(size = 14.dp, tint = O.BlueDeep, strokeWidth = 2.4f)
    }
}

/**
 * The premium floating bottom navigation (reference-faithful).
 *
 * Three primary destinations (Home, Explore, Orders & Wishlist) live inside ONE
 * floating rounded capsule. Cart sits in its own separate circular capsule with
 * a small intentional gap between the two. The active destination smoothly
 * expands into a blue-tinted pill showing icon + label; inactive ones stay
 * compact circles. The cart carries a live item-count badge.
 */
@Composable
fun FloatingBottomNav(
    current: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cartCount = AppStore.cartCount()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Main capsule: Home / Explore / Orders — navigation group.
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(O.Card.copy(alpha = 0.94f), O.Sand.copy(alpha = 0.68f))
                    ),
                    RoundedCornerShape(Radius.pill)
                )
                .border(1.dp, Color.White.copy(alpha = 0.55f), RoundedCornerShape(Radius.pill))
                .shadow(6.dp, RoundedCornerShape(Radius.pill))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                tab = Nav.bottomTabs[0],
                active = current == Nav.HOME,
                onClick = { onSelect(Nav.HOME); AppStore.haptic() }
            )
            Spacer(Modifier.width(24.dp))
            NavItem(
                tab = Nav.bottomTabs[1],
                active = current == Nav.EXPLORE,
                onClick = { onSelect(Nav.EXPLORE); AppStore.haptic() }
            )
            Spacer(Modifier.width(24.dp))
            NavItem(
                tab = Nav.bottomTabs[2],
                active = current == Nav.ORDERS,
                onClick = { onSelect(Nav.ORDERS); AppStore.haptic() }
            )
        }
        // Cart is intentionally detached — it is the checkout action, not
        // navigation. The whole cluster stays centered, so both outer
        // margins are symmetric.
        Spacer(Modifier.width(16.dp))
        NavItem(
            tab = Nav.bottomTabs[3],
            active = current == Nav.CART,
            badge = cartCount,
            standalone = true,
            onClick = { onSelect(Nav.CART); AppStore.haptic() }
        )
    }
}

/** One capsule slot: compact circle that expands to an icon+label pill when active. */
@Composable
private fun NavItem(
    tab: Nav.Tab,
    active: Boolean,
    modifier: Modifier = Modifier,
    badge: Int = 0,
    standalone: Boolean = false,
    onClick: () -> Unit
) {
    // Label width drives the pill expansion (0 when compact). Measure each
    // label's natural text width so the expanded pill fits its text exactly —
    // no clipping, and same-length labels size identically.
    val labelW = remember { Animatable(0f) }
    val textMeasurer = rememberTextMeasurer()
    var labelPx by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(tab.label) {
        labelPx = textMeasurer.measure(
            tab.label,
            TextStyle(fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
        ).size.width.toFloat()
    }
    val density = LocalDensity.current.density
    LaunchedEffect(active, labelPx) {
        labelW.animateTo(
            if (active) labelPx / density + 18f else 0f,
            tween(320)
        )
    }

    // Orders hub shows a receipt (order semantics); a heart implies wishlist-only.
    val icon: @Composable (Modifier, Color) -> Unit = when (tab.route) {
        Nav.HOME -> { m, t -> IcHome(m, t, 20.dp, 2f) }
        Nav.EXPLORE -> { m, t -> IcGrid(m, t, 20.dp, 2f) }
        Nav.ORDERS -> { m, t -> IcReceipt(m, t, 20.dp, 2f) }
        else -> { m, t -> IcCart(m, t, 20.dp, 2f) }
    }

    val frame = if (standalone) {
        Modifier
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF34383E), O.Charcoal)
                ),
                RoundedCornerShape(Radius.pill)
            )
            .shadow(6.dp, RoundedCornerShape(Radius.pill))
            .padding(horizontal = 6.dp, vertical = 6.dp)
    } else Modifier

    // The detached cart pill is charcoal, so its icon/label use cream/sage
    // tints for contrast; the main capsule uses the sage-ink treatment.
    val iconInactive = if (standalone) O.Card else O.Slate
    val labelColor = if (standalone) {
        if (active) O.BlueDeep else O.Card
    } else O.BlueDeep

    Row(
        modifier = modifier
            .then(frame)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .height(44.dp)
                .width(44.dp + (labelW.value).dp)
                .background(if (active) O.BlueSoft else Color.Transparent, RoundedCornerShape(Radius.pill))
                .clip(RoundedCornerShape(Radius.pill)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                icon(Modifier, if (active) O.BlueDeep else iconInactive)
                if (badge > 0) {
                    Box(Modifier.align(Alignment.TopEnd).offset(x = 3.dp)) {
                        Badge(count = badge)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .width((labelW.value).dp)
                    .graphicsLayer { alpha = if (labelW.value > 4f) 1f else 0f },
                contentAlignment = Alignment.CenterStart
            ) {
                if (labelW.value > 4f) {
                    Text(
                    tab.label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = labelColor,
                    modifier = Modifier.padding(start = 4.dp)
                )
                }
            }
        }
    }
}

/** Small count badge, offset to the top-right of its parent. */
@Composable
private fun Badge(count: Int) {
    val text = if (count > 99) "99+" else count.toString()
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(O.Danger)
            .border(1.5.dp, Color.White, CircleShape)
            .padding(horizontal = 4.dp, vertical = 1.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = Color.White, lineHeight = 11.sp)
    }
}
