package com.benjiiross.coachandco.domain.services

import com.benjiiross.coachandco.core.enums.Gender
import com.benjiiross.coachandco.core.enums.UserType
import com.benjiiross.coachandco.core.exceptions.EmailAlreadyTakenException
import com.benjiiross.coachandco.core.exceptions.ResourceNotFoundException
import com.benjiiross.coachandco.domain.dto.user.UserRequest
import com.benjiiross.coachandco.domain.models.User
import com.benjiiross.coachandco.domain.repositories.IUserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate

class UserServiceTest {
  private val repo = mockk<IUserRepository>()
  private val service = UserService(repo)

  private fun createMockUser(id: Int? = 1) =
      User(
          id = id,
          email = "test@example.com",
          name = "John",
          surname = "Doe",
          gender = Gender.MALE,
          birthday = LocalDate(1990, 1, 1),
          phone = "0600000000",
          type = UserType.CLIENT,
      )

  private fun createMockUserRequest() =
      UserRequest(
          email = "test@example.com",
          name = "John",
          surname = "Doe",
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
    val request = createMockUserRequest()
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
}
