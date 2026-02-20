package com.benjiiross.coachandco.domain.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.benjiiross.coachandco.core.exceptions.EmailAlreadyTakenException
import com.benjiiross.coachandco.core.exceptions.EmailOrPasswordIncorrect
import com.benjiiross.coachandco.core.exceptions.InvalidJWT
import com.benjiiross.coachandco.core.exceptions.ResourceNotFoundException
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.domain.model.User
import com.benjiiross.coachandco.domain.repository.UserRepository
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import io.ktor.server.auth.jwt.JWTPrincipal
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.mindrot.jbcrypt.BCrypt

class AuthServiceTest {

    private val repo = mockk<UserRepository>()
    private val secret = "test-secret"
    private val domain = "http://test.local"
    private val audience = "test-audience"
    private val service = AuthService(repo, secret, domain, audience)

    private val birthday = LocalDate(1990, 1, 1)

    private fun userWithHashedPassword(
        id: Int? = 1,
        email: String = "user@test.com",
        password: String = "password123",
    ) = User(
        id = id,
        email = email,
        passwordHash = BCrypt.hashpw(password, BCrypt.gensalt()),
        firstName = "Test",
        lastName = "User",
        gender = Gender.MALE,
        birthday = birthday,
        phone = "0600000000",
        type = UserType.CLIENT,
    )

    // ── generateToken ─────────────────────────────────────────────────────────

    @Test
    fun `generateToken produces verifiable JWT with userId claim`() {
        val token = service.generateToken(42)

        val decoded = JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(domain)
            .build()
            .verify(token)

        assertEquals(42, decoded.getClaim("userId").asInt())
    }

    @Test
    fun `generateToken sets expiry approximately 1 hour from now`() {
        val before = System.currentTimeMillis()
        val token = service.generateToken(1)
        val after = System.currentTimeMillis()

        val expiry = JWT.decode(token).expiresAt.time

        assertTrue(expiry > before + 3_500_000L) // at least ~58 min
        assertTrue(expiry < after + 3_700_000L)  // at most ~61 min
    }

    @Test
    fun `generateToken uses correct audience and issuer`() {
        val token = service.generateToken(1)
        val decoded = JWT.decode(token)

        assertTrue(decoded.audience.contains(audience))
        assertEquals(domain, decoded.issuer)
    }

    // ── login ─────────────────────────────────────────────────────────────────

    @Test
    fun `login returns user when credentials are correct`() = runTest {
        val user = userWithHashedPassword()
        coEvery { repo.findByEmail("user@test.com") } returns user

        val result = service.login("user@test.com", "password123")

        assertEquals("user@test.com", result.email)
        coVerify(exactly = 1) { repo.findByEmail("user@test.com") }
    }

    @Test
    fun `login throws EmailOrPasswordIncorrect when email is not found`() = runTest {
        coEvery { repo.findByEmail(any()) } returns null

        assertFailsWith<EmailOrPasswordIncorrect> {
            service.login("nobody@test.com", "password123")
        }
    }

    @Test
    fun `login throws EmailOrPasswordIncorrect when password is wrong`() = runTest {
        val user = userWithHashedPassword(password = "correct_password")
        coEvery { repo.findByEmail(any()) } returns user

        assertFailsWith<EmailOrPasswordIncorrect> {
            service.login("user@test.com", "wrong_password")
        }
    }

    // ── register ──────────────────────────────────────────────────────────────

    @Test
    fun `register creates user with hashed password`() = runTest {
        val request = RegisterRequest(
            email = "new@test.com",
            password = "plaintext",
            firstName = "New",
            lastName = "User",
            gender = Gender.FEMALE,
            birthday = birthday,
            phone = "0700000000",
            type = UserType.CLIENT,
        )
        val capturedUser = slot<User>()
        coEvery { repo.findByEmail(request.email) } returns null
        coEvery { repo.createUser(capture(capturedUser)) } answers {
            capturedUser.captured.copy(id = 10)
        }

        val result = service.register(request)

        assertEquals(10, result.id)
        assertEquals("new@test.com", result.email)
        assertTrue(BCrypt.checkpw("plaintext", capturedUser.captured.passwordHash))
    }

    @Test
    fun `register throws EmailAlreadyTakenException when email is taken`() = runTest {
        coEvery { repo.findByEmail(any()) } returns userWithHashedPassword()

        assertFailsWith<EmailAlreadyTakenException> {
            service.register(
                RegisterRequest("user@test.com", "pw", "A", "B", Gender.MALE, birthday, "0600000000", UserType.CLIENT)
            )
        }
    }

    // ── me ────────────────────────────────────────────────────────────────────

    @Test
    fun `me returns UserResponse for valid JWT principal`() = runTest {
        val user = userWithHashedPassword(id = 1)
        coEvery { repo.findById(1) } returns user

        val token = service.generateToken(1)
        val principal = JWTPrincipal(JWT.decode(token))

        val result = service.me(principal)

        assertEquals("user@test.com", result.email)
        assertEquals(1, result.id)
    }

    @Test
    fun `me throws InvalidJWT when principal is null`() = runTest {
        assertFailsWith<InvalidJWT> { service.me(null) }
    }

    @Test
    fun `me throws ResourceNotFoundException when user is not found`() = runTest {
        coEvery { repo.findById(any()) } returns null

        val token = service.generateToken(999)
        val principal = JWTPrincipal(JWT.decode(token))

        assertFailsWith<ResourceNotFoundException> { service.me(principal) }
    }
}
