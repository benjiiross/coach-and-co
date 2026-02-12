package com.benjiiross.coachandco.presentation.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextTitleSmall
import com.benjiiross.coachandco.presentation.preview.Pixel9aPreview
import com.benjiiross.coachandco.presentation.preview.ThemePreview
import com.benjiiross.coachandco.presentation.theme.Gaps
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CoachAndCoButton(
    text: String,
    onClick: () -> Unit,
    variant: CoachAndCoButtonVariant = CoachAndCoButtonVariant.Primary,
    modifier: Modifier = Modifier,
) {
    val colors =
        when (variant) {
            CoachAndCoButtonVariant.Primary ->
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            CoachAndCoButtonVariant.Dark ->
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                )
            CoachAndCoButtonVariant.Light ->
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
        }

    val border =
        if (variant == CoachAndCoButtonVariant.Primary) {
            null
        } else {
            BorderStroke(Gaps.XXS, MaterialTheme.colorScheme.outline)
        }

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        border = border,
        shape = MaterialTheme.shapes.medium,
    ) {
        CoachAndCoTextTitleSmall(
            text = text,
            modifier = Modifier.padding(horizontal = Gaps.SM, vertical = Gaps.XS),
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CoachAndCoButton(
    textRes: StringResource,
    onClick: () -> Unit,
    variant: CoachAndCoButtonVariant = CoachAndCoButtonVariant.Primary,
    modifier: Modifier = Modifier,
) {
    CoachAndCoButton(
        text = stringResource(textRes),
        onClick = onClick,
        variant = variant,
        modifier = modifier,
    )
}

enum class CoachAndCoButtonVariant {
    Primary,
    Dark,
    Light,
}

@Pixel9aPreview
@Composable
fun CoachAndCoButtonsPreview() = ThemePreview {
    Column(verticalArrangement = Arrangement.spacedBy(Gaps.SM)) {
        CoachAndCoButton(text = "Primary", onClick = {})
        CoachAndCoButton(
            text = "Light Variant",
            onClick = {},
            variant = CoachAndCoButtonVariant.Light,
        )
        CoachAndCoButton(
            text = "Dark Variant",
            onClick = {},
            variant = CoachAndCoButtonVariant.Dark,
        )
    }
}
