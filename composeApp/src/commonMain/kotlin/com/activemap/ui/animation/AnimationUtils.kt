package com.activemap.ui.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

data class AnimationConfig(
    val enabled: Boolean = true
)

class AnimationStates {
    var recommendationsVisible by mutableStateOf(false)
    var challengesVisible by mutableStateOf(false)
    var placesVisible by mutableStateOf(false)
    var reportVisible by mutableStateOf(false)
}

@Composable
fun rememberAnimationStates(): AnimationStates {
    return remember { AnimationStates() }
}

@Composable
fun fadeInSlideUp(
    visible: Boolean,
    config: AnimationConfig,
    content: @Composable (Modifier) -> Unit
) {
    if (!config.enabled) {
        content(Modifier)
        return
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    val translateY by animateFloatAsState(
        targetValue = if (visible) 0f else 30f,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    content(
        Modifier.graphicsLayer {
            this.alpha = alpha
            this.translationY = translateY
        }
    )
}

@Composable
fun pulseGlow(
    config: AnimationConfig,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    if (!config.enabled) {
        content(modifier)
        return
    }

    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1800,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    content(
        modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    )
}

@Composable
fun bounceAppear(
    visible: Boolean,
    config: AnimationConfig,
    content: @Composable (Modifier) -> Unit
) {
    if (!config.enabled) {
        content(Modifier)
        return
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = keyframes {
            durationMillis = 400
            0.0f at 0 using FastOutSlowInEasing
            0.9f at 100 using FastOutSlowInEasing
            1.1f at 200 using FastOutSlowInEasing
            1.0f at 300 using FastOutSlowInEasing
            if (visible) 1.0f at 400 using FastOutSlowInEasing
        }
    )

    content(
        Modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    )
}

@Composable
fun listItemAnimation(
    index: Int,
    config: AnimationConfig,
    content: @Composable (Modifier) -> Unit
) {
    if (!config.enabled) {
        content(Modifier)
        return
    }

    val offset by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(
            durationMillis = 300 + (index * 50),
            easing = FastOutSlowInEasing
        )
    )

    content(
        Modifier.graphicsLayer {
            translationY = offset.toPx()
            alpha = if (offset == 0.dp) 1f else 0f
        }
    )
}
