package com.benjiiross.coachandco.presentation.component.button

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
import com.benjiiross.coachandco.presentation.component.text.CoachAndCoTextTitleSmall
import com.benjiiross.coachandco.presentation.preview.Pixel9aPreview
import com.benjiiross.coachandco.presentation.preview.ThemePreview
import com.benjiiross.coachandco.presentation.theme.Gaps

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CoachAndCoButton(
    text: String,
    onClick: () -> Unit,
    isDarkVariant: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme

    val colors =
        if (isDarkVariant) {
            ButtonDefaults.buttonColors(
                containerColor = colorScheme.inverseSurface,
                contentColor = colorScheme.inverseOnSurface,
            )
        } else {
            ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary,
            )
        }

    val border =
        if (isDarkVariant) {
            BorderStroke(Gaps.XXS, MaterialTheme.colorScheme.outline)
        } else {
            null
        }

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        border = border,
        shape = MaterialTheme.shapes.medium,
    ) {
        CoachAndCoTextTitleSmall(modifier = Modifier.padding(horizontal = Gaps.S, vertical = Gaps.XS), text = text)
    }
}

@Pixel9aPreview
@Composable
fun CoachAndCoButtonsPreview() = ThemePreview {
    Column(verticalArrangement = Arrangement.spacedBy(Gaps.S)) {
        CoachAndCoButton(text = "Primary", onClick = {})
        CoachAndCoButton(text = "Dark Variant", onClick = {}, isDarkVariant = true)
    }
}
