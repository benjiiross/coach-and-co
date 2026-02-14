package com.benjiiross.coachandco.presentation.screens.landing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coach_and_co.mobile.composeapp.generated.resources.Res
import coach_and_co.mobile.composeapp.generated.resources.landing_body_description
import coach_and_co.mobile.composeapp.generated.resources.landing_btn_login
import coach_and_co.mobile.composeapp.generated.resources.landing_btn_start
import coach_and_co.mobile.composeapp.generated.resources.landing_display_tagline
import coach_and_co.mobile.composeapp.generated.resources.landing_title_app
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButton
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButtonVariant
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextBody
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextDisplay
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextTitle
import com.benjiiross.coachandco.presentation.preview.Pixel9aPreview
import com.benjiiross.coachandco.presentation.preview.ThemePreview
import com.benjiiross.coachandco.presentation.screens.landing.components.ImageCarousel
import com.benjiiross.coachandco.presentation.screens.landing.components.PulseDot
import com.benjiiross.coachandco.presentation.theme.Gaps

@Composable
fun LandingScreen(onNavigateRegister: () -> Unit, onNavigateLogin: () -> Unit) {
    Column(
        modifier =
            Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.inverseSurface)
    ) {
        ImageCarousel(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = Gaps.M),
            horizontalAlignment = Alignment.Start,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Gaps.M),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Gaps.SM),
                ) {
                    PulseDot(color = MaterialTheme.colorScheme.primary)

                    CoachAndCoTextTitle(
                        textRes = Res.string.landing_title_app,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                CoachAndCoTextDisplay(
                    textRes = Res.string.landing_display_tagline,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                )
                CoachAndCoTextBody(
                    textRes = Res.string.landing_body_description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = Gaps.XL),
                verticalArrangement = Arrangement.spacedBy(Gaps.M),
            ) {
                CoachAndCoButton(
                    textRes = Res.string.landing_btn_start,
                    onClick = onNavigateRegister,
                    modifier = Modifier.fillMaxWidth(),
                )
                CoachAndCoButton(
                    textRes = Res.string.landing_btn_login,
                    onClick = onNavigateLogin,
                    variant = CoachAndCoButtonVariant.Dark,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Pixel9aPreview
@Composable
fun LandingScreenPreview() {
    ThemePreview { LandingScreen(onNavigateRegister = {}, onNavigateLogin = {}) }
}
