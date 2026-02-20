package com.benjiiross.coachandco.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.string

class TokenStorage(private val settings: Settings) {
    private var storedToken by settings.string(KEY, defaultValue = "")

    fun getToken(): String? = storedToken.ifEmpty { null }

    fun saveToken(value: String) {
        storedToken = value
    }

    fun clearToken() {
        settings.remove(KEY)
    }

    private companion object {
        const val KEY = "auth_token"
    }
}
