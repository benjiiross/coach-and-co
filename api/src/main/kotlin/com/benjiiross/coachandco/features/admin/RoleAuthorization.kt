package com.benjiiross.coachandco.features.admin

import com.benjiiross.coachandco.core.exceptions.ForbiddenException
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal

val RoleBasedAuthorization =
    createRouteScopedPlugin(
        name = "RoleBasedAuthorization",
        createConfiguration = { AuthorizationConfig() },
    ) {
      on(AuthenticationChecked) { call ->
        val principal = call.principal<JWTPrincipal>()
        val userRole = principal?.payload?.getClaim("role")?.asString()

        if (userRole != pluginConfig.requiredRole) {
          throw ForbiddenException()
        }
      }
    }
