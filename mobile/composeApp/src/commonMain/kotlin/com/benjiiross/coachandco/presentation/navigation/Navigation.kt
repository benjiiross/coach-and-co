package com.benjiiross.coachandco.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.benjiiross.coachandco.data.TokenStorage
import com.benjiiross.coachandco.presentation.components.layout.CoachAndCoScaffold
import com.benjiiross.coachandco.presentation.screens.profile.ProfileScreen
import com.benjiiross.coachandco.presentation.profile.ProfileViewModel
import com.benjiiross.coachandco.presentation.screens.home.HomeScreen
import com.benjiiross.coachandco.presentation.screens.landing.LandingScreen
import com.benjiiross.coachandco.presentation.screens.login.LoginScreen
import com.benjiiross.coachandco.presentation.screens.login.LoginViewModel
import org.koin.compose.koinInject
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
                onLoginSuccess = { navigationManager.navigateTo(Route.Main.Profile) },
            )
        }
    }

    composable<Route.Main.Profile> {
        val viewModel = koinViewModel<ProfileViewModel>()
        val tokenStorage = koinInject<TokenStorage>()

        CoachAndCoScaffold(onExit = navigationManager::navigateBack) { innerPadding ->
            ProfileScreen(
                viewModel = viewModel,
                innerPadding = innerPadding,
                onLogout = {
                    tokenStorage.clearToken()
                    navigationManager.navigateTo(Route.Landing)
                },
            )
        }
    }

    composable<Route.Main.Home> {
        HomeScreen()
    }
}
