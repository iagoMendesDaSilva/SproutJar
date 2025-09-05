package com.sproutjar.utils

import android.content.pm.PackageInfo
import com.sproutjar.R
import com.sproutjar.data.models.AppIcon


object AppIcons {
    lateinit var icons: List<AppIcon>

    fun initialize(packageInfo: PackageInfo) {
        icons = listOf(
            AppIcon(
                "${packageInfo.packageName}.MainActivity",
                R.string.greenish,
                R.drawable.sprout_jar_logo
            ),
            AppIcon(
                "${packageInfo.packageName}.MainActivityDark",
                R.string.dark,
                R.drawable.sprout_jar_logo
            ),
            AppIcon(
                "${packageInfo.packageName}.MainActivityLight",
                R.string.light,
                R.drawable.sprout_jar_logo
            ),
        )
    }
}