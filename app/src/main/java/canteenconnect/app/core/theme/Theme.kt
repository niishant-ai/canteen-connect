package canteenconnect.app.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * AURA — brand + design tokens for the canteen-shopping app.
 * Centralized so the whole app is reskinned from here; screens reference [O]
 * (colour), [Sp] (spacing) and [Radius] only.
 *
 * AURA system: a natural, premium palette of sage, warm sand and deep charcoal,
 * with a two-tier glassmorphic card treatment. All screens read the tokens below,
 * so re-skinning the app is a single edit to this object.
 */
object Brand {
    const val NAME = "AURA"
    const val TAGLINE = "Your Authorized Canteen Hub"
    const val CANTEEN_ID = "CTN45218"
    const val USER_NAME = "Naman Kumar"
    const val MEMBER_CODE = "CCM-2481-7742"
    const val DEMO_BADGE = "DEMO"
}

/** AURA colour system (light-first; a dimmed dark set is provided for the
 *  system dark theme). Natural sage + warm sand + deep charcoal. */
object O {
    // Brand sage (primary actions, active states, icons)
    val Blue = Color(0xFF7E9278)          // sage
    val BlueDeep = Color(0xFF5F7257)      // deep sage ink
    val BlueSoft = Color(0xFFDCE5D4)      // soft sage surface
    val BlueFaint = Color(0xFFEEF2E6)     // faint sage wash

    // Neutrals (charcoal + warm sand)
    val Ink = Color(0xFF1D1F21)           // primary text (deep charcoal)
    val Slate = Color(0xFF6E7268)         // secondary text
    val Mute = Color(0xFF9BA093)          // tertiary / placeholder
    val Line = Color(0xFFD9DDCE)         // hairline borders
    val LineSoft = Color(0xFFE8EBE0)
    val Card = Color(0xFFFDFCF7)         // warm glass card white
    val Bg = Color(0xFFF1EDE0)           // app background (warm sand)
    val Surface = Color(0xFFFBFAF4)

    // AURA signature
    val Charcoal = Color(0xFF232528)     // primary CTA fill
    val Sand = Color(0xFFEFE9DC)
    val SageGlass = Color(0x99DCE5D4)    // translucent sage glass tint

    // Semantic
    val Success = Color(0xFF3F9E63)
    val SuccessSoft = Color(0xFFE4F1E8)
    val Danger = Color(0xFFC24545)
    val DangerSoft = Color(0xFFF7E4E4)
    val Amber = Color(0xFFC89A2B)
    val AmberSoft = Color(0xFFF5EBD3)
    val Teal = Color(0xFF4E8577)
    val TealSoft = Color(0xFFDAEAE4)

    // Product accent tiles (index -> (soft surface, deep ink)), AURA naturals
    fun surfaceOf(a: Int) = Accent[a]?.first ?: Accent[0]!!.first
    fun deepOf(a: Int) = Accent[a]?.second ?: Accent[0]!!.second
    val Accent = mapOf(
        0 to Pair(Color(0xFFDCE5D4), Color(0xFF5F7257)),  // sage
        1 to Pair(Color(0xFFE8E1CC), Color(0xFF8A7A55)),  // sand
        2 to Pair(Color(0xFFD8E8D5), Color(0xFF4C7A5A)),  // moss
        3 to Pair(Color(0xFFF0DBCB), Color(0xFFA86A3E)),  // clay
        4 to Pair(Color(0xFFF5ECD2), Color(0xFFBE941C)),  // butter
        5 to Pair(Color(0xFFEFE0DC), Color(0xFF8E5A52)),  // mauve
        6 to Pair(Color(0xFFE8E6DE), Color(0xFF4A4E46))   // stone
    )
}

/** 8dp spacing system. */
object Sp {
    val s1 = 4.dp
    val s2 = 8.dp
    val s3 = 12.dp
    val s4 = 16.dp
    val s5 = 20.dp
    val s6 = 28.dp
    val s7 = 40.dp
}

object Radius {
    val pill = 999.dp
    val xl = 28.dp
    val lg = 20.dp
    val card = 18.dp
    val md = 14.dp
    val sm = 10.dp
}

fun opLightColors() = lightColorScheme(
    primary = O.Blue,
    onPrimary = Color.White,
    primaryContainer = O.BlueSoft,
    onPrimaryContainer = O.BlueDeep,
    secondary = O.Teal,
    onSecondary = Color.White,
    tertiary = O.Amber,
    background = O.Bg,
    onBackground = O.Ink,
    surface = O.Surface,
    onSurface = O.Ink,
    surfaceVariant = O.BlueFaint,
    onSurfaceVariant = O.Slate,
    outline = O.Line,
    outlineVariant = O.LineSoft,
    error = O.Danger,
    onError = Color.White
)

fun opDarkColors() = darkColorScheme(
    primary = Color(0xFFA9BCA1),
    onPrimary = Color(0xFF101210),
    primaryContainer = Color(0xFF2A332A),
    onPrimaryContainer = Color(0xFFDCE5D4),
    background = Color(0xFF101210),
    onBackground = Color(0xFFEDE9DD),
    surface = Color(0xFF171A15),
    onSurface = Color(0xFFEDE9DD),
    surfaceVariant = Color(0xFF21261F),
    onSurfaceVariant = Color(0xFFA8AFA0),
    outline = Color(0xFF3A4138),
    error = Color(0xFFE48B8B),
    onError = Color(0xFF101210)
)

val opTypography = Typography(
    displayLarge = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, color = O.Ink, letterSpacing = -0.5.sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, color = O.Ink, letterSpacing = -0.4.sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = O.Ink, letterSpacing = -0.3.sp),
    headlineSmall = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = O.Ink, letterSpacing = -0.2.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = O.Ink, letterSpacing = -0.1.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = O.Ink),
    titleSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = O.Ink),
    bodyLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp, color = O.Ink, lineHeight = 21.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp, color = O.Ink, lineHeight = 20.sp),
    bodySmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.5.sp, color = O.Slate, lineHeight = 17.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp, color = O.Ink),
    labelMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp, color = O.Ink, letterSpacing = 0.1.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 11.5.sp, color = O.Slate, letterSpacing = 0.2.sp)
)

@Composable
fun AuraTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (dark) opDarkColors() else opLightColors(),
        typography = opTypography,
        shapes = Shapes(
            extraLarge = RoundedCornerShape(Radius.xl),
            large = RoundedCornerShape(Radius.lg),
            medium = RoundedCornerShape(Radius.md),
            small = RoundedCornerShape(Radius.sm),
            extraSmall = RoundedCornerShape(Radius.sm)
        )
    ) {
        content()
    }
}

/** Legacy alias so older code keeps resolving. */
@Deprecated("Use AuraTheme", ReplaceWith("AuraTheme"))
@Composable
fun OpsharTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = AuraTheme(dark = dark, content = content)

@Deprecated("Use AuraTheme", ReplaceWith("AuraTheme"))
@Composable
fun CanteenConnectTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) = AuraTheme(dark = dark, content = content)

/** Legacy palette alias (maps to the AURA sage system). */
@Deprecated("Use O", ReplaceWith("O"))
object CC {
    val Lime = O.Blue
    val LimeDeep = O.BlueDeep
    val LimeSoft = O.BlueSoft
    val Charcoal = O.Ink
    val Ivory = O.Bg
    val Slate = O.Slate
    val Inky = Color(0xFF0E1220)
    val DemoSurface = O.Accent
    fun surfaceOf(a: Int) = O.surfaceOf(a)
    fun deepOf(a: Int) = O.deepOf(a)
}
