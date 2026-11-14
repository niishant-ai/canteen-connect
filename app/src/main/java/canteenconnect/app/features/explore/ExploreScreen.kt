package canteenconnect.app.features.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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

/** Explore: category grid + canteen list, both wired to real navigation. */
@Composable
fun ExploreScreen(onNav: (String) -> Unit) {
    val s = AppStore.state()
    val canteens = AppStore.cityCanteens(s.city)
    LaunchedEffect(s.city) { AppStoreStateHolder.setCityCanteens(canteens) }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Explore", subtitle = "Categories & canteens")
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 132.dp)) {
            SectionHeader("Browse categories")
            // two-column category grid
            val cats = DemoData.categories
            cats.chunked(2).forEach { row ->
                Row(Modifier.fillMaxWidth().padding(horizontal = Sp.s5, vertical = Sp.s2),
                    horizontalArrangement = Arrangement.spacedBy(Sp.s3),
                    verticalAlignment = Alignment.Top) {
                    row.forEach { c ->
                        CategoryCell(c, Modifier.weight(1f)) { onNav(Nav.category(c.id)) }
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(Sp.s4))

            SectionHeader("Canteens in ${s.city}", action = "(${canteens.size})",
                onAction = { if (canteens.isEmpty()) onNav(Nav.LOCATION) else { } })
            if (canteens.isEmpty()) {
                EmptyState(
                    title = "No canteens in ${s.city}",
                    body = "This demo seeds canteens for New Delhi, Bengaluru, Pune, Hyderabad, Chandigarh and Lucknow.",
                    glyph = { IcStore(size = 30.dp, tint = O.Mute) },
                    actionLabel = "Change city",
                    onAction = { onNav(Nav.LOCATION) }
                )
            } else {
                canteens.forEach { c ->
                    CanteenCard(
                        c,
                        modifier = Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2),
                        onView = { onNav(Nav.canteen(c.id)) }
                    )
                }
            }
            Spacer(Modifier.height(Sp.s5))
        }
    }
}

/** A compact category tile used in the Explore two-column grid. */
@Composable
fun CategoryCell(c: canteenconnect.app.data.models.Category, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .background(O.Card, RoundedCornerShape(Radius.lg))
            .clickable(onClick = onClick)
            .padding(Sp.s4),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(Radius.lg)),
        ) {
            CategoryImage(
                c,
                modifier = Modifier.matchParentSize(),
                fallback = { m ->
                    Box(
                        m.background(O.surfaceOf(c.accent), RoundedCornerShape(Radius.lg)),
                        contentAlignment = Alignment.Center
                    ) { CategoryGlyphLocal(c) }
                }
            )
        }
        Spacer(Modifier.height(Sp.s2))
        Text(c.name, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center,
            maxLines = 1, overflow = TextOverflow.Ellipsis, color = O.Ink)
        Text(c.blurb, fontSize = 10.5.sp, color = O.Slate, textAlign = TextAlign.Center,
            maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp,
            modifier = Modifier.padding(top = 2.dp))
    }
}

@Composable
private fun CategoryGlyphLocal(c: canteenconnect.app.data.models.Category) {
    val deep = O.deepOf(c.accent)
    when (c.id) {
        "grocery" -> IcWheat(Modifier, deep, 26.dp, 1.7f)
        "snacks" -> IcCookie(Modifier, deep, 26.dp, 1.7f)
        "beverages" -> IcCoffee(Modifier, deep, 26.dp, 1.7f)
        "dairy" -> IcMilk(Modifier, deep, 26.dp, 1.7f)
        "packaged" -> IcUtensils(Modifier, deep, 26.dp, 1.7f)
        "produce" -> IcCarrot(Modifier, deep, 26.dp, 1.7f)
        "personal" -> IcDroplets(Modifier, deep, 26.dp, 1.7f)
        "household" -> IcBasket(Modifier, deep, 26.dp, 1.7f)
        "stationery" -> IcPencil(Modifier, deep, 26.dp, 1.7f)
        "kitchen" -> IcCookingPot(Modifier, deep, 26.dp, 1.7f)
        "cleaning" -> IcSprayCan(Modifier, deep, 26.dp, 1.7f)
        else -> IcGift(Modifier, deep, 26.dp, 1.7f)
    }
}
