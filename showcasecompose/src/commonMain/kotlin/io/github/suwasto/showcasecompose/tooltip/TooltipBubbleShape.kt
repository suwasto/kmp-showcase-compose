package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

class TooltipBubbleShape(
    private val direction: TooltipDirection,
    private val cornerRadius: Dp,
    private val arrowSize: Dp
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        with(density) {
            val arrow = arrowSize.toPx()
            val radius = cornerRadius.toPx()

            val path = Path()

            when (direction) {
                TooltipDirection.Top -> {
                    val body = Rect(0f, 0f, size.width, size.height - arrow)
                    path.addRoundRect(RoundRect(body, radius, radius))

                    path.moveTo(size.width / 2 - arrow, body.bottom)
                    path.lineTo(size.width / 2, body.bottom + arrow)
                    path.lineTo(size.width / 2 + arrow, body.bottom)
                }

                TooltipDirection.Bottom -> {
                    val body = Rect(0f, arrow, size.width, size.height)
                    path.addRoundRect(RoundRect(body, radius, radius))

                    path.moveTo(size.width / 2 - arrow, body.top)
                    path.lineTo(size.width / 2, body.top - arrow)
                    path.lineTo(size.width / 2 + arrow, body.top)
                }

                TooltipDirection.Left -> {
                    val body = Rect(0f, 0f, size.width - arrow, size.height)
                    path.addRoundRect(RoundRect(body, radius, radius))

                    path.moveTo(body.right, size.height / 2 - arrow)
                    path.lineTo(body.right + arrow, size.height / 2)
                    path.lineTo(body.right, size.height / 2 + arrow)
                }

                TooltipDirection.Right -> {
                    val body = Rect(arrow, 0f, size.width, size.height)
                    path.addRoundRect(RoundRect(body, radius, radius))

                    path.moveTo(body.left, size.height / 2 - arrow)
                    path.lineTo(body.left - arrow, size.height / 2)
                    path.lineTo(body.left, size.height / 2 + arrow)
                }
            }

            path.close()
            return Outline.Generic(path)
        }
    }
}
