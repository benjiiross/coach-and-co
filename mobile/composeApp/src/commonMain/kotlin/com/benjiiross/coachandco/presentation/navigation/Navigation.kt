package com.benjiiross.coachandco.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.benjiiross.coachandco.presentation.landing.LandingScreen

fun NavGraphBuilder.router(navController: NavHostController) {
    composable<Landing> {
        LandingScreen()
    }
}

fun NavHostController.safePopBackStack(fallbackRoute: Any) {
    if (!popBackStack()) {
        navigate(fallbackRoute)
    }
}
