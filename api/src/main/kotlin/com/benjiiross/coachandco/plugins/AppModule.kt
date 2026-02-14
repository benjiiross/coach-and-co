package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.core.config.Env
import com.benjiiross.coachandco.data.repositories.UserRepositoryImpl
import com.benjiiross.coachandco.domain.repository.UserRepository
import com.benjiiross.coachandco.domain.services.AuthService
import com.benjiiross.coachandco.domain.services.UserService
import org.koin.dsl.module

val appModule = module {
  single<UserRepository> { UserRepositoryImpl() }
  single { UserService(get()) }

  single { AuthService(get(), Env.jwtSecret, Env.jwtDomain, Env.jwtAudience) }
}
