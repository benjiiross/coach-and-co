package com.benjiiross.coachandco.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.benjiiross.coachandco.domain.enums.Gender
import com.benjiiross.coachandco.presentation.UiMessage
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButton
import com.benjiiross.coachandco.presentation.components.button.CoachAndCoButtonVariant
import com.benjiiross.coachandco.presentation.components.layout.AppMessageBanner
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextBody
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextCaption
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextTitle
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextTitleSmall
import com.benjiiross.coachandco.presentation.components.textfield.CoachAndCoTextField
import com.benjiiross.coachandco.presentation.profile.ProfileUiState
import com.benjiiross.coachandco.presentation.profile.ProfileViewModel
import com.benjiiross.coachandco.presentation.theme.Gaps
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    innerPadding: PaddingValues,
    onLogout: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentMessage by remember { mutableStateOf<UiMessage?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.messages.collect { message ->
            currentMessage = message
            delay(3000L)
            currentMessage = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> ProfileLoadingState(innerPadding)
            uiState.profile == null && uiState.loadError != null -> ProfileErrorState(
                message = uiState.loadError ?: "Erreur inconnue",
                onRetry = viewModel::loadProfile,
                innerPadding = innerPadding,
            )
            else -> ProfileContent(
                uiState = uiState,
                innerPadding = innerPadding,
                onFirstNameChange = viewModel::onFirstNameChange,
                onLastNameChange = viewModel::onLastNameChange,
                onGenderChange = viewModel::onGenderChange,
                onBirthdayChange = viewModel::onBirthdayChange,
                onPhoneChange = viewModel::onPhoneChange,
                onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
                onNewPasswordChange = viewModel::onNewPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onSave = viewModel::saveProfile,
                onLogout = onLogout,
            )
        }

        AppMessageBanner(
            message = currentMessage,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = innerPadding.calculateTopPadding() + Gaps.SM),
        )
    }
}

@Composable
private fun ProfileContent(
    uiState: ProfileUiState,
    innerPadding: PaddingValues,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onGenderChange: (Gender?) -> Unit,
    onBirthdayChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSave: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = Gaps.M, vertical = Gaps.L),
        verticalArrangement = Arrangement.spacedBy(Gaps.M),
    ) {
        ProfileHeader(
            firstName = uiState.firstName,
            lastName = uiState.lastName,
            email = uiState.profile?.email ?: "",
        )

        HorizontalDivider()

        ProfileSectionTitle(title = "Informations personnelles")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Gaps.SM),
        ) {
            CoachAndCoTextField(
                value = uiState.firstName,
                onValueChange = onFirstNameChange,
                label = "Prénom",
                modifier = Modifier.weight(1f),
                leadingIcon = Icons.Default.Person,
                error = uiState.firstNameError,
                imeAction = ImeAction.Next,
            )
            CoachAndCoTextField(
                value = uiState.lastName,
                onValueChange = onLastNameChange,
                label = "Nom",
                modifier = Modifier.weight(1f),
                error = uiState.lastNameError,
                imeAction = ImeAction.Next,
            )
        }

        GenderDropdown(
            selected = uiState.gender,
            onSelected = onGenderChange,
            modifier = Modifier.fillMaxWidth(),
        )

        CoachAndCoTextField(
            value = uiState.birthday,
            onValueChange = onBirthdayChange,
            label = "Date de naissance (AAAA-MM-JJ)",
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Default.CalendarMonth,
            error = uiState.birthdayError,
            imeAction = ImeAction.Next,
        )

        CoachAndCoTextField(
            value = uiState.phone,
            onValueChange = onPhoneChange,
            label = "Téléphone",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Phone,
            leadingIcon = Icons.Default.Phone,
            imeAction = ImeAction.Next,
        )

        HorizontalDivider()

        ProfileSectionTitle(title = "Changer le mot de passe")

        CoachAndCoTextField(
            value = uiState.currentPassword,
            onValueChange = onCurrentPasswordChange,
            label = "Mot de passe actuel",
            modifier = Modifier.fillMaxWidth(),
            isPassword = true,
            leadingIcon = Icons.Default.Lock,
            imeAction = ImeAction.Next,
        )
        CoachAndCoTextField(
            value = uiState.newPassword,
            onValueChange = onNewPasswordChange,
            label = "Nouveau mot de passe",
            modifier = Modifier.fillMaxWidth(),
            isPassword = true,
            leadingIcon = Icons.Default.LockOpen,
            imeAction = ImeAction.Next,
        )
        CoachAndCoTextField(
            value = uiState.confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmer le mot de passe",
            modifier = Modifier.fillMaxWidth(),
            isPassword = true,
            leadingIcon = Icons.Default.LockOpen,
            error = uiState.passwordError,
            imeAction = ImeAction.Done,
            onImeAction = onSave,
        )

        Spacer(modifier = Modifier.height(Gaps.SM))

        CoachAndCoButton(
            text = "Enregistrer les modifications",
            onClick = onSave,
            isLoading = uiState.isSaving,
            modifier = Modifier.fillMaxWidth(),
        )

        CoachAndCoButton(
            text = "Se déconnecter",
            onClick = onLogout,
            variant = CoachAndCoButtonVariant.Light,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun Gender.label(): String = when (this) {
    Gender.MALE -> "Homme"
    Gender.FEMALE -> "Femme"
    Gender.OTHER -> "Autre"
    Gender.PREFER_NOT_TO_SAY -> "Préfère ne pas dire"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdown(
    selected: Gender?,
    onSelected: (Gender?) -> Unit,
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
            label = { CoachAndCoTextCaption("Genre") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Gender.entries.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(gender.label()) },
                    onClick = {
                        onSelected(gender)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    firstName: String,
    lastName: String,
    email: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Gaps.SM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Gaps.M),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(72.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                CoachAndCoTextTitle(
                    text = buildString {
                        if (firstName.isNotEmpty()) append(firstName.first().uppercaseChar())
                        if (lastName.isNotEmpty()) append(lastName.first().uppercaseChar())
                    },
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(Gaps.XXS)) {
            CoachAndCoTextTitle(
                text = "$firstName $lastName".trim().ifEmpty { "Mon profil" },
            )
            CoachAndCoTextCaption(
                text = email,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ProfileSectionTitle(title: String) {
    CoachAndCoTextTitleSmall(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = Gaps.XS),
    )
}

@Composable
private fun ProfileLoadingState(innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ProfileErrorState(
    message: String,
    onRetry: () -> Unit,
    innerPadding: PaddingValues,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(Gaps.M),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.height(Gaps.M))
        CoachAndCoTextBody(
            text = message,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(Gaps.M))
        CoachAndCoButton(text = "Réessayer", onClick = onRetry)
    }
}
