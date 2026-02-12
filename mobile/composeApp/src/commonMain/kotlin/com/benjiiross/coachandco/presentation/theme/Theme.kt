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
import coach_and_co.composeapp.generated.resources.montserrat_bold
import coach_and_co.composeapp.generated.resources.montserrat_extra_bold
import coach_and_co.composeapp.generated.resources.montserrat_medium
import coach_and_co.composeapp.generated.resources.montserrat_regular
import org.jetbrains.compose.resources.Font

object Gaps {
    val S = 12.dp
    val M = 16.dp
    val L = 24.dp
    val XL = 32.dp
}

private val AppColors =
    lightColorScheme()

@Composable
private fun montserratTypography(): Typography {
    val montserratFont =
        FontFamily(
            Font(Res.font.montserrat_regular, FontWeight.Normal),
            Font(Res.font.montserrat_medium, FontWeight.Medium),
            Font(Res.font.montserrat_bold, FontWeight.Bold),
            Font(Res.font.montserrat_extra_bold, FontWeight.ExtraBold),
        )

    return Typography(
        displayLarge =
            TextStyle(fontFamily = montserratFont, fontWeight = FontWeight.Bold, fontSize = 32.sp),
        headlineLarge =
            TextStyle(
                fontFamily = montserratFont,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
            ),
        titleLarge =
            TextStyle(
                fontFamily = montserratFont,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = montserratFont,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                lineHeight = 24.sp,
            ),
        bodySmall =
            TextStyle(
                fontFamily = montserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 18.sp,
            ),
    )
}

@Composable
fun CoachAndCoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = AppColors, typography = montserratTypography(), content = content)
}
