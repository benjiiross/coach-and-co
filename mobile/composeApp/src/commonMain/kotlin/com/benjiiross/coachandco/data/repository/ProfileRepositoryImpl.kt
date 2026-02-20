package com.benjiiross.coachandco.data.repository

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.repository.ProfileRepository
import com.benjiiross.coachandco.dto.auth.UserResponse
import com.benjiiross.coachandco.dto.error.ApiErrorResponse
import com.benjiiross.coachandco.dto.profile.ProfileError
import com.benjiiross.coachandco.dto.profile.UpdateProfileRequest
import com.benjiiross.coachandco.routes.Api
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class ProfileRepositoryImpl(
    private val client: HttpClient,
) : ProfileRepository {

    override suspend fun getProfile(): Outcome<UserResponse, ProfileError> {
        return try {
            val response = client.get(Api.Auth.Me())

            when (response.status) {
                HttpStatusCode.OK -> Outcome.Success(response.body<UserResponse>())
                HttpStatusCode.Unauthorized -> Outcome.Failure(ProfileError.Unauthorized)
                HttpStatusCode.NotFound -> Outcome.Failure(ProfileError.NotFound)
                HttpStatusCode.InternalServerError -> Outcome.Failure(ProfileError.ServerError)
                else -> {
                    val error = runCatching { response.body<ApiErrorResponse>() }.getOrNull()
                    Outcome.Failure(ProfileError.Unknown(error?.message ?: "Unknown error"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> Outcome.Failure(ProfileError.Unauthorized)
                else -> Outcome.Failure(ProfileError.Unknown(e.message ?: "Request failed"))
            }
        } catch (e: ServerResponseException) {
            Outcome.Failure(ProfileError.ServerError)
        } catch (e: Exception) {
            Outcome.Failure(ProfileError.NetworkError)
        }
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Outcome<UserResponse, ProfileError> {
        return try {
            val response = client.patch(Api.Auth.Me()) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

            when (response.status) {
                HttpStatusCode.OK -> Outcome.Success(response.body<UserResponse>())
                HttpStatusCode.Unauthorized -> Outcome.Failure(ProfileError.Unauthorized)
                HttpStatusCode.NotFound -> Outcome.Failure(ProfileError.NotFound)
                HttpStatusCode.InternalServerError -> Outcome.Failure(ProfileError.ServerError)
                else -> {
                    val error = runCatching { response.body<ApiErrorResponse>() }.getOrNull()
                    Outcome.Failure(ProfileError.Unknown(error?.message ?: "Unknown error"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> Outcome.Failure(ProfileError.Unauthorized)
                else -> Outcome.Failure(ProfileError.Unknown(e.message ?: "Request failed"))
            }
        } catch (e: ServerResponseException) {
            Outcome.Failure(ProfileError.ServerError)
        } catch (e: Exception) {
            Outcome.Failure(ProfileError.NetworkError)
        }
    }
}