package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.PopupPositionProvider

class ReportingTooltipPositionProvider(
    private val anchor: Rect,
    private val direction: TooltipDirection,
    private val marginPx: Int,
    private val arrowAlignment: ArrowAlignment,
    private val arrowSizePx: Int,
    private val onPositionResolved: () -> Unit
) : PopupPositionProvider {

    private var lastContentSize: IntSize? = null
    private var resolved = false

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {

        if (!resolved && popupContentSize != lastContentSize) {
            lastContentSize = popupContentSize
            resolved = true
            onPositionResolved()
        }

        val horizontalShift =
            if (direction == TooltipDirection.Top || direction == TooltipDirection.Bottom) {
                bubbleShift(
                    alignment = arrowAlignment,
                    bubbleSize = popupContentSize.width,
                    arrowSizePx = arrowSizePx
                )
            } else 0

        val verticalShift =
            if (direction == TooltipDirection.Start || direction == TooltipDirection.End) {
                bubbleShift(
                    alignment = arrowAlignment,
                    bubbleSize = popupContentSize.height,
                    arrowSizePx = arrowSizePx
                )
            } else 0


        return when (direction) {
            TooltipDirection.Top ->
                IntOffset(
                    x = (anchor.center.x - popupContentSize.width / 2).toInt() + horizontalShift,
                    y = (anchor.top - popupContentSize.height - marginPx).toInt()
                )

            TooltipDirection.Bottom ->
                IntOffset(
                    x = (anchor.center.x - popupContentSize.width / 2).toInt() + horizontalShift,
                    y = (anchor.bottom + marginPx).toInt()
                )

            TooltipDirection.Start ->
                IntOffset(
                    x = (anchor.left - popupContentSize.width - marginPx).toInt(),
                    y = (anchor.center.y - popupContentSize.height / 2).toInt() + verticalShift
                )

            TooltipDirection.End ->
                IntOffset(
                    x = (anchor.right + marginPx).toInt(),
                    y = (anchor.center.y - popupContentSize.height / 2).toInt() + verticalShift
                )
        }
    }

    private fun bubbleShift(
        alignment: ArrowAlignment,
        bubbleSize: Int,
        arrowSizePx: Int
    ): Int = when (alignment) {
        ArrowAlignment.Center -> 0
        ArrowAlignment.Start -> (bubbleSize / 2 - arrowSizePx)
        ArrowAlignment.End -> -(bubbleSize / 2 - arrowSizePx)
    }

}