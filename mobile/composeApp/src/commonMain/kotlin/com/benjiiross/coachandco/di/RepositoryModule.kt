package com.benjiiross.coachandco.di

import com.benjiiross.coachandco.data.auth.AuthRepositoryImpl
import com.benjiiross.coachandco.domain.auth.AuthRepository
import org.koin.dsl.module

val repositoryModule = module { single<AuthRepository> { AuthRepositoryImpl() } }
