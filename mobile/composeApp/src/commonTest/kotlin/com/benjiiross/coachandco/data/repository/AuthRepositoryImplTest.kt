package com.benjiiross.coachandco.data.repository

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.LoginRequest
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import com.benjiiross.coachandco.dto.auth.UserResponse
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
import kotlin.test.assertTrue

class AuthRepositoryImplTest {

    private val baseUrl = "http://test.local"
    private val birthday = LocalDate(1990, 1, 1)
    private val json = Json { ignoreUnknownKeys = true }

    private val fakeUserResponse = UserResponse(
        id = 1, email = "user@test.com", firstName = "John", lastName = "Doe",
        gender = Gender.MALE, birthday = birthday, phone = "0600000000", type = UserType.CLIENT,
    )

    private val fakeAuthResponse = AuthResponse(token = "fake.token", user = fakeUserResponse)

    private fun buildClient(engine: MockEngine) = HttpClient(engine) {
        install(ContentNegotiation) { json(json) }
        install(Resources)
    }

    private fun jsonContent() = headersOf(HttpHeaders.ContentType, "application/json")

    // ── login ─────────────────────────────────────────────────────────────────

    @Test
    fun `login returns Success on HTTP 200`() = runTest {
        val engine = MockEngine {
            respond(json.encodeToString(AuthResponse.serializer(), fakeAuthResponse), HttpStatusCode.OK, jsonContent())
        }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val result = repo.login(LoginRequest("user@test.com", "password"))

        assertIs<Outcome.Success<AuthResponse>>(result)
        assertEquals("fake.token", result.value.token)
    }

    @Test
    fun `login returns InvalidCredentials on HTTP 401`() = runTest {
        val engine = MockEngine {
            respond("", HttpStatusCode.Unauthorized, jsonContent())
        }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val result = repo.login(LoginRequest("user@test.com", "wrong"))

        assertIs<Outcome.Failure<AuthError>>(result)
        assertEquals(AuthError.InvalidCredentials, result.error)
    }

    @Test
    fun `login returns ServerError on HTTP 500`() = runTest {
        val engine = MockEngine {
            respond("", HttpStatusCode.InternalServerError, jsonContent())
        }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val result = repo.login(LoginRequest("user@test.com", "password"))

        assertIs<Outcome.Failure<AuthError>>(result)
        assertEquals(AuthError.ServerError, result.error)
    }

    @Test
    fun `login returns NetworkError on connection failure`() = runTest {
        val engine = MockEngine { throw Exception("Network failure") }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val result = repo.login(LoginRequest("user@test.com", "password"))

        assertIs<Outcome.Failure<AuthError>>(result)
        assertEquals(AuthError.NetworkError, result.error)
    }

    // ── refreshToken ──────────────────────────────────────────────────────────

    @Test
    fun `refreshToken returns Success with new tokens on HTTP 200`() = runTest {
        val refreshedResponse = fakeAuthResponse.copy(
            token = "new.access.token",
            refreshToken = "new.refresh.token",
        )
        val engine = MockEngine {
            respond(json.encodeToString(AuthResponse.serializer(), refreshedResponse), HttpStatusCode.OK, jsonContent())
        }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val result = repo.refreshToken("old.refresh.token")

        assertIs<Outcome.Success<AuthResponse>>(result)
        assertEquals("new.access.token", result.value.token)
        assertEquals("new.refresh.token", result.value.refreshToken)
    }

    @Test
    fun `refreshToken returns Unauthorized on HTTP 401`() = runTest {
        val engine = MockEngine {
            respond("", HttpStatusCode.Unauthorized, jsonContent())
        }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val result = repo.refreshToken("expired.refresh.token")

        assertIs<Outcome.Failure<AuthError>>(result)
        assertEquals(AuthError.Unauthorized, result.error)
    }

    @Test
    fun `refreshToken returns ServerError on HTTP 500`() = runTest {
        val engine = MockEngine {
            respond("", HttpStatusCode.InternalServerError, jsonContent())
        }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val result = repo.refreshToken("refresh.token")

        assertIs<Outcome.Failure<AuthError>>(result)
        assertEquals(AuthError.ServerError, result.error)
    }

    @Test
    fun `refreshToken returns NetworkError on connection failure`() = runTest {
        val engine = MockEngine { throw Exception("Network failure") }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val result = repo.refreshToken("refresh.token")

        assertIs<Outcome.Failure<AuthError>>(result)
        assertEquals(AuthError.NetworkError, result.error)
    }

    // ── register ──────────────────────────────────────────────────────────────

    @Test
    fun `register returns Success on HTTP 200`() = runTest {
        val engine = MockEngine {
            respond(json.encodeToString(AuthResponse.serializer(), fakeAuthResponse), HttpStatusCode.OK, jsonContent())
        }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val request = RegisterRequest(
            email = "new@test.com", password = "password", firstName = "John",
            lastName = "Doe", gender = Gender.MALE, birthday = birthday,
            phone = "0600000000", type = UserType.CLIENT,
        )

        val result = repo.register(request)

        assertIs<Outcome.Success<AuthResponse>>(result)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `register returns InvalidCredentials on HTTP 409 Conflict`() = runTest {
        val engine = MockEngine {
            respond("", HttpStatusCode.Conflict, jsonContent())
        }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val request = RegisterRequest(
            email = "taken@test.com", password = "password", firstName = "John",
            lastName = "Doe", gender = Gender.MALE, birthday = birthday,
            phone = "0600000000", type = UserType.CLIENT,
        )

        val result = repo.register(request)

        assertIs<Outcome.Failure<AuthError>>(result)
        assertEquals(AuthError.InvalidCredentials, result.error)
    }

    @Test
    fun `register returns NetworkError on connection failure`() = runTest {
        val engine = MockEngine { throw Exception("Network failure") }
        val repo = AuthRepositoryImpl(buildClient(engine), baseUrl)

        val request = RegisterRequest(
            email = "new@test.com", password = "password", firstName = "John",
            lastName = "Doe", gender = Gender.MALE, birthday = birthday,
            phone = "0600000000", type = UserType.CLIENT,
        )

        val result = repo.register(request)

        assertIs<Outcome.Failure<AuthError>>(result)
        assertEquals(AuthError.NetworkError, result.error)
    }
}
