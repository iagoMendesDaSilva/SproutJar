package com.sproutjar.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sproutjar.data.models.AppSettings
import com.sproutjar.data.models.Currency
import com.sproutjar.data.models.Languages
import com.sproutjar.data.models.Languages.Companion.getDeviceDefaultLanguage
import com.sproutjar.data.models.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreService @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppPreference {

    companion object {
        internal val KEY_THEME = stringPreferencesKey("KEY_THEME")
        internal val KEY_LANGUAGE = stringPreferencesKey("KEY_LANGUAGE")
        internal val KEY_BIOMETRICS = booleanPreferencesKey("KEY_BIOMETRICS")
        internal val KEY_CURRENCY = stringPreferencesKey("KEY_CURRENCY")
        internal val KEY_NOTIFICATIONS = booleanPreferencesKey("KEY_NOTIFICATIONS")

    }

    override fun appSettings(): Flow<AppSettings> {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }
            .map { preference ->
                AppSettings(
                    theme = enumValueOf(preference[KEY_THEME] ?: Theme.SPROUT_JAR.name),
                    language = preference[KEY_LANGUAGE]?.let { enumValueOf<Languages>(it) }
                        ?: getDeviceDefaultLanguage(),                    biometrics = preference[KEY_BIOMETRICS] ?: false,
                    currency = enumValueOf(preference[KEY_CURRENCY] ?: Currency.USD.name),
                    notifications = preference[KEY_NOTIFICATIONS] ?: false,
                )
            }
    }

    override suspend fun saveAppSettings(appSettings: AppSettings) {
        dataStore.edit { preference ->
            preference[KEY_THEME] = appSettings.theme.name
            preference[KEY_LANGUAGE] = appSettings.language.name
            preference[KEY_BIOMETRICS] = appSettings.biometrics
            preference[KEY_CURRENCY] = appSettings.currency.name
            preference[KEY_NOTIFICATIONS] = appSettings.notifications
        }
    }
}