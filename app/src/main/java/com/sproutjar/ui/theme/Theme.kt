package com.sproutjar.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.sproutjar.data.models.Theme

private val SproutJarColorScheme = darkColorScheme(
    primary = LightGreen,
    onPrimary = DarkGreen,
    secondary = Color.White,
    background = DarkGreen,
    surface = MediumGreen,
    secondaryContainer = Color.Transparent,
    onSecondaryContainer = Color.White,
    surfaceContainer = MediumGreen,
    onSurfaceVariant = Color.White.copy(.5f),
    primaryContainer = LightGreen,

)

private val DarkColorScheme = darkColorScheme(
    primary = LightGreen,
    onPrimary = DarkGreen,
    secondary = Color.White,
    background = DarkBackground,
    surface = DarkSurface,
    secondaryContainer = Color.Transparent,
    onSecondaryContainer = Color.White,
    surfaceContainer = DarkSurface,
    onSurfaceVariant = Color.White.copy(.5f),
    primaryContainer = Color.White,

    )

private val LightColorScheme = lightColorScheme(
    primary = LightGreen,
    onPrimary = DarkGreen,
    secondary = Color.Black,
    background = LightBackground,
    surface = LightSurface,
    secondaryContainer = Color.Transparent,
    onSecondaryContainer = Color.Black,
    surfaceContainer = LightSurface,
    onSurfaceVariant = Color.Black.copy(.5f),
    primaryContainer = Color.Black,

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