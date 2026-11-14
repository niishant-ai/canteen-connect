package canteenconnect.app.features.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
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

@Composable
fun SearchScreen(onNav: (String) -> Unit, onBack: () -> Unit) {
    val s = AppStore.state()
    val focus = remember { FocusRequester() }
    val tfState = rememberTextFieldState("")
    LaunchedEffect(Unit) { focus.requestFocus() }
    val query = tfState.text.toString()
    LaunchedEffect(query) { if (query.isNotBlank()) AppStore.recordSearch(query) }

    val q = query.trim().lowercase()
    val prodResults = DemoData.products.filter {
        q.isEmpty() ||
            it.name.lowercase().contains(q) ||
            it.brand.lowercase().contains(q) ||
            it.tagline.lowercase().contains(q)
    }.take(30)
    val canteenResults = AppStore.cityCanteens().filter {
        q.isEmpty() ||
            it.name.lowercase().contains(q) ||
            it.area.lowercase().contains(q) ||
            it.city.lowercase().contains(q)
    }
    val catResults = DemoData.categories.filter {
        q.isEmpty() || it.name.lowercase().contains(q)
    }.take(8)

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar("Search", onBack = onBack)
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Sp.s5, vertical = Sp.s2)
                .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(Radius.pill))
                .border(BorderStroke(1.dp, O.Line), RoundedCornerShape(Radius.pill))
                .padding(horizontal = Sp.s4, vertical = Sp.s3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IcSearch(size = 16.dp, tint = O.Slate, strokeWidth = 2f)
            Spacer(Modifier.width(Sp.s3))
            BasicTextField(
                state = tfState,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier.weight(1f).focusRequester(focus),
                textStyle = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                cursorBrush = SolidColor(O.Blue)
            )
            if (query.isNotEmpty()) {
                Box(
                    Modifier.size(26.dp).clickable { tfState.setTextAndPlaceCursorAtEnd("") },
                    contentAlignment = Alignment.Center
                ) { IcX(size = 15.dp, tint = O.Slate, strokeWidth = 2.2f) }
            }
        }

        if (q.isEmpty()) {
            // suggestion panels
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 132.dp)) {
                if (s.recent.isNotEmpty()) {
                    SectionHeader("Recent", action = "Clear", onAction = { AppStore.clearRecent() })
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = Sp.s5, vertical = Sp.s2)
                    ) {
                        s.recent.forEach { r ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(Radius.pill))
                                    .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(Radius.pill))
                                    .border(BorderStroke(1.dp, O.Line), RoundedCornerShape(Radius.pill))
                                    .clickable { tfState.setTextAndPlaceCursorAtEnd(r) }
                                    .padding(end = Sp.s2)
                            ) {
                                Text(r, fontSize = 12.5.sp, color = O.Ink,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp))
                            }
                        }
                    }
                    Spacer(Modifier.height(Sp.s2))
                }
                SectionHeader("Popular")
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = Sp.s5, vertical = Sp.s2)
                ) {
                    listOf("rice", "tea", "milk", "biscuits", "soap", "notebook", "juice", "onion").forEach { r ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Radius.pill))
                                .background(O.BlueSoft, RoundedCornerShape(Radius.pill))
                                .clickable { tfState.setTextAndPlaceCursorAtEnd(r) }
                                .padding(end = Sp.s2)
                        ) {
                            Text(r, fontSize = 12.5.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                                color = O.BlueDeep, modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp))
                        }
                    }
                }
                Spacer(Modifier.height(Sp.s2))
                SectionHeader("Categories")
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = Sp.s5, vertical = Sp.s2)
                ) {
                    DemoData.categories.forEach { c ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(Radius.pill))
                                .background(O.surfaceOf(c.accent), RoundedCornerShape(Radius.pill))
                                .clickable { onNav(Nav.category(c.id)) }
                                .padding(end = Sp.s2)
                        ) {
                            Text(c.name, fontSize = 12.5.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                                color = O.deepOf(c.accent),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp))
                        }
                    }
                }
                Spacer(Modifier.height(Sp.s5))
            }
        } else {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = 132.dp)) {
                if (canteenResults.isNotEmpty()) {
                    SectionHeader("Canteens", action = "(${canteenResults.size})")
                    canteenResults.forEach { c ->
                        CanteenCard(c, modifier = Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2),
                            onView = { onNav(Nav.canteen(c.id)) })
                    }
                }
                if (catResults.isNotEmpty()) {
                    SectionHeader("Categories")
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = Sp.s5, vertical = Sp.s2)
                    ) {
                        catResults.forEach { c ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(Radius.pill))
                                    .background(O.surfaceOf(c.accent), RoundedCornerShape(Radius.pill))
                                    .clickable { onNav(Nav.category(c.id)) }
                                    .padding(end = Sp.s2)
                            ) {
                                Text(c.name, fontSize = 12.5.sp,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                                    color = O.deepOf(c.accent),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp))
                            }
                        }
                    }
                }
                if (prodResults.isEmpty() && canteenResults.isEmpty() && catResults.isEmpty()) {
                    EmptyState(
                        title = "No results",
                        body = "We couldn't find anything for “${query}”. Try a different word or category.",
                        glyph = { IcSearch(size = 30.dp, tint = O.Mute) },
                        actionLabel = "See popular",
                        onAction = { tfState.setTextAndPlaceCursorAtEnd("") }
                    )
                } else {
                    SectionHeader("Products", action = "(${prodResults.size})")
                    Column(Modifier.padding(horizontal = Sp.s5, vertical = Sp.s2)) {
                        prodResults.chunked(2).forEach { rowItems ->
                            Row(Modifier.fillMaxWidth().padding(bottom = Sp.s3),
                                horizontalArrangement = Arrangement.spacedBy(Sp.s3),
                                verticalAlignment = Alignment.Top) {
                                rowItems.forEach { p ->
                                    ProductCard(p, modifier = Modifier.weight(1f),
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
}
