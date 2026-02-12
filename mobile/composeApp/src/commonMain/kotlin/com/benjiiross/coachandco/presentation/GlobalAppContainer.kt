package com.benjiiross.coachandco.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.benjiiross.coachandco.presentation.navigation.Landing
import com.benjiiross.coachandco.presentation.navigation.router
import com.benjiiross.coachandco.presentation.theme.CoachAndCoTheme

@Composable
fun GlobalAppContainer(navController: NavHostController = rememberNavController()) {
    CoachAndCoTheme {
        NavHost(
            navController = navController,
            startDestination = Landing,
            modifier = Modifier.fillMaxSize(),
        ) {

            router(navController = navController)
        }
    }
}
