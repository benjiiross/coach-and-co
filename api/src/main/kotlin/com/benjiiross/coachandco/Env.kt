package com.benjiiross.coachandco

object Env {
  val dbUrl = getEnvOrThrow("DB_URL") ?: "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
  val dbUser = getEnvOrThrow("DB_USERNAME") ?: "sa"
  val dbPassword = getEnvOrThrow("DB_PASSWORD") ?: ""
  val dbDriver = getEnvOrThrow("DB_DRIVER") "org.h2.Driver"

  val jwtSecret = getEnvOrThrow("JWT_SECRET")

  private fun getEnvOrThrow(name: String) =
      System.getenv(name) ?: error("CRITICAL ERROR: Missing env variable [$name].")
}
