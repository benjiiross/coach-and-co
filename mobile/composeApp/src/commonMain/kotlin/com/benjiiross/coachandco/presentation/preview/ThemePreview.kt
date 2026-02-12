package com.benjiiross.coachandco.presentation.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.benjiiross.coachandco.presentation.theme.CoachAndCoTheme

@Composable
fun ThemePreview(content: @Composable () -> Unit) {
    CoachAndCoTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}