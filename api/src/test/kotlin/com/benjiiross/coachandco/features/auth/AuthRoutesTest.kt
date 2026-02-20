package com.benjiiross.coachandco.features.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.benjiiross.coachandco.core.exceptions.EmailAlreadyTakenException
import com.benjiiross.coachandco.core.exceptions.EmailOrPasswordIncorrect
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.domain.model.User
import com.benjiiross.coachandco.domain.services.AuthService
import com.benjiiross.coachandco.dto.auth.AuthResponse
import com.benjiiross.coachandco.dto.auth.LoginRequest
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import com.benjiiross.coachandco.dto.auth.UserResponse
import com.benjiiross.coachandco.features.authRoutes
import com.benjiiross.coachandco.plugins.configureSecurity
import com.benjiiross.coachandco.plugins.configureSerialization
import com.benjiiross.coachandco.plugins.configureStatusPages
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRoutesTest {

    private val secret = "test-secret"
    private val domain = "http://test.local"
    private val audience = "test-audience"

    private val authService = mockk<AuthService>()

    private val birthday = LocalDate(1990, 1, 1)

    private val fakeUser = User(
        id = 1,
        email = "user@test.com",
        passwordHash = "hashed",
        firstName = "John",
        lastName = "Doe",
        gender = Gender.MALE,
        birthday = birthday,
        phone = "0600000000",
        type = UserType.CLIENT,
    )

    private val fakeUserResponse = UserResponse(
        id = 1,
        email = "user@test.com",
        firstName = "John",
        lastName = "Doe",
        gender = Gender.MALE,
        birthday = birthday,
        phone = "0600000000",
        type = UserType.CLIENT,
    )

    private val fakeToken = JWT.create()
        .withAudience(audience)
        .withIssuer(domain)
        .withClaim("userId", 1)
        .sign(Algorithm.HMAC256(secret))

    @Before
    fun setUp() {
        startKoin { modules(module { single { authService } }) }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    private fun testApp(block: suspend io.ktor.server.testing.ApplicationTestBuilder.() -> Unit) =
        testApplication {
            environment {
                config = MapApplicationConfig(
                    "jwt.secret" to secret,
                    "jwt.domain" to domain,
                    "jwt.audience" to audience,
                )
            }
            application {
                configureSerialization()
                configureStatusPages()
                configureSecurity(environment.config)
                install(Resources)
                routing { authRoutes() }
            }
            block()
        }

    // ── POST /auth/login ──────────────────────────────────────────────────────

    @Test
    fun `POST login returns 200 with token on valid credentials`() = testApp {
        coEvery { authService.login(any(), any()) } returns fakeUser
        coEvery { authService.generateToken(1) } returns fakeToken

        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(LoginRequest.serializer(), LoginRequest("user@test.com", "password")))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("token"))
    }

    @Test
    fun `POST login returns 401 on wrong credentials`() = testApp {
        coEvery { authService.login(any(), any()) } throws EmailOrPasswordIncorrect()

        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(LoginRequest.serializer(), LoginRequest("user@test.com", "wrong")))
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    // ── POST /auth/register ───────────────────────────────────────────────────

    @Test
    fun `POST register returns 200 on success`() = testApp {
        coEvery { authService.register(any()) } returns fakeUser
        coEvery { authService.generateToken(1) } returns fakeToken

        val request = RegisterRequest(
            email = "new@test.com",
            password = "password",
            firstName = "John",
            lastName = "Doe",
            gender = Gender.MALE,
            birthday = birthday,
            phone = "0600000000",
            type = UserType.CLIENT,
        )

        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(RegisterRequest.serializer(), request))
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `POST register returns 409 when email is already taken`() = testApp {
        coEvery { authService.register(any()) } throws EmailAlreadyTakenException()

        val request = RegisterRequest(
            email = "taken@test.com",
            password = "password",
            firstName = "John",
            lastName = "Doe",
            gender = Gender.MALE,
            birthday = birthday,
            phone = "0600000000",
            type = UserType.CLIENT,
        )

        val response = client.post("/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(RegisterRequest.serializer(), request))
        }

        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    // ── POST /auth/logout ─────────────────────────────────────────────────────

    @Test
    fun `POST logout returns 200`() = testApp {
        val response = client.post("/auth/logout")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    // ── GET /auth/me ──────────────────────────────────────────────────────────

    @Test
    fun `GET me returns 401 without token`() = testApp {
        val response = client.get("/auth/me")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `GET me returns 200 with valid token`() = testApp {
        coEvery { authService.me(any()) } returns fakeUserResponse

        val response = client.get("/auth/me") {
            bearerAuth(fakeToken)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
