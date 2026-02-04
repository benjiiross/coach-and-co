package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.core.config.Env
import com.benjiiross.coachandco.data.tables.UsersTable
import io.ktor.server.application.Application
import io.ktor.server.application.log
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

fun Application.configureDatabases() {
  Database.connect(
      url = Env.dbUrl,
      user = Env.dbUser,
      password = Env.dbPassword,
      driver = Env.dbDriver,
  )
  transaction { SchemaUtils.create(UsersTable) }

  log.info("Successfully connected to DB")
}
