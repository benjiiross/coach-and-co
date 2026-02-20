package com.benjiiross.coachandco.presentation.screens.login

import app.cash.turbine.test
import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.fakes.FakeAuthRepository
import com.benjiiross.coachandco.fakes.fakeAuthResponse
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeRepo = FakeAuthRepository()
    private lateinit var viewModel: LoginViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(fakeRepo)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    fun `initial state has empty fields and no error`() = runTest {
        val state = viewModel.uiState.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertFalse(state.isSuccess)
        assertNull(state.error)
    }

    // ── Field updates ─────────────────────────────────────────────────────────

    @Test
    fun `onEmailChange updates email and clears error`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.onEmailChange("test@example.com")
            val state = awaitItem()

            assertEquals("test@example.com", state.email)
            assertNull(state.error)
        }
    }

    @Test
    fun `onPasswordChange updates password and clears error`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.onPasswordChange("secret123")
            val state = awaitItem()

            assertEquals("secret123", state.password)
            assertNull(state.error)
        }
    }

    // ── login success ─────────────────────────────────────────────────────────

    @Test
    fun `login sets isSuccess to true on success`() = runTest {
        fakeRepo.loginResult = Outcome.Success(fakeAuthResponse)

        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.onEmailChange("user@test.com")
            awaitItem()
            viewModel.onPasswordChange("password")
            awaitItem()

            viewModel.login()
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertTrue(successState.isSuccess)
            assertFalse(successState.isLoading)
            assertNull(successState.error)
        }
    }

    // ── login failure ─────────────────────────────────────────────────────────

    @Test
    fun `login sets error on InvalidCredentials`() = runTest {
        fakeRepo.loginResult = Outcome.Failure(AuthError.InvalidCredentials)

        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.login()
            awaitItem() // loading = true

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertFalse(errorState.isSuccess)
            assertNotNull(errorState.error)
        }
    }

    @Test
    fun `login sets error on NetworkError`() = runTest {
        fakeRepo.loginResult = Outcome.Failure(AuthError.NetworkError)

        viewModel.uiState.test {
            awaitItem()

            viewModel.login()
            awaitItem() // loading

            val errorState = awaitItem()
            assertNotNull(errorState.error)
        }
    }

    // ── clearError ────────────────────────────────────────────────────────────

    @Test
    fun `clearError sets error to null`() = runTest {
        fakeRepo.loginResult = Outcome.Failure(AuthError.ServerError)

        viewModel.uiState.test {
            awaitItem()

            viewModel.login()
            awaitItem() // loading
            awaitItem() // error state

            viewModel.clearError()
            val clearedState = awaitItem()

            assertNull(clearedState.error)
        }
    }
}
