package com.sproutjar.data.models

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.sproutjar.R

sealed class SettingsSection(
    @StringRes val title: Int,
    val items: List<SettingsItem>
) {
    object Preferences : SettingsSection(
        R.string.preferences,
        listOf(
            SettingsItem.Notifications,
            SettingsItem.Theme,
            SettingsItem.Currency,
            SettingsItem.Language,
            SettingsItem.AppIcon
        )
    )

    object Security : SettingsSection(
        R.string.security,
        listOf(
            SettingsItem.Biometrics
        )
    )

    object Account : SettingsSection(
        R.string.account,
        listOf(
            SettingsItem.ResetAccount
        )
    )

    companion object {
        val sections: List<SettingsSection> = listOf(
            Preferences,
            Security,
            Account
        )
    }
}