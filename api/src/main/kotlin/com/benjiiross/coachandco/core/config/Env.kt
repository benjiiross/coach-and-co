package com.benjiiross.coachandco.core.config

object Env {
  val dbUrl = System.getenv("DB_URL") ?: "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
  val dbUser = System.getenv("DB_USERNAME") ?: "sa"
  val dbPassword = System.getenv("DB_PASSWORD") ?: "123"
  val dbDriver = System.getenv("DB_DRIVER") ?: "org.h2.Driver"
  val jwtSecret = System.getenv("JWT_SECRET") ?: ""
}
