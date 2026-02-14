package com.benjiiross.coachandco.core.config

object Env {
  val isDevelopment = true
  val dbUrl = System.getenv("DB_URL") ?: ""
  val dbUser = System.getenv("DB_USERNAME") ?: ""
  val dbPassword = System.getenv("DB_PASSWORD") ?: ""
  val dbDriver = System.getenv("DB_DRIVER") ?: ""
  val jwtSecret = System.getenv("JWT_SECRET") ?: ""
  val jwtDomain = System.getenv("JWT_ISSUER") ?: ""
  val jwtAudience = System.getenv("JWT_AUDIENCE") ?: ""
}
