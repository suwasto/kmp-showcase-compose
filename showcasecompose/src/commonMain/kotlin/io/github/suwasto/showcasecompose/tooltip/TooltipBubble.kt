package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun TooltipBubble(
    direction: TooltipDirection,
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
                    arrowSize = style.arrowSize
                )
            )
            .background(style.backgroundColor)
            .padding(12.dp)
    ) {
        content()
    }
}
