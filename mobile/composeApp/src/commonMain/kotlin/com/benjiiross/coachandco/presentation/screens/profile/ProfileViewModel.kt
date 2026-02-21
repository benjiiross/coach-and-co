package com.benjiiross.coachandco.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.repository.ProfileRepository
import com.benjiiross.coachandco.dto.auth.UserResponse
import com.benjiiross.coachandco.dto.profile.ProfileError
import com.benjiiross.coachandco.dto.profile.UpdateProfileRequest
import com.benjiiross.coachandco.presentation.UiMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val profile: UserResponse? = null,
    val loadError: String? = null,

    val firstName: String = "",
    val lastName: String = "",
    val gender: Gender? = null,
    val birthday: String = "",
    val phone: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",

    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val passwordError: String? = null,
)

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<UiMessage>()
    val messages: SharedFlow<UiMessage> = _messages.asSharedFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadError = null) }

            when (val result = profileRepository.getProfile()) {
                is Outcome.Success -> {
                    val p = result.value
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            profile = p,
                            firstName = p.firstName,
                            lastName = p.lastName,
                            gender = p.gender,
                            birthday = p.birthday.toString(),
                            phone = p.phone,
                        )
                    }
                }
                is Outcome.Failure -> {
                    _uiState.update {
                        it.copy(isLoading = false, loadError = result.error.toMessage())
                    }
                }
            }
        }
    }

    fun onFirstNameChange(value: String) =
        _uiState.update { it.copy(firstName = value, firstNameError = null) }

    fun onLastNameChange(value: String) =
        _uiState.update { it.copy(lastName = value, lastNameError = null) }

    fun onGenderChange(value: Gender?) =
        _uiState.update { it.copy(gender = value) }

    fun onBirthdayChange(value: String) =
        _uiState.update { it.copy(birthday = value) }

    fun onPhoneChange(value: String) =
        _uiState.update { it.copy(phone = value) }

    fun onCurrentPasswordChange(value: String) =
        _uiState.update { it.copy(currentPassword = value, passwordError = null) }

    fun onNewPasswordChange(value: String) =
        _uiState.update { it.copy(newPassword = value, passwordError = null) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, passwordError = null) }

    fun saveProfile() {
        if (!validate()) return

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val request = UpdateProfileRequest(
                firstName = state.firstName.trim(),
                lastName = state.lastName.trim(),
                gender = state.gender,
                birthday = state.birthday.trim().takeIf { it.isNotEmpty() },
                phone = state.phone.trim().takeIf { it.isNotEmpty() },
                currentPassword = state.currentPassword.trim().takeIf { it.isNotEmpty() },
                newPassword = state.newPassword.trim().takeIf { it.isNotEmpty() },
            )

            when (val result = profileRepository.updateProfile(request)) {
                is Outcome.Success -> {
                    val p = result.value
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            profile = p,
                            currentPassword = "",
                            newPassword = "",
                            confirmPassword = "",
                        )
                    }
                    _messages.emit(UiMessage.Success("Profil mis à jour"))
                }
                is Outcome.Failure -> {
                    _uiState.update { it.copy(isSaving = false) }
                    _messages.emit(UiMessage.Error(result.error.toMessage()))
                }
            }
        }
    }

    private fun validate(): Boolean {
        val state = _uiState.value

        val firstNameError = if (state.firstName.isBlank()) "Le prénom est requis" else null
        val lastNameError = if (state.lastName.isBlank()) "Le nom est requis" else null

        val passwordError = if (state.newPassword.isNotEmpty()) {
            when {
                state.currentPassword.isBlank() -> "Mot de passe actuel requis"
                state.newPassword != state.confirmPassword -> "Les mots de passe ne correspondent pas"
                state.newPassword.length < 8 -> "Minimum 8 caractères"
                else -> null
            }
        } else null

        _uiState.update {
            it.copy(
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                passwordError = passwordError,
            )
        }

        return firstNameError == null && lastNameError == null && passwordError == null
    }

    private fun ProfileError.toMessage(): String = when (this) {
        is ProfileError.Unauthorized -> "Session expirée, veuillez vous reconnecter"
        is ProfileError.ServerError -> "Erreur serveur, réessayez plus tard"
        is ProfileError.NetworkError -> "Pas de connexion réseau"
        is ProfileError.NotFound -> "Profil introuvable"
        is ProfileError.Unknown -> message
    }
}
