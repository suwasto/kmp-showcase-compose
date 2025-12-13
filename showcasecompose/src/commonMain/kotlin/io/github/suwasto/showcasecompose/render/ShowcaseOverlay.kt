package io.github.suwasto.showcasecompose.render

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import io.github.suwasto.showcasecompose.core.ShowcaseShape
import io.github.suwasto.showcasecompose.core.ShowcaseState
import kotlin.math.max

@Composable
fun ShowcaseOverlay(
    state: ShowcaseState,
    dimColor: Color = Color.Black.copy(alpha = 0.7f)
) {
    if (!state.isActive) return

    val step = state.steps[state.currentIndex]
    val rect = step.rect
    val density = LocalDensity.current
    val statusBarHeightPx = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toFloat()
    }


    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { state.next() }   // tap to go next
            }
            .drawWithContent {
                drawContent()

                val center = Offset(
                    rect.left + rect.width / 2,
                    rect.top + rect.height / 2
                )

                drawIntoCanvas { canvas ->

                    // Create a paint for later masking
                    val clearPaint = Paint().apply {
                        blendMode = BlendMode.Clear
                    }

                    val layerRect = Rect(0f, 0f, size.width, size.height)
                    canvas.saveLayer(layerRect, Paint())
                    canvas.clipRect(
                        0f,
                        statusBarHeightPx,
                        size.width,
                        size.height
                    )
                    drawRect(
                        color = dimColor,
                        size = size
                    )

                    when (val shape = step.shape) {

                        ShowcaseShape.Circle -> {
                            val radius = max(rect.width, rect.height) / 2f
                            canvas.drawCircle(
                                center = Offset(center.x, center.y),
                                radius = radius,
                                paint = clearPaint
                            )
                        }

                        is ShowcaseShape.Rounded -> {
                            val corner = with(density) { shape.cornerRadius.toPx() }
                            canvas.drawRoundRect(
                                left = rect.left,
                                top = rect.top,
                                right = rect.right,
                                bottom = rect.bottom,
                                radiusX = corner,
                                radiusY = corner,
                                paint = clearPaint
                            )
                        }

                        ShowcaseShape.Rect -> {
                            canvas.drawRect(
                                left = rect.left,
                                top = rect.top,
                                right = rect.right,
                                bottom = rect.bottom,
                                paint = clearPaint
                            )
                        }
                    }

                    canvas.restore()
                }
            }
    ) {}
    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        step.content()
    }
}