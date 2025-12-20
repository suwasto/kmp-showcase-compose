package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.PopupPositionProvider

class ReportingTooltipPositionProvider(
    private val anchor: Rect,
    private val direction: TooltipDirection,
    private val marginPx: Int,
    private val onArrowCenterResolved: (Float) -> Unit,
    private val onPositionResolved: () -> Unit = {}
) : PopupPositionProvider {

    private var resolved = false
    private var lastContentSize: IntSize? = null

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {

        // Compute the raw bubble offset
        val offset = when (direction) {
            TooltipDirection.Top ->
                IntOffset(
                    x = (anchor.center.x - popupContentSize.width / 2).toInt(),
                    y = (anchor.top - popupContentSize.height - marginPx).toInt()
                )

            TooltipDirection.Bottom ->
                IntOffset(
                    x = (anchor.center.x - popupContentSize.width / 2).toInt(),
                    y = (anchor.bottom + marginPx).toInt()
                )

            TooltipDirection.Start ->
                IntOffset(
                    x = (anchor.left - popupContentSize.width - marginPx).toInt(),
                    y = (anchor.center.y - popupContentSize.height / 2).toInt()
                )

            TooltipDirection.End ->
                IntOffset(
                    x = (anchor.right + marginPx).toInt(),
                    y = (anchor.center.y - popupContentSize.height / 2).toInt()
                )
        }

        // Clamp bubble inside the window if needed
        val clampedX = offset.x.coerceIn(0, windowSize.width - popupContentSize.width)
        val clampedY = offset.y.coerceIn(0, windowSize.height - popupContentSize.height)
        val finalOffset = IntOffset(clampedX, clampedY)

        // Compute arrow center relative to bubble
        val arrowCenter = when (direction) {
            TooltipDirection.Top, TooltipDirection.Bottom ->
                (anchor.center.x - finalOffset.x).coerceIn(0f, popupContentSize.width.toFloat())
            TooltipDirection.Start, TooltipDirection.End ->
                (anchor.center.y - finalOffset.y).coerceIn(0f, popupContentSize.height.toFloat())
        }

        onArrowCenterResolved(arrowCenter)

        // Trigger resolved only when size changes
        if (!resolved || lastContentSize != popupContentSize) {
            lastContentSize = popupContentSize
            resolved = true
            onPositionResolved()
        }

        return finalOffset
    }
}