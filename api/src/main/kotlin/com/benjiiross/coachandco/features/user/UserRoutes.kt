package com.benjiiross.coachandco.features.user

import com.benjiiross.coachandco.data.UsersTable
import com.benjiiross.coachandco.domain.models.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Route.userRoutes() {
  route("/users") {
    post {
      val user = call.receive<User>()
      transaction {
        UsersTable.insert {
          it[email] = user.email
          it[name] = user.name
          it[surname] = user.surname
          it[gender] = user.gender
          it[birthday] = user.birthday
          it[phone] = user.phone
          it[isCoach] = user.isCoach
          it[isClient] = user.isClient
        }
      }
      call.respond(HttpStatusCode.Created)
    }
  }
}
