package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.core.config.Env
import com.benjiiross.coachandco.database.repositories.UserRepositoryImpl
import com.benjiiross.coachandco.domain.repositories.IUserRepository
import com.benjiiross.coachandco.domain.services.AuthService
import com.benjiiross.coachandco.domain.services.UserService
import org.koin.dsl.module

val appModule = module {
  single<IUserRepository> { UserRepositoryImpl() }
  single { UserService(get()) }

  single { AuthService(get(), Env.jwtSecret, Env.jwtIssuer, Env.jwtAudience) }
}
