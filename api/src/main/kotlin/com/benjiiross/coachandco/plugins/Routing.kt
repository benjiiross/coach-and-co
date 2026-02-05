package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.features.user.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.resources.Resources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
  install(Resources)

  routing {
    get("/") { call.respondText("Hello World!") }

    userRoutes()
  }
}
