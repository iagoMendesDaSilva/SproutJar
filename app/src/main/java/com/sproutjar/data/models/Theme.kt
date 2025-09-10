package com.sproutjar.data.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import com.sproutjar.R
import com.sproutjar.ui.theme.DarkSurface
import com.sproutjar.ui.theme.LightSurface
import com.sproutjar.ui.theme.MediumGreen

enum class Theme(@StringRes val title: Int) {
    DARK(R.string.dark),
    LIGHT(R.string.light),
    SPROUT_JAR(R.string.greenish);
}

 val Theme.icon
    get() = when (this) {
        Theme.DARK -> Icons.Default.DarkMode
        Theme.LIGHT -> Icons.Default.LightMode
        Theme.SPROUT_JAR -> Icons.Default.Palette
    }

 val Theme.color
    get() = when (this) {
        Theme.DARK -> DarkSurface
        Theme.LIGHT -> LightSurface
        Theme.SPROUT_JAR -> MediumGreen
    }