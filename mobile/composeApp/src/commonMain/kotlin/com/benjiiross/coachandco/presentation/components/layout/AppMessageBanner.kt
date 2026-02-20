package com.benjiiross.coachandco.presentation.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benjiiross.coachandco.presentation.UiMessage
import com.benjiiross.coachandco.presentation.theme.Gaps

@Composable
fun AppMessageBanner(
    message: UiMessage?,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut(),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Gaps.M),
    ) {
        val containerColor = when (message) {
            is UiMessage.Success -> MaterialTheme.colorScheme.primaryContainer
            is UiMessage.Error -> MaterialTheme.colorScheme.errorContainer
            null -> MaterialTheme.colorScheme.surface
        }
        val contentColor = when (message) {
            is UiMessage.Success -> MaterialTheme.colorScheme.onPrimaryContainer
            is UiMessage.Error -> MaterialTheme.colorScheme.onErrorContainer
            null -> MaterialTheme.colorScheme.onSurface
        }
        val icon = when (message) {
            is UiMessage.Success -> Icons.Default.CheckCircle
            is UiMessage.Error -> Icons.Default.ErrorOutline
            null -> Icons.Default.CheckCircle
        }
        val text = when (message) {
            is UiMessage.Success -> message.text
            is UiMessage.Error -> message.text
            null -> ""
        }

        Card(
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = containerColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = Gaps.M, vertical = Gaps.SM),
                horizontalArrangement = Arrangement.spacedBy(Gaps.SM),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor,
                )
            }
        }
    }
}
