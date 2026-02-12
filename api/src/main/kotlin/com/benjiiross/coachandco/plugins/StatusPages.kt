package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.core.exceptions.AppContextException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureStatusPages() {
  install(StatusPages) {
    exception<AppContextException> { call, cause ->
      call.respond(cause.statusCode, "An unknown error happened")
    }

    exception<Throwable> { call, cause ->
      call.respond(HttpStatusCode.InternalServerError, "An unknown error happened")
      cause.printStackTrace()
    }
  }
}
