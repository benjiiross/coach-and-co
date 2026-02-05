package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.database.DatabaseFactory
import io.ktor.server.application.Application
import io.ktor.server.application.log

fun Application.configureDatabases() {
  DatabaseFactory.init()
  log.info("Database initialized successfully")
}
