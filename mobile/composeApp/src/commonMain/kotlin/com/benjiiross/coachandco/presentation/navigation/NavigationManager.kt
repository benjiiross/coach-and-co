package com.benjiiross.coachandco.presentation.navigation

interface NavigationManager {
    fun navigateTo(route: Route)

    fun navigateBack()

    fun navigateBackTo(route: Route)
}
