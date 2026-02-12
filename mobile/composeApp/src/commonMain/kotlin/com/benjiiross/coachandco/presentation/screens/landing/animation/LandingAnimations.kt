package com.benjiiross.coachandco.presentation.screens.landing.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield

object LandingAnimations {
    @Composable
    fun AutoScrollEffect(pagerState: PagerState, pageCount: Int) {
        LaunchedEffect(pagerState) {
            while (true) {
                yield()
                delay(LandingAnimationConstants.SCROLL_DELAY_DURATION)
                if (pageCount > 0) {
                    pagerState.animateScrollToPage(
                        page = (pagerState.currentPage + 1) % pageCount,
                        animationSpec =
                            tween(
                                durationMillis = LandingAnimationConstants.SCROLL_DURATION,
                                easing = FastOutSlowInEasing,
                            ),
                    )
                }
            }
        }
    }

    @Composable
    fun rememberPulseScale(transition: InfiniteTransition): Float {
        val radius by
            transition.animateFloat(
                initialValue = 10f,
                targetValue = 60f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            tween(LandingAnimationConstants.PULSE_DURATION, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
                label = "Radius",
            )

        return radius
    }

    @Composable
    fun rememberAlpha(transition: InfiniteTransition): Float {
        val alpha by
            transition.animateFloat(
                initialValue = 0.6f,
                targetValue = 0f,
                animationSpec =
                    infiniteRepeatable(
                        animation =
                            tween(LandingAnimationConstants.PULSE_DURATION, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart,
                    ),
                label = "Alpha",
            )

        return alpha
    }
}
