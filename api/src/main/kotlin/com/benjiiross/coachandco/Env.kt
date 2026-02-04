package com.benjiiross.coachandco

object Env {
  val dbUrl = getEnvOrThrow("DB_URL")
  val dbUser = getEnvOrThrow("DB_USERNAME")
  val dbPassword = getEnvOrThrow("DB_PASSWORD")
  val dbDriver = getEnvOrThrow("DB_DRIVER")

  val jwtSecret = getEnvOrThrow("JWT_SECRET")

  private fun getEnvOrThrow(name: String) =
      System.getenv(name) ?: error("CRITICAL ERROR: Missing env variable [$name].")
}
