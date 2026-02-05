package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.core.exceptions.EmailAlreadyTakenException
import com.benjiiross.coachandco.core.exceptions.InvalidIdException
import com.benjiiross.coachandco.core.exceptions.ResourceNotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.configureStatusPages() {
  install(StatusPages) {
    exception<InvalidIdException> { call, cause ->
      call.respond(HttpStatusCode.BadRequest, mapOf("error" to cause.message))
    }

    exception<ResourceNotFoundException> { call, cause ->
      call.respond(HttpStatusCode.NotFound, mapOf("error" to cause.message))
    }

    exception<EmailAlreadyTakenException> { call, cause ->
      call.respond(HttpStatusCode.Conflict, mapOf("error" to cause.message))
    }

    exception<Throwable> { call, cause ->
      call.respond(HttpStatusCode.InternalServerError, mapOf("error" to cause.message))
    }
  }
}
