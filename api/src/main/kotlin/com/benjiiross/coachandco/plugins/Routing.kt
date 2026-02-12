package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.features.admin.adminRoutes
import com.benjiiross.coachandco.features.authRoutes
import com.benjiiross.coachandco.features.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing

fun Application.configureRouting() {
  install(Resources)

  routing {
    userRoutes()
    authRoutes()
    adminRoutes()
  }
}
