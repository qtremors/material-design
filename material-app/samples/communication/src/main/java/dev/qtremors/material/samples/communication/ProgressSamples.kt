package dev.qtremors.material.samples.communication

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.qtremors.material.core.designsystem.progressSpring

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProgressIndicatorsSample(modifier: Modifier = Modifier) {
    var progress by remember { mutableFloatStateOf(0.62f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = progressSpring(),
        label = "determinate indicator progress",
    )
    val displayedProgress = animatedProgress.coerceIn(0f, 1f)
    Column(modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Text("Determinate ${ (displayedProgress * 100).toInt() }%")
        LinearProgressIndicator(progress = { displayedProgress }, modifier = Modifier.fillMaxWidth())
        LinearWavyProgressIndicator(progress = { displayedProgress }, modifier = Modifier.fillMaxWidth())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(progress = { displayedProgress })
            CircularWavyProgressIndicator(progress = { displayedProgress })
            CircularWavyProgressIndicator()
        }
        Slider(value = progress, onValueChange = { progress = it })
    }
}
