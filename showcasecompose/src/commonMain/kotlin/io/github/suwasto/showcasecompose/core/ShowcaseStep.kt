package io.github.suwasto.showcasecompose.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShowcaseStep(
    val rect: Rect,
    val shape: ShowcaseShape = ShowcaseShape.Rect,
    val highlightPadding: Dp = 0.dp,
    val content: @Composable (highlightRect: Rect) -> Unit
)