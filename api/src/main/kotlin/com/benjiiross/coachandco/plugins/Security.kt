package com.benjiiross.coachandco.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.benjiiross.coachandco.core.config.Env
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureSecurity() {
  val jwtAudience = "coach-and-co-users"
  val jwtDomain = "https://coach-and-co.com"
  val jwtSecret = Env.jwtSecret

  install(Authentication) {
    jwt("auth-jwt") {
      realm = "Access to 'me' and private routes"
      verifier(
          JWT.require(Algorithm.HMAC256(jwtSecret))
              .withAudience(jwtAudience)
              .withIssuer(jwtDomain)
              .build()
      )
      validate { credential ->
        val containsAudience = credential.payload.audience.contains(jwtAudience)
        val hasUserId = credential.payload.getClaim("userId").asInt() != null

        if (containsAudience && hasUserId) {
          JWTPrincipal(credential.payload)
        } else {
          null
        }
      }
    }
  }
}
