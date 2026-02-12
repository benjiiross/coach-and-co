package com.benjiiross.coachandco.features

import com.benjiiross.coachandco.domain.dto.auth.AuthResponse
import com.benjiiross.coachandco.domain.dto.auth.LoginRequest
import com.benjiiross.coachandco.domain.dto.user.UserRequest
import com.benjiiross.coachandco.domain.mappers.toResponse
import com.benjiiross.coachandco.domain.services.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
  val authService by inject<AuthService>()

  route("/auth") {
    post("/login") {
      val request = call.receive<LoginRequest>()

      val user = authService.login(request.email, request.password)
      val token = authService.generateToken(requireNotNull(user.id))

      call.respond(AuthResponse(token = token, user = user.toResponse()))
    }

    post("/logout") { call.respond(HttpStatusCode.OK, mapOf("message" to "Logged out")) }

    post("/register") {
      val request = call.receive<UserRequest>()

      val response = authService.register(request)

      call.respond(HttpStatusCode.Created, response)
    }

    authenticate("auth-jwt") {
      get("/me") {
        val principal = call.principal<JWTPrincipal>()

        val user = authService.me(principal)

        call.respond(user)
      }
    }
  }
}
