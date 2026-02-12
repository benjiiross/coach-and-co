package com.benjiiross.coachandco

import androidx.compose.runtime.Composable
import com.benjiiross.coachandco.di.repositoryModule
import com.benjiiross.coachandco.di.viewModelModule
import com.benjiiross.coachandco.presentation.GlobalAppContainer
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = { modules(viewModelModule, repositoryModule) }) {
        GlobalAppContainer()
    }
}
