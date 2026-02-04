package com.benjiiross.coachandco

import com.benjiiross.coachandco.plugins.configureDatabases
import com.benjiiross.coachandco.plugins.configureFrameworks
import com.benjiiross.coachandco.plugins.configureHTTP
import com.benjiiross.coachandco.plugins.configureMonitoring
import com.benjiiross.coachandco.plugins.configureRouting
import com.benjiiross.coachandco.plugins.configureSecurity
import com.benjiiross.coachandco.plugins.configureSerialization
import io.ktor.server.application.Application

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureDatabases()
  configureFrameworks()
  configureHTTP()
  configureMonitoring()
  configureRouting()
  configureSecurity()
  configureSerialization()
}
