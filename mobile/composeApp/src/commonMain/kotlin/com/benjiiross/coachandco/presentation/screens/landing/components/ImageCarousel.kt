package com.benjiiross.coachandco.presentation.screens.landing.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coach_and_co.composeapp.generated.resources.Res
import coach_and_co.composeapp.generated.resources.landing_children
import coach_and_co.composeapp.generated.resources.landing_football
import coach_and_co.composeapp.generated.resources.landing_gym
import coach_and_co.composeapp.generated.resources.landing_old
import coach_and_co.composeapp.generated.resources.landing_running
import coach_and_co.composeapp.generated.resources.landing_tennis
import com.benjiiross.coachandco.presentation.screens.landing.animation.LandingAnimations
import com.benjiiross.coachandco.presentation.theme.Gaps
import org.jetbrains.compose.resources.painterResource

@Composable
fun ImageCarousel(modifier: Modifier) {
    val images =
        listOf(
            Res.drawable.landing_children,
            Res.drawable.landing_old,
            Res.drawable.landing_gym,
            Res.drawable.landing_running,
            Res.drawable.landing_football,
            Res.drawable.landing_tennis,
        )

    val pagerState = rememberPagerState(pageCount = { images.size })

    LandingAnimations.AutoScrollEffect(pagerState, images.size)

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 1,
            userScrollEnabled = false,
        ) { page ->
            Image(
                painter = painterResource(images[page]),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Box(
            modifier =
                Modifier.fillMaxSize()
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY,
                            )
                    )
        )

        PageIndicator(
            pageCount = images.size,
            currentPage = pagerState.currentPage,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = Gaps.L),
        )
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(Gaps.S)) {
        repeat(pageCount) { index ->
            val color =
                if (currentPage == index) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface

            Box(modifier = Modifier.size(Gaps.S).clip(CircleShape).background(color))
        }
    }
}
