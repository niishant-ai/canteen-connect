package canteenconnect.app.features.location

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp
import canteenconnect.app.data.demo.DemoData

/**
 * Manual location selection. "Use current location" is a demo mapping: it
 * selects New Delhi / Vikas Sadan without requesting real GPS.
 */
@Composable
fun LocationScreen(onDone: () -> Unit) {
    val s = AppStore.state()
    var city by remember { mutableStateOf(s.city) }
    var area by remember { mutableStateOf(s.area) }
    var note by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .background(O.Bg)
            .verticalScroll(rememberScrollState())
    ) {
        TopBar("Set Location", subtitle = "Pick your city & area", onBack = onDone)
        Spacer(Modifier.height(Sp.s2))

        // Use current location (demo)
        Row(
            Modifier
                .fillMaxWidth()
                .background(O.BlueSoft, RoundedCornerShape(Radius.md))
                .padding(horizontal = Sp.s5, vertical = Sp.s2)
                .clickable {
                    city = "New Delhi"; area = "Vikas Sadan"
                    note = "Demo: mapped to Vikas Sadan, New Delhi. Real GPS requires location permission — enabled in a future version."
                    AppStore.haptic()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(38.dp).clip(CircleShape).background(O.Card, CircleShape),
                contentAlignment = Alignment.Center
            ) { IcMapPin(size = 18.dp, tint = O.BlueDeep, strokeWidth = 2f) }
            Spacer(Modifier.width(Sp.s3))
            Column(Modifier.weight(1f)) {
                Text("Use current location", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.BlueDeep)
                Text("Approximates your nearest demo area", fontSize = 11.5.sp, color = O.Slate)
            }
        }
        note?.let {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(O.Card, RoundedCornerShape(Radius.md))
                    .border(BorderStroke(1.dp, O.Line), RoundedCornerShape(Radius.md))
                    .padding(horizontal = Sp.s5, vertical = Sp.s2)
            ) {
                Text(
                    it, fontSize = 11.5.sp, color = O.Slate,
                    lineHeight = 15.sp, modifier = Modifier.padding(Sp.s3).weight(1f)
                )
            }
        }

        Spacer(Modifier.height(Sp.s3))
        SectionHeader("City")
        val cities = DemoData.cities
        Column(Modifier.fillMaxWidth().padding(horizontal = Sp.s5, vertical = Sp.s1)) {
            cities.forEachIndexed { i, c ->
                CityRow(c.city == city, c.city) {
                    city = c.city
                    area = c.areas.firstOrNull { it == area } ?: c.areas[0]
                }
                if (i != cities.lastIndex) Spacer(Modifier.height(Sp.s2))
            }
        }

        Spacer(Modifier.height(Sp.s3))
        SectionHeader("Area")
        val areas = DemoData.cities.first { it.city == city }.areas
        Column(Modifier.fillMaxWidth().padding(horizontal = Sp.s5, vertical = Sp.s1)) {
            areas.forEachIndexed { i, a ->
                CityRow(a == area, a) {
                    area = a
                    AppStore.haptic()
                }
                if (i != areas.lastIndex) Spacer(Modifier.height(Sp.s2))
            }
        }

        Spacer(Modifier.height(Sp.s5))
        PrimaryButton("Continue to Home", modifier = Modifier.fillMaxWidth().padding(Sp.s5)) {
            AppStore.setLocation(city, area, AppStore.pinForArea(area, city))
            AppStore.haptic()
            onDone()
        }
        Spacer(Modifier.height(Sp.s3))
    }
}

@Composable
private fun CityRow(selected: Boolean, label: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(if (selected) O.BlueSoft else Color.White, RoundedCornerShape(Radius.md))
            .border(BorderStroke(1.dp, if (selected) O.Blue else O.Line), RoundedCornerShape(Radius.md))
            .clickable(onClick = onClick)
            .padding(horizontal = Sp.s4, vertical = Sp.s3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            IcCheck(size = 15.dp, tint = O.BlueDeep, strokeWidth = 2.6f)
            Spacer(Modifier.width(Sp.s3))
        } else {
            Box(Modifier.size(15.dp))
        }
        Text(label, fontSize = 14.5.sp,
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = if (selected) O.BlueDeep else O.Ink, modifier = Modifier.weight(1f))
        if (selected) Chip("Selected", O.BlueSoft, O.BlueDeep)
    }
}
