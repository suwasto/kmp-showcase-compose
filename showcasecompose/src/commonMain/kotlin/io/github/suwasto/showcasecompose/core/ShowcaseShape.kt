package io.github.suwasto.showcasecompose.core

import androidx.compose.ui.unit.Dp

sealed class ShowcaseShape {
    object Circle : ShowcaseShape()
    data class Rounded(val cornerRadius: Dp) : ShowcaseShape()
    object Rect : ShowcaseShape()
}