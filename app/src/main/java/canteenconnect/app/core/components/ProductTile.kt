package canteenconnect.app.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import canteenconnect.app.R
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.data.models.Category
import canteenconnect.app.data.models.Product

/**
 * Product visuals for AURA.
 *
 * Every card/tile shows a real product photo (squared, cropped to fit) with a
 * warm surface behind it so letterboxing never shows. Items without a photo
 * fall back to the premium vector tile (soft pastel surface + Lucide glyph),
 * so no card ever renders blank.
 */
private val PRODUCT_IMAGES: Map<String, Int> by lazy {
    val m = mutableMapOf<String, Int>()
    fun add(res: Int, ids: List<String>) { ids.forEach { m[it] = res } }
    // Groceries
    add(R.drawable.ph_rice, listOf("g1"))
    add(R.drawable.ph_atta, listOf("g2", "g5", "g6"))
    add(R.drawable.ph_ghee, listOf("g7"))
    add(R.drawable.ph_jar, listOf("g8", "d2"))
    add(R.drawable.ph_salt, listOf("g9"))
    add(R.drawable.cat_grocery, listOf("g4", "g10"))
    add(R.drawable.cat_snacks, listOf("g3", "s2", "s5", "p2", "v6"))
    // Snacks
    add(R.drawable.ph_biscuit, listOf("s1", "s4", "o1"))
    add(R.drawable.ph_noodles, listOf("s3", "p3"))
    add(R.drawable.cat_kitchen, listOf("s6", "h4"))
    // Beverages
    add(R.drawable.cat_beverages, listOf("b1", "b2"))
    add(R.drawable.ph_water, listOf("b3", "k3"))
    add(R.drawable.ph_milo, listOf("b4", "b6"))
    add(R.drawable.ph_milk, listOf("b5", "d1"))
    // Dairy
    add(R.drawable.cat_dairy, listOf("d3", "d4", "d5"))
    // Packaged
    add(R.drawable.cat_packaged, listOf("p1", "p4"))
    add(R.drawable.ph_jar, listOf("p5"))
    // Produce
    add(R.drawable.cat_produce, listOf("v1", "v2", "v3", "v4", "v5"))
    // Personal care
    add(R.drawable.ph_soap, listOf("pc1"))
    add(R.drawable.ph_shampoo, listOf("pc2"))
    add(R.drawable.ph_toothpaste, listOf("pc3"))
    add(R.drawable.cat_personal, listOf("pc4"))
    // Household / cleaning
    add(R.drawable.cat_cleaning, listOf("h1", "h2", "cl3", "cl4"))
    add(R.drawable.cat_household, listOf("h3", "cl1", "cl2"))
    // Stationery / kitchen
    add(R.drawable.cat_stationery, listOf("st1", "st2", "st3", "st4"))
    add(R.drawable.cat_kitchen, listOf("k1", "k2", "k4"))
    m
}

fun productImageRes(id: String): Int = PRODUCT_IMAGES[id] ?: 0

private val CATEGORY_IMAGES: Map<String, Int> by lazy {
    mapOf(
        "grocery" to R.drawable.cat_grocery,
        "snacks" to R.drawable.cat_snacks,
        "beverages" to R.drawable.cat_beverages,
        "dairy" to R.drawable.cat_dairy,
        "packaged" to R.drawable.cat_packaged,
        "produce" to R.drawable.cat_produce,
        "personal" to R.drawable.cat_personal,
        "household" to R.drawable.cat_household,
        "stationery" to R.drawable.cat_stationery,
        "kitchen" to R.drawable.cat_kitchen,
        "cleaning" to R.drawable.cat_cleaning
    )
}

fun categoryImageRes(id: String): Int = CATEGORY_IMAGES[id] ?: 0

/**
 * The canonical product "image": a square-cropped photo over the product's
 * warm surface. Falls back to the vector glyph tile when no photo is mapped.
 */
@Composable
fun ProductImage(
    product: Product,
    modifier: Modifier = Modifier,
    fallbackGlyphSize: Dp = 46.dp
) {
    val res = productImageRes(product.id)
    if (res != 0) {
        Box(
            modifier
                .background(O.surfaceOf(product.accent), RoundedCornerShape(Radius.md))
        ) {
            Image(
                painter = painterResource(res),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    } else {
        Box(
            modifier
                .background(O.surfaceOf(product.accent), RoundedCornerShape(Radius.md)),
            contentAlignment = Alignment.Center
        ) {
            ProductGlyph(product, size = fallbackGlyphSize)
        }
    }
}

/** Category tile image: real photo when available, else the vector glyph. */
@Composable
fun CategoryImage(
    c: Category,
    modifier: Modifier = Modifier,
    fallback: @Composable (Modifier) -> Unit
) {
    val res = categoryImageRes(c.id)
    if (res != 0) {
        Box(modifier.background(O.surfaceOf(c.accent))) {
            Image(
                painter = painterResource(res),
                contentDescription = c.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    } else {
        fallback(modifier)
    }
}

/** Vector "product image" for AURA (fallback when no photo is mapped). */
@Composable
fun ProductGlyph(
    product: Product,
    modifier: Modifier = Modifier,
    tint: Color = O.deepOf(product.accent),
    size: Dp = 30.dp,
    strokeWidth: Float = 1.7f
) {
    when (product.id) {
        // Groceries
        "g1" -> IcWheat(modifier, tint, size, strokeWidth)
        "g2" -> IcWheat(modifier, tint, size, strokeWidth)
        "g3" -> IcBean(modifier, tint, size, strokeWidth)
        "g4" -> IcBean(modifier, tint, size, strokeWidth)
        "g5" -> IcWheat(modifier, tint, size, strokeWidth)
        "g6" -> IcWheat(modifier, tint, size, strokeWidth)
        "g7" -> IcMilk(modifier, tint, size, strokeWidth)
        "g8" -> IcDroplets(modifier, tint, size, strokeWidth)
        "g9" -> IcShell(modifier, tint, size, strokeWidth)
        "g10" -> IcFlame(modifier, tint, size, strokeWidth)
        // Snacks
        "s1" -> IcCookie(modifier, tint, size, strokeWidth)
        "s2" -> IcNut(modifier, tint, size, strokeWidth)
        "s3" -> IcWheat(modifier, tint, size, strokeWidth)
        "s4" -> IcCookie(modifier, tint, size, strokeWidth)
        "s5" -> IcBean(modifier, tint, size, strokeWidth)
        "s6" -> IcCroissant(modifier, tint, size, strokeWidth)
        // Beverages
        "b1" -> IcCoffee(modifier, tint, size, strokeWidth)
        "b2" -> IcCoffee(modifier, tint, size, strokeWidth)
        "b3" -> IcGlassWater(modifier, tint, size, strokeWidth)
        "b4" -> IcCitrus(modifier, tint, size, strokeWidth)
        "b5" -> IcMilk(modifier, tint, size, strokeWidth)
        "b6" -> IcSoda(modifier, tint, size, strokeWidth)
        // Dairy
        "d1" -> IcMilk(modifier, tint, size, strokeWidth)
        "d2" -> IcShell(modifier, tint, size, strokeWidth)
        "d3" -> IcBeef(modifier, tint, size, strokeWidth)
        "d4" -> IcBeef(modifier, tint, size, strokeWidth)
        "d5" -> IcCake(modifier, tint, size, strokeWidth)
        // Packaged
        "p1" -> IcSoup(modifier, tint, size, strokeWidth)
        "p2" -> IcSalad(modifier, tint, size, strokeWidth)
        "p3" -> IcSoup(modifier, tint, size, strokeWidth)
        "p4" -> IcCroissant(modifier, tint, size, strokeWidth)
        "p5" -> IcShell(modifier, tint, size, strokeWidth)
        // Produce
        "v1" -> IcBanana(modifier, tint, size, strokeWidth)
        "v2" -> IcApple(modifier, tint, size, strokeWidth)
        "v3" -> IcApple(modifier, tint, size, strokeWidth)
        "v4" -> IcApple(modifier, tint, size, strokeWidth)
        "v5" -> IcLeaf(modifier, tint, size, strokeWidth)
        "v6" -> IcNut(modifier, tint, size, strokeWidth)
        // Personal care
        "pc1" -> IcDroplets(modifier, tint, size, strokeWidth)
        "pc2" -> IcDroplets(modifier, tint, size, strokeWidth)
        "pc3" -> IcDroplets(modifier, tint, size, strokeWidth)
        "pc4" -> IcSprayCan(modifier, tint, size, strokeWidth)
        // Household
        "h1" -> IcSprout(modifier, tint, size, strokeWidth)
        "h2" -> IcSprout(modifier, tint, size, strokeWidth)
        "h3" -> IcTrash(modifier, tint, size, strokeWidth)
        "h4" -> IcFlame(modifier, tint, size, strokeWidth)
        // Stationery
        "st1" -> IcList(modifier, tint, size, strokeWidth)
        "st2" -> IcPencil(modifier, tint, size, strokeWidth)
        "st3" -> IcList(modifier, tint, size, strokeWidth)
        "st4" -> IcRuler(modifier, tint, size, strokeWidth)
        // Kitchen
        "k1" -> IcCoffee(modifier, tint, size, strokeWidth)
        "k2" -> IcCookingPot(modifier, tint, size, strokeWidth)
        "k3" -> IcGlassWater(modifier, tint, size, strokeWidth)
        "k4" -> IcGlassWater(modifier, tint, size, strokeWidth)
        // Cleaning
        "cl1" -> IcDroplets(modifier, tint, size, strokeWidth)
        "cl2" -> IcSprayCan(modifier, tint, size, strokeWidth)
        "cl3" -> IcSprayCan(modifier, tint, size, strokeWidth)
        "cl4" -> IcShell(modifier, tint, size, strokeWidth)
        // Other
        "o1" -> IcGift(modifier, tint, size, strokeWidth)
        "o2" -> IcFlashlight(modifier, tint, size, strokeWidth)
        "o3" -> IcSmartphone(modifier, tint, size, strokeWidth)
        else -> when (product.categoryId) {
            "grocery" -> IcWheat(modifier, tint, size, strokeWidth)
            "snacks" -> IcCookie(modifier, tint, size, strokeWidth)
            "beverages" -> IcCoffee(modifier, tint, size, strokeWidth)
            "dairy" -> IcMilk(modifier, tint, size, strokeWidth)
            "packaged" -> IcUtensils(modifier, tint, size, strokeWidth)
            "produce" -> IcCarrot(modifier, tint, size, strokeWidth)
            "personal" -> IcDroplets(modifier, tint, size, strokeWidth)
            "household" -> IcTrash(modifier, tint, size, strokeWidth)
            "stationery" -> IcPencil(modifier, tint, size, strokeWidth)
            "kitchen" -> IcCookingPot(modifier, tint, size, strokeWidth)
            "cleaning" -> IcSprayCan(modifier, tint, size, strokeWidth)
            else -> IcGift(modifier, tint, size, strokeWidth)
        }
    }
}

/** A soft rounded tile holding a [ProductImage] — the canonical "product image". */
@Composable
fun ProductTile(
    product: Product,
    modifier: Modifier = Modifier,
    size: Dp = 72.dp,
    glyphSize: Dp = 30.dp,
    radius: Dp = 18.dp
) {
    ProductImage(
        product,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(radius)),
        fallbackGlyphSize = glyphSize
    )
}
