package com.benjiiross.coachandco.data.repository

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.domain.repository.AuthRepository
import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.LoginRequest
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import com.benjiiross.coachandco.dto.error.ApiErrorResponse
import com.benjiiross.coachandco.routes.Api
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
class AuthRepositoryImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : AuthRepository {
    override suspend fun login(authDetails: LoginRequest): Outcome<AuthResponse, AuthError> {
        return try {
            val response: HttpResponse = client.post(Api.Auth.Login()) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email = authDetails.email, password = authDetails.password))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    Outcome.Success(authResponse)
                }
                HttpStatusCode.Unauthorized -> {
                    Outcome.Failure(AuthError.InvalidCredentials)
                }
                HttpStatusCode.InternalServerError -> {
                    Outcome.Failure(AuthError.ServerError)
                }
                else -> {
                    try {
                        val errorResponse = response.body<ApiErrorResponse>()
                        Outcome.Failure(AuthError.Unknown(errorResponse.message))
                    } catch (e: Exception) {
                        Outcome.Failure(AuthError.Unknown("Unknown error occurred"))
                    }
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> Outcome.Failure(AuthError.InvalidCredentials)
                else -> Outcome.Failure(AuthError.Unknown(e.message))
            }
        } catch (e: ServerResponseException) {
            Outcome.Failure(AuthError.ServerError)
        } catch (e: Exception) {
            Outcome.Failure(AuthError.NetworkError)
        }
    }

    override suspend fun register(user: RegisterRequest): Outcome<AuthResponse, AuthError> {
        return try {
            val response: HttpResponse = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    Outcome.Success(authResponse)
                }

                HttpStatusCode.Unauthorized -> {
                    Outcome.Failure(AuthError.InvalidCredentials)
                }

                HttpStatusCode.Conflict -> {
                    Outcome.Failure(AuthError.InvalidCredentials)
                }

                HttpStatusCode.InternalServerError -> {
                    Outcome.Failure(AuthError.ServerError)
                }

                else -> {
                    try {
                        val errorResponse = response.body<ApiErrorResponse>()
                        Outcome.Failure(AuthError.Unknown(errorResponse.message))
                    } catch (_: Exception) {
                        Outcome.Failure(AuthError.Unknown("Unknown error occurred"))
                    }
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> Outcome.Failure(AuthError.InvalidCredentials)
                else -> Outcome.Failure(AuthError.Unknown(e.message))
            }
        } catch (_: ServerResponseException) {
            Outcome.Failure(AuthError.ServerError)
        } catch (_: Exception) {
            Outcome.Failure(AuthError.NetworkError)
        }
    }
}