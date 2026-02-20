package com.benjiiross.coachandco.domain.services

import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.core.exceptions.EmailAlreadyTakenException
import com.benjiiross.coachandco.core.exceptions.ResourceNotFoundException
import com.benjiiross.coachandco.domain.model.User
import com.benjiiross.coachandco.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate

class UserServiceTest {
  private val repo = mockk<UserRepository>()
  private val service = UserService(repo)

  private fun createMockUser(id: Int? = 1) =
      User(
          id = id,
          email = "test@example.com",
          passwordHash = "1234",
          firstName = "John",
          lastName = "Doe",
          gender = Gender.MALE,
          birthday = LocalDate(1990, 1, 1),
          phone = "0600000000",
          type = UserType.CLIENT,
      )

  @Test
  fun `getUserById should return UserResponse when user exists`() = runTest {
    val mockUser = createMockUser()
    coEvery { repo.findById(1) } returns mockUser

    val result = service.getUserById(1)

    assertEquals("test@example.com", result.email)
    coVerify(exactly = 1) { repo.findById(1) }
  }

  @Test
  fun `getUserById should throw ResourceNotFoundException when user does not exist`() = runTest {
    coEvery { repo.findById(any()) } returns null

    assertFailsWith<ResourceNotFoundException> { service.getUserById(99) }
  }

  @Test
  fun `registerUser should throw EmailAlreadyTakenException if email exists`() = runTest {
    val request = createMockUser()
    val existingUser = createMockUser()

    coEvery { repo.findByEmail(request.email) } returns existingUser

    assertFailsWith<EmailAlreadyTakenException> { service.registerUser(request) }

    coVerify { repo.findByEmail("test@example.com") }
  }

  @Test
  fun `deleteUser should throw exception if repo returns false`() = runTest {
    coEvery { repo.deleteUser(any()) } returns false

    assertFailsWith<ResourceNotFoundException> { service.deleteUser(1) }
  }

  @Test
  fun `deleteUser should succeed when repo returns true`() = runTest {
    coEvery { repo.deleteUser(1) } returns true

    service.deleteUser(1) // should not throw
  }

  @Test
  fun `getAllUsers returns mapped list`() = runTest {
    coEvery { repo.getAllUsers() } returns listOf(createMockUser(1), createMockUser(2))

    val result = service.getAllUsers()

    assertEquals(2, result.size)
    assertEquals("test@example.com", result[0].email)
  }

  @Test
  fun `getAllUsers returns empty list when no users`() = runTest {
    coEvery { repo.getAllUsers() } returns emptyList()

    assertEquals(emptyList(), service.getAllUsers())
  }

  @Test
  fun `getUserByEmail returns UserResponse when user exists`() = runTest {
    coEvery { repo.findByEmail("test@example.com") } returns createMockUser()

    val result = service.getUserByEmail("test@example.com")

    assertEquals("test@example.com", result.email)
  }

  @Test
  fun `getUserByEmail throws ResourceNotFoundException when not found`() = runTest {
    coEvery { repo.findByEmail(any()) } returns null

    assertFailsWith<ResourceNotFoundException> {
      service.getUserByEmail("missing@example.com")
    }
  }

  @Test
  fun `updateUser returns updated UserResponse`() = runTest {
    val mockUser = createMockUser()
    coEvery { repo.updateUser(1, mockUser) } returns mockUser

    val result = service.updateUser(1, mockUser)

    assertEquals("test@example.com", result.email)
  }

  @Test
  fun `updateUser throws ResourceNotFoundException when user not found`() = runTest {
    val mockUser = createMockUser()
    coEvery { repo.updateUser(any(), any()) } returns null

    assertFailsWith<ResourceNotFoundException> {
      service.updateUser(99, mockUser)
    }
  }

  @Test
  fun `registerUser creates user when email is not taken`() = runTest {
    val request = createMockUser(id = null)
    coEvery { repo.findByEmail(request.email) } returns null
    coEvery { repo.createUser(any()) } returns createMockUser(id = 5)

    val result = service.registerUser(request)

    assertEquals(5, result.id)
    assertEquals("test@example.com", result.email)
  }
}
