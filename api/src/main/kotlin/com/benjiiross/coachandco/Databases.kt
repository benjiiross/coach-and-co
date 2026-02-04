package com.benjiiross.coachandco

import com.benjiiross.coachandco.data.UsersTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureDatabases() {
  Database.connect(
      url = Env.dbUrl,
      user = Env.dbUser,
      password = Env.dbPassword,
      driver = "org.postgresql.Driver",
  )
  transaction { SchemaUtils.create(UsersTable) }
}
