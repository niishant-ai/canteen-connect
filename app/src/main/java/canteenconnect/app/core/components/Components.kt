package canteenconnect.app.core.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp
import canteenconnect.app.data.demo.DemoData
import canteenconnect.app.data.models.*

/** Bridges AppStore access for the stateless card without a Composable. */
object AppStoreStateHolder {
    private var cityCanteens: List<Canteen> = DemoData.canteens
    fun setCityCanteens(list: List<Canteen>) { cityCanteens = list }
    fun currentCityCanteens(): List<Canteen> = cityCanteens
}

/** Small discount percentage helper (0 when no discount). */
fun discountPct(p: Product): Int {
    if (p.originalPrice <= p.price) return 0
    return ((p.originalPrice - p.price) * 100 / p.originalPrice)
}

/** Animated wishlist heart: outlined when off, filled red when on. */
@Composable
fun WishlistHeart(
    active: Boolean,
    modifier: Modifier = Modifier,
    iconSize: Dp = 18.dp
) {
    val heart = remember {
        Path().apply { PathParser().apply { parsePathString(IcHeart_DATA.pathData.first()) }.toPath(this) }
    }
    Canvas(modifier = Modifier.size(iconSize).then(modifier)) {
        val s = size.width / 24f
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                color = if (active) O.Danger else Color(0xFF9AA3B8)
                style = if (active) PaintingStyle.Fill else PaintingStyle.Stroke
                strokeWidth = 1.8f * s
                strokeCap = StrokeCap.Round
                strokeJoin = StrokeJoin.Round
                isAntiAlias = true
            }
            canvas.save()
            canvas.scale(s, s)
            canvas.drawPath(heart, paint)
            canvas.restore()
        }
    }
}

/** First canteen (in the current city scope) that stocks a product. */
fun anyStockingCanteen(productId: String): String {
    val scoping = AppStoreStateHolder.currentCityCanteens()
    val pool = if (scoping.isNotEmpty() && scoping.none { DemoData.stock(it.id).contains(productId) }) DemoData.canteens else scoping
    return pool.firstOrNull { DemoData.stock(it.id).contains(productId) }?.id
        ?: DemoData.canteens.first().id
}

fun fmtDistance(m: Int): String =
    if (m < 1000) "$m m" else String.format("%.1f km", m / 1000.0)

fun relTime(ms: Long): String {
    val mins = ((System.currentTimeMillis() - ms) / 60000L).coerceAtLeast(0)
    return when {
        mins < 1 -> "Just now"
        mins < 60 -> "$mins min ago"
        mins < 60 * 24 -> "${mins / 60} h ago"
        else -> "${mins / (60 * 24)} d ago"
    }
}

/**
 * AURA liquid-glass surfaces.
 *
 * True multi-layer blur isn't available without a heavy blur dependency, so
 * these implement a convincing "glassmorphism" look with GPU-cheap primitives:
 * a warm translucent gradient fill, a bright top sheen line, and a soft hairline
 * border. Readable over light content; no continuous animation, no layout cost.
 */
@Composable
fun Modifier.glass(shape: RoundedCornerShape = RoundedCornerShape(Radius.md)): Modifier =
    this
        .clip(shape)
        .background(
            Brush.linearGradient(listOf(
                O.Card.copy(alpha = 0.9f),
                O.Sand.copy(alpha = 0.6f)
            ))
        )
        .border(1.dp, Color.White.copy(alpha = 0.55f), shape)
        .drawBehind {
            drawLine(
                start = Offset(10.dp.toPx(), 1.dp.toPx()),
                end = Offset(size.width - 10.dp.toPx(), 1.dp.toPx()),
                color = Color.White.copy(alpha = 0.7f),
                strokeWidth = 1.dp.toPx()
            )
        }

/** Full-bleed warm glass band for top bars. */
@Composable
fun Modifier.glassBand(): Modifier =
    this
        .background(
            Brush.linearGradient(listOf(
                O.Card.copy(alpha = 0.85f),
                O.Sand.copy(alpha = 0.5f)
            ))
        )
        .drawBehind {
            drawLine(
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                color = Color.White.copy(alpha = 0.8f),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                start = Offset(0f, size.height - 1.dp.toPx()),
                end = Offset(size.width, size.height - 1.dp.toPx()),
                color = O.Line.copy(alpha = 0.6f),
                strokeWidth = 1.dp.toPx()
            )
        }

/** Round glass icon-button surface. */
@Composable
fun Modifier.glassCircle(): Modifier =
    this
        .clip(CircleShape)
        .background(
            Brush.linearGradient(listOf(
                O.Card.copy(alpha = 0.92f),
                O.Sand.copy(alpha = 0.62f)
            ))
        )
        .border(1.dp, Color.White.copy(alpha = 0.6f), CircleShape)
        .drawBehind {
            drawLine(
                start = Offset(6.dp.toPx(), 2.dp.toPx()),
                end = Offset(size.width - 6.dp.toPx(), 2.dp.toPx()),
                color = Color.White.copy(alpha = 0.7f),
                strokeWidth = 1.dp.toPx()
            )
        }

/** Small pastel pill chip. */
@Composable
fun Chip(label: String, bg: Color = O.BlueSoft, fg: Color = O.BlueDeep, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(8.dp), color = bg, modifier = modifier) {
        Box(
            Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(label, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = fg, lineHeight = 14.sp)
        }
    }
}

/** Selectable pill filter/sort chip. */
@Composable
fun FilterChip(
    label: String,
    active: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(Radius.pill),
        color = if (active) O.BlueSoft else O.Card,
        border = BorderStroke(1.dp, if (active) O.Blue else O.Line),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            label, fontSize = 12.5.sp,
            fontWeight = if (active) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = if (active) O.BlueDeep else O.Slate,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
        )
    }
}

/** "DEMO" data badge used to flag non-production data. */
@Composable
fun DemoBadge(modifier: Modifier = Modifier) {
    if (AppStore.stateSnapshot.demoBadgeVisible) {
        Surface(shape = RoundedCornerShape(6.dp), color = O.Ink, modifier = modifier) {
            Box(Modifier.padding(horizontal = 6.dp, vertical = 2.dp)) {
                Text("DEMO", fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = O.BlueSoft)
            }
        }
    }
}

/** Filled primary CTA. */
@Composable
fun PrimaryButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fill: Boolean = true,
    leading: (@Composable (RowScope.() -> Unit))? = null,
    onClick: () -> Unit
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val motionOff = AppStore.stateSnapshot.settingsReducedMotion
    val scale by animateFloatAsState(
        targetValue = if (pressed && !motionOff) 0.97f else 1f,
        animationSpec = tween(150),
        label = "pressScale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = tween(150),
        label = "pressAlpha"
    )
    val bg by animateColorAsState(
        targetValue = when {
            !enabled -> O.Mute
            fill -> O.Charcoal
            else -> O.Card
        }, label = "btnBg"
    )
    Surface(
        shape = RoundedCornerShape(Radius.md),
        color = bg,
        border = if (fill) null else BorderStroke(1.dp, O.Line),
        shadowElevation = if (fill) 6.dp else 0.dp,
        modifier = modifier.height(52.dp)
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
                this.alpha = alpha
            }
            .shadow(if (fill) 8.dp else 0.dp, RoundedCornerShape(Radius.md))
            .clickable(
                interactionSource = source,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
    ) {
        Row(Modifier.fillMaxSize().padding(horizontal = Sp.s5), verticalAlignment = Alignment.CenterVertically) {
            leading?.invoke(this)
            if (leading != null) Spacer(Modifier.width(Sp.s2))
            Text(
                label, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold,
                color = when {
                    !enabled -> O.Mute
                    fill -> Color.White
                    else -> O.Ink
                }
            )
        }
    }
}

/** Small outlined/ghost button. */
@Composable
fun GhostButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val motionOff = AppStore.stateSnapshot.settingsReducedMotion
    val scale by animateFloatAsState(
        targetValue = if (pressed && !motionOff) 0.97f else 1f,
        animationSpec = tween(150),
        label = "ghostScale"
    )
    Surface(
        shape = RoundedCornerShape(Radius.md),
        color = O.Card,
        border = BorderStroke(1.dp, O.Line),
        modifier = modifier.height(52.dp)
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
            }
            .clickable(interactionSource = source, indication = null, onClick = onClick)
    ) {
        Box(Modifier.fillMaxSize().padding(horizontal = Sp.s5), contentAlignment = Alignment.Center) {
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = O.Ink)
        }
    }
}

/** Section title + optional trailing "See All". */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Sp.s5, vertical = Sp.s2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
        if (action != null) {
            Row(
                Modifier
                    .clickable(enabled = onAction != null, onClick = onAction ?: {})
                    .padding(horizontal = Sp.s2, vertical = Sp.s1),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    action,
                    style = MaterialTheme.typography.labelMedium,
                    color = O.Blue
                )
                IcChevronRight(size = 14.dp, tint = O.Blue, strokeWidth = 2.4f)
            }
        }
    }
}

/**
 * Premium product card. The parent controls its width/weight.
 * Vector tile, discount badge, wishlist heart, name, brand, price line.
 */
@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    canteenId: String? = null,
    showCartButton: Boolean = true,
    showWish: Boolean = true,
    onProduct: (String) -> Unit
) {
    val inWish = AppStore.isWishlisted(product.id)
    ProductCard(
        product = product,
        inWish = inWish,
        onProduct = { onProduct(product.id) },
        onWish = { AppStore.toggleWishlist(product.id); AppStore.haptic() },
        onCart = {
            val target = canteenId ?: anyStockingCanteen(product.id)
            AppStore.addToCart(product.id, target)
            AppStore.haptic()
        },
        showCartButton = showCartButton,
        showWish = showWish,
        modifier = modifier
    )
}

/** Non-stateful variant so parents can pass live wishlist state in. */
@Composable
fun ProductCard(
    product: Product,
    inWish: Boolean,
    modifier: Modifier = Modifier,
    showCartButton: Boolean = true,
    showWish: Boolean = true,
    onProduct: () -> Unit,
    onWish: () -> Unit,
    onCart: () -> Unit
) {
    val pct = discountPct(product)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.card))
            .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))
            .clickable(onClick = onProduct)
            .padding(Sp.s3)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Radius.md)),
        ) {
            ProductImage(product, modifier = Modifier.matchParentSize())
            if (pct > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                        .background(O.Danger, RoundedCornerShape(7.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("-$pct%", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
                        color = Color.White, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                }
            }
            if (showWish) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(O.Card.copy(alpha = 0.94f))
                        .clickable(onClick = onWish),
                    contentAlignment = Alignment.Center
                ) {
                    WishlistHeart(inWish)
                }
            }
        }
        Spacer(Modifier.height(Sp.s3))
        Text(product.name, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold,
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(product.brand + " · " + product.unit, fontSize = 11.5.sp,
            color = O.Slate, maxLines = 1, overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 1.dp))
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
            Text("₹${product.price}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
            if (product.originalPrice > product.price) {
                Text(
                    "₹${product.originalPrice}", fontSize = 11.sp, color = O.Mute,
                    textDecoration = TextDecoration.LineThrough,
                    modifier = Modifier.padding(start = 5.dp, bottom = 1.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            if (showCartButton) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = O.BlueSoft,
                    modifier = Modifier.size(30.dp).clickable(onClick = onCart)
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        IcPlus(size = 16.dp, tint = O.BlueDeep, strokeWidth = 2.6f)
                    }
                }
            }
        }
    }
}

/** Soft rounded canteen glyph tile (store icon on an accent surface). */
@Composable
fun CanteenGlyph(accent: Int, size: androidx.compose.ui.unit.Dp = 44.dp, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.32f))
            .background(O.surfaceOf(accent), RoundedCornerShape(size * 0.32f)),
        contentAlignment = Alignment.Center
    ) {
        IcStore(size = size * 0.52f, tint = O.deepOf(accent), strokeWidth = 1.7f)
    }
}

/** Canteen card with glyph tile, status, services and a CTA. */
@Composable
fun CanteenCard(
    c: Canteen,
    modifier: Modifier = Modifier,
    onView: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(O.Card, RoundedCornerShape(Radius.card))
            .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))
            .clickable(onClick = onView)
            .padding(Sp.s4)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(Radius.md))
                .background(O.surfaceOf(c.accent)),
            contentAlignment = Alignment.Center
        ) {
            IcStore(size = 26.dp, tint = O.deepOf(c.accent), strokeWidth = 1.7f)
        }
        Spacer(Modifier.width(Sp.s3))
        Column(Modifier.weight(1f)) {
            Text(c.name, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, maxLines = 1,
                overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IcMapPin(size = 13.dp, tint = O.Slate, strokeWidth = 2f)
                Spacer(Modifier.width(3.dp))
                Text("${c.area} · ${fmtDistance(c.distanceM)}",
                    fontSize = 12.sp, color = O.Slate, maxLines = 1,
                    overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
            }
            Spacer(Modifier.height(Sp.s2))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                if (c.openNow) Chip("Open", O.SuccessSoft, O.Success)
                else Chip("Closed", O.DangerSoft, O.Danger)
                if (c.pickup) Chip("Pickup", O.BlueSoft, O.BlueDeep)
                if (c.delivery) Chip("Delivery", O.TealSoft, O.Teal)
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(Alignment.CenterVertically)) {
            Text("View", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = O.BlueDeep)
            IcChevronRight(size = 16.dp, tint = O.Blue, strokeWidth = 2.4f)
        }
    }
}

/** Empty-state: icon in a soft circle + copy + optional action. */
@Composable
fun EmptyState(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    glyph: @Composable (() -> Unit) = {
        IcSearch(size = 30.dp, tint = O.Mute)
    },
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(Sp.s6, Sp.s5),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(O.BlueSoft),
            contentAlignment = Alignment.Center
        ) {
            glyph()
        }
        Spacer(Modifier.height(Sp.s4))
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
        Spacer(Modifier.height(Sp.s2))
        Text(body, fontSize = 13.5.sp, color = O.Slate,
            textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = Sp.s6))
        if (actionLabel != null) {
            Spacer(Modifier.height(Sp.s4))
            PrimaryButton(actionLabel, modifier = Modifier.wrapContentWidth()) { onAction?.invoke() }
        }
    }
}

/** Shimmer skeleton card matching [ProductCard] dimensions. */
@Composable
fun ShimmerCard(modifier: Modifier = Modifier) {
    val a = O.LineSoft; val b = O.Card
    Surface(color = O.Card, shape = RoundedCornerShape(Radius.card),
        modifier = modifier.fillMaxWidth().border(1.dp, O.LineSoft, RoundedCornerShape(Radius.card))) {
        Column(Modifier.padding(Sp.s3)) {
            Box(Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(Radius.md)).background(a))
            Spacer(Modifier.height(Sp.s3))
            Box(Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)).background(a))
            Spacer(Modifier.height(Sp.s2))
            Box(Modifier.fillMaxWidth(0.6f).height(10.dp).clip(RoundedCornerShape(5.dp)).background(a))
            Spacer(Modifier.height(Sp.s2))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.width(56.dp).height(14.dp).clip(RoundedCornerShape(7.dp)).background(a))
                Spacer(Modifier.width(Sp.s2))
                Box(Modifier.width(44.dp).height(10.dp).clip(RoundedCornerShape(5.dp)).background(b))
                Spacer(Modifier.weight(1f))
                Box(Modifier.size(30.dp).clip(RoundedCornerShape(10.dp)).background(a))
            }
        }
    }
}
