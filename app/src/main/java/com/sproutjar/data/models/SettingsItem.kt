package com.sproutjar.data.models

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.vector.ImageVector
import com.sproutjar.R

sealed class SettingsItem(
    val id: SettingId,
    val icon: ImageVector,
    @StringRes val title: Int,
    @StringRes val description: Int
) {
    object Notifications : SettingsItem(
        SettingId.NOTIFICATIONS,
        Icons.Default.Notifications,
        R.string.notifications,
        R.string.notifications_desc
    )

    object Theme : SettingsItem(
        SettingId.THEME,
        Icons.Default.Palette,
        R.string.theme,
        R.string.theme_desc
    )

    object Currency : SettingsItem(
        SettingId.CURRENCY,
        Icons.Default.AttachMoney,
        R.string.currency,
        R.string.currency_desc
    )

    object Language : SettingsItem(
        SettingId.LANGUAGE,
        Icons.Default.Language,
        R.string.language,
        R.string.language_desc
    )

    object AppIcon : SettingsItem(
        SettingId.APP_ICON,
        Icons.Default.Savings,
        R.string.app_icon,
        R.string.app_icon_desc
    )

    object Biometrics : SettingsItem(
        SettingId.BIOMETRICS,
        Icons.Default.Fingerprint,
        R.string.biometrics,
        R.string.biometrics_desc
    )

    object ResetAccount : SettingsItem(
        SettingId.RESET_ACCOUNT,
        Icons.Default.Delete,
        R.string.reset_account_label,
        R.string.reset_account_desc_label
    )
}