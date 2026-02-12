package com.benjiiross.coachandco.features

import com.benjiiross.coachandco.core.exceptions.ForbiddenException
import com.benjiiross.coachandco.domain.models.User
import com.benjiiross.coachandco.domain.services.UserService
import com.benjiiross.coachandco.util.receiveId
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
  val userService by inject<UserService>()

  route("/users") {
    authenticate("auth-jwt") {
      get {
        val email = call.queryParameters["email"]

        val response =
            if (email != null) {
              userService.getUserByEmail(email)
            } else {
              userService.getAllUsers()
            }

        call.respond(response)
      }

      get("/{id}") {
        val userId = call.receiveId()

        val result = userService.getUserById(userId)

        call.respond(result)
      }

      put("/{id}") {
        val userId = call.receiveId()
        val principal = call.principal<JWTPrincipal>()
        val loggedInId = principal?.payload?.getClaim("userId")?.asInt()

        if (userId != loggedInId) throw ForbiddenException()

        val user = call.receive<User>()
        val result = userService.updateUser(userId, user)

        call.respond(result)
      }

      delete("/{id}") {
        val userId = call.receiveId()
        val principal = call.principal<JWTPrincipal>()
        val loggedInId = principal?.payload?.getClaim("userId")?.asInt()

        if (userId != loggedInId) throw ForbiddenException()

        userService.deleteUser(userId)

        call.respond(HttpStatusCode.NoContent)
      }
    }
  }
}
