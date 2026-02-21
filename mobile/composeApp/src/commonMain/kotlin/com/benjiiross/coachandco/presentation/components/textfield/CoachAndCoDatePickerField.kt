package com.benjiiross.coachandco.presentation.components.textfield

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.benjiiross.coachandco.presentation.components.text.CoachAndCoTextCaption
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachAndCoDatePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value.toEpochMillisOrNull(),
    )

    LaunchedEffect(value) {
        datePickerState.selectedDateMillis = value.toEpochMillisOrNull()
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value.toDisplayDate(),
            onValueChange = {},
            readOnly = true,
            label = { CoachAndCoTextCaption(label) },
            leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
            isError = error != null,
            supportingText = error?.let { { CoachAndCoTextCaption(it, color = MaterialTheme.colorScheme.error) } },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.matchParentSize().clickable { showDialog = true })
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onValueChange(it.toIsoDate()) }
                    showDialog = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Annuler") }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun String.toEpochMillisOrNull(): Long? =
    try { LocalDate.parse(this).toEpochDays() * 86_400_000L } catch (_: Exception) { null }

private fun Long.toIsoDate(): String =
    LocalDate.fromEpochDays((this / 86_400_000L).toInt()).toString()

private fun String.toDisplayDate(): String =
    try {
        val d = LocalDate.parse(this)
        "${d.day.toString().padStart(2, '0')}/${d.month.toString().padStart(2, '0')}/${d.year}"
    } catch (_: Exception) { this }
