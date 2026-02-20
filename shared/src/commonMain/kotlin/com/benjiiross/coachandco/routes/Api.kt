package com.benjiiross.coachandco.routes

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
class Api {
    @Resource("/auth")
    @Serializable
    class Auth(val parent: Api = Api()) {
        @Resource("/login")
        @Serializable
        class Login(val parent: Auth = Auth())

        @Resource("/register")
        @Serializable
        class Register(val parent: Auth = Auth())

        @Resource("/logout")
        @Serializable
        class Logout(val parent: Auth = Auth())

        @Resource("/me")
        @Serializable
        class Me(val parent: Auth = Auth())
    }
}