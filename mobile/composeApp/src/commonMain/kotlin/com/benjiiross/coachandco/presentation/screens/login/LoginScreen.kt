package com.benjiiross.coachandco.presentation.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import coach_and_co.mobile.composeapp.generated.resources.Res
import coach_and_co.mobile.composeapp.generated.resources.login_email
import coach_and_co.mobile.composeapp.generated.resources.login_greeting
import coach_and_co.mobile.composeapp.generated.resources.login_greeting_tagline
import coach_and_co.mobile.composeapp.generated.resources.login_login
import coach_and_co.mobile.composeapp.generated.resources.login_password
import coach_and_co.mobile.composeapp.generated.resources.login_register
import com.benjiiross.coachandco.presentation.UiMessage
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButton
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButtonVariant
import com.benjiiross.coachandco.presentation.components.layout.AppMessageBanner
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextBody
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextTitle
import com.benjiiross.coachandco.presentation.components.textfield.CoachAndCoTextField
import com.benjiiross.coachandco.presentation.preview.Pixel9aPreview
import com.benjiiross.coachandco.presentation.preview.ThemePreview
import com.benjiiross.coachandco.presentation.theme.Gaps
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    innerPadding: PaddingValues,
    onNavigateRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentMessage by remember { mutableStateOf<UiMessage?>(null) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.messages.collect { message ->
            currentMessage = message
            delay(3000L)
            currentMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginContent(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLogin = viewModel::login,
            innerPadding = innerPadding,
            onNavigateRegister = onNavigateRegister,
        )

        AppMessageBanner(
            message = currentMessage,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = innerPadding.calculateTopPadding() + Gaps.SM),
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
        modifier = Modifier
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

@Pixel9aPreview
@Composable
fun LoginScreenPreview() {
    ThemePreview {
        LoginScreen(
            innerPadding = PaddingValues(),
            onNavigateRegister = {},
            onLoginSuccess = {},
        )
    }
}
