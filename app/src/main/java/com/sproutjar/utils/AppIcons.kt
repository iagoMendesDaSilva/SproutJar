package com.sproutjar.utils

import android.content.pm.PackageInfo
import com.sproutjar.R
import com.sproutjar.data.models.AppIcon
import com.sproutjar.data.models.Theme


object AppIcons {
    lateinit var icons: List<AppIcon>

    fun initialize(packageInfo: PackageInfo) {
        icons = listOf(
            AppIcon(
                Theme.SPROUT_JAR,
                "${packageInfo.packageName}.MainActivity",
                R.drawable.sprout_jar_logo
            ),
            AppIcon(
                Theme.DARK,
                "${packageInfo.packageName}.MainActivityDark",
                R.drawable.sprout_jar_logo_white
            ),
            AppIcon(
                Theme.LIGHT,
                "${packageInfo.packageName}.MainActivityLight",
                R.drawable.sprout_jar_logo_black
            ),
        )
    }
}