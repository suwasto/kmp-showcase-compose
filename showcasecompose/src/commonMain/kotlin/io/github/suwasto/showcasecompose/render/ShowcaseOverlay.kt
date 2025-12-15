package io.github.suwasto.showcasecompose.render

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.suwasto.showcasecompose.core.ShowcaseShape
import io.github.suwasto.showcasecompose.core.ShowcaseState
import kotlin.math.max

sealed interface ShowcaseStyle {
    data class Standard(val shape: ShowcaseShape) : ShowcaseStyle

    data class WaterDropRipple(
        val color: Color = Color.Cyan,
        val strokeWidth: Dp = 20.dp,
        val rippleCount: Int = 3,
        val durationMillis: Int = 2000,
        val maxRadius: Dp = 60.dp
    ) : ShowcaseStyle

    data class PulsingCircle(
        val color: Color = Color.Cyan,
        val strokeWidth: Dp = 20.dp,
        val durationMillis: Int = 1000,
        val maxRadius: Dp = 24.dp
    ) : ShowcaseStyle
}

@Composable
internal fun ShowcaseOverlay(
    state: ShowcaseState,
    dimColor: Color = Color.Black.copy(alpha = 0.7f)
) {
    if (!state.isActive) return

    val step = state.steps[state.currentIndex]
    val density = LocalDensity.current

    val paddingPx = with(density) { step.highlightPadding.toPx() }
    val rect = step.rect.inflate(paddingPx)


    val statusBarHeightPx = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toFloat()
    }

    val rippleProgress = if (step.style is ShowcaseStyle.WaterDropRipple) {
        val transition = rememberInfiniteTransition()
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = step.style.durationMillis,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        ).value
    } else 0f

    val pulseProgress = if (step.style is ShowcaseStyle.PulsingCircle) {
        val transition = rememberInfiniteTransition()
        transition.animateFloat(
            initialValue = 0.7f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = step.style.durationMillis,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        ).value
    } else 1f

    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(state.currentIndex) {
                detectTapGestures { tapOffset ->
                    if (rect.contains(tapOffset)) {
                        step.onClickHighlight()
                    }
                }
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

                    when (step.style) {
                        is ShowcaseStyle.Standard -> {
                            drawCutout(canvas, step.style.shape, rect, clearPaint, density)
                        }

                        is ShowcaseStyle.WaterDropRipple -> {
                            val maxRadiusPx = with(density) { step.style.maxRadius.toPx() }
                            val strokeWidthPx = with(density) { step.style.strokeWidth.toPx() }

                            repeat(step.style.rippleCount) { i ->
                                val progressOffset = (rippleProgress + i * 0.3f) % 1f
                                val radius =
                                    max(rect.width, rect.height) / 2f + progressOffset * maxRadiusPx
                                val alpha = 0.4f * (1f - progressOffset)

                                canvas.drawCircle(
                                    center = center,
                                    radius = radius,
                                    paint = Paint().apply {
                                        color = step.style.color.copy(alpha = alpha)
                                        style = PaintingStyle.Stroke
                                        this.strokeWidth = strokeWidthPx
                                    }
                                )
                            }
                            drawCutout(canvas, ShowcaseShape.Circle, rect, clearPaint, density)
                        }

                        is ShowcaseStyle.PulsingCircle -> {
                            val baseRadius = max(rect.width, rect.height) / 2f
                            val maxRadiusPx = with(density) { step.style.maxRadius.toPx() }
                            val strokeWidthPx = with(density) { step.style.strokeWidth.toPx() }

                            val radius = baseRadius + pulseProgress * maxRadiusPx

                            canvas.drawCircle(
                                center = center,
                                radius = radius,
                                paint = Paint().apply {
                                    color = step.style.color.copy(alpha = 0.3f)
                                    style = PaintingStyle.Stroke
                                    strokeWidth = strokeWidthPx
                                }
                            )
                            drawCutout(canvas, ShowcaseShape.Circle, rect, clearPaint, density)
                        }
                    }

                    canvas.restore()
                }
            }
    ) {}
    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        step.content(rect)
    }
}

private fun drawCutout(
    canvas: Canvas,
    shape: ShowcaseShape,
    rect: Rect,
    clearPaint: Paint,
    density: Density
) {
    when (shape) {
        ShowcaseShape.Circle -> {
            val radius = max(rect.width, rect.height) / 2f
            canvas.drawCircle(
                center = Offset(rect.left + rect.width / 2, rect.top + rect.height / 2),
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
}
