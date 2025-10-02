package com.sproutjar.ui.screens.settingsScreen

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.sproutjar.R
import com.sproutjar.data.models.AppIcon
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.Currency
import com.sproutjar.data.models.DialogInfo
import com.sproutjar.data.models.GlobalDialogState
import com.sproutjar.data.models.Languages
import com.sproutjar.data.models.MessageDialog
import com.sproutjar.data.models.SettingId
import com.sproutjar.data.models.SettingsItem
import com.sproutjar.data.models.SettingsSection
import com.sproutjar.data.models.SettingsSection.Companion.sections
import com.sproutjar.data.models.Theme
import com.sproutjar.data.models.color
import com.sproutjar.data.models.icon
import com.sproutjar.navigation.Screens
import com.sproutjar.ui.composables.LogoLabel
import com.sproutjar.ui.composables.SearchHeader
import com.sproutjar.ui.theme.Green
import com.sproutjar.ui.theme.SproutJarTheme
import com.sproutjar.utils.AppIcons
import com.sproutjar.utils.ConnectionState
import com.sproutjar.utils.DevicePreviews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    connection: ConnectionState,
    navController: NavHostController,
    appSettings: AppSettings,
    saveAppSettings: (AppSettings) -> Unit,
    showGlobalDialog: (GlobalDialogState) -> Unit
) {
    SettingsScreenUI(appSettings, saveAppSettings, navController, showGlobalDialog)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsScreenUI(
    appSettings: AppSettings,
    saveAppSettings: (AppSettings) -> Unit,
    navController: NavHostController,
    showGlobalDialog: (GlobalDialogState) -> Unit
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var searchText by remember { mutableStateOf("") }
    val showBottomSheet = remember { mutableStateOf<SettingId?>(null) }

    val isBiometricAvailable = if (LocalInspectionMode.current) false
    else remember {
        BiometricManager.from(context).canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    val filteredSections = sections.filterBySearch(searchText)

    val settingActions: Map<SettingId, () -> Unit> = mapOf(
        SettingId.NOTIFICATIONS to { saveAppSettings(appSettings.copy(notifications = !appSettings.notifications)) },
        SettingId.THEME to { showBottomSheet.value = SettingId.THEME },
        SettingId.CURRENCY to { showBottomSheet.value = SettingId.CURRENCY },
        SettingId.LANGUAGE to { showBottomSheet.value = SettingId.LANGUAGE },
        SettingId.APP_ICON to { showBottomSheet.value = SettingId.APP_ICON },
        SettingId.BIOMETRICS to { saveAppSettings(appSettings.copy(biometrics = !appSettings.biometrics)) },
        SettingId.RESET_ACCOUNT to {
            showGlobalDialog(
                GlobalDialogState(
                    dialogInfo = DialogInfo(
                        MessageDialog(
                            R.string.reset_account,
                            R.string.reset_account_desc
                        )
                    ),
                    onSuccess = {
                        saveAppSettings(AppSettings())
                        navController.navigate(Screens.SplashScreen.name) { popUpTo(0) }
                    }
                )
            )
        }
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding(), start = 12.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchHeader(R.string.settings) { searchText = it }
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                if (filteredSections.isEmpty())
                    LogoLabel(
                        appSettings.theme,
                        MessageDialog(R.string.not_found_settings, R.string.settings_unavailable)
                    )
                else {
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
                                    onPress = settingActions[item.id] ?: {}
                                )
                            }
                        }
                    }
                }
            }
        }

        showBottomSheet.value?.let { id ->
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp,
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet.value = null }
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .padding(
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (id) {
                        SettingId.THEME -> SettingContentBottomSheet(
                            R.string.theme,
                            R.string.theme_desc,
                            Theme.entries,
                            itemContent = { theme ->
                                SettingItemContentBottomSheet(
                                    icon = {
                                        Icon(
                                            theme.icon,
                                            contentDescription = stringResource(theme.title),
                                            tint = if (theme == Theme.LIGHT) Color.Black else Color.White
                                        )
                                    },
                                    label = theme.title,
                                    backgroundColor = theme.color
                                )
                            },
                            onSelect = {
                                saveAppSettings(appSettings.copy(theme = it))
                                closeBottomSheet(scope, sheetState, showBottomSheet)
                            }
                        )

                        SettingId.CURRENCY -> SettingContentBottomSheet(
                            R.string.currency,
                            R.string.currency_desc,
                            Currency.entries,
                            itemContent = { currency ->
                                SettingItemContentBottomSheet(
                                    icon = {
                                        Image(
                                            painterResource(currency.image),
                                            contentDescription = stringResource(currency.title),
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    },
                                    label = currency.title,
                                )
                            },
                            onSelect = {
                                saveAppSettings(appSettings.copy(currency = it))
                                closeBottomSheet(scope, sheetState, showBottomSheet)
                            }
                        )

                        SettingId.LANGUAGE -> SettingContentBottomSheet(
                            R.string.language,
                            R.string.language_desc,
                            Languages.entries,
                            itemContent = { lang ->
                                SettingItemContentBottomSheet(
                                    icon = {
                                        Image(
                                            painterResource(lang.image),
                                            contentDescription = stringResource(
                                                lang.title
                                            ),
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    },
                                    label = lang.title,
                                )
                            },
                            onSelect = {
                                saveAppSettings(appSettings.copy(language = it))
                                AppCompatDelegate.setApplicationLocales(
                                    LocaleListCompat.forLanguageTags(
                                        it.code
                                    )
                                )
                                closeBottomSheet(scope, sheetState, showBottomSheet)
                            }
                        )

                        SettingId.APP_ICON -> SettingContentBottomSheet(
                            R.string.app_icon,
                            R.string.app_icon_desc,
                            AppIcons.icons,
                            itemContent = { icon ->
                                SettingItemContentBottomSheet(
                                    icon = {
                                        Image(
                                            painterResource(icon.foregroundResource),
                                            contentDescription = stringResource(icon.theme.title),
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    },
                                    label = icon.theme.title,
                                    backgroundColor = icon.theme.color
                                )
                            },
                            onSelect = {
                                updateAppIcon(it, context, packageManager)
                                closeBottomSheet(scope, sheetState, showBottomSheet)
                            }
                        )

                        else -> {}
                    }
                }
            }
        }
    }
}


@Composable
fun SettingItemContentBottomSheet(
    icon: @Composable () -> Unit,
    @StringRes label: Int,
    backgroundColor: Color = MaterialTheme.colorScheme.surface
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(backgroundColor, CircleShape)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) { icon() }
        Text(
            stringResource(label),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}

@Composable
fun <T> SettingContentBottomSheet(
    @StringRes title: Int,
    @StringRes description: Int,
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    onSelect: (T) -> Unit
) {
    Text(stringResource(title), style = MaterialTheme.typography.headlineSmall)
    Text(
        stringResource(description),
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(bottom = 30.dp, top = 10.dp),
    )
    LazyRow(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(items) { index, item ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .clickable { onSelect(item) })
            { itemContent(item) }
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

@OptIn(ExperimentalMaterial3Api::class)
private fun closeBottomSheet(
    scope: CoroutineScope,
    sheetState: SheetState,
    showBottomSheet: MutableState<SettingId?>
) {
    scope.launch { sheetState.hide() }
        .invokeOnCompletion { if (!sheetState.isVisible) showBottomSheet.value = null }
}

private fun updateAppIcon(appIconItem: AppIcon, context: Context, packageManager: PackageManager) {
    AppIcons.icons.forEachIndexed { index, appIcon ->
        packageManager.setComponentEnabledSetting(
            ComponentName(context, appIcon.component),
            if (appIconItem == appIcon) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}


@Composable
private fun List<SettingsSection>.filterBySearch(searchText: String): List<SettingsSection> {
    if (searchText.isEmpty()) return this
    return this.filter { section ->
        stringResource(section.title).contains(searchText, ignoreCase = true) ||
                section.items.any {
                    stringResource(it.title).contains(
                        searchText,
                        ignoreCase = true
                    )
                }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@DevicePreviews
@Composable
fun SettingsPreview() {
    SproutJarTheme {
        Scaffold {
            SettingsScreenUI(AppSettings(), saveAppSettings = {}, rememberNavController()) {}
        }
    }
}