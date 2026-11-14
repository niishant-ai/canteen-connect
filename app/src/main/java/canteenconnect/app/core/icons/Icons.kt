package canteenconnect.app.core.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Opshar icon set: Lucide-derived outline icons drawn as stroke paths.
// 24-unit grid, rounded caps/joins, one consistent style across the app.

internal data class IconData(val name: String, val pathData: List<String>)

@Composable
internal fun opsharIcon(
    data: IconData,
    modifier: Modifier = Modifier,
    tint: Color = Color(0xFF24203A),
    iconSize: Dp = 24.dp,
    strokeWidth: Float = 2f,
) {
    val paths = remember(data) {
        data.pathData.map { d ->
            Path().apply { PathParser().apply { parsePathString(d) }.toPath(this) }
        }
    }
    Canvas(modifier = Modifier.size(iconSize).then(modifier)) {
        val s = (size.width / 24f).coerceAtLeast(0f)
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.scale(s, s)
            val paint = Paint().apply {
                style = PaintingStyle.Stroke
                color = tint
                this.strokeWidth = strokeWidth
                strokeCap = StrokeCap.Round
                strokeJoin = StrokeJoin.Round
                isAntiAlias = true
            }
            paths.forEach { canvas.drawPath(it, paint) }
            canvas.restore()
        }
    }
}

internal val IcHome_DATA = IconData("Home", listOf("M15 21v-8a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v8", "M3 10a2 2 0 0 1 .709-1.528l7-6a2 2 0 0 1 2.582 0l7 6A2 2 0 0 1 21 10v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"))
internal val IcCompass_DATA = IconData("Compass", listOf("M 2 12 a 10 10 0 1 0 20 0 a 10 10 0 1 0 -20 0", "m16.24 7.76-1.804 5.411a2 2 0 0 1-1.265 1.265L7.76 16.24l1.804-5.411a2 2 0 0 1 1.265-1.265z"))
internal val IcLayers_DATA = IconData("Layers", listOf("M12.83 2.18a2 2 0 0 0-1.66 0L2.6 6.08a1 1 0 0 0 0 1.83l8.58 3.91a2 2 0 0 0 1.66 0l8.58-3.9a1 1 0 0 0 0-1.83z", "M2 12a1 1 0 0 0 .58.91l8.6 3.91a2 2 0 0 0 1.65 0l8.58-3.9A1 1 0 0 0 22 12", "M2 17a1 1 0 0 0 .58.91l8.6 3.91a2 2 0 0 0 1.65 0l8.58-3.9A1 1 0 0 0 22 17"))
internal val IcCart_DATA = IconData("Cart", listOf("m2.05 2.05 1.099-.028a1 1 0 0 1 1.008.815l2.69 14.347A1 1 0 0 0 7.83 18H18", "M4.563 5h16.435a1 1 0 0 1 .981 1.204l-1.026 6.226A2 2 0 0 1 18.962 14H6.25", "M 16 20 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0", "M 6 20 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0"))
internal val IcBag_DATA = IconData("Bag", listOf("M16 10a4 4 0 0 1-8 0", "M3.103 6.034h17.794", "M3.4 5.467a2 2 0 0 0-.4 1.2V20a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6.667a2 2 0 0 0-.4-1.2l-2-2.667A2 2 0 0 0 17 2H7a2 2 0 0 0-1.6.8z"))
internal val IcSearch_DATA = IconData("Search", listOf("m21 21-4.34-4.34", "M 3 11 a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0"))
internal val IcSliders_DATA = IconData("Sliders", listOf("M10 5H3", "M12 19H3", "M14 3v4", "M16 17v4", "M21 12h-9", "M21 19h-5", "M21 5h-7", "M8 10v4", "M8 12H3"))
internal val IcBell_DATA = IconData("Bell", listOf("M10.268 21a2 2 0 0 0 3.464 0", "M3.262 15.326A1 1 0 0 0 4 17h16a1 1 0 0 0 .74-1.673C19.41 13.956 18 12.499 18 8A6 6 0 0 0 6 8c0 4.499-1.411 5.956-2.738 7.326"))
internal val IcChevronDown_DATA = IconData("ChevronDown", listOf("m6 9 6 6 6-6"))
internal val IcChevronRight_DATA = IconData("ChevronRight", listOf("m9 18 6-6-6-6"))
internal val IcChevronLeft_DATA = IconData("ChevronLeft", listOf("m15 18-6-6 6-6"))
internal val IcX_DATA = IconData("X", listOf("M18 6 6 18", "m6 6 12 12"))
internal val IcUser_DATA = IconData("User", listOf("M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2", "M 8 7 a 4 4 0 1 0 8 0 a 4 4 0 1 0 -8 0"))
internal val IcSettings_DATA = IconData("Settings", listOf("M9.671 4.136a2.34 2.34 0 0 1 4.659 0 2.34 2.34 0 0 0 3.319 1.915 2.34 2.34 0 0 1 2.33 4.033 2.34 2.34 0 0 0 0 3.831 2.34 2.34 0 0 1-2.33 4.033 2.34 2.34 0 0 0-3.319 1.915 2.34 2.34 0 0 1-4.659 0 2.34 2.34 0 0 0-3.32-1.915 2.34 2.34 0 0 1-2.33-4.033 2.34 2.34 0 0 0 0-3.831A2.34 2.34 0 0 1 6.35 6.051a2.34 2.34 0 0 0 3.319-1.915", "M 9 12 a 3 3 0 1 0 6 0 a 3 3 0 1 0 -6 0"))
internal val IcMapPin_DATA = IconData("MapPin", listOf("M20 10c0 4.993-5.539 10.193-7.399 11.799a1 1 0 0 1-1.202 0C9.539 20.193 4 14.993 4 10a8 8 0 0 1 16 0", "M 9 10 a 3 3 0 1 0 6 0 a 3 3 0 1 0 -6 0"))
internal val IcHeart_DATA = IconData("Heart", listOf("M2 9.5a5.5 5.5 0 0 1 9.591-3.676.56.56 0 0 0 .818 0A5.49 5.49 0 0 1 22 9.5c0 2.29-1.5 4-3 5.5l-5.492 5.313a2 2 0 0 1-3 .019L5 15c-1.5-1.5-3-3.2-3-5.5"))
internal val IcBox_DATA = IconData("Box", listOf("M21 8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16Z", "m3.3 7 8.7 5 8.7-5", "M12 22V12"))
internal val IcTruck_DATA = IconData("Truck", listOf("M14 18V6a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2v11a1 1 0 0 0 1 1h2", "M15 18H9", "M19 18h2a1 1 0 0 0 1-1v-3.65a1 1 0 0 0-.22-.624l-3.48-4.35A1 1 0 0 0 17.52 8H14", "M 15 18 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0", "M 5 18 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0"))
internal val IcBadgePercent_DATA = IconData("BadgePercent", listOf("M3.85 8.62a4 4 0 0 1 4.78-4.77 4 4 0 0 1 6.74 0 4 4 0 0 1 4.78 4.78 4 4 0 0 1 0 6.74 4 4 0 0 1-4.77 4.78 4 4 0 0 1-6.75 0 4 4 0 0 1-4.78-4.77 4 4 0 0 1 0-6.76Z", "m15 9-6 6", "M9 9h.01", "M15 15h.01"))
internal val IcCreditCard_DATA = IconData("CreditCard", listOf("M 4 5 L 20 5 Q 22 5 22 7 L 22 17 Q 22 19 20 19 L 4 19 Q 2 19 2 17 L 2 7 Q 2 5 4 5 Z", "M 0 0 L 0 0", "M6 14h2"))
internal val IcLogout_DATA = IconData("Logout", listOf("m16 17 5-5-5-5", "M21 12H9", "M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"))
internal val IcShieldCheck_DATA = IconData("ShieldCheck", listOf("M20 13c0 5-3.5 7.5-7.66 8.95a1 1 0 0 1-.67-.01C7.5 20.5 4 18 4 13V6a1 1 0 0 1 1-1c2 0 4.5-1.2 6.24-2.72a1.17 1.17 0 0 1 1.52 0C14.51 3.81 17 5 19 5a1 1 0 0 1 1 1z", "m9 12 2 2 4-4"))
internal val IcStar_DATA = IconData("Star", listOf("M11.525 2.295a.53.53 0 0 1 .95 0l2.31 4.679a2.123 2.123 0 0 0 1.595 1.16l5.166.756a.53.53 0 0 1 .294.904l-3.736 3.638a2.123 2.123 0 0 0-.611 1.878l.882 5.14a.53.53 0 0 1-.771.56l-4.618-2.428a2.122 2.122 0 0 0-1.973 0L6.396 21.01a.53.53 0 0 1-.77-.56l.881-5.139a2.122 2.122 0 0 0-.611-1.879L2.16 9.795a.53.53 0 0 1 .294-.906l5.165-.755a2.122 2.122 0 0 0 1.597-1.16z"))
internal val IcPlus_DATA = IconData("Plus", listOf("M5 12h14", "M12 5v14"))
internal val IcMinus_DATA = IconData("Minus", listOf("M5 12h14"))
internal val IcCheck_DATA = IconData("Check", listOf("M20 6 9 17l-5-5"))
internal val IcCopy_DATA = IconData("Copy", listOf("M 10 8 L 20 8 Q 22 8 22 10 L 22 20 Q 22 22 20 22 L 10 22 Q 8 22 8 20 L 8 10 Q 8 8 10 8 Z", "M4 16c-1.1 0-2-.9-2-2V4c0-1.1.9-2 2-2h10c1.1 0 2 .9 2 2"))
internal val IcArrowRight_DATA = IconData("ArrowRight", listOf("M5 12h14", "m12 5 7 7-7 7"))
internal val IcClock_DATA = IconData("Clock", listOf("M 2 12 a 10 10 0 1 0 20 0 a 10 10 0 1 0 -20 0", "M12 6v6l4 2"))
internal val IcPackage_DATA = IconData("Package", listOf("M11 21.73a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73z", "M12 22V12", "M 3.29 7 L 12 12 L 20.71 7", "m7.5 4.27 9 5.15"))
internal val IcInfo_DATA = IconData("Info", listOf("M 2 12 a 10 10 0 1 0 20 0 a 10 10 0 1 0 -20 0", "M12 16v-4", "M12 8h.01"))
internal val IcPhone_DATA = IconData("Phone", listOf("M13.832 16.568a1 1 0 0 0 1.213-.303l.355-.465A2 2 0 0 1 17 15h3a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2A18 18 0 0 1 2 4a2 2 0 0 1 2-2h3a2 2 0 0 1 2 2v3a2 2 0 0 1-.8 1.6l-.468.351a1 1 0 0 0-.292 1.233 14 14 0 0 0 6.392 6.384"))
internal val IcLock_DATA = IconData("Lock", listOf("M 5 11 L 19 11 Q 21 11 21 13 L 21 20 Q 21 22 19 22 L 5 22 Q 3 22 3 20 L 3 13 Q 3 11 5 11 Z", "M7 11V7a5 5 0 0 1 10 0v4"))
internal val IcSmartphone_DATA = IconData("Smartphone", listOf("M 7 2 L 17 2 Q 19 2 19 4 L 19 20 Q 19 22 17 22 L 7 22 Q 5 22 5 20 L 5 4 Q 5 2 7 2 Z", "M12 18h.01"))
internal val IcCalendar_DATA = IconData("Calendar", listOf("M8 2v3", "M16 2v3", "M 5 3 L 19 3 Q 21 3 21 5 L 21 19 Q 21 21 19 21 L 5 21 Q 3 21 3 19 L 3 5 Q 3 3 5 3 Z", "M3 9h18"))
internal val IcFlame_DATA = IconData("Flame", listOf("M12 3q1 4 4 6.5t3 5.5a1 1 0 0 1-14 0 5 5 0 0 1 1-3 1 1 0 0 0 5 0c0-2-1.5-3-1.5-5q0-2 2.5-4"))
internal val IcSparkles_DATA = IconData("Sparkles", listOf("M11.017 2.814a1 1 0 0 1 1.966 0l1.051 5.558a2 2 0 0 0 1.594 1.594l5.558 1.051a1 1 0 0 1 0 1.966l-5.558 1.051a2 2 0 0 0-1.594 1.594l-1.051 5.558a1 1 0 0 1-1.966 0l-1.051-5.558a2 2 0 0 0-1.594-1.594l-5.558-1.051a1 1 0 0 1 0-1.966l5.558-1.051a2 2 0 0 0 1.594-1.594z", "M20 2v4", "M22 4h-4", "M 2 20 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0"))
internal val IcZap_DATA = IconData("Zap", listOf("M15.914 4a1.5 1.5 0 00-2.474-1.561l-9 9A1.5 1.5 0 005.5 14h4.002a.5.5 0 01.471.666L8.086 20a1.5 1.5 0 002.475 1.56l9-9A1.5 1.5 0 0018.5 10h-3.997a.5.5 0 01-.472-.667z"))
internal val IcTag_DATA = IconData("Tag", listOf("M12.586 2.586A2 2 0 0 0 11.172 2H4a2 2 0 0 0-2 2v7.172a2 2 0 0 0 .586 1.414l8.704 8.704a2.426 2.426 0 0 0 3.42 0l6.58-6.58a2.426 2.426 0 0 0 0-3.42z", "M 7 7.5 a 0.5 0.5 0 1 0 1 0 a 0.5 0.5 0 1 0 -1 0"))
internal val IcPercent_DATA = IconData("Percent", listOf("M 0 0 L 0 0", "M 4 6.5 a 2.5 2.5 0 1 0 5 0 a 2.5 2.5 0 1 0 -5 0", "M 15 17.5 a 2.5 2.5 0 1 0 5 0 a 2.5 2.5 0 1 0 -5 0"))
internal val IcList_DATA = IconData("List", listOf("M3 5h.01", "M3 12h.01", "M3 19h.01", "M8 5h13", "M8 12h13", "M8 19h13"))
internal val IcMenu_DATA = IconData("Menu", listOf("M4 5h16", "M4 12h16", "M4 19h16"))
internal val IcGrid_DATA = IconData("Grid", listOf("M 4 3 L 9 3 Q 10 3 10 4 L 10 9 Q 10 10 9 10 L 4 10 Q 3 10 3 9 L 3 4 Q 3 3 4 3 Z", "M 15 3 L 20 3 Q 21 3 21 4 L 21 9 Q 21 10 20 10 L 15 10 Q 14 10 14 9 L 14 4 Q 14 3 15 3 Z", "M 15 14 L 20 14 Q 21 14 21 15 L 21 20 Q 21 21 20 21 L 15 21 Q 14 21 14 20 L 14 15 Q 14 14 15 14 Z", "M 4 14 L 9 14 Q 10 14 10 15 L 10 20 Q 10 21 9 21 L 4 21 Q 3 21 3 20 L 3 15 Q 3 14 4 14 Z"))
internal val IcRotateCcw_DATA = IconData("RotateCcw", listOf("M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8", "M3 3v5h5"))
internal val IcWallet_DATA = IconData("Wallet", listOf("M19 7V4a1 1 0 0 0-1-1H5a2 2 0 0 0 0 4h15a1 1 0 0 1 1 1v4h-3a2 2 0 0 0 0 4h3a1 1 0 0 0 1-1v-2a1 1 0 0 0-1-1", "M3 5v14a2 2 0 0 0 2 2h15a1 1 0 0 0 1-1v-4"))
internal val IcUtensils_DATA = IconData("Utensils", listOf("M3 2v7c0 1.1.9 2 2 2h4a2 2 0 0 0 2-2V2", "M7 2v20", "M21 15V2a5 5 0 0 0-5 5v6c0 1.1.9 2 2 2h3Zm0 0v7"))
internal val IcBasket_DATA = IconData("Basket", listOf("m15 11-1 9", "m19 11-4-7", "M2 11h20", "m3.5 11 1.6 7.4a2 2 0 0 0 2 1.6h9.8a2 2 0 0 0 2-1.6l1.7-7.4", "M4.5 15.5h15", "m5 11 4-7", "m9 11 1 9"))
internal val IcStore_DATA = IconData("Store", listOf("M15 21v-5a1 1 0 0 0-1-1h-4a1 1 0 0 0-1 1v5", "M17.774 10.31a1.12 1.12 0 0 0-1.549 0 2.5 2.5 0 0 1-3.451 0 1.12 1.12 0 0 0-1.548 0 2.5 2.5 0 0 1-3.452 0 1.12 1.12 0 0 0-1.549 0 2.5 2.5 0 0 1-3.77-3.248l2.889-4.184A2 2 0 0 1 7 2h10a2 2 0 0 1 1.653.873l2.895 4.192a2.5 2.5 0 0 1-3.774 3.244", "M4 10.95V19a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2v-8.05"))
internal val IcBadgeCheck_DATA = IconData("BadgeCheck", listOf("M3.85 8.62a4 4 0 0 1 4.78-4.77 4 4 0 0 1 6.74 0 4 4 0 0 1 4.78 4.78 4 4 0 0 1 0 6.74 4 4 0 0 1-4.77 4.78 4 4 0 0 1-6.75 0 4 4 0 0 1-4.78-4.77 4 4 0 0 1 0-6.76Z", "m16 9-5.5 5.5L8 12"))
internal val IcReceipt_DATA = IconData("Receipt", listOf("M12 17V7", "M16 8h-6a2 2 0 0 0 0 4h4a2 2 0 0 1 0 4H8", "M4 3a1 1 0 0 1 1-1 1.3 1.3 0 0 1 .7.2l.933.6a1.3 1.3 0 0 0 1.4 0l.934-.6a1.3 1.3 0 0 1 1.4 0l.933.6a1.3 1.3 0 0 0 1.4 0l.933-.6a1.3 1.3 0 0 1 1.4 0l.934.6a1.3 1.3 0 0 0 1.4 0l.933-.6A1.3 1.3 0 0 1 19 2a1 1 0 0 1 1 1v18a1 1 0 0 1-1 1 1.3 1.3 0 0 1-.7-.2l-.933-.6a1.3 1.3 0 0 0-1.4 0l-.934.6a1.3 1.3 0 0 1-1.4 0l-.933-.6a1.3 1.3 0 0 0-1.4 0l-.933.6a1.3 1.3 0 0 1-1.4 0l-.934-.6a1.3 1.3 0 0 0-1.4 0l-.933.6a1.3 1.3 0 0 1-.7.2 1 1 0 0 1-1-1z"))
internal val IcPackageCheck_DATA = IconData("PackageCheck", listOf("M12 22V12", "m16 17 2 2 4-4", "M21 11.127V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.729l7 4a2 2 0 0 0 2 .001l1.32-.753", "M3.29 7 12 12l8.71-5", "m7.5 4.27 8.997 5.148"))
internal val IcTimer_DATA = IconData("Timer", listOf("M 0 0 L 0 0", "M 0 0 L 0 0", "M 4 14 a 8 8 0 1 0 16 0 a 8 8 0 1 0 -16 0"))
internal val IcCar_DATA = IconData("Car", listOf("M19 17h2c.6 0 1-.4 1-1v-3c0-.9-.7-1.7-1.5-1.9C18.7 10.6 16 10 16 10s-1.3-1.4-2.2-2.3c-.5-.4-1.1-.7-1.8-.7H5c-.6 0-1.1.4-1.4.9l-1.4 2.9A3.7 3.7 0 0 0 2 12v4c0 .6.4 1 1 1h2", "M 5 17 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0", "M9 17h6", "M 15 17 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0"))
internal val IcGift_DATA = IconData("Gift", listOf("M12 7v14", "M20 11v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2v-8", "M7.5 7a1 1 0 0 1 0-5A4.8 8 0 0 1 12 7a4.8 8 0 0 1 4.5-5 1 1 0 0 1 0 5", "M 4 7 L 20 7 Q 21 7 21 8 L 21 10 Q 21 11 20 11 L 4 11 Q 3 11 3 10 L 3 8 Q 3 7 4 7 Z"))
internal val IcMessage_DATA = IconData("Message", listOf("M2.992 16.342a2 2 0 0 1 .094 1.167l-1.065 3.29a1 1 0 0 0 1.236 1.168l3.413-.998a2 2 0 0 1 1.099.092 10 10 0 1 0-4.777-4.719"))
internal val IcThumb_DATA = IconData("Thumb", listOf("M15 5.88 14 10h5.83a2 2 0 0 1 1.92 2.56l-2.33 8A2 2 0 0 1 17.5 22H4a2 2 0 0 1-2-2v-8a2 2 0 0 1 2-2h2.76a2 2 0 0 0 1.79-1.11L12 2a3.13 3.13 0 0 1 3 3.88Z", "M7 10v12"))
internal val IcTrash_DATA = IconData("Trash", listOf("M10 11v6", "M14 11v6", "M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6", "M3 6h18", "M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"))
internal val IcScale_DATA = IconData("Scale", listOf("M12 3v18", "m19 8 3 8a5 5 0 0 1-6 0zV7", "M3 7h1a17 17 0 0 0 8-2 17 17 0 0 0 8 2h1", "m5 8 3 8a5 5 0 0 1-6 0zV7", "M7 21h10"))
internal val IcCheckCircle_DATA = IconData("CheckCircle", listOf("M21.801 10A10 10 0 1 1 17 3.335", "m9 11 3 3L22 4"))
internal val IcCircleX_DATA = IconData("CircleX", listOf("M 2 12 a 10 10 0 1 0 20 0 a 10 10 0 1 0 -20 0", "m15 9-6 6", "m9 9 6 6"))
internal val IcCirclePercent_DATA = IconData("CirclePercent", listOf("M 2 12 a 10 10 0 1 0 20 0 a 10 10 0 1 0 -20 0", "m15 9-6 6", "M9 9h.01", "M15 15h.01"))
internal val IcLifeBuoy_DATA = IconData("LifeBuoy", listOf("M 2 12 a 10 10 0 1 0 20 0 a 10 10 0 1 0 -20 0", "m4.93 4.93 4.24 4.24", "m14.83 9.17 4.24-4.24", "m14.83 14.83 4.24 4.24", "m9.17 14.83-4.24 4.24", "M 8 12 a 4 4 0 1 0 8 0 a 4 4 0 1 0 -8 0"))
internal val IcHeadphones_DATA = IconData("Headphones", listOf("M3 14h3a2 2 0 0 1 2 2v3a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-7a9 9 0 0 1 18 0v7a2 2 0 0 1-2 2h-1a2 2 0 0 1-2-2v-3a2 2 0 0 1 2-2h3"))
internal val IcSoda_DATA = IconData("Soda", listOf("m6 8 1.75 12.28a2 2 0 0 0 2 1.72h4.54a2 2 0 0 0 2-1.72L18 8", "M5 8h14", "M7 15a6.47 6.47 0 0 1 5 0 6.47 6.47 0 0 0 5 0", "m12 8 1-6h2"))
internal val IcApple_DATA = IconData("Apple", listOf("M12 6.528V3a1 1 0 0 1 1-1h0", "M18.237 21A15 15 0 0 0 22 11a6 6 0 0 0-10-4.472A6 6 0 0 0 2 11a15.1 15.1 0 0 0 3.763 10 3 3 0 0 0 3.648.648 5.5 5.5 0 0 1 5.178 0A3 3 0 0 0 18.237 21"))
internal val IcCookie_DATA = IconData("Cookie", listOf("M11 17h.01", "M11.496 2c.324-.016.558.292.529.615a4 4 0 004.235 4.368.713.713 0 01.758.757 4 4 0 004.366 4.237c.323-.03.63.204.614.527a10 10 0 01-2.915 6.566A1 1 0 114.93 4.918 10 10 0 0111.496 2", "M12 12h.01", "M16 16h.01", "M16 3h.01", "M21 4h.01", "M21 8h.01", "M7 14h.01", "M9 8h.01"))
internal val IcMilk_DATA = IconData("Milk", listOf("M8 2h8", "M9 2v2.789a4 4 0 0 1-.672 2.219l-.656.984A4 4 0 0 0 7 10.212V20a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2v-9.789a4 4 0 0 0-.672-2.219l-.656-.984A4 4 0 0 1 15 4.788V2", "M7 15a6.472 6.472 0 0 1 5 0 6.47 6.47 0 0 0 5 0"))
internal val IcCarrot_DATA = IconData("Carrot", listOf("M15 16a1 1 0 0 0-7-7q-4 4-5.987 12.385a.5.5 0 0 0 .602.602Q11 20 15 16l-3-3", "M15 9q4 4 7 0-3-4-7 0 4-4 0-7-4 3 0 7", "m8 15-2.58-2.58"))
internal val IcDroplets_DATA = IconData("Droplets", listOf("M7 16.3c2.2 0 4-1.83 4-4.05 0-1.16-.57-2.26-1.71-3.19S7.29 6.75 7 5.3c-.29 1.45-1.14 2.84-2.29 3.76S3 11.1 3 12.25c0 2.22 1.8 4.05 4 4.05z", "M12.56 6.6A10.97 10.97 0 0 0 14 3.02c.5 2.5 2 4.9 4 6.5s3 3.5 3 5.5a6.98 6.98 0 0 1-11.91 4.97"))
internal val IcPencil_DATA = IconData("Pencil", listOf("M21.174 6.812a1 1 0 0 0-3.986-3.987L3.842 16.174a2 2 0 0 0-.5.83l-1.321 4.352a.5.5 0 0 0 .623.622l4.353-1.32a2 2 0 0 0 .83-.497z", "m15 5 4 4"))
internal val IcCpu_DATA = IconData("Cpu", listOf("M12 20v2", "M12 2v2", "M17 20v2", "M17 2v2", "M2 12h2", "M2 17h2", "M2 7h2", "M20 12h2", "M20 17h2", "M20 7h2", "M7 20v2", "M7 2v2", "M 6 4 L 18 4 Q 20 4 20 6 L 20 18 Q 20 20 18 20 L 6 20 Q 4 20 4 18 L 4 6 Q 4 4 6 4 Z", "M 9 8 L 15 8 Q 16 8 16 9 L 16 15 Q 16 16 15 16 L 9 16 Q 8 16 8 15 L 8 9 Q 8 8 9 8 Z"))
internal val IcSprout_DATA = IconData("Sprout", listOf("M14 9.536V7a4 4 0 0 1 4-4h1.5a.5.5 0 0 1 .5.5V5a4 4 0 0 1-4 4 4 4 0 0 0-4 4c0 2 1 3 1 5a5 5 0 0 1-1 3", "M4 9a5 5 0 0 1 8 4 5 5 0 0 1-8-4", "M5 21h14"))
internal val IcLeaf_DATA = IconData("Leaf", listOf("M11 20a10 10 0 0010-10 25.9 25.9 0 00-1.04-7.281 1 1 0 00-1.755-.325C15.833 5.5 13 5.5 9.8 6.1A7 7 0 0011 20", "M2 21a5 5 0 012.911-4.544C7.613 15.212 8.351 15.24 11 13"))
internal val IcCoffee_DATA = IconData("Coffee", listOf("M10 2v2", "M14 2v2", "M16 8a1 1 0 0 1 1 1v8a4 4 0 0 1-4 4H7a4 4 0 0 1-4-4V9a1 1 0 0 1 1-1h14a4 4 0 1 1 0 8h-1", "M6 2v2"))
internal val IcWheat_DATA = IconData("Wheat", listOf("M2 22 16 8", "M3.47 12.53 5 11l1.53 1.53a3.5 3.5 0 0 1 0 4.94L5 19l-1.53-1.53a3.5 3.5 0 0 1 0-4.94Z", "M7.47 8.53 9 7l1.53 1.53a3.5 3.5 0 0 1 0 4.94L9 15l-1.53-1.53a3.5 3.5 0 0 1 0-4.94Z", "M11.47 4.53 13 3l1.53 1.53a3.5 3.5 0 0 1 0 4.94L13 11l-1.53-1.53a3.5 3.5 0 0 1 0-4.94Z", "M20 2h2v2a4 4 0 0 1-4 4h-2V6a4 4 0 0 1 4-4Z", "M11.47 17.47 13 19l-1.53 1.53a3.5 3.5 0 0 1-4.94 0L5 19l1.53-1.53a3.5 3.5 0 0 1 4.94 0Z", "M15.47 13.47 17 15l-1.53 1.53a3.5 3.5 0 0 1-4.94 0L9 15l1.53-1.53a3.5 3.5 0 0 1 4.94 0Z", "M19.47 9.47 21 11l-1.53 1.53a3.5 3.5 0 0 1-4.94 0L13 11l1.53-1.53a3.5 3.5 0 0 1 4.94 0Z"))
internal val IcQrCode_DATA = IconData("QrCode", listOf("M 4 3 L 7 3 Q 8 3 8 4 L 8 7 Q 8 8 7 8 L 4 8 Q 3 8 3 7 L 3 4 Q 3 3 4 3 Z", "M 17 3 L 20 3 Q 21 3 21 4 L 21 7 Q 21 8 20 8 L 17 8 Q 16 8 16 7 L 16 4 Q 16 3 17 3 Z", "M 4 16 L 7 16 Q 8 16 8 17 L 8 20 Q 8 21 7 21 L 4 21 Q 3 21 3 20 L 3 17 Q 3 16 4 16 Z", "M21 16h-3a2 2 0 0 0-2 2v3", "M21 21v.01", "M12 7v3a2 2 0 0 1-2 2H7", "M3 12h.01", "M12 3h.01", "M12 16v.01", "M16 12h1", "M21 12v.01", "M12 21v-1"))
internal val IcRupee_DATA = IconData("Rupee", listOf("m14 20-8-8h4c5.333 0 5.333-8 0-8", "M6 4h12", "M6 8h12"))
internal val IcBanknote_DATA = IconData("Banknote", listOf("M 4 6 L 20 6 Q 22 6 22 8 L 22 16 Q 22 18 20 18 L 4 18 Q 2 18 2 16 L 2 8 Q 2 6 4 6 Z", "M 10 12 a 2 2 0 1 0 4 0 a 2 2 0 1 0 -4 0", "M6 12h.01M18 12h.01"))
internal val IcInbox_DATA = IconData("Inbox", listOf("M 22 12 L 16 12 L 14 15 L 10 15 L 8 12 L 2 12", "M5.45 5.11 2 12v6a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2v-6l-3.45-6.89A2 2 0 0 0 16.76 4H7.24a2 2 0 0 0-1.79 1.11z"))
internal val IcKeyRound_DATA = IconData("KeyRound", listOf("M2.586 17.414A2 2 0 0 0 2 18.828V21a1 1 0 0 0 1 1h3a1 1 0 0 0 1-1v-1a1 1 0 0 1 1-1h1a1 1 0 0 0 1-1v-1a1 1 0 0 1 1-1h.172a2 2 0 0 0 1.414-.586l.814-.814a6.5 6.5 0 1 0-4-4z", "M 16 7.5 a 0.5 0.5 0 1 0 1 0 a 0.5 0.5 0 1 0 -1 0"))
internal val IcWeight_DATA = IconData("Weight", listOf("M 9 5 a 3 3 0 1 0 6 0 a 3 3 0 1 0 -6 0", "M6.5 8a2 2 0 0 0-1.905 1.46L2.1 18.5A2 2 0 0 0 4 21h16a2 2 0 0 0 1.925-2.54L19.4 9.5A2 2 0 0 0 17.48 8Z"))
internal val IcMap_DATA = IconData("Map", listOf("M14.106 5.553a2 2 0 0 0 1.788 0l3.659-1.83A1 1 0 0 1 21 4.619v12.764a1 1 0 0 1-.553.894l-4.553 2.277a2 2 0 0 1-1.788 0l-4.212-2.106a2 2 0 0 0-1.788 0l-3.659 1.83A1 1 0 0 1 3 19.381V6.618a1 1 0 0 1 .553-.894l4.553-2.277a2 2 0 0 1 1.788 0z", "M15 5.764v15", "M9 3.236v15"))
internal val IcBean_DATA = IconData("Bean", listOf("M10.165 6.598C9.954 7.478 9.64 8.36 9 9c-.64.64-1.521.954-2.402 1.165A6 6 0 0 0 8 22c7.732 0 14-6.268 14-14a6 6 0 0 0-11.835-1.402Z", "M5.341 10.62a4 4 0 1 0 5.279-5.28"))
internal val IcCake_DATA = IconData("Cake", listOf("M20 21v-8a2 2 0 0 0-2-2H6a2 2 0 0 0-2 2v8", "M4 16s.5-1 2-1 2.5 2 4 2 2.5-2 4-2 2.5 2 4 2 2-1 2-1", "M2 21h20", "M7 8v3", "M12 8v3", "M17 8v3", "M7 4h.01", "M12 4h.01", "M17 4h.01"))
internal val IcCandy_DATA = IconData("Candy", listOf("M10 7v10.9", "M14 6.1V17", "M16 7V3a1 1 0 0 1 1.707-.707 2.5 2.5 0 0 0 2.152.717 1 1 0 0 1 1.131 1.131 2.5 2.5 0 0 0 .717 2.152A1 1 0 0 1 21 8h-4", "M16.536 7.465a5 5 0 0 0-7.072 0l-2 2a5 5 0 0 0 0 7.07 5 5 0 0 0 7.072 0l2-2a5 5 0 0 0 0-7.07", "M8 17v4a1 1 0 0 1-1.707.707 2.5 2.5 0 0 0-2.152-.717 1 1 0 0 1-1.131-1.131 2.5 2.5 0 0 0-.717-2.152A1 1 0 0 1 3 16h4"))
internal val IcCherry_DATA = IconData("Cherry", listOf("M2 17a5 5 0 0 0 10 0c0-2.76-2.5-5-5-3-2.5-2-5 .24-5 3Z", "M12 17a5 5 0 0 0 10 0c0-2.76-2.5-5-5-3-2.5-2-5 .24-5 3Z", "M7 14c3.22-2.91 4.29-8.75 5-12 1.66 2.38 4.94 9 5 12", "M22 9c-4.29 0-7.14-2.33-10-7 5.71 0 10 4.67 10 7Z"))
internal val IcCitrus_DATA = IconData("Citrus", listOf("M21.66 17.67a1.08 1.08 0 0 1-.04 1.6A12 12 0 0 1 4.73 2.38a1.1 1.1 0 0 1 1.61-.04z", "M19.65 15.66A8 8 0 0 1 8.35 4.34", "m14 10-5.5 5.5", "M14 17.85V10H6.15"))
internal val IcWine_DATA = IconData("Wine", listOf("M8 22h8", "M7 10h10", "M12 15v7", "M12 15a5 5 0 0 0 5-5c0-2-.5-4-2-8H9c-1.5 4-2 6-2 8a5 5 0 0 0 5 5Z"))
internal val IcEgg_DATA = IconData("Egg", listOf("M12 2C8 2 4 8 4 14a8 8 0 0 0 16 0c0-6-4-12-8-12"))
internal val IcSoup_DATA = IconData("Soup", listOf("M11.248 3c.273.1.808.53.747 1.36-.05.83-.938 1.2-.989 2.02-.06.78.333 1.24.727 1.62", "M16.252 3c.268.1.794.53.745 1.36-.06.83-.923 1.2-.993 2.02-.05.78.338 1.24.725 1.62", "M19.5 12 22 6", "M4 12a1 1 0 00-.99 1.133A9 9 0 0012 21a9 9 0 008.99-7.867A1 1 0 0020 12z", "M6.252 3c.268.1.794.53.745 1.36-.06.83-.923 1.2-.993 2.02-.05.78.338 1.24.735 1.62", "M7 21h10"))
internal val IcGlassWater_DATA = IconData("GlassWater", listOf("M5.116 4.104A1 1 0 0 1 6.11 3h11.78a1 1 0 0 1 .994 1.105L17.19 20.21A2 2 0 0 1 15.2 22H8.8a2 2 0 0 1-2-1.79z", "M6 12a5 5 0 0 1 6 0 5 5 0 0 0 6 0"))
internal val IcCookingPot_DATA = IconData("CookingPot", listOf("M2 12h20", "M20 12v8a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2v-8", "m4 8 16-4", "m8.86 6.78-.45-1.81a2 2 0 0 1 1.45-2.43l1.94-.48a2 2 0 0 1 2.43 1.46l.45 1.8"))
internal val IcShell_DATA = IconData("Shell", listOf("M14 11a2 2 0 1 1-4 0 4 4 0 0 1 8 0 6 6 0 0 1-12 0 8 8 0 0 1 16 0 10 10 0 1 1-20 0 11.93 11.93 0 0 1 2.42-7.22 2 2 0 1 1 3.16 2.44"))
internal val IcSprayCan_DATA = IconData("SprayCan", listOf("M3 3h.01", "M7 5h.01", "M11 7h.01", "M3 7h.01", "M7 9h.01", "M3 11h.01", "M 15 5 L 19 5 L 19 9 L 15 9 Z", "m19 9 2 2v10c0 .6-.4 1-1 1h-6c-.6 0-1-.4-1-1V11l2-2", "m13 14 8-2", "m13 19 8-2"))
internal val IcSyringe_DATA = IconData("Syringe", listOf("m18 2 4 4", "m17 7 3-3", "M19 9 8.7 19.3c-1 1-2.5 1-3.4 0l-.6-.6c-1-1-1-2.5 0-3.4L15 5", "m9 11 4 4", "m5 19-3 3", "m14 4 6 6"))
internal val IcFlashlight_DATA = IconData("Flashlight", listOf("M12 13v1", "M17 2a1 1 0 0 1 1 1v4a3 3 0 0 1-.6 1.8l-.6.8A4 4 0 0 0 16 12v8a2 2 0 0 1-2 2H10a2 2 0 0 1-2-2v-8a4 4 0 0 0-.8-2.4l-.6-.8A3 3 0 0 1 6 7V3a1 1 0 0 1 1-1z", "M6 6h12"))
internal val IcBanana_DATA = IconData("Banana", listOf("M4 13c3.5-2 8-2 10 2a5.5 5.5 0 0 1 8 5", "M5.15 17.89c5.52-1.52 8.65-6.89 7-12C11.55 4 11.5 2 13 2c3.22 0 5 5.5 5 8 0 6.5-4.2 12-10.49 12C5.11 22 2 22 2 20c0-1.5 1.14-1.55 3.15-2.11Z"))
internal val IcCroissant_DATA = IconData("Croissant", listOf("M10.2 18H4.774a1.5 1.5 0 0 1-1.352-.97 11 11 0 0 1 .132-6.487", "M18 10.2V4.774a1.5 1.5 0 0 0-.97-1.352 11 11 0 0 0-6.486.132", "M18 5a4 3 0 0 1 4 3 2 2 0 0 1-2 2 10 10 0 0 0-5.139 1.42", "M5 18a3 4 0 0 0 3 4 2 2 0 0 0 2-2 10 10 0 0 1 1.42-5.14", "M8.709 2.554a10 10 0 0 0-6.155 6.155 1.5 1.5 0 0 0 .676 1.626l9.807 5.42a2 2 0 0 0 2.718-2.718l-5.42-9.807a1.5 1.5 0 0 0-1.626-.676"))
internal val IcRuler_DATA = IconData("Ruler", listOf("M21.3 15.3a2.4 2.4 0 0 1 0 3.4l-2.6 2.6a2.4 2.4 0 0 1-3.4 0L2.7 8.7a2.41 2.41 0 0 1 0-3.4l2.6-2.6a2.41 2.41 0 0 1 3.4 0Z", "m14.5 12.5 2-2", "m11.5 9.5 2-2", "m8.5 6.5 2-2", "m17.5 15.5 2-2"))
internal val IcChefHat_DATA = IconData("ChefHat", listOf("M10 11v5", "M14 11v5", "M6 12.65c0-.457-.316-.844-.727-1.041A4 4 0 017.52 4.034a1.53 1.53 0 001.367-.547 4 4 0 016.225 0 1.53 1.53 0 001.367.547 4 4 0 012.248 7.575c-.411.197-.727.584-.727 1.04V19a2 2 0 01-2 2H8a2 2 0 01-2-2z", "M6 16h12"))
internal val IcDrumstick_DATA = IconData("Drumstick", listOf("M15.4 15.63a7.875 6 135 1 1 6.23-6.23 4.5 3.43 135 0 0-6.23 6.23", "m8.29 12.71-2.6 2.6a2.5 2.5 0 1 0-1.65 4.65A2.5 2.5 0 1 0 8.7 18.3l2.59-2.59"))
internal val IcBeef_DATA = IconData("Beef", listOf("M16.4 13.7A6.5 6.5 0 1 0 6.28 6.6c-1.1 3.13-.78 3.9-3.18 6.08A3 3 0 0 0 5 18c4 0 8.4-1.8 11.4-4.3", "m18.5 6 1.754 3.5a6.48 6.48 0 0 1-1.854 8.2C15.4 20.2 11 22 7 22a3 3 0 0 1-2.68-1.66L2.4 16.5", "M 10 8.5 a 2.5 2.5 0 1 0 5 0 a 2.5 2.5 0 1 0 -5 0"))
internal val IcDonut_DATA = IconData("Donut", listOf("M20.5 10a2.5 2.5 0 0 1-2.4-3H18a2.95 2.95 0 0 1-2.6-4.4 10 10 0 1 0 6.3 7.1c-.3.2-.8.3-1.2.3", "M 9 12 a 3 3 0 1 0 6 0 a 3 3 0 1 0 -6 0"))
internal val IcNut_DATA = IconData("Nut", listOf("M16.847 5.847 20 9a7.23 7.23 0 011.551 7.516C21.241 17.352 21 17.932 21 19v1a1 1 0 01-1 1h-1c-1.069 0-1.648.242-2.485.552A7.2 7.2 0 019.002 20l-3.155-3.153", "M18.21 5.43c-1.71.69-5.07 1.07-6.71 1.07.46 1.38.91 2.74.61 4.88a.88.88 0 01-.73.74c-1.78.28-3.54-.17-4.88-.62 0 1.64-.38 5-1.07 6.71-.21.52-.82.55-1.17.12A10 10 0 0118.33 4.26c.43.35.4.97-.12 1.17", "M4.93 4.93 3 3a.7.7 0 010-1"))
internal val IcSalad_DATA = IconData("Salad", listOf("M19.496 12a2.5 2.5 0 00.399-2.214A2 2 0 0020.32 6.5 2 2 0 0019 3a2 2 0 00-1.5.68 2 2 0 00-3.287.424 2.5 2.5 0 00-3.189 2.06A3 3 0 0012 12l4-4", "M4 12a1 1 0 00-.99 1.133A9 9 0 0012 21a9 9 0 008.99-7.867A1 1 0 0020 12z", "M7 21h10", "M9.85 6.907A3.5 3.5 0 005.05 12"))
internal val IcIceCreamCone_DATA = IconData("IceCreamCone", listOf("m7 11 4.08 10.35a1 1 0 0 0 1.84 0L17 11", "M17 7A5 5 0 0 0 7 7", "M17 7a2 2 0 0 1 0 4H7a2 2 0 0 1 0-4"))

@Composable
fun IcHome(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcHome_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCompass(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCompass_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcLayers(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcLayers_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCart(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCart_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBag(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBag_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSearch(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSearch_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSliders(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSliders_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBell(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBell_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcChevronDown(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcChevronDown_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcChevronRight(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcChevronRight_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcChevronLeft(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcChevronLeft_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcX(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcX_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcUser(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcUser_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSettings(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSettings_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcMapPin(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcMapPin_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcHeart(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcHeart_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBox(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBox_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcTruck(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcTruck_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBadgePercent(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBadgePercent_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCreditCard(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCreditCard_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcLogout(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcLogout_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcShieldCheck(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcShieldCheck_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcStar(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcStar_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcPlus(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcPlus_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcMinus(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcMinus_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCheck(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCheck_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCopy(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCopy_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcArrowRight(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcArrowRight_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcClock(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcClock_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcPackage(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcPackage_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcInfo(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcInfo_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcPhone(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcPhone_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcLock(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcLock_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSmartphone(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSmartphone_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCalendar(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCalendar_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcFlame(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcFlame_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSparkles(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSparkles_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcZap(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcZap_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcTag(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcTag_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcPercent(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcPercent_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcList(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcList_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcMenu(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcMenu_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcGrid(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcGrid_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcRotateCcw(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcRotateCcw_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcWallet(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcWallet_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcUtensils(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcUtensils_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBasket(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBasket_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcStore(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcStore_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBadgeCheck(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBadgeCheck_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcReceipt(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcReceipt_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcPackageCheck(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcPackageCheck_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcTimer(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcTimer_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCar(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCar_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcGift(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcGift_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcMessage(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcMessage_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcThumb(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcThumb_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcTrash(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcTrash_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcScale(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcScale_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCheckCircle(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCheckCircle_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCircleX(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCircleX_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCirclePercent(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCirclePercent_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcLifeBuoy(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcLifeBuoy_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcHeadphones(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcHeadphones_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSoda(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSoda_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcApple(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcApple_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCookie(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCookie_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcMilk(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcMilk_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCarrot(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCarrot_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcDroplets(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcDroplets_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcPencil(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcPencil_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCpu(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCpu_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSprout(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSprout_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcLeaf(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcLeaf_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCoffee(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCoffee_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcWheat(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcWheat_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcQrCode(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcQrCode_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcRupee(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcRupee_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBanknote(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBanknote_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcInbox(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcInbox_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcKeyRound(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcKeyRound_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcWeight(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcWeight_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcMap(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcMap_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBean(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBean_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCake(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCake_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCandy(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCandy_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCherry(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCherry_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCitrus(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCitrus_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcWine(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcWine_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcEgg(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcEgg_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSoup(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSoup_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcGlassWater(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcGlassWater_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCookingPot(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCookingPot_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcShell(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcShell_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSprayCan(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSprayCan_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSyringe(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSyringe_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcFlashlight(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcFlashlight_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBanana(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBanana_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcCroissant(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcCroissant_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcRuler(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcRuler_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcChefHat(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcChefHat_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcDrumstick(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcDrumstick_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcBeef(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcBeef_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcDonut(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcDonut_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcNut(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcNut_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcSalad(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcSalad_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

@Composable
fun IcIceCreamCone(modifier: Modifier = Modifier, tint: Color = Color(0xFF24203A), size: Dp = 24.dp, strokeWidth: Float = 2f) =
    opsharIcon(IcIceCreamCone_DATA, modifier = modifier, tint = tint, iconSize = size, strokeWidth = strokeWidth)

