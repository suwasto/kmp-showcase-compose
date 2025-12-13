package io.github.suwasto.showcasecompose.tooltip

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Tooltip(
    anchorRect: Rect,
    direction: TooltipDirection,
    bubbleStyle: TooltipBubbleStyle = TooltipBubbleStyle(),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val marginPx = with(density) { 8.dp.roundToPx() }

    var positionReady by remember { mutableStateOf(false) }

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
            onPositionResolved = {
                positionReady = true
            }
        ),
        properties = PopupProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {

        Box(
            modifier = modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                transformOrigin = transformOriginFor(direction)
            }
        ) {
            TooltipBubble(
                direction = direction,
                style = bubbleStyle
            ) {
                content()
            }
        }
    }
}