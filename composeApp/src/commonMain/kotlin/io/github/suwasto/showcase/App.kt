package io.github.suwasto.showcase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.suwasto.showcasecompose.core.ShowcaseShape
import io.github.suwasto.showcasecompose.core.ShowcaseStep
import io.github.suwasto.showcasecompose.core.rememberShowcaseController
import io.github.suwasto.showcasecompose.modifier.captureBounds
import io.github.suwasto.showcasecompose.render.ShowcaseHost
import io.github.suwasto.showcasecompose.tooltip.Tooltip
import io.github.suwasto.showcasecompose.tooltip.TooltipBubbleStyle
import io.github.suwasto.showcasecompose.tooltip.TooltipDirection
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {

        val showcaseController = rememberShowcaseController()
        val layouts = remember { mutableStateMapOf<String, Rect>() }
        val steps by remember {
            derivedStateOf {
                listOfNotNull(
                    layouts["one"]?.let { getShowcaseOne(it) },
                    layouts["two"]?.let { getShowcaseTwo(it) }
                )
            }
        }

        Scaffold {
            ShowcaseHost(
                controller = showcaseController,
                modifier = Modifier.padding(it),
                enableNextOnOverlayClick = true
            ) {
                Column {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        Text(
                            "Show case highlight here!!!",
                            fontSize = 20.sp,
                            modifier = Modifier.captureBounds { rect ->
                                layouts["one"] = rect
                            }.align(Alignment.TopCenter)
                        )
                        Text(
                            "Then here!!!",
                            fontSize = 20.sp,
                            modifier = Modifier.captureBounds { rect ->
                                layouts["two"] = rect
                            }.align(Alignment.Center)
                        )
                    }

                    Button(
                        modifier = Modifier,
                        onClick = {
                            showcaseController.start(steps)
                        }
                    ) {
                        Text("START SHOWCASE")
                    }
                }
            }
        }
    }
}

private fun getShowcaseOne(rect: Rect) = ShowcaseStep(
    rect = rect,
) {
    Tooltip(
        anchorRect = rect,
        direction = TooltipDirection.Bottom,
        bubbleStyle = TooltipBubbleStyle(),
        clippingEnable = true
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Hello showcase one!!!"
        )
    }
}

private fun getShowcaseTwo(rect: Rect) = ShowcaseStep(
    rect = rect,
    shape = ShowcaseShape.Rounded(12.dp),
    highlightPadding = 12.dp
) { highlightRect ->
    Tooltip(
        anchorRect = highlightRect,
        direction = TooltipDirection.End
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            Text("Hello two!!!")
        }
    }
}
