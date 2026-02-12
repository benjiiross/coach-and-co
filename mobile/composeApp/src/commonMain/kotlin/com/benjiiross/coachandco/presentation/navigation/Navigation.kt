package com.benjiiross.coachandco.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.benjiiross.coachandco.presentation.screens.landing.LandingScreen

fun NavGraphBuilder.router(navController: NavHostController) {
    composable<Landing> { LandingScreen(onNavigateRegister = {}, onNavigateLogin = {}) }
}

fun NavHostController.safePopBackStack(fallbackRoute: Any) {
    if (!popBackStack()) {
        navigate(fallbackRoute)
    }
}
