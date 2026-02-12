package com.benjiiross.coachandco.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.benjiiross.coachandco.presentation.components.layout.CoachAndCoScaffold
import com.benjiiross.coachandco.presentation.screens.landing.LandingScreen
import com.benjiiross.coachandco.presentation.screens.login.LoginScreen
import com.benjiiross.coachandco.presentation.screens.login.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.router(navigationManager: NavigationManager) {
    composable<Route.Landing> {
        LandingScreen(
            onNavigateRegister = {},
            onNavigateLogin = { navigationManager.navigateTo(Route.Auth.Login) },
        )
    }

    composable<Route.Auth.Login> {
        val viewModel = koinViewModel<LoginViewModel>()

        CoachAndCoScaffold(onExit = navigationManager::navigateBack) { innerPadding ->
            LoginScreen(
                viewModel = viewModel,
                innerPadding = innerPadding,
                onNavigateRegister = { navigationManager.navigateTo(Route.Auth.Register) },
            )
        }
    }
}
