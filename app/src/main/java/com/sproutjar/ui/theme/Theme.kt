package com.sproutjar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.sproutjar.data.models.Theme
import androidx.compose.ui.graphics.Color

private val SproutJarColorScheme = darkColorScheme(
    primary = LightGreen,
    onPrimary = DarkGreen,
    background = DarkGreen,
    surface = MediumGreen,
    secondaryContainer = Color.Transparent,
    onSecondaryContainer = Color.White,
    surfaceContainer = MediumGreen,
    onSurfaceVariant = Color.White.copy(.5f)

)

private val DarkColorScheme = darkColorScheme(
    primary = LightGreen,
    onPrimary = DarkGreen,
    background = DarkBackground,
    surface = DarkSurface

)

private val LightColorScheme = lightColorScheme(
    primary = LightGreen,
    onPrimary = DarkGreen,
    background = LightBackground,
    surface = LightSurface

)

@Composable
fun SproutJarTheme(
    theme: Theme = Theme.SPROUT_JAR,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        Theme.SPROUT_JAR -> SproutJarColorScheme
        Theme.DARK -> DarkColorScheme
        Theme.LIGHT -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}