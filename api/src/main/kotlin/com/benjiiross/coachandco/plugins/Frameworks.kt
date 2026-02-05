package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.data.repositories.PostgresUserRepository
import com.benjiiross.coachandco.domain.repositories.IUserRepository
import com.benjiiross.coachandco.domain.services.UserService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
  single<IUserRepository> { PostgresUserRepository() }
  single { UserService(get()) }
}

fun Application.configureFrameworks() {
  install(Koin) {
    slf4jLogger()
    modules(appModule)
  }
}
