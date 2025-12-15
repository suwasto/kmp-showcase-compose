package io.github.suwasto.showcasecompose.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.suwasto.showcasecompose.render.ShowcaseStyle

data class ShowcaseStep(
    val rect: Rect,
    val style: ShowcaseStyle = ShowcaseStyle.Standard(),
    val highlightPadding: Dp = 0.dp,
    val content: @Composable (highlightRect: Rect) -> Unit
)