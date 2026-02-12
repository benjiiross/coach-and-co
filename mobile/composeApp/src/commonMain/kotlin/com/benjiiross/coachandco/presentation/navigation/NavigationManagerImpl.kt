package com.benjiiross.coachandco.presentation.navigation

import androidx.navigation.NavHostController

class NavigationManagerImpl(private val navController: NavHostController) : NavigationManager {
    override fun navigateTo(route: Route) {
        navController.navigate(route)
    }

    override fun navigateBack() {
        if (!navController.popBackStack()) {
            navController.navigate(Route.Landing)
        }
    }

    override fun navigateBackTo(route: Route) {
        navController.navigate(route) { popUpTo(route) { inclusive = true } }
    }
}
