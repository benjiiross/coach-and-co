package com.benjiiross.coachandco.presentation.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
private fun CoachAndCoBaseText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    fontWeight: FontWeight? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        style = if (fontWeight != null) style.copy(fontWeight = fontWeight) else style,
        color = color,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = if (maxLines != Int.MAX_VALUE) TextOverflow.Ellipsis else TextOverflow.Clip,
    )
}

@Composable
fun CoachAndCoTextDisplay(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Center,
) {
    CoachAndCoBaseText(text, MaterialTheme.typography.displayLarge, modifier, color, textAlign)
}

@Composable
fun CoachAndCoTextDisplay(
    textRes: StringResource,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Center,
) = CoachAndCoTextDisplay(stringResource(textRes), modifier, color, textAlign)

@Composable
fun CoachAndCoTextTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
) {
    CoachAndCoBaseText(text, MaterialTheme.typography.titleLarge, modifier, color, textAlign, maxLines)
}

@Composable
fun CoachAndCoTextTitle(
    textRes: StringResource,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
) = CoachAndCoTextTitle(stringResource(textRes), modifier, color, textAlign, maxLines)

@Composable
fun CoachAndCoTextTitleSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
) {
    CoachAndCoBaseText(text, MaterialTheme.typography.bodyLarge, modifier, color, textAlign, maxLines)
}

@Composable
fun CoachAndCoTextTitleSmall(
    textRes: StringResource,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
) = CoachAndCoTextTitleSmall(stringResource(textRes), modifier, color, textAlign, maxLines)

@Composable
fun CoachAndCoTextBody(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
) {
    CoachAndCoBaseText(text, MaterialTheme.typography.bodyLarge, modifier, color, textAlign)
}

@Composable
fun CoachAndCoTextBody(
    textRes: StringResource,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
) = CoachAndCoTextBody(stringResource(textRes), modifier, color, textAlign)

@Composable
fun CoachAndCoTextCaption(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = 3,
) {
    CoachAndCoBaseText(text, MaterialTheme.typography.bodySmall, modifier, color, textAlign, maxLines)
}

@Composable
fun CoachAndCoTextCaption(
    textRes: StringResource,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = 3,
) = CoachAndCoTextCaption(stringResource(textRes), modifier, color, textAlign, maxLines)
