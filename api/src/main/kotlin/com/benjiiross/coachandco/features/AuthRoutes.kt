package com.benjiiross.coachandco.features

import com.benjiiross.coachandco.domain.services.AuthService
import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.LoginRequest
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import com.benjiiross.coachandco.mappers.toResponse
import com.benjiiross.coachandco.routes.Api
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val authService by inject<AuthService>()

    post<Api.Auth.Login> {
        val loginRequest = call.receive<LoginRequest>()

        val user = authService.login(loginRequest.email, loginRequest.password)
        val token = authService.generateToken(requireNotNull(user.id))

        call.respond(
            HttpStatusCode.OK,
            AuthResponse(
                token = token,
                user = user.toResponse()
            )
        )
    }

    post("/register") {
        val request = call.receive<RegisterRequest>()

        val user = authService.register(request)
        val token = authService.generateToken(requireNotNull(user.id))

        call.respond(
            HttpStatusCode.OK,
            AuthResponse(
                token = token,
                user = user.toResponse()
            )
        )

    }

    post("/logout") { call.respond(HttpStatusCode.OK, mapOf("message" to "Logged out")) }

    authenticate("auth-jwt") {
        get("/me") {
            val principal = call.principal<JWTPrincipal>()

            val user = authService.me(principal)

            call.respond(user)
        }
    }
}
