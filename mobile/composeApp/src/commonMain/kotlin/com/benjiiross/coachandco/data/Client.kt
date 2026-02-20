package com.benjiiross.coachandco.data

import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.RefreshRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createClient(tokenStorage: TokenStorage) = HttpClient(CIO) {
    install(Resources)
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        })
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30_000
        connectTimeoutMillis = 30_000
        socketTimeoutMillis = 30_000
    }

    defaultRequest {
        url(ApiConfig.URL)
    }

    install(Auth) {
        bearer {
            loadTokens {
                val token = tokenStorage.getToken() ?: return@loadTokens null
                BearerTokens(
                    accessToken = token,
                    refreshToken = tokenStorage.getRefreshToken() ?: "",
                )
            }

            refreshTokens {
                val refreshToken =
                    oldTokens?.refreshToken?.takeIf { it.isNotEmpty() }
                        ?: run {
                            tokenStorage.clearAll()
                            return@refreshTokens null
                        }

                val response =
                    try {
                        client.post("${ApiConfig.URL}/auth/refresh") {
                            contentType(ContentType.Application.Json)
                            setBody(RefreshRequest(refreshToken))
                            markAsRefreshTokenRequest()
                        }
                    } catch (_: Exception) {
                        tokenStorage.clearAll()
                        return@refreshTokens null
                    }

                if (response.status == HttpStatusCode.OK) {
                    val body = response.body<AuthResponse>()
                    tokenStorage.saveToken(body.token)
                    body.refreshToken?.let { tokenStorage.saveRefreshToken(it) }
                    BearerTokens(
                        accessToken = body.token,
                        refreshToken = body.refreshToken ?: refreshToken,
                    )
                } else {
                    tokenStorage.clearAll()
                    null
                }
            }
        }
    }
}
