package com.benjiiross.coachandco.presentation.screens.landing.components

import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.benjiiross.coachandco.presentation.screens.landing.animation.LandingAnimations
import com.benjiiross.coachandco.presentation.theme.Gaps

@Composable
fun PulseDot(
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier.Companion,
) {
    val transition = rememberInfiniteTransition(label = "PulseTransition")

    val radius = LandingAnimations.rememberPulseScale(transition)
    val alpha = LandingAnimations.rememberAlpha(transition)

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(Gaps.XL)) {
            drawCircle(color = color, radius = radius, alpha = alpha)
        }

        Canvas(modifier = Modifier.size(Gaps.SM)) { drawCircle(color = color) }
    }
}
