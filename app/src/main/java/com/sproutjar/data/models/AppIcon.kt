package com.sproutjar.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AppIcon(
    val theme: Theme,
    val component: String,
    @DrawableRes val foregroundResource: Int,
)