package com.benjiiross.coachandco.di

import com.benjiiross.coachandco.data.repository.AuthRepositoryImpl
import com.benjiiross.coachandco.domain.repository.AuthRepository
import com.benjiiross.coachandco.presentation.screens.login.LoginViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModules = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
            defaultRequest {
                url("")
            }
        }
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            client = get(),
            baseUrl = ""
        )
    }

    viewModelOf(::LoginViewModel)
}