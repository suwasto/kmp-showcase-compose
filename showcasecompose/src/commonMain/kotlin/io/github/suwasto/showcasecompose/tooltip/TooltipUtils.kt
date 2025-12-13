package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.ui.graphics.TransformOrigin

fun transformOriginFor(direction: TooltipDirection): TransformOrigin {
    return when (direction) {
        TooltipDirection.Top -> TransformOrigin(0.5f, 1f)
        TooltipDirection.Bottom -> TransformOrigin(0.5f, 0f)
        TooltipDirection.Start -> TransformOrigin(1f, 0.5f)
        TooltipDirection.End -> TransformOrigin(0f, 0.5f)
    }
}