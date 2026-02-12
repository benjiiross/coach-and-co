package com.benjiiross.coachandco.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coach_and_co.composeapp.generated.resources.Res
import coach_and_co.composeapp.generated.resources.inter_black
import coach_and_co.composeapp.generated.resources.inter_bold
import coach_and_co.composeapp.generated.resources.inter_extra_bold
import coach_and_co.composeapp.generated.resources.inter_medium
import coach_and_co.composeapp.generated.resources.inter_regular
import org.jetbrains.compose.resources.Font

@Composable
fun CoachAndCoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = CoachAndCoColors, typography = interTypography(), content = content)
}

object Gaps {
    val XXS = 2.dp
    val XS = 4.dp
    val S = 8.dp
    val SM = 12.dp
    val M = 16.dp
    val L = 24.dp
    val XL = 32.dp
    val XXL = 48.dp
}

private val CoachAndCoColors =
    lightColorScheme(
        primary = GreenCoach,
        onPrimary = WhiteCoach,
        surface = WhiteCoach,
        onSurface = BlackCoach,
        onSurfaceVariant = GrayCoach,
        inverseSurface = BlackCoach,
        inverseOnSurface = WhiteCoach,
        outline = GreenCoach,
    )

@Composable
private fun interTypography(): Typography {
    val interFont =
        FontFamily(
            Font(Res.font.inter_regular, FontWeight.Normal),
            Font(Res.font.inter_medium, FontWeight.Medium),
            Font(Res.font.inter_bold, FontWeight.Bold),
            Font(Res.font.inter_extra_bold, FontWeight.ExtraBold),
            Font(Res.font.inter_black, FontWeight.Black),
        )

    return Typography(
        displayLarge =
            TextStyle(fontFamily = interFont, fontWeight = FontWeight.Black, fontSize = 48.sp),
        titleLarge =
            TextStyle(fontFamily = interFont, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp),
        bodyLarge =
            TextStyle(
                fontFamily = interFont,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 24.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = interFont,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
            ),
    )
}
