package com.benjiiross.coachandco.data.repository

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.dto.auth.UserResponse
import com.benjiiross.coachandco.dto.profile.ProfileError
import com.benjiiross.coachandco.dto.profile.UpdateProfileRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ProfileRepositoryImplTest {

    private val json = Json { ignoreUnknownKeys = true }
    private val birthday = LocalDate(1990, 1, 1)

    private val fakeUserResponse = UserResponse(
        id = 1, email = "user@test.com", firstName = "John", lastName = "Doe",
        gender = Gender.MALE, birthday = birthday, phone = "0600000000", type = UserType.CLIENT,
    )

    private fun buildClient(engine: MockEngine) = HttpClient(engine) {
        install(ContentNegotiation) { json(json) }
        install(Resources)
    }

    private fun jsonContent() = headersOf(HttpHeaders.ContentType, "application/json")

    // ── getProfile ────────────────────────────────────────────────────────────

    @Test
    fun `getProfile returns Success on HTTP 200`() = runTest {
        val engine = MockEngine {
            respond(json.encodeToString(UserResponse.serializer(), fakeUserResponse), HttpStatusCode.OK, jsonContent())
        }
        val repo = ProfileRepositoryImpl(buildClient(engine))

        val result = repo.getProfile()

        assertIs<Outcome.Success<UserResponse, ProfileError>>(result)
        assertEquals("user@test.com", result.value.email)
    }

    @Test
    fun `getProfile returns Unauthorized on HTTP 401`() = runTest {
        val engine = MockEngine { respond("", HttpStatusCode.Unauthorized, jsonContent()) }
        val repo = ProfileRepositoryImpl(buildClient(engine))

        val result = repo.getProfile()

        assertIs<Outcome.Failure<UserResponse, ProfileError>>(result)
        assertEquals(ProfileError.Unauthorized, result.error)
    }

    @Test
    fun `getProfile returns NotFound on HTTP 404`() = runTest {
        val engine = MockEngine { respond("", HttpStatusCode.NotFound, jsonContent()) }
        val repo = ProfileRepositoryImpl(buildClient(engine))

        val result = repo.getProfile()

        assertIs<Outcome.Failure<UserResponse, ProfileError>>(result)
        assertEquals(ProfileError.NotFound, result.error)
    }

    @Test
    fun `getProfile returns ServerError on HTTP 500`() = runTest {
        val engine = MockEngine { respond("", HttpStatusCode.InternalServerError, jsonContent()) }
        val repo = ProfileRepositoryImpl(buildClient(engine))

        val result = repo.getProfile()

        assertIs<Outcome.Failure<UserResponse, ProfileError>>(result)
        assertEquals(ProfileError.ServerError, result.error)
    }

    @Test
    fun `getProfile returns NetworkError on connection failure`() = runTest {
        val engine = MockEngine { throw Exception("Network failure") }
        val repo = ProfileRepositoryImpl(buildClient(engine))

        val result = repo.getProfile()

        assertIs<Outcome.Failure<UserResponse, ProfileError>>(result)
        assertEquals(ProfileError.NetworkError, result.error)
    }

    // ── updateProfile ─────────────────────────────────────────────────────────

    @Test
    fun `updateProfile returns Success on HTTP 200`() = runTest {
        val engine = MockEngine {
            respond(json.encodeToString(UserResponse.serializer(), fakeUserResponse), HttpStatusCode.OK, jsonContent())
        }
        val repo = ProfileRepositoryImpl(buildClient(engine))

        val result = repo.updateProfile(UpdateProfileRequest(firstName = "John", lastName = "Doe"))

        assertIs<Outcome.Success<UserResponse, ProfileError>>(result)
        assertEquals(1, result.value.id)
    }

    @Test
    fun `updateProfile returns Unauthorized on HTTP 401`() = runTest {
        val engine = MockEngine { respond("", HttpStatusCode.Unauthorized, jsonContent()) }
        val repo = ProfileRepositoryImpl(buildClient(engine))

        val result = repo.updateProfile(UpdateProfileRequest(firstName = "John", lastName = "Doe"))

        assertIs<Outcome.Failure<UserResponse, ProfileError>>(result)
        assertEquals(ProfileError.Unauthorized, result.error)
    }

    @Test
    fun `updateProfile returns NetworkError on connection failure`() = runTest {
        val engine = MockEngine { throw Exception("Network failure") }
        val repo = ProfileRepositoryImpl(buildClient(engine))

        val result = repo.updateProfile(UpdateProfileRequest(firstName = "John", lastName = "Doe"))

        assertIs<Outcome.Failure<UserResponse, ProfileError>>(result)
        assertEquals(ProfileError.NetworkError, result.error)
    }
}
