package com.benjiiross.coachandco.presentation.screens.register

import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.fakes.FakeAuthRepository
import com.benjiiross.coachandco.fakes.fakeAuthResponse
import com.benjiiross.coachandco.fakes.fakeTokenStorage
import com.benjiiross.coachandco.presentation.UiMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import app.cash.turbine.test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeRepo = FakeAuthRepository()
    private lateinit var viewModel: RegisterViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewModel(fakeRepo, fakeTokenStorage())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /** Fill all required fields with valid data so register() can reach the API. */
    private fun fillValidForm() {
        viewModel.onEmailChange("user@test.com")
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("password123")
        viewModel.onFirstNameChange("John")
        viewModel.onLastNameChange("Doe")
        viewModel.onGenderChange(Gender.MALE)
        viewModel.onBirthdayChange("1990-01-01")
        viewModel.onPhoneChange("0600000000")
        viewModel.onTypeChange(UserType.CLIENT)
    }

    // ── Initial state ──────────────────────────────────────────────────────────

    @Test
    fun `initial state has empty fields and no errors`() = runTest {
        val state = viewModel.uiState.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals("", state.confirmPassword)
        assertEquals("", state.firstName)
        assertEquals("", state.lastName)
        assertNull(state.gender)
        assertEquals("", state.birthday)
        assertEquals("", state.phone)
        assertEquals(UserType.CLIENT, state.type)
        assertFalse(state.isLoading)
        assertFalse(state.isSuccess)
        assertNull(state.emailError)
        assertNull(state.passwordError)
        assertNull(state.firstNameError)
        assertNull(state.lastNameError)
    }

    // ── Field updates ──────────────────────────────────────────────────────────

    @Test
    fun `onEmailChange updates email and clears error`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onEmailChange("user@test.com")
            val state = awaitItem()
            assertEquals("user@test.com", state.email)
            assertNull(state.emailError)
        }
    }

    @Test
    fun `onPasswordChange updates password and clears error`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onPasswordChange("secret123")
            val state = awaitItem()
            assertEquals("secret123", state.password)
            assertNull(state.passwordError)
        }
    }

    @Test
    fun `onGenderChange updates gender and clears error`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onGenderChange(Gender.FEMALE)
            assertEquals(Gender.FEMALE, awaitItem().gender)
        }
    }

    @Test
    fun `onTypeChange updates type`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTypeChange(UserType.COACH)
            assertEquals(UserType.COACH, awaitItem().type)
        }
    }

    // ── Validation ─────────────────────────────────────────────────────────────

    @Test
    fun `register sets emailError when email is blank`() = runTest {
        viewModel.register()
        assertNotNull(viewModel.uiState.value.emailError)
    }

    @Test
    fun `register sets firstNameError when firstName is blank`() = runTest {
        viewModel.onEmailChange("user@test.com")
        viewModel.register()
        assertNotNull(viewModel.uiState.value.firstNameError)
    }

    @Test
    fun `register sets lastNameError when lastName is blank`() = runTest {
        viewModel.onEmailChange("user@test.com")
        viewModel.onFirstNameChange("John")
        viewModel.register()
        assertNotNull(viewModel.uiState.value.lastNameError)
    }

    @Test
    fun `register sets genderError when gender is null`() = runTest {
        viewModel.onEmailChange("user@test.com")
        viewModel.onFirstNameChange("John")
        viewModel.onLastNameChange("Doe")
        viewModel.register()
        assertNotNull(viewModel.uiState.value.genderError)
    }

    @Test
    fun `register sets birthdayError when birthday is blank`() = runTest {
        viewModel.onEmailChange("user@test.com")
        viewModel.onFirstNameChange("John")
        viewModel.onLastNameChange("Doe")
        viewModel.onGenderChange(Gender.MALE)
        viewModel.register()
        assertNotNull(viewModel.uiState.value.birthdayError)
    }

    @Test
    fun `register sets birthdayError for invalid date format`() = runTest {
        viewModel.onBirthdayChange("not-a-date")
        viewModel.register()
        assertNotNull(viewModel.uiState.value.birthdayError)
    }

    @Test
    fun `register sets phoneError when phone is blank`() = runTest {
        viewModel.onEmailChange("user@test.com")
        viewModel.onFirstNameChange("John")
        viewModel.onLastNameChange("Doe")
        viewModel.onGenderChange(Gender.MALE)
        viewModel.onBirthdayChange("1990-01-01")
        viewModel.register()
        assertNotNull(viewModel.uiState.value.phoneError)
    }

    @Test
    fun `register sets passwordError when password is blank`() = runTest {
        viewModel.register()
        assertNotNull(viewModel.uiState.value.passwordError)
    }

    @Test
    fun `register sets passwordError when password is too short`() = runTest {
        viewModel.onPasswordChange("short")
        viewModel.register()
        assertNotNull(viewModel.uiState.value.passwordError)
    }

    @Test
    fun `register sets confirmPasswordError when passwords do not match`() = runTest {
        viewModel.onPasswordChange("password123")
        viewModel.onConfirmPasswordChange("different")
        viewModel.register()
        assertNotNull(viewModel.uiState.value.confirmPasswordError)
    }

    @Test
    fun `register does not call api when validation fails`() = runTest {
        viewModel.register()
        assertEquals(0, fakeRepo.registerCallCount)
    }

    @Test
    fun `field change clears its associated error`() = runTest {
        viewModel.register() // triggers all errors

        viewModel.onEmailChange("user@test.com")
        assertNull(viewModel.uiState.value.emailError)

        viewModel.onFirstNameChange("John")
        assertNull(viewModel.uiState.value.firstNameError)

        viewModel.onBirthdayChange("1990-01-01")
        assertNull(viewModel.uiState.value.birthdayError)
    }

    // ── Success ────────────────────────────────────────────────────────────────

    @Test
    fun `register sets isSuccess to true on success`() = runTest {
        fakeRepo.registerResult = Outcome.Success(fakeAuthResponse)
        fillValidForm()

        viewModel.register()

        val state = viewModel.uiState.value
        assertTrue(state.isSuccess)
        assertFalse(state.isLoading)
    }

    @Test
    fun `register saves token on success`() = runTest {
        fakeRepo.registerResult = Outcome.Success(fakeAuthResponse)
        val settings = com.benjiiross.coachandco.fakes.TestSettings()
        val tokenStorage = com.benjiiross.coachandco.data.TokenStorage(settings)
        viewModel = RegisterViewModel(fakeRepo, tokenStorage)
        fillValidForm()

        viewModel.register()

        assertEquals(fakeAuthResponse.token, tokenStorage.getToken())
    }

    // ── Failure ────────────────────────────────────────────────────────────────

    @Test
    fun `register emits error message on EmailAlreadyTaken (InvalidCredentials)`() = runTest {
        fakeRepo.registerResult = Outcome.Failure(AuthError.InvalidCredentials)
        fillValidForm()

        viewModel.messages.test {
            viewModel.register()
            assertIs<UiMessage.Error>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `register emits error message on NetworkError`() = runTest {
        fakeRepo.registerResult = Outcome.Failure(AuthError.NetworkError)
        fillValidForm()

        viewModel.messages.test {
            viewModel.register()
            assertIs<UiMessage.Error>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `register emits error message on ServerError`() = runTest {
        fakeRepo.registerResult = Outcome.Failure(AuthError.ServerError)
        fillValidForm()

        viewModel.messages.test {
            viewModel.register()
            assertIs<UiMessage.Error>(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `register sets isLoading to false after failure`() = runTest {
        fakeRepo.registerResult = Outcome.Failure(AuthError.ServerError)
        fillValidForm()

        viewModel.register()

        assertFalse(viewModel.uiState.value.isLoading)
    }
}
