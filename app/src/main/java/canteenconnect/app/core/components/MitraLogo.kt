package canteenconnect.app.core.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Canteen Mitra mark — a steaming bowl. Same geometry as
 * `branding/canteen-mitra-logo.svg` and the launcher `ic_fg.xml`
 * (108-unit grid; the mark occupies x 30–78, y 29–85).
 *
 * @param tint   bowl + outer steam colour
 * @param accent centre steam wisp (lime by default)
 */
@Composable
fun MitraMark(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    tint: Color = Color.White,
    accent: Color = Color(0xFFC9E55B),
) {
    val (steamL, steamC, steamR, bowl, foot) = remember {
        listOf(
            "M42 48C38 43 46 40 42 34",
            "M54 48C50 42 58 38 54 29",
            "M66 48C62 43 70 40 66 34",
            "M30 56H78C78 70 67.5 79 54 79C40.5 79 30 70 30 56Z",
            "M44 85H64",
        ).map { d -> Path().apply { PathParser().parsePathString(d).toPath(this) } }
    }
    Canvas(modifier = modifier.size(size)) {
        // Crop the 108-grid to the mark's bounds (x 24..84, y 24..90) and centre it.
        val k = minOf(this.size.width / 60f, this.size.height / 66f)
        val dx = (this.size.width - 60f * k) / 2f - 24f * k
        val dy = (this.size.height - 66f * k) / 2f - 24f * k
        val round = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        withTransform({
            translate(dx, dy)
            scale(k, k, pivot = Offset.Zero)
        }) {
            drawPath(steamL, tint, style = round)
            drawPath(steamC, accent, style = round)
            drawPath(steamR, tint, style = round)
            drawPath(bowl, tint)
            drawPath(foot, tint, style = round)
        }
    }
}
