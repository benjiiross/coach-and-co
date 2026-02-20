package com.benjiiross.coachandco.mappers

import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.domain.model.User
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlinx.datetime.LocalDate

class UserMapperTest {

    private val birthday = LocalDate(1990, 6, 15)

    private fun user(id: Int? = 1) = User(
        id = id,
        email = "john@example.com",
        passwordHash = "hashedpw",
        firstName = "John",
        lastName = "Doe",
        gender = Gender.MALE,
        birthday = birthday,
        phone = "0600000000",
        type = UserType.CLIENT,
    )

    // ── toResponse ────────────────────────────────────────────────────────────

    @Test
    fun `toResponse maps all fields correctly`() {
        val response = user(id = 42).toResponse()

        assertEquals(42, response.id)
        assertEquals("john@example.com", response.email)
        assertEquals("John", response.firstName)
        assertEquals("Doe", response.lastName)
        assertEquals(Gender.MALE, response.gender)
        assertEquals(birthday, response.birthday)
        assertEquals("0600000000", response.phone)
        assertEquals(UserType.CLIENT, response.type)
    }

    @Test
    fun `toResponse throws when id is null`() {
        assertFailsWith<IllegalArgumentException> {
            user(id = null).toResponse()
        }
    }

    // ── toModel ───────────────────────────────────────────────────────────────

    @Test
    fun `toModel strips the id`() {
        val model = user(id = 99).copy(id = null)

        assertNull(model.id)
        assertEquals("john@example.com", model.email)
        assertEquals("hashedpw", model.passwordHash)
        assertEquals("John", model.firstName)
        assertEquals("Doe", model.lastName)
    }

    // ── RegisterRequest.toUser ────────────────────────────────────────────────

    @Test
    fun `RegisterRequest toUser maps all fields and uses provided hash`() {
        val request = RegisterRequest(
            email = "jane@example.com",
            password = "plain",
            firstName = "Jane",
            lastName = "Smith",
            gender = Gender.FEMALE,
            birthday = birthday,
            phone = "0700000000",
            type = UserType.COACH,
        )

        val user = request.toUser("hashed_value")

        assertNull(user.id)
        assertEquals("jane@example.com", user.email)
        assertEquals("hashed_value", user.passwordHash)
        assertEquals("Jane", user.firstName)
        assertEquals("Smith", user.lastName)
        assertEquals(Gender.FEMALE, user.gender)
        assertEquals(birthday, user.birthday)
        assertEquals("0700000000", user.phone)
        assertEquals(UserType.COACH, user.type)
    }

    // ── List.toResponse ───────────────────────────────────────────────────────

    @Test
    fun `List toResponse maps each user preserving order`() {
        val users = listOf(user(id = 1), user(id = 2), user(id = 3))
        val responses = users.toResponse()

        assertEquals(3, responses.size)
        assertEquals(1, responses[0].id)
        assertEquals(2, responses[1].id)
        assertEquals(3, responses[2].id)
    }

    @Test
    fun `empty list toResponse returns empty list`() {
        assertEquals(emptyList(), emptyList<User>().toResponse())
    }
}
