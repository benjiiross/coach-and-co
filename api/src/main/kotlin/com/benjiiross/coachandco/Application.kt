package com.benjiiross.coachandco

import io.ktor.server.application.Application

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureFrameworks()
  configureSerialization()
  configureDatabases()
  configureHTTP()
  configureMonitoring()
  configureSecurity()
  configureRouting()
}
