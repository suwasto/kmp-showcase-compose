package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import kotlin.math.min

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
        val arrowH = arrowSize.toPx()
        val arrowW = arrowH
        val path = Path()

        // 1. Define Body Rect
        val rect = when (direction) {
            TooltipDirection.Top -> Rect(0f, 0f, size.width, size.height - arrowH)
            TooltipDirection.Bottom -> Rect(0f, arrowH, size.width, size.height)
            TooltipDirection.Start -> Rect(0f, 0f, size.width - arrowH, size.height)
            TooltipDirection.End -> Rect(arrowH, 0f, size.width, size.height)
        }

        // 2. CRITICAL: Calculate the radius to ensure a perfect capsule (half of the shorter side)
        val r = (min(rect.width, rect.height) / 2f).coerceAtMost(cornerRadius.toPx())

        path.apply {
            reset()

            // 3. Determine which specific corner must be flat
            val flattenTopLeft = (direction == TooltipDirection.End && arrowAlignment == ArrowAlignment.Start) ||
                    (direction == TooltipDirection.Bottom && arrowAlignment == ArrowAlignment.Start)
            val flattenTopRight = (direction == TooltipDirection.Start && arrowAlignment == ArrowAlignment.Start) ||
                    (direction == TooltipDirection.Bottom && arrowAlignment == ArrowAlignment.End)
            val flattenBottomRight = (direction == TooltipDirection.Start && arrowAlignment == ArrowAlignment.End) ||
                    (direction == TooltipDirection.Top && arrowAlignment == ArrowAlignment.End)
            val flattenBottomLeft = (direction == TooltipDirection.End && arrowAlignment == ArrowAlignment.End) ||
                    (direction == TooltipDirection.Top && arrowAlignment == ArrowAlignment.Start)

            // --- TOP EDGE ---
            if (flattenTopLeft) moveTo(rect.left, rect.top) else moveTo(rect.left + r, rect.top)

            if (direction == TooltipDirection.Bottom) {
                drawDynamicArrow(this, rect.width, arrowW, arrowH, rect.top, isVertical = false, isOpposite = true)
            }

            lineTo(if (flattenTopRight) rect.right else rect.right - r, rect.top)
            if (!flattenTopRight) {
                arcTo(Rect(rect.right - r * 2, rect.top, rect.right, rect.top + r * 2), 270f, 90f, false)
            }

            // --- RIGHT EDGE ---
            if (direction == TooltipDirection.Start) {
                drawDynamicArrow(this, rect.height, arrowW, arrowH, rect.right, isVertical = true, isOpposite = false)
            }

            lineTo(rect.right, if (flattenBottomRight) rect.bottom else rect.bottom - r)
            if (!flattenBottomRight) {
                arcTo(Rect(rect.right - r * 2, rect.bottom - r * 2, rect.right, rect.bottom), 0f, 90f, false)
            }

            // --- BOTTOM EDGE ---
            if (direction == TooltipDirection.Top) {
                drawDynamicArrow(this, rect.width, arrowW, arrowH, rect.bottom, isVertical = false, isOpposite = false)
            }

            lineTo(if (flattenBottomLeft) rect.left else rect.left + r, rect.bottom)
            if (!flattenBottomLeft) {
                arcTo(Rect(rect.left, rect.bottom - r * 2, rect.left + r * 2, rect.bottom), 90f, 90f, false)
            }

            // --- LEFT EDGE ---
            if (direction == TooltipDirection.End) {
                drawDynamicArrow(this, rect.height, arrowW, arrowH, rect.left, isVertical = true, isOpposite = true)
            }

            lineTo(rect.left, if (flattenTopLeft) rect.top else rect.top + r)
            if (!flattenTopLeft) {
                arcTo(Rect(rect.left, rect.top, rect.left + r * 2, rect.top + r * 2), 180f, 90f, false)
            }

            close()
        }
        Outline.Generic(path)
    }

    private fun drawDynamicArrow(
        path: Path,
        limit: Float,
        w: Float,
        h: Float,
        linePos: Float,
        isVertical: Boolean,
        isOpposite: Boolean
    ) {
        val tipPos = if (isOpposite) linePos - h else linePos + h

        when (arrowAlignment) {
            ArrowAlignment.Center -> {
                val c = limit / 2f
                if (isVertical) {
                    path.lineTo(linePos, c - w / 2f)
                    path.lineTo(tipPos, c)
                    path.lineTo(linePos, c + w / 2f)
                } else {
                    path.lineTo(c - w / 2f, linePos)
                    path.lineTo(c, tipPos)
                    path.lineTo(c + w / 2f, linePos)
                }
            }
            ArrowAlignment.Start -> {
                // Right-Angled: The tip is flush with the start of the edge (0)
                if (isVertical) {
                    path.lineTo(linePos, 0f)
                    path.lineTo(tipPos, 0f)
                    path.lineTo(linePos, w)
                } else {
                    path.lineTo(0f, linePos)
                    path.lineTo(0f, tipPos)
                    path.lineTo(w, linePos)
                }
            }
            ArrowAlignment.End -> {
                // Right-Angled: The tip is flush with the end of the edge (limit)
                if (isVertical) {
                    path.lineTo(linePos, limit - w)
                    path.lineTo(tipPos, limit)
                    path.lineTo(linePos, limit)
                } else {
                    path.lineTo(limit - w, linePos)
                    path.lineTo(limit, tipPos)
                    path.lineTo(limit, linePos)
                }
            }
        }
    }

}
