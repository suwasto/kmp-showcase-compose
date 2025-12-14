package io.github.suwasto.showcasecompose.core

import androidx.compose.ui.geometry.Rect

fun Rect.inflate(padding: Float): Rect =
    Rect(
        left - padding,
        top - padding,
        right + padding,
        bottom + padding
    )