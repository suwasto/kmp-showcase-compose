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

    if (controller.state.isActive) {
        val step = controller.state.steps[controller.state.currentIndex]
        val density = LocalDensity.current

        val paddingPx = with(density) { step.highlightPadding.toPx() }
        val rect = step.rect.inflate(paddingPx)
        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(rect) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull() ?: continue
                            val pos = change.position
                            if (!rect.contains(pos)) {
                                // Consume only outside the highlighted rect
                                change.consume()
                            }
                        }
                    }
                }
        )
    }

}
