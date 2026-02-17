package com.benjiiross.coachandco.data.repository

import com.benjiiross.coachandco.core.Result
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
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
import kotlinx.datetime.LocalDate

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : AuthRepository {
    override suspend fun login(authDetails: LoginRequest): Result<AuthResponse, AuthError> {
        return try {
            val response: HttpResponse = client.post(Api.Auth.Login()) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email = authDetails.email, password = authDetails.password))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    Result.Success(authResponse)
                }
                HttpStatusCode.Unauthorized -> {
                    Result.Failure(AuthError.InvalidCredentials)
                }
                HttpStatusCode.InternalServerError -> {
                    Result.Failure(AuthError.ServerError)
                }
                else -> {
                    try {
                        val errorResponse = response.body<ApiErrorResponse>()
                        Result.Failure(AuthError.Unknown(errorResponse.message))
                    } catch (e: Exception) {
                        Result.Failure(AuthError.Unknown("Unknown error occurred"))
                    }
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> Result.Failure(AuthError.InvalidCredentials)
                else -> Result.Failure(AuthError.Unknown(e.message ?: "Request failed"))
            }
        } catch (e: ServerResponseException) {
            Result.Failure(AuthError.ServerError)
        } catch (e: Exception) {
            Result.Failure(AuthError.NetworkError)
        }
    }

    override suspend fun register(user: RegisterRequest): Result<AuthResponse, AuthError> {
        return try {
            val response: HttpResponse = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val authResponse = response.body<AuthResponse>()
                    Result.Success(authResponse)
                }

                HttpStatusCode.Unauthorized -> {
                    Result.Failure(AuthError.InvalidCredentials)
                }

                HttpStatusCode.InternalServerError -> {
                    Result.Failure(AuthError.ServerError)
                }

                else -> {
                    try {
                        val errorResponse = response.body<ApiErrorResponse>()
                        Result.Failure(AuthError.Unknown(errorResponse.message))
                    } catch (e: Exception) {
                        Result.Failure(AuthError.Unknown("Unknown error occurred"))
                    }
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> Result.Failure(AuthError.InvalidCredentials)
                else -> Result.Failure(AuthError.Unknown(e.message ?: "Request failed"))
            }
        } catch (e: ServerResponseException) {
            Result.Failure(AuthError.ServerError)
        } catch (e: Exception) {
            Result.Failure(AuthError.NetworkError)
        }
    }
}