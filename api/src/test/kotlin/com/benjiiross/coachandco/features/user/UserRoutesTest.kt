package com.benjiiross.coachandco.features.user

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.benjiiross.coachandco.core.exceptions.ResourceNotFoundException
import com.benjiiross.coachandco.domain.services.UserService
import com.benjiiross.coachandco.features.userRoutes
import com.benjiiross.coachandco.plugins.configureSecurity
import com.benjiiross.coachandco.plugins.configureSerialization
import com.benjiiross.coachandco.plugins.configureStatusPages
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.assertEquals

class UserRoutesTest {

    private val secret = "test-secret"
    private val domain = "http://test.local"
    private val audience = "test-audience"

    private val userService = mockk<UserService>()

    private val fakeToken = JWT.create()
        .withAudience(audience)
        .withIssuer(domain)
        .withClaim("userId", 1)
        .sign(Algorithm.HMAC256(secret))

    @Before
    fun setUp() {
        startKoin { modules(module { single { userService } }) }
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
                routing { userRoutes() }
            }
            block()
        }

    @Test
    fun `GET users-id with invalid ID should return 400 BadRequest`() = testApp {
        val response = client.get("/users/abc") {
            bearerAuth(fakeToken)
        }
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `GET users with unknown email should return 404`() = testApp {
        coEvery { userService.getUserByEmail("nonexistent@test.com") } throws ResourceNotFoundException("User")

        val response = client.get("/users?email=nonexistent@test.com") {
            bearerAuth(fakeToken)
        }
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
