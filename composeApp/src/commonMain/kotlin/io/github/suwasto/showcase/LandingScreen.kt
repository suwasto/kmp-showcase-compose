package io.github.suwasto.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LandingScreen(
    onGetStartedClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Gray)) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp)
        ) {
            Button(
                onClick = { onGetStartedClick(Showcase.STANDAR.name) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("STANDAR WITH TOOLTIP", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onGetStartedClick(Showcase.ANIMATED.name) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("ANIMATED DIM WITH TOOLTIP", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onGetStartedClick(Showcase.ANIMATED_HIGHLIGHT_WATERDROP.name) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("WATERDROP RIPPLE HIGHLIGHT", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onGetStartedClick(Showcase.ANIMATED_PULSING_CIRCLE.name) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("PULSING CIRCLE HIGHLIGHT", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
