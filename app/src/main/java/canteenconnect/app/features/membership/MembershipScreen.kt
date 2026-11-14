package canteenconnect.app.features.membership

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import canteenconnect.app.core.AppStore
import canteenconnect.app.core.components.DemoBadge
import canteenconnect.app.core.components.PrimaryButton
import canteenconnect.app.core.icons.*
import canteenconnect.app.core.theme.Brand
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp

/**
 * Sign-in gate (Canteen / Employee ID, mobile, OTP) — reference-faithful.
 * The auth system is a demo: the identity is the seeded member, OTP is
 * validated locally (any 4 digits), and no data leaves the device.
 * "Continue as guest" keeps the app usable without sign-in.
 */
@Composable
fun MembershipScreen(onContinue: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }   // 0 id, 1 mobile, 2 otp
    var idText by remember { mutableStateOf("") }
    var phoneText by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf(Array(4) { "" }) }
    var otpFocus by remember { mutableIntStateOf(0) }

    Column(
        Modifier
            .fillMaxWidth()
            .background(O.Bg)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(horizontal = Sp.s5)
            .padding(bottom = Sp.s6)
    ) {
        Spacer(Modifier.height(Sp.s6))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(O.BlueSoft),
                contentAlignment = Alignment.Center
            ) { IcShieldCheck(size = 22.dp, tint = O.BlueDeep, strokeWidth = 1.8f) }
            Spacer(Modifier.width(Sp.s3))
            Column(Modifier.weight(1f)) {
                Text("Welcome to ${Brand.NAME}", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                Text("Sign in to shop at registered canteens", fontSize = 12.5.sp, color = O.Slate)
            }
            DemoBadge()
        }
        Spacer(Modifier.height(Sp.s5))

        // member card (seeded identity, demo-labelled)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(O.Card)
                .border(BorderStroke(1.dp, O.LineSoft), RoundedCornerShape(22.dp))
        ) {
            Column(Modifier.padding(Sp.s4)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(44.dp).clip(CircleShape).background(O.BlueSoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(Brand.USER_NAME.take(2), fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.BlueDeep)
                    }
                    Spacer(Modifier.width(Sp.s3))
                    Column(Modifier.weight(1f)) {
                        Text(Brand.USER_NAME, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                        Text("Canteen ID · ${Brand.CANTEEN_ID}", fontSize = 11.5.sp, color = O.Slate)
                    }
                    Box(
                        Modifier.clip(RoundedCornerShape(8.dp)).background(O.SuccessSoft)
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("MEMBER", fontSize = 9.5.sp, fontWeight = FontWeight.ExtraBold, color = O.Success)
                    }
                }
                Spacer(Modifier.height(Sp.s4))
                KRow("Employee ID", Brand.MEMBER_CODE)
                KRow("Canteen", "Sadar Canteen · New Delhi")
                KRow("Access", "Canteen-rate pricing", last = true)
            }
        }
        Spacer(Modifier.height(Sp.s4))

        when (step) {
            0 -> {
                FieldBlock("Canteen / Employee ID") {
                    InputField("CTN45218 or " + Brand.MEMBER_CODE, idText,
                        { idText = it }, { IcSmartphone(Modifier, O.Slate, 18.dp, 2f) })
                }
                Spacer(Modifier.height(Sp.s3))
                PrimaryButton("Continue", modifier = Modifier.fillMaxWidth(), enabled = idText.trim().isNotEmpty()) {
                    step = 1
                }
            }
            1 -> {
                FieldBlock("Mobile number") {
                    InputField("10-digit mobile", phoneText,
                        { v -> if (v.length <= 10) phoneText = v.filter { c -> c.isDigit() } },
                        { IcPhone(Modifier, O.Slate, 18.dp, 2f) }, keyboardType = KeyboardType.Phone)
                }
                Text("OTP will be sent to this number (demo).", fontSize = 11.sp, color = O.Mute,
                    modifier = Modifier.padding(top = Sp.s2, start = 4.dp))
                Spacer(Modifier.height(Sp.s3))
                PrimaryButton("Send OTP", modifier = Modifier.fillMaxWidth(), enabled = phoneText.length == 10) {
                    step = 2
                }
            }
            else -> {
                Text("Enter the 4-digit code", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink)
                Text("Demo: any 4 digits are accepted.", fontSize = 11.5.sp, color = O.Mute,
                    modifier = Modifier.padding(top = 2.dp))
                Spacer(Modifier.height(Sp.s3))
                Row(horizontalArrangement = Arrangement.spacedBy(Sp.s3), modifier = Modifier.fillMaxWidth()) {
                    otp.indices.forEach { i ->
                        OtpBox(i, otp[i], otpFocus == i, Modifier.weight(1f)) { ch ->
                            otp = otp.copyOf()
                            otp[i] = ch
                            if (ch.isNotEmpty() && i < 3) otpFocus = i + 1
                        }
                    }
                }
                Spacer(Modifier.height(Sp.s3))
                PrimaryButton(
                    "Verify & Sign In",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = otp.all { it.isNotEmpty() }
                ) {
                    AppStore.enterAsMember()
                    onContinue()
                }
            }
        }
        Spacer(Modifier.height(Sp.s3))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("New here? ", fontSize = 13.sp, color = O.Slate)
            Text(
                "Continue as guest",
                fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = O.Blue,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        AppStore.enterAsGuest()
                        onContinue()
                    }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
        Spacer(Modifier.height(Sp.s4))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Radius.md))
                .background(O.BlueFaint),
        ) {
            Row(Modifier.padding(Sp.s3), verticalAlignment = Alignment.CenterVertically) {
                IcInfo(size = 18.dp, tint = O.BlueDeep, strokeWidth = 2f)
                Spacer(Modifier.width(Sp.s3))
                Text(
                    "Authorized canteen users only. This demo verifies the seeded member identity locally — no OTP or login is sent to a server and no documents are collected.",
                    fontSize = 11.5.sp, color = O.Slate, lineHeight = 16.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FieldBlock(title: String, content: @Composable () -> Unit) {
    Column {
        Text(title, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = O.Slate)
        Spacer(Modifier.height(Sp.s2))
        content()
    }
}

@Composable
private fun InputField(
    placeholder: String,
    value: String,
    onValue: (String) -> Unit,
    leading: @Composable () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val tfState = rememberTextFieldState(value)
    LaunchedEffect(value) { tfState.setTextAndPlaceCursorAtEnd(value) }
    LaunchedEffect(tfState) {
        snapshotFlow { tfState.text.toString() }.collect { onValue(it) }
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(O.Card)
            .border(BorderStroke(1.dp, O.Line), RoundedCornerShape(14.dp))
            .padding(horizontal = Sp.s3, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leading()
        Spacer(Modifier.width(Sp.s3))
        Box(Modifier.weight(1f)) {
            if (tfState.text.isEmpty()) {
                Text(placeholder, fontSize = 14.5.sp, color = O.Mute)
            }
            BasicTextField(
                state = tfState,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxSize(),
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun OtpBox(index: Int, value: String, focused: Boolean, modifier: Modifier, onChange: (String) -> Unit) {
    val tfState = rememberTextFieldState(value)
    LaunchedEffect(value) { tfState.setTextAndPlaceCursorAtEnd(value) }
    LaunchedEffect(tfState) {
        snapshotFlow { tfState.text.toString() }.collect { t ->
            onChange(t.lastOrNull()?.toString() ?: "")
        }
    }
    Box(
        modifier
            .height(54.dp)
            .clip(RoundedCornerShape(Radius.md))
            .background(if (focused) O.BlueSoft else Color.White, RoundedCornerShape(Radius.md))
            .border(BorderStroke(1.dp, if (focused) O.Blue else O.Line), RoundedCornerShape(Radius.md)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            state = tfState,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxSize(),
            textStyle = MaterialTheme.typography.titleLarge,
            cursorBrush = androidx.compose.ui.graphics.SolidColor(O.Blue)
        )
    }
}

@Composable
private fun KRow(label: String, value: String, last: Boolean = false) {
    Row(
        Modifier.fillMaxWidth().padding(bottom = if (last) 0.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 12.5.sp, color = O.Slate, modifier = Modifier.weight(0.4f))
        Text(value, fontSize = 12.5.sp, fontWeight = FontWeight.ExtraBold, color = O.Ink, modifier = Modifier.weight(0.6f))
    }
}
