package com.benjiiross.coachandco.presentation.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import coach_and_co.mobile.composeapp.generated.resources.Res
import coach_and_co.mobile.composeapp.generated.resources.error_invalid_credentials
import coach_and_co.mobile.composeapp.generated.resources.error_network
import coach_and_co.mobile.composeapp.generated.resources.error_server
import coach_and_co.mobile.composeapp.generated.resources.error_unauthorized
import coach_and_co.mobile.composeapp.generated.resources.error_unknown
import coach_and_co.mobile.composeapp.generated.resources.login_email
import coach_and_co.mobile.composeapp.generated.resources.login_greeting
import coach_and_co.mobile.composeapp.generated.resources.login_greeting_tagline
import coach_and_co.mobile.composeapp.generated.resources.login_login
import coach_and_co.mobile.composeapp.generated.resources.login_password
import coach_and_co.mobile.composeapp.generated.resources.login_register
import com.benjiiross.coachandco.core.StringResource
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButton
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButtonVariant
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextBody
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextTitle
import com.benjiiross.coachandco.presentation.components.textfield.CoachAndCoTextField
import com.benjiiross.coachandco.presentation.preview.Pixel9aPreview
import com.benjiiross.coachandco.presentation.preview.ThemePreview
import com.benjiiross.coachandco.presentation.theme.Gaps
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    innerPadding: PaddingValues,
    onNavigateRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = uiState.error?.toDisplayString()

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        LoginContent(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLogin = viewModel::login,
            innerPadding = innerPadding,
            onNavigateRegister = onNavigateRegister,
        )
    }
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    innerPadding: PaddingValues,
    onNavigateRegister: () -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxSize()
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Gaps.M)
                .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(Gaps.L))

        CoachAndCoTextTitle(textRes = Res.string.login_greeting)
        CoachAndCoTextBody(textRes = Res.string.login_greeting_tagline)

        Spacer(modifier = Modifier.height(Gaps.XL))

        CoachAndCoTextField(
            value = uiState.email,
            onValueChange = { onEmailChange(it) },
            labelRes = Res.string.login_email,
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Email,
        )

        Spacer(modifier = Modifier.height(Gaps.M))

        CoachAndCoTextField(
            value = uiState.password,
            onValueChange = { onPasswordChange(it) },
            labelRes = Res.string.login_password,
            isPassword = true,
            leadingIcon = Icons.Default.Lock,
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.padding(vertical = Gaps.XL).windowInsetsPadding(WindowInsets.ime),
            verticalArrangement = Arrangement.spacedBy(Gaps.M),
        ) {
            CoachAndCoButton(
                textRes = Res.string.login_login,
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth(),
            )
            CoachAndCoButton(
                textRes = Res.string.login_register,
                onClick = onNavigateRegister,
                variant = CoachAndCoButtonVariant.Light,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun StringResource.toDisplayString(): String {
    return when (this) {
        is StringResource.Text -> value
        is StringResource.ResourceId -> {
            val stringRes = when (key) {
                "error_invalid_credentials" -> Res.string.error_invalid_credentials
                "error_network" -> Res.string.error_network
                "error_server" -> Res.string.error_server
                "error_unauthorized" -> Res.string.error_unauthorized
                "error_unknown" -> Res.string.error_unknown
                else -> Res.string.error_unknown
            }
            stringResource(stringRes)
        }
    }
}

@Pixel9aPreview
@Composable
fun LoginScreenPreview() {
    ThemePreview {
        LoginScreen(
            innerPadding = PaddingValues(),
            onNavigateRegister = {},
            onLoginSuccess = {},
            snackbarHostState = remember { SnackbarHostState() })
    }
}
