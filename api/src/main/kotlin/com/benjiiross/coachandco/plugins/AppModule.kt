package com.benjiiross.coachandco.plugins

import com.benjiiross.coachandco.data.repositories.UserRepositoryImpl
import com.benjiiross.coachandco.domain.repository.UserRepository
import com.benjiiross.coachandco.domain.services.AuthService
import com.benjiiross.coachandco.domain.services.UserService
import org.koin.dsl.module
import io.ktor.server.config.ApplicationConfig


fun createAppModule(config: ApplicationConfig) = module {
    single<UserRepository> { UserRepositoryImpl() }
    single { UserService(get()) }

    single {
        AuthService(
            get(),
            jwtSecret = config.propertyOrNull("jwt.secret")!!.getString(),
            jwtDomain = config.propertyOrNull("jwt.domain")!!.getString(),
            jwtAudience = config.propertyOrNull("jwt.audience")!!.getString(),
        )
    }
}
