package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.core.exceptions.AppContextException
import com.benjiiross.coachandco.dto.error.ApiErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<AppContextException> { call, cause ->
            call.respond(
                status = cause.statusCode,
                message = ApiErrorResponse(
                    code = cause::class.simpleName ?: "UNKNOWN_ERROR",
                    message = cause.message
                )
            )
        }

        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ApiErrorResponse(
                    code = "INTERNAL_SERVER_ERROR",
                    message = "An unexpected error occurred"
                )
            )
        }
    }
}
