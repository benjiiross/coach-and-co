package com.benjiiross.coachandco.di

import com.benjiiross.coachandco.presentation.screens.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module { viewModel { LoginViewModel(get()) } }
