package com.benjiiross.coachandco.presentation.screens.profile

import app.cash.turbine.test
import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.dto.profile.ProfileError
import com.benjiiross.coachandco.fakes.FakeProfileRepository
import com.benjiiross.coachandco.fakes.fakeUserResponse
import com.benjiiross.coachandco.presentation.UiMessage
import com.benjiiross.coachandco.presentation.profile.ProfileViewModel
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

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeRepo = FakeProfileRepository()
    private lateinit var viewModel: ProfileViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(): ProfileViewModel {
        viewModel = ProfileViewModel(fakeRepo)
        return viewModel
    }

    // ── loadProfile ───────────────────────────────────────────────────────────

    @Test
    fun `loadProfile populates form fields on success`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("John", state.firstName)
            assertEquals("Doe", state.lastName)
            assertEquals(Gender.MALE, state.gender)
            assertEquals("1990-01-01", state.birthday)
            assertEquals("0600000000", state.phone)
            assertEquals(fakeUserResponse, state.profile)
        }
    }

    @Test
    fun `loadProfile sets loadError on failure`() = runTest {
        fakeRepo.getProfileResult = Outcome.Failure(ProfileError.NetworkError)
        buildViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertNotNull(state.loadError)
            assertNull(state.profile)
        }
    }

    // ── Field updates ─────────────────────────────────────────────────────────

    @Test
    fun `onFirstNameChange updates firstName and clears error`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onFirstNameChange("Jane")
            val state = awaitItem()

            assertEquals("Jane", state.firstName)
            assertNull(state.firstNameError)
        }
    }

    @Test
    fun `onGenderChange updates gender`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onGenderChange(Gender.FEMALE)
            assertEquals(Gender.FEMALE, awaitItem().gender)
        }
    }

    @Test
    fun `onBirthdayChange updates birthday`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onBirthdayChange("2000-06-15")
            val state = awaitItem()

            assertEquals("2000-06-15", state.birthday)
        }
    }

    // ── validate ──────────────────────────────────────────────────────────────

    @Test
    fun `saveProfile sets firstNameError when firstName is blank`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onFirstNameChange("")
            awaitItem()

            viewModel.saveProfile()
            val state = awaitItem()

            assertNotNull(state.firstNameError)
        }
    }

    @Test
    fun `saveProfile sets lastNameError when lastName is blank`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onLastNameChange("")
            awaitItem()

            viewModel.saveProfile()
            val state = awaitItem()

            assertNotNull(state.lastNameError)
        }
    }

    @Test
    fun `saveProfile sets passwordError when newPassword lacks currentPassword`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onNewPasswordChange("newpassword")
            awaitItem()

            viewModel.saveProfile()
            val state = awaitItem()

            assertNotNull(state.passwordError)
        }
    }

    @Test
    fun `saveProfile sets passwordError when passwords do not match`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onCurrentPasswordChange("current")
            awaitItem()
            viewModel.onNewPasswordChange("newpassword")
            awaitItem()
            viewModel.onConfirmPasswordChange("different")
            awaitItem()

            viewModel.saveProfile()
            val state = awaitItem()

            assertNotNull(state.passwordError)
        }
    }

    @Test
    fun `saveProfile sets passwordError when newPassword is too short`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.uiState.test {
            awaitItem()

            viewModel.onCurrentPasswordChange("current")
            awaitItem()
            viewModel.onNewPasswordChange("short")
            awaitItem()
            viewModel.onConfirmPasswordChange("short")
            awaitItem()

            viewModel.saveProfile()
            val state = awaitItem()

            assertNotNull(state.passwordError)
        }
    }

    // ── saveProfile success / failure ─────────────────────────────────────────

    @Test
    fun `saveProfile emits success message and clears password fields`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        fakeRepo.updateProfileResult = Outcome.Success(fakeUserResponse)
        buildViewModel()

        viewModel.messages.test {
            viewModel.saveProfile()
            val message = awaitItem()
            assertIs<UiMessage.Success>(message)
            cancelAndIgnoreRemainingEvents()
        }

        val state = viewModel.uiState.value
        assertEquals("", state.currentPassword)
        assertEquals("", state.newPassword)
        assertEquals("", state.confirmPassword)
    }

    @Test
    fun `saveProfile emits error message on failure`() = runTest {
        fakeRepo.getProfileResult = Outcome.Success(fakeUserResponse)
        fakeRepo.updateProfileResult = Outcome.Failure(ProfileError.ServerError)
        buildViewModel()

        viewModel.messages.test {
            viewModel.saveProfile()
            val message = awaitItem()
            assertIs<UiMessage.Error>(message)
            cancelAndIgnoreRemainingEvents()
        }

        assertFalse(viewModel.uiState.value.isSaving)
    }
}
