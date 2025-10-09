package com.sproutjar.ui.screens.settingsScreen.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sproutjar.data.models.SettingsSection.Companion.sections
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sproutjar.R
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.Currency
import com.sproutjar.data.models.SettingId
import com.sproutjar.data.models.SettingsItem
import com.sproutjar.data.models.SettingsSection
import com.sproutjar.data.models.Theme
import com.sproutjar.data.models.Transaction
import com.sproutjar.data.models.TransactionType
import com.sproutjar.ui.screens.potScreen.composables.TransactionsList
import com.sproutjar.ui.theme.Green
import com.sproutjar.ui.theme.SproutJarTheme
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingsList(
    filteredSections: List<SettingsSection>,
    appSettings: AppSettings,
    isBiometricAvailable: Boolean,
    onPress: (settingsItem: SettingsItem) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        filteredSections.forEach { section ->
            stickyHeader { Text(stringResource(section.title)) }
            items(section.items) { item ->
                SettingCard(
                    settingItem = item,
                    switchValue = when (item.id) {
                        SettingId.NOTIFICATIONS -> appSettings.notifications
                        SettingId.BIOMETRICS -> appSettings.biometrics
                        else -> null
                    },
                    enabled = item.id != SettingId.BIOMETRICS || isBiometricAvailable,
                    onPress = { onPress(item) }
                )
            }
        }
    }
}

@Composable
fun SettingCard(
    settingItem: SettingsItem,
    switchValue: Boolean? = null,
    enabled: Boolean = true,
    onPress: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onPress() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                Modifier
                    .padding(end = 8.dp)
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) { Icon(settingItem.icon, contentDescription = stringResource(settingItem.title)) }
            Column {
                Text(stringResource(settingItem.title), style = MaterialTheme.typography.bodySmall)
                Text(
                    stringResource(settingItem.description),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .alpha(0.5f)
                        .padding(top = 3.dp)
                )
            }
        }
        switchValue?.let {
            Switch(
                enabled = enabled,
                checked = it,
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Green,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(0.1f),
                    uncheckedBorderColor = Color.Transparent
                ),
                onCheckedChange = { onPress() }
            )
        }
    }
}


@Preview
@Composable
fun SettingsListPreview() {
    SproutJarTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            SettingsList(sections, AppSettings(), true) {}
        }
    }
}