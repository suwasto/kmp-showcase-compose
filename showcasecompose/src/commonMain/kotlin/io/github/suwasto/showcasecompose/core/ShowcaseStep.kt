package io.github.suwasto.showcasecompose.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.suwasto.showcasecompose.render.ShowcaseStyle

data class ShowcaseStep(
    val rect: Rect,
    val style: ShowcaseStyle = ShowcaseStyle.Standard(
        shape = ShowcaseShape.Rect
    ),
    val highlightPadding: Dp = 0.dp,
    val onClickHighlight: () -> Unit = {},
    val enableDimAnim: Boolean = true,
    val dimAnimationDurationMillis: Int = 800,
    val content: @Composable (highlightRect: Rect) -> Unit
)