package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class TooltipBubbleStyle(
    val backgroundColor: Color = Color(0xFFFFFFFF),
    val cornerRadius: Dp = 8.dp,
    val arrowSize: Dp = 8.dp,
)