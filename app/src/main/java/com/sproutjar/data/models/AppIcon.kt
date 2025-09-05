package com.sproutjar.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AppIcon(
    val component: String,
    @StringRes val name: Int,
    @DrawableRes val foregroundResource: Int,
)