package com.benjiiross.coachandco.di

import com.benjiiross.coachandco.data.ApiConfig
import com.benjiiross.coachandco.data.TokenStorage
import com.benjiiross.coachandco.data.createClient
import com.benjiiross.coachandco.data.repository.AuthRepositoryImpl
import com.benjiiross.coachandco.data.repository.ProfileRepositoryImpl
import com.benjiiross.coachandco.domain.repository.AuthRepository
import com.benjiiross.coachandco.domain.repository.ProfileRepository
import com.benjiiross.coachandco.presentation.profile.ProfileViewModel
import com.benjiiross.coachandco.presentation.screens.login.LoginViewModel
import com.benjiiross.coachandco.presentation.screens.register.RegisterViewModel
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { Settings() }
    single { TokenStorage(get()) }
    single { createClient(get()) }

    single<AuthRepository> {
        AuthRepositoryImpl(
            client = get(),
            baseUrl = ApiConfig.URL,
        )
    }

    single<ProfileRepository> { ProfileRepositoryImpl(get()) }

    viewModel { ProfileViewModel(get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
}
