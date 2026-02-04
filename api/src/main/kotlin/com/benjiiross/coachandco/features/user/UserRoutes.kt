package com.benjiiross.coachandco.features.user

import com.benjiiross.coachandco.domain.models.User
import com.benjiiross.coachandco.domain.repositories.IUserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
  val userRepository by inject<IUserRepository>()

  route("/users") {
    get {
      val users = userRepository.getAllUsers()
      call.respond(HttpStatusCode.OK, users)
    }

    get("/{id}") {
      val userId =
          call.parameters["id"]?.toIntOrNull()
              ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid user ID")

      val user =
          userRepository.findById(userId)
              ?: return@get call.respond(HttpStatusCode.NotFound, "User not found")

      call.respond(HttpStatusCode.OK, user)
    }

    post {
      val user = call.receive<User>()
      val createdUser = userRepository.createUser(user)
      call.respond(HttpStatusCode.Created, createdUser)
    }

    put("/{id}") {
      val userId =
          call.parameters["id"]?.toIntOrNull()
              ?: return@put call.respond(HttpStatusCode.BadRequest, "Invalid user ID")

      val user = call.receive<User>()
      val updatedUser =
          userRepository.updateUser(userId, user)
              ?: return@put call.respond(HttpStatusCode.NotFound, "User not found")

      call.respond(HttpStatusCode.OK, updatedUser)
    }

    delete("/{id}") {
      val userId =
          call.parameters["id"]?.toIntOrNull()
              ?: return@delete call.respond(HttpStatusCode.BadRequest, "Invalid user ID")

      val deleted = userRepository.deleteUser(userId)
      if (deleted) {
        call.respond(HttpStatusCode.NoContent)
      } else {
        call.respond(HttpStatusCode.NotFound, "User not found")
      }
    }
  }
}
