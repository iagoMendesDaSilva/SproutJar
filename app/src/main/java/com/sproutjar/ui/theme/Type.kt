package com.sproutjar.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sproutjar.R

val sulSansFamily = FontFamily(
    Font(R.font.sulsans_light, FontWeight.Light),
    Font(R.font.sulsans_black, FontWeight.Black),
    Font(R.font.sulsans_bold, FontWeight.Bold),
    Font(R.font.sulsans_medium, FontWeight.Medium),
    Font(R.font.sulsans_regular, FontWeight.Normal),
    Font(R.font.sulsans_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.sulsans_blackitalic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.sulsans_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.sulsans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.sulsans_italic, FontWeight.Normal, FontStyle.Italic),
)

internal object TypographyTokens {
    val DisplayLarge = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    )
    val DisplayMedium = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    )
    val DisplaySmall = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    )
    val HeadlineLarge = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    )
    val HeadlineMedium = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    )
    val HeadlineSmall = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )
    val TitleLarge = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
    val TitleMedium = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    )
    val TitleSmall = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
    val BodyLarge = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    val BodyMedium = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
    val BodySmall = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
    val LabelLarge = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
    val LabelMedium = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    val LabelSmall = TextStyle(
        fontFamily = sulSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
}

val Typography = Typography(
    displayLarge = TypographyTokens.DisplayLarge,
    displayMedium = TypographyTokens.DisplayMedium,
    displaySmall = TypographyTokens.DisplaySmall,
    headlineLarge = TypographyTokens.HeadlineLarge,
    headlineMedium = TypographyTokens.HeadlineMedium,
    headlineSmall = TypographyTokens.HeadlineSmall,
    titleLarge = TypographyTokens.TitleLarge,
    titleMedium = TypographyTokens.TitleMedium,
    titleSmall = TypographyTokens.TitleSmall,
    bodyLarge = TypographyTokens.BodyLarge,
    bodyMedium = TypographyTokens.BodyMedium,
    bodySmall = TypographyTokens.BodySmall,
    labelLarge = TypographyTokens.LabelLarge,
    labelMedium = TypographyTokens.LabelMedium,
    labelSmall = TypographyTokens.LabelSmall
)