package io.github.suwasto.showcasecompose.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp

data class ShowcaseStep(
    val rect: Rect,
    val shape: ShowcaseShape = ShowcaseShape.Rect,
    val content: @Composable () -> Unit
)