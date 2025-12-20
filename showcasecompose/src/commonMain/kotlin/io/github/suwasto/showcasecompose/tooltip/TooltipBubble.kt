package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun TooltipBubble(
    direction: TooltipDirection,
    arrowCenter: Float,
    style: TooltipBubbleStyle = TooltipBubbleStyle(),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Box(
        modifier = modifier
            .clip(
                TooltipBubbleShape(
                    direction = direction,
                    cornerRadius = style.cornerRadius,
                    arrowSize = style.arrowSize,
                    arrowCenter = arrowCenter
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
