package com.benjiiross.coachandco.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object Landing : Route

    sealed interface Auth : Route {
        @Serializable data object Login : Auth

        @Serializable data object Register : Auth

        @Serializable data class ForgotPassword(val email: String = "") : Auth
    }

    sealed interface Main : Route {
        @Serializable data object Home : Main

        @Serializable data class Profile(val userId: String) : Main
    }
}
