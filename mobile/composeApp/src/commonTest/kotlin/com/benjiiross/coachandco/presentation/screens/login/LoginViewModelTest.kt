package com.benjiiross.coachandco.presentation.screens.login

import app.cash.turbine.test
import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.fakes.FakeAuthRepository
import com.benjiiross.coachandco.fakes.TestSettings
import com.benjiiross.coachandco.fakes.fakeAuthResponse
import com.benjiiross.coachandco.fakes.fakeTokenStorage
import com.benjiiross.coachandco.data.TokenStorage
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
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeRepo = FakeAuthRepository()
    private lateinit var viewModel: LoginViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(fakeRepo, fakeTokenStorage())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has empty fields`() = runTest {
        val state = viewModel.uiState.value
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertFalse(state.isSuccess)
    }

    @Test
    fun `onEmailChange updates email`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onEmailChange("test@example.com")
            val state = awaitItem()

            assertEquals("test@example.com", state.email)
        }
    }

    @Test
    fun `onPasswordChange updates password`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onPasswordChange("secret123")
            val state = awaitItem()

            assertEquals("secret123", state.password)
        }
    }

    @Test
    fun `login sets isSuccess to true on success`() = runTest {
        fakeRepo.loginResult = Outcome.Success(fakeAuthResponse)

        viewModel.onEmailChange("user@test.com")
        viewModel.onPasswordChange("password")
        viewModel.login()

        val state = viewModel.uiState.value
        assertTrue(state.isSuccess)
        assertFalse(state.isLoading)
    }

    @Test
    fun `login saves token on success`() = runTest {
        fakeRepo.loginResult = Outcome.Success(fakeAuthResponse)
        val settings = TestSettings()
        val tokenStorage = TokenStorage(settings)
        viewModel = LoginViewModel(fakeRepo, tokenStorage)

        viewModel.login()

        assertEquals(fakeAuthResponse.token, tokenStorage.getToken())
    }

    @Test
    fun `login emits error message on InvalidCredentials`() = runTest {
        fakeRepo.loginResult = Outcome.Failure(AuthError.InvalidCredentials)

        viewModel.messages.test {
            viewModel.login()
            val message = awaitItem()
            assertIs<UiMessage.Error>(message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login emits error message on NetworkError`() = runTest {
        fakeRepo.loginResult = Outcome.Failure(AuthError.NetworkError)

        viewModel.messages.test {
            viewModel.login()
            val message = awaitItem()
            assertIs<UiMessage.Error>(message)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login emits error message on ServerError`() = runTest {
        fakeRepo.loginResult = Outcome.Failure(AuthError.ServerError)

        viewModel.messages.test {
            viewModel.login()
            val message = awaitItem()
            assertIs<UiMessage.Error>(message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
