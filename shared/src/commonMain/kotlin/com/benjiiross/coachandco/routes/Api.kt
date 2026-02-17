package com.benjiiross.coachandco.routes

import com.benjiiross.coachandco.dto.auth.LoginRequest
import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Resource("/")
@Serializable
class Api {
    @Resource("auth")
    @Serializable
    class Auth(val parent: Api = Api()) {
        @Resource("login")
        @Serializable
        class Login(val parent: Auth = Auth())
    }
}