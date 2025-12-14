@file:OptIn(ExperimentalUuidApi::class)

package io.github.suwasto.showcasecompose.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import kotlin.uuid.ExperimentalUuidApi

fun Modifier.captureBounds(
    onBoundsReady: (Rect) -> Unit
): Modifier = onGloballyPositioned { layout ->
    val pos = layout.boundsInRoot()
    onBoundsReady(Rect(pos.left, pos.top, pos.right, pos.bottom))
}