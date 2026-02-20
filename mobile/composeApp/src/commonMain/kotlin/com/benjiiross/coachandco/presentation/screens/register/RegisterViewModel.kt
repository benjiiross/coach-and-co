package com.benjiiross.coachandco.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benjiiross.coachandco.core.Outcome
import com.benjiiross.coachandco.data.TokenStorage
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.domain.enums.UserType
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.domain.repository.AuthRepository
import com.benjiiross.coachandco.dto.auth.RegisterRequest
import com.benjiiross.coachandco.presentation.UiMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val gender: Gender? = null,
    val birthday: String = "",
    val phone: String = "",
    val type: UserType = UserType.CLIENT,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,

    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val genderError: String? = null,
    val birthdayError: String? = null,
    val phoneError: String? = null,
)

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val tokenStorage: TokenStorage,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<UiMessage>()
    val messages: SharedFlow<UiMessage> = _messages.asSharedFlow()

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value, emailError = null) }

    fun onPasswordChange(value: String) =
        _uiState.update { it.copy(password = value, passwordError = null) }

    fun onConfirmPasswordChange(value: String) =
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }

    fun onFirstNameChange(value: String) =
        _uiState.update { it.copy(firstName = value, firstNameError = null) }

    fun onLastNameChange(value: String) =
        _uiState.update { it.copy(lastName = value, lastNameError = null) }

    fun onGenderChange(value: Gender?) =
        _uiState.update { it.copy(gender = value, genderError = null) }

    fun onBirthdayChange(value: String) =
        _uiState.update { it.copy(birthday = value, birthdayError = null) }

    fun onPhoneChange(value: String) =
        _uiState.update { it.copy(phone = value, phoneError = null) }

    fun onTypeChange(value: UserType) =
        _uiState.update { it.copy(type = value) }

    fun register() {
        if (!validate()) return

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val request = RegisterRequest(
                email = state.email.trim(),
                password = state.password,
                firstName = state.firstName.trim(),
                lastName = state.lastName.trim(),
                gender = requireNotNull(state.gender),
                birthday = LocalDate.parse(state.birthday.trim()),
                phone = state.phone.trim(),
                type = state.type,
            )

            when (val result = authRepository.register(request)) {
                is Outcome.Success -> {
                    tokenStorage.saveToken(result.value.token)
                    result.value.refreshToken?.let { tokenStorage.saveRefreshToken(it) }
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                is Outcome.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _messages.emit(UiMessage.Error(result.error.toMessage()))
                }
            }
        }
    }

    private fun validate(): Boolean {
        val s = _uiState.value

        val emailError = if (s.email.isBlank()) "L'email est requis" else null
        val passwordError = when {
            s.password.isBlank() -> "Le mot de passe est requis"
            s.password.length < 8 -> "Minimum 8 caractères"
            else -> null
        }
        val confirmPasswordError =
            if (s.confirmPassword != s.password) "Les mots de passe ne correspondent pas" else null
        val firstNameError = if (s.firstName.isBlank()) "Le prénom est requis" else null
        val lastNameError = if (s.lastName.isBlank()) "Le nom est requis" else null
        val genderError = if (s.gender == null) "Le genre est requis" else null
        val birthdayError = if (s.birthday.isBlank()) {
            "La date de naissance est requise"
        } else {
            try {
                LocalDate.parse(s.birthday.trim())
                null
            } catch (_: Exception) {
                "Format invalide (AAAA-MM-JJ)"
            }
        }
        val phoneError = if (s.phone.isBlank()) "Le téléphone est requis" else null

        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                genderError = genderError,
                birthdayError = birthdayError,
                phoneError = phoneError,
            )
        }

        return listOf(
            emailError, passwordError, confirmPasswordError,
            firstNameError, lastNameError, genderError, birthdayError, phoneError,
        ).all { it == null }
    }

    private fun AuthError.toMessage(): String = when (this) {
        is AuthError.InvalidCredentials -> "Cet email est déjà utilisé"
        is AuthError.NetworkError -> "Pas de connexion réseau"
        is AuthError.ServerError -> "Erreur serveur, réessayez plus tard"
        is AuthError.Unauthorized -> "Session expirée, veuillez vous reconnecter"
        is AuthError.Unknown -> message
    }
}
