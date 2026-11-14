package canteenconnect.app.features.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.DemoBadge
import canteenconnect.app.core.components.GhostButton
import canteenconnect.app.core.components.PrimaryButton
import canteenconnect.app.core.icons.IcArrowRight
import canteenconnect.app.core.icons.IcStore
import canteenconnect.app.core.icons.IcTag
import canteenconnect.app.core.icons.IcUser
import canteenconnect.app.core.theme.Brand
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp

/**
 * AURA onboarding — authorization & verification entry screen.
 * Canteen/Employee ID entry with a live validation cue, an aligned benefit
 * rail and a single charcoal "Verify & Enter Canteen" primary action.
 */
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var canteenId by remember { mutableStateOf(Brand.CANTEEN_ID) }
    var employeeId by remember { mutableStateOf(Brand.MEMBER_CODE) }
    val entered = canteenId.isNotBlank() && employeeId.isNotBlank()

    Box(Modifier.fillMaxSize().background(O.Bg)) {
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = Sp.s5)
            .padding(bottom = Sp.s7)
    ) {
        Spacer(Modifier.height(Sp.s6))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                Brand.NAME,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp,
                color = O.Ink,
                modifier = Modifier.weight(1f)
            )
            AccessChip()
        }
        Spacer(Modifier.height(Sp.s4))

        Text(
            "Your Authorized Canteen Hub",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp,
            color = O.Ink
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Sign in to access registered canteen rates & authorized inventory.",
            fontSize = 13.sp,
            color = O.Slate,
            lineHeight = 18.sp
        )
        Spacer(Modifier.height(Sp.s5))

        // ID panel — one glass card holding both entries
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.lg))
                .background(O.Card, RoundedCornerShape(Radius.lg))
                .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.lg))
        ) {
            Column(Modifier.padding(vertical = 6.dp)) {
                IdRow(
                    label = "Enter Canteen ID",
                    hint = "e.g. ${Brand.CANTEEN_ID}",
                    value = canteenId,
                    onValue = { canteenId = it },
                    showValid = canteenId.isNotBlank(),
                    glyph = { IcUser(Modifier, O.BlueDeep, 22.dp, 1.7f) }
                )
                HorizontalDivider(Modifier.padding(horizontal = 12.dp), thickness = 1.dp, color = O.LineSoft)
                IdRow(
                    label = "Enter Employee ID",
                    hint = "e.g. ${Brand.MEMBER_CODE}",
                    value = employeeId,
                    onValue = { employeeId = it },
                    glyph = { IcTag(Modifier, O.BlueDeep, 22.dp, 1.7f) }
                )
            }
        }
        Spacer(Modifier.height(Sp.s5))

        // benefit rail — aligned icon tiles on one vertical rail
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.lg))
                .background(O.Card, RoundedCornerShape(Radius.lg))
                .border(1.dp, O.LineSoft, RoundedCornerShape(Radius.lg))
                .padding(vertical = 6.dp)
        ) {
            Column(Modifier.fillMaxWidth()) {
                BenefitRow(title = "Canteen Rate Pricing", sub = "Subsidized internal personnel rates") {
                    IcTag(Modifier, O.BlueDeep, 20.dp, 1.8f)
                }
                HorizontalDivider(Modifier.padding(horizontal = 12.dp), thickness = 1.dp, color = O.LineSoft)
                BenefitRow(title = "In-Store Pickup", sub = "Seamless quick-collection counter orders") {
                    IcStore(Modifier, O.BlueDeep, 20.dp, 1.8f)
                }
            }
        }
        Spacer(Modifier.height(Sp.s5))

        PrimaryButton(
            "Verify & Enter Canteen",
            modifier = Modifier.fillMaxWidth(),
            enabled = entered,
            leading = {
                Spacer(Modifier.width(Sp.s2))
                IcArrowRight(size = 16.dp, tint = Color.White, strokeWidth = 2.4f)
            }
        ) {
            AppStore.completeOnboarding()
            onFinish()
        }
        Spacer(Modifier.height(Sp.s3))
        GhostButton("Register Canteen ID", modifier = Modifier.fillMaxWidth()) {
            AppStore.completeOnboarding()
            onFinish()
        }
        Spacer(Modifier.height(Sp.s4))
        Text(
            "Internal personnel use only · Rates verified against canteen registry",
            fontSize = 10.5.sp,
            color = O.Mute,
            textAlign = TextAlign.Center
        )
    }
    DemoBadge(Modifier.align(Alignment.BottomEnd).padding(bottom = 12.dp))
    }
}

/** "Canteen Access" glass status chip with a live green dot. */
@Composable
private fun AccessChip() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(Radius.pill))
            .background(O.BlueSoft, RoundedCornerShape(Radius.pill))
            .border(1.dp, O.Line, RoundedCornerShape(Radius.pill))
            .padding(start = 6.dp, end = 12.dp, top = 6.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(7.dp).clip(CircleShape).background(O.Success))
        Spacer(Modifier.width(6.dp))
        Text("Canteen Access", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = O.BlueDeep)
    }
}

/** 44dp icon tile on the left + editable value + optional VALID cue. */
@Composable
private fun IdRow(
    label: String,
    hint: String,
    value: String,
    onValue: ((String) -> Unit)?,
    showValid: Boolean = false,
    glyph: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(Radius.md))
                .background(O.BlueSoft, RoundedCornerShape(Radius.md))
                .border(1.dp, O.Line, RoundedCornerShape(Radius.md)),
            contentAlignment = Alignment.Center
        ) {
            glyph()
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = O.Mute)
            Spacer(Modifier.height(1.dp))
            if (onValue != null) {
                BasicTextField(
                    value = value,
                    onValueChange = onValue,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        color = if (value.isEmpty()) O.Mute else O.Ink
                    ),
                    cursorBrush = SolidColor(O.Blue),
                    singleLine = true
                )
            } else {
                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = O.Ink)
            }
        }
        if (showValid) {
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(Radius.pill))
                    .background(O.SuccessSoft, RoundedCornerShape(Radius.pill))
                    .border(1.dp, O.Success.copy(alpha = 0.4f), RoundedCornerShape(Radius.pill))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text("VALID ✓", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = O.Success)
            }
        }
    }
}

@Composable
private fun BenefitRow(title: String, sub: String, glyph: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(Radius.md))
                .background(O.BlueSoft, RoundedCornerShape(Radius.md))
                .border(1.dp, O.Line, RoundedCornerShape(Radius.md)),
            contentAlignment = Alignment.Center
        ) {
            glyph()
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 13.5.sp, fontWeight = FontWeight.Bold, color = O.Ink)
            Spacer(Modifier.height(2.dp))
            Text(sub, fontSize = 11.5.sp, color = O.Slate)
        }
    }
}
