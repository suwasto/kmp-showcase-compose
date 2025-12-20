package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

class TooltipBubbleShape(
    private val direction: TooltipDirection,
    private val cornerRadius: Dp,
    private val arrowSize: Dp,
    private val arrowAlignment: ArrowAlignment,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {

        val arrow = arrowSize.toPx()
        val radius = cornerRadius.toPx()
        val path = Path()

        when (direction) {

            TooltipDirection.Top -> {
                val body = Rect(0f, 0f, size.width, size.height - arrow)
                val x = alignedOffset(arrowAlignment, size.width, arrow, radius)

                path.addRoundRect(RoundRect(body, radius, radius))

                path.moveTo(x - arrow, body.bottom)
                path.lineTo(x, body.bottom + arrow)
                path.lineTo(x + arrow, body.bottom)
            }

            TooltipDirection.Bottom -> {
                val body = Rect(0f, arrow, size.width, size.height)
                val x = alignedOffset(arrowAlignment, size.width, arrow, radius)

                path.addRoundRect(RoundRect(body, radius, radius))

                path.moveTo(x - arrow, body.top)
                path.lineTo(x, body.top - arrow)
                path.lineTo(x + arrow, body.top)
            }

            TooltipDirection.Start -> {
                val body = Rect(0f, 0f, size.width - arrow, size.height)
                val y = alignedOffset(arrowAlignment, size.height, arrow, radius)

                path.addRoundRect(RoundRect(body, radius, radius))

                path.moveTo(body.right, y - arrow)
                path.lineTo(body.right + arrow, y)
                path.lineTo(body.right, y + arrow)
            }

            TooltipDirection.End -> {
                val body = Rect(arrow, 0f, size.width, size.height)
                val y = alignedOffset(arrowAlignment, size.height, arrow, radius)

                path.addRoundRect(RoundRect(body, radius, radius))

                path.moveTo(body.left, y - arrow)
                path.lineTo(body.left - arrow, y)
                path.lineTo(body.left, y + arrow)
            }
        }

        path.close()
        Outline.Generic(path)
    }

    private fun alignedOffset(
        alignment: ArrowAlignment,
        available: Float,
        arrowW: Float,
        radius: Float
    ): Float {
        val minSafe = radius*2 + arrowW
        val maxSafe = available - (radius*2) - arrowW

        // Crash Prevention: If minSafe > maxSafe, the corners meet or overlap.
        // In this case, the only valid place for the arrow is the dead center.
        if (minSafe >= maxSafe) return available / 2f

        val raw = when (alignment) {
            ArrowAlignment.Start -> minSafe
            ArrowAlignment.Center -> available / 2f
            ArrowAlignment.End -> maxSafe
        }
        return raw.coerceIn(minSafe, maxSafe)
    }

}
