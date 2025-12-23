package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun Tooltip(
    anchorRect: Rect,
    direction: TooltipDirection,
    bubbleStyle: TooltipBubbleStyle = TooltipBubbleStyle(),
    content: @Composable () -> Unit
) {

    val density = LocalDensity.current
    val marginPx = with(density) { 8.dp.roundToPx() }
    val statusBarHeightPx = WindowInsets.statusBars.getTop(density)

    var positionReady by remember { mutableStateOf(false) }
    var arrowCenter by remember { mutableStateOf(0f) }

    val scale by animateFloatAsState(
        targetValue = if (positionReady) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "tooltip-scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (positionReady) 1f else 0f,
        animationSpec = tween(120),
        label = "tooltip-alpha"
    )

    Popup(
        popupPositionProvider = ReportingTooltipPositionProvider(
            anchor = anchorRect,
            direction = direction,
            marginPx = marginPx,
            statusBarHeightPx = statusBarHeightPx,
            onArrowCenterResolved = { arrowCenter = it },
            onPositionResolved = {
                positionReady = true
            }
        ),
        properties = PopupProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            clippingEnabled = true
        )
    ) {

        Box(
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                transformOrigin = transformOriginFor(direction)
            }
        ) {
            TooltipBubble(
                direction = direction,
                style = bubbleStyle,
                arrowCenter = arrowCenter
            ) {
                content()
            }
        }
    }
}