package com.sproutjar.utils

import com.sproutjar.data.models.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppPreference {
    fun appSettings(): Flow<AppSettings>
    suspend fun saveAppSettings(appSettings: AppSettings)
}