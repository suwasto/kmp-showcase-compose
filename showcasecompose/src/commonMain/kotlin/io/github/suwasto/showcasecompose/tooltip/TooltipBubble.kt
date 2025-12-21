package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity

@Composable
internal fun TooltipBubble(
    direction: TooltipDirection,
    arrowCenter: Float,
    style: TooltipBubbleStyle = TooltipBubbleStyle(),
    content: @Composable () -> Unit
) {

    val density = LocalDensity.current

    val padStartPx = with(density) { style.bubblePaddingStart.toPx() }
    val padTopPx = with(density) { style.bubblePaddingTop.toPx() }

    val adjustedArrowCenter = when (direction) {
        TooltipDirection.Top, TooltipDirection.Bottom -> arrowCenter - padStartPx
        TooltipDirection.Start, TooltipDirection.End -> arrowCenter - padTopPx
    }.coerceAtLeast(0f)

    Box(
        modifier = Modifier
            .padding(
                start = style.bubblePaddingStart,
                end = style.bubblePaddingEnd,
                top = style.bubblePaddingTop,
                bottom = style.bubblePaddingBottom
            )
            .clip(
                TooltipBubbleShape(
                    direction = direction,
                    cornerRadius = style.cornerRadius,
                    arrowSize = style.arrowSize,
                    arrowCenter = adjustedArrowCenter
                )
            )
            .background(style.backgroundColor)
            .padding(
                when (direction) {
                    TooltipDirection.Top -> PaddingValues(bottom = style.arrowSize)
                    TooltipDirection.Bottom -> PaddingValues(top = style.arrowSize)
                    TooltipDirection.Start -> PaddingValues(end = style.arrowSize)
                    TooltipDirection.End -> PaddingValues(start = style.arrowSize)
                }
            )
    ) {
        content()
    }
}
