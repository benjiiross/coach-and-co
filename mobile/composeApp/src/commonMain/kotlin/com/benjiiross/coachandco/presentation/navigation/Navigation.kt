package com.benjiiross.coachandco.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.benjiiross.coachandco.presentation.components.layout.CoachAndCoScaffold
import com.benjiiross.coachandco.presentation.screens.profile.ProfileScreen
import com.benjiiross.coachandco.presentation.profile.ProfileViewModel
import com.benjiiross.coachandco.presentation.screens.home.HomeScreen
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

        CoachAndCoScaffold(onExit = navigationManager::navigateBack) { innerPadding, snackbarHostState ->
            LoginScreen(
                viewModel = viewModel,
                innerPadding = innerPadding,
                snackbarHostState = snackbarHostState,
                onNavigateRegister = { navigationManager.navigateTo(Route.Auth.Register) },
                onLoginSuccess = {
                    val loggedInUser = viewModel.uiState.value.userId

                    navigationManager.navigateTo(Route.Main.Profile(
                    userId = loggedInUser
                ))},
            )
        }
    }

    composable<Route.Main.Profile> {
        val viewModel = koinViewModel<ProfileViewModel>()

        CoachAndCoScaffold(onExit = navigationManager::navigateBack) { innerPadding, snackbarHostState ->
            ProfileScreen(
                viewModel = viewModel,
                innerPadding = innerPadding,
                snackbarHostState = snackbarHostState,
                onLogout = {
                    // Clear token si besoin, puis :
                    navigationManager.navigateTo(Route.Landing)
                },
            )
        }
    }

    composable<Route.Main.Home> {
        HomeScreen()
    }
}
