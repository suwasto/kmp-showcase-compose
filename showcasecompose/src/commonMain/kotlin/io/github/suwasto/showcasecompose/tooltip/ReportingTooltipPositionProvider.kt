package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.PopupPositionProvider

class ReportingTooltipPositionProvider(
    private val anchor: Rect,
    private val direction: TooltipDirection,
    private val marginPx: Int,
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

        return when (direction) {
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

            TooltipDirection.Left ->
                IntOffset(
                    x = (anchor.left - popupContentSize.width - marginPx).toInt(),
                    y = (anchor.center.y - popupContentSize.height / 2).toInt()
                )

            TooltipDirection.Right ->
                IntOffset(
                    x = (anchor.right + marginPx).toInt(),
                    y = (anchor.center.y - popupContentSize.height / 2).toInt()
                )
        }
    }
}