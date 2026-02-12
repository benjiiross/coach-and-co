package com.benjiiross.coachandco.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.benjiiross.coachandco.presentation.navigation.NavigationManagerImpl
import com.benjiiross.coachandco.presentation.navigation.Route
import com.benjiiross.coachandco.presentation.navigation.router
import com.benjiiross.coachandco.presentation.theme.CoachAndCoTheme

@Composable
fun GlobalAppContainer(navController: NavHostController = rememberNavController()) {
    val navigationManager = remember(navController) { NavigationManagerImpl(navController) }

    CoachAndCoTheme {
        NavHost(
            navController = navController,
            startDestination = Route.Landing,
            modifier = Modifier.fillMaxSize(),
        ) {
            router(navigationManager)
        }
    }
}
