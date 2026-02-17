package com.benjiiross.coachandco.di

import com.benjiiross.coachandco.data.client
import com.benjiiross.coachandco.data.repository.AuthRepositoryImpl
import com.benjiiross.coachandco.domain.repository.AuthRepository
import com.benjiiross.coachandco.presentation.screens.login.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModules = module {
    single { client }

    single<AuthRepository> {
        AuthRepositoryImpl(
            client = get(),
            baseUrl = "http://192.168.10.16:8080"
        )
    }

    viewModelOf(::LoginViewModel)
}