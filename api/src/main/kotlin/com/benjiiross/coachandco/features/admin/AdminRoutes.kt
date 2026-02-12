package com.benjiiross.coachandco.features.admin

import com.benjiiross.coachandco.domain.services.UserService
import com.benjiiross.coachandco.util.receiveId
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.adminRoutes() {
  val userService by inject<UserService>()

  route("/admin") {
    authenticate("auth-jwt") {
      install(RoleBasedAuthorization) { requiredRole = "ADMIN" }

      route("/users") {
        patch("/{id}/ban") {
          val userId = call.receiveId()

          userService.deleteUser(userId)

          call.respond(HttpStatusCode.OK, mapOf("message" to "User $userId banned"))
        }

        get("/deleted") { call.respond(userService.getDeletedUsers()) }
      }
    }
  }
}
