package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.data.database.DatabaseFactory
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.config.ApplicationConfig

fun Application.configureDatabases(config : ApplicationConfig) {
  DatabaseFactory.init(config)
  log.info("Database initialized successfully")
}
