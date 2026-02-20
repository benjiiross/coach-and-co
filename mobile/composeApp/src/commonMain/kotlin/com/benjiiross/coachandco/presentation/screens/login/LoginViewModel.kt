package com.benjiiross.coachandco.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benjiiross.coachandco.data.TokenStorage
import com.benjiiross.coachandco.domain.error.AuthError
import com.benjiiross.coachandco.domain.repository.AuthRepository
import com.benjiiross.coachandco.dto.auth.LoginRequest
import com.benjiiross.coachandco.presentation.UiMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val tokenStorage: TokenStorage,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<UiMessage>()
    val messages: SharedFlow<UiMessage> = _messages.asSharedFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun login() {
        viewModelScope.launch {
            if (!validate()) return@launch

            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.login(
                authDetails = LoginRequest(
                    email = _uiState.value.email.trim(),
                    password = _uiState.value.password
                )
            )

            result
                .onSuccess { authResponse ->
                    tokenStorage.saveToken(authResponse.token)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    _messages.emit(UiMessage.Error(error.toMessage()))
                }
        }
    }

    private suspend fun validate(): Boolean {
        val state = _uiState.value
        return when {
            state.email.isBlank() -> {
                _messages.emit(UiMessage.Error("L'email est requis"))
                false
            }
            state.password.isBlank() -> {
                _messages.emit(UiMessage.Error("Le mot de passe est requis"))
                false
            }
            else -> true
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
)

private fun AuthError.toMessage(): String = when (this) {
    is AuthError.InvalidCredentials -> "Email ou mot de passe incorrect"
    is AuthError.NetworkError -> "Pas de connexion réseau"
    is AuthError.ServerError -> "Erreur serveur, réessayez plus tard"
    is AuthError.Unauthorized -> "Session expirée, veuillez vous reconnecter"
    is AuthError.Unknown -> message
}
