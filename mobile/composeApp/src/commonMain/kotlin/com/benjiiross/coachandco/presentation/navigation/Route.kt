package com.benjiiross.coachandco.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute

@Serializable object Landing : AppRoute
