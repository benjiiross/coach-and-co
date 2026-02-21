package com.benjiiross.coachandco.presentation.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.presentation.UiMessage
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButton
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButtonVariant
import com.benjiiross.coachandco.presentation.components.layout.AppMessageBanner
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextBody
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextTitle
import com.benjiiross.coachandco.presentation.components.textfield.CoachAndCoDatePickerField
import com.benjiiross.coachandco.presentation.components.textfield.CoachAndCoTextField
import com.benjiiross.coachandco.presentation.preview.Pixel9aPreview
import com.benjiiross.coachandco.presentation.preview.ThemePreview
import com.benjiiross.coachandco.presentation.theme.Gaps
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    innerPadding: PaddingValues,
    onNavigateLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentMessage by remember { mutableStateOf<UiMessage?>(null) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onRegisterSuccess()
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
        RegisterContent(
            uiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            onFirstNameChange = viewModel::onFirstNameChange,
            onLastNameChange = viewModel::onLastNameChange,
            onGenderChange = viewModel::onGenderChange,
            onBirthdayChange = viewModel::onBirthdayChange,
            onPhoneChange = viewModel::onPhoneChange,
            onRegister = viewModel::register,
            onNavigateLogin = onNavigateLogin,
            innerPadding = innerPadding,
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
private fun RegisterContent(
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onGenderChange: (Gender?) -> Unit,
    onBirthdayChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onRegister: () -> Unit,
    onNavigateLogin: () -> Unit,
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = Gaps.M)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(Gaps.L))

        CoachAndCoTextTitle(text = "Créer un compte")
        CoachAndCoTextBody(text = "Rejoignez Coach & Co dès aujourd'hui")

        Spacer(modifier = Modifier.height(Gaps.XL))

        // ── Identity ──────────────────────────────────────────────────────────

        RegisterSectionTitle("Identité")

        Spacer(modifier = Modifier.height(Gaps.SM))

        Row(horizontalArrangement = Arrangement.spacedBy(Gaps.SM)) {
            CoachAndCoTextField(
                value = uiState.firstName,
                onValueChange = onFirstNameChange,
                label = "Prénom",
                leadingIcon = Icons.Default.Person,
                error = uiState.firstNameError,
                modifier = Modifier.weight(1f),
            )
            CoachAndCoTextField(
                value = uiState.lastName,
                onValueChange = onLastNameChange,
                label = "Nom",
                error = uiState.lastNameError,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(Gaps.M))

        GenderDropdown(
            selected = uiState.gender,
            onSelect = onGenderChange,
            error = uiState.genderError,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(Gaps.M))

        CoachAndCoDatePickerField(
            value = uiState.birthday,
            onValueChange = onBirthdayChange,
            label = "Date de naissance",
            error = uiState.birthdayError,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(Gaps.L))

        // ── Contact ───────────────────────────────────────────────────────────

        RegisterSectionTitle("Coordonnées")

        Spacer(modifier = Modifier.height(Gaps.SM))

        CoachAndCoTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "Email",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Email,
            error = uiState.emailError,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(Gaps.M))

        CoachAndCoTextField(
            value = uiState.phone,
            onValueChange = onPhoneChange,
            label = "Téléphone",
            keyboardType = KeyboardType.Phone,
            leadingIcon = Icons.Default.Phone,
            error = uiState.phoneError,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(Gaps.L))

        // ── Security ──────────────────────────────────────────────────────────

        RegisterSectionTitle("Sécurité")

        Spacer(modifier = Modifier.height(Gaps.SM))

        CoachAndCoTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "Mot de passe",
            isPassword = true,
            leadingIcon = Icons.Default.Lock,
            error = uiState.passwordError,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(Gaps.M))

        CoachAndCoTextField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmer le mot de passe",
            isPassword = true,
            leadingIcon = Icons.Default.Lock,
            error = uiState.confirmPasswordError,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.weight(1f))

        // ── Actions ───────────────────────────────────────────────────────────

        Column(
            modifier = Modifier.padding(vertical = Gaps.XL).windowInsetsPadding(WindowInsets.ime),
            verticalArrangement = Arrangement.spacedBy(Gaps.M),
        ) {
            CoachAndCoButton(
                text = "Créer mon compte",
                onClick = onRegister,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
            CoachAndCoButton(
                text = "Déjà un compte ? Se connecter",
                onClick = onNavigateLogin,
                variant = CoachAndCoButtonVariant.Light,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun RegisterSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdown(
    selected: Gender?,
    onSelect: (Gender?) -> Unit,
    error: String?,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = selected?.label() ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Genre") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Gender.entries.forEach { gender ->
                androidx.compose.material3.DropdownMenuItem(
                    text = { Text(gender.label()) },
                    onClick = {
                        onSelect(gender)
                        expanded = false
                    },
                )
            }
        }
    }
}

private fun Gender.label(): String = when (this) {
    Gender.MALE -> "Homme"
    Gender.FEMALE -> "Femme"
    Gender.OTHER -> "Autre"
    Gender.PREFER_NOT_TO_SAY -> "Préfère ne pas dire"
}

@Pixel9aPreview
@Composable
fun RegisterScreenPreview() {
    ThemePreview {
        RegisterScreen(
            innerPadding = PaddingValues(),
            onNavigateLogin = {},
            onRegisterSuccess = {},
        )
    }
}
