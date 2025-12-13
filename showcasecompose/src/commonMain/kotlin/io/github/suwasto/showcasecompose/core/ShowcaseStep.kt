package io.github.suwasto.showcasecompose.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp

data class ShowcaseStep(
    val rect: Rect,                        // rectangle of target view
    val shape: ShowcaseShape = ShowcaseShape.Rounded(12.dp),
    val content: @Composable () -> Unit
)