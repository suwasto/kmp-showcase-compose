package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

class TooltipBubbleShape(
    private val direction: TooltipDirection,
    private val cornerRadius: Dp,
    private val arrowSize: Dp,
    private val arrowCenter: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {

        val arrow = arrowSize.toPx()
        val radius = cornerRadius.toPx()
        val path = Path()

        fun clamp(value: Float, min: Float, max: Float): Float {
            return if (min >= max) value else value.coerceIn(min, max)
        }

        when (direction) {

            TooltipDirection.Top -> {
                val body = Rect(0f, 0f, size.width, size.height - arrow)
                path.addRoundRect(RoundRect(body, radius, radius))

                val min = radius + arrow
                val max = size.width - radius - arrow
                val x = clamp(arrowCenter, min, max)

                path.moveTo(x - arrow, body.bottom)
                path.lineTo(x, body.bottom + arrow)
                path.lineTo(x + arrow, body.bottom)
            }

            TooltipDirection.Bottom -> {
                val body = Rect(0f, arrow, size.width, size.height)
                path.addRoundRect(RoundRect(body, radius, radius))

                val min = radius + arrow
                val max = size.width - radius - arrow
                val x = clamp(arrowCenter, min, max)

                path.moveTo(x - arrow, body.top)
                path.lineTo(x, body.top - arrow)
                path.lineTo(x + arrow, body.top)
            }

            TooltipDirection.Start -> {
                val body = Rect(0f, 0f, size.width - arrow, size.height)
                path.addRoundRect(RoundRect(body, radius, radius))

                val min = radius + arrow
                val max = size.height - radius - arrow
                val y = clamp(arrowCenter, min, max)

                path.moveTo(body.right, y - arrow)
                path.lineTo(body.right + arrow, y)
                path.lineTo(body.right, y + arrow)
            }

            TooltipDirection.End -> {
                val body = Rect(arrow, 0f, size.width, size.height)
                path.addRoundRect(RoundRect(body, radius, radius))

                val min = radius + arrow
                val max = size.height - radius - arrow
                val y = clamp(arrowCenter, min, max)

                path.moveTo(body.left, y - arrow)
                path.lineTo(body.left - arrow, y)
                path.lineTo(body.left, y + arrow)
            }
        }

        path.close()
        Outline.Generic(path)
    }
}
