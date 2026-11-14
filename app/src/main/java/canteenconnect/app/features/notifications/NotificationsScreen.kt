package canteenconnect.app.features.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import canteenconnect.app.core.theme.O
import canteenconnect.app.core.theme.Radius
import canteenconnect.app.core.theme.Sp

@Composable
fun NotificationsScreen(onBack: () -> Unit) {
    val s = AppStore.state()
    val unread = s.notifications.count { !it.read }

    Column(Modifier.fillMaxSize().background(O.Bg)) {
        TopBar(
            "Notifications",
            subtitle = if (unread == 0) "All caught up" else "$unread unread",
            onBack = onBack
        ) {
            Text(
                "Mark all read",
                fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = O.Blue,
                modifier = Modifier
                    .clickable {
                        AppStore.markAllRead(); AppStore.haptic()
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(bottom = Sp.s5)) {
            s.notifications.forEach { n ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(if (n.read) Color.White else O.BlueFaint, RoundedCornerShape(Radius.md))
                        .clickable { AppStore.markRead(n.id) }
                        .padding(horizontal = Sp.s5)
                        .padding(vertical = Sp.s3),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NotifGlyph(n, unread)
                    Spacer(Modifier.width(Sp.s3))
                    Column(Modifier.weight(1f)) {
                        Text(n.title, fontSize = 13.5.sp,
                            fontWeight = if (n.read) FontWeight.SemiBold else FontWeight.ExtraBold,
                            color = O.Ink,
                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(n.body, fontSize = 12.sp, color = O.Slate,
                            maxLines = 2, overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 1.dp))
                        Text(relTime(n.ts), fontSize = 10.5.sp, color = O.Mute,
                            modifier = Modifier.padding(top = 2.dp))
                    }
                    if (!n.read) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(O.Blue, CircleShape))
                    }
                }
            }
            Spacer(Modifier.height(Sp.s3))
        }
    }
}

/** A notification tile driven by its content (offer, order, canteen, tip). */
@Composable
private fun NotifGlyph(n: canteenconnect.app.data.models.DemoNotification, unread: Int) {
    val hot = !n.read
    Box(
        Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (hot) O.BlueSoft else O.BlueFaint, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        when (n.id) {
            "n1" -> IcTruck(size = 20.dp, tint = if (hot) O.BlueDeep else O.Slate, strokeWidth = 2f)
            "n2" -> IcStore(size = 20.dp, tint = if (hot) O.BlueDeep else O.Slate, strokeWidth = 2f)
            "n3" -> IcBox(size = 20.dp, tint = if (hot) O.BlueDeep else O.Slate, strokeWidth = 2f)
            "n4" -> IcGift(size = 20.dp, tint = if (hot) O.BlueDeep else O.Slate, strokeWidth = 2f)
            else -> IcInfo(size = 20.dp, tint = if (hot) O.BlueDeep else O.Slate, strokeWidth = 2f)
        }
    }
}
