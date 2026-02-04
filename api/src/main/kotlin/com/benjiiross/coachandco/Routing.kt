package com.benjiiross.coachandco

import com.benjiiross.coachandco.features.user.userRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.resources.Resources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
  install(Resources)
  install(StatusPages) {
    exception<Throwable> { call, cause ->
      call.respondText(
          text = cause.localizedMessage ?: "Unknown error",
          status = HttpStatusCode.InternalServerError,
      )
    }
  }

  routing {
    get("/") { call.respondText("Hello World!") }

    userRoutes()
  }
}
