package io.github.suwasto.showcasecompose.render

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import io.github.suwasto.showcasecompose.core.ShowcaseController

@Composable
fun ShowcaseHost(
    controller: ShowcaseController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    Box(modifier = modifier.fillMaxSize()) {
        content()
    }

    ShowcaseOverlay(
        state = controller.state
    )

}
