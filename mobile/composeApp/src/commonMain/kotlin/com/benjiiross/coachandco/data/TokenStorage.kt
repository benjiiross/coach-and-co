package com.benjiiross.coachandco.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.string

class TokenStorage(private val settings: Settings) {
    private var storedToken by settings.string(ACCESS_KEY, defaultValue = "")
    private var storedRefreshToken by settings.string(REFRESH_KEY, defaultValue = "")

    fun getToken(): String? = storedToken.ifEmpty { null }

    fun saveToken(value: String) {
        storedToken = value
    }

    fun clearToken() {
        settings.remove(ACCESS_KEY)
    }

    fun getRefreshToken(): String? = storedRefreshToken.ifEmpty { null }

    fun saveRefreshToken(value: String) {
        storedRefreshToken = value
    }

    fun clearAll() {
        settings.remove(ACCESS_KEY)
        settings.remove(REFRESH_KEY)
    }

    private companion object {
        const val ACCESS_KEY = "auth_token"
        const val REFRESH_KEY = "auth_refresh_token"
    }
}
