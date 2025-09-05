package com.sproutjar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sproutjar.data.models.AppSettings
import com.sproutjar.utils.AppPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appPreference: AppPreference
) : ViewModel() {

    val appSettings: StateFlow<AppSettings> = appPreference.appSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), AppSettings())

    fun saveAppSettings(appSettings: AppSettings) {
        viewModelScope.launch {
            appPreference.saveAppSettings(appSettings)
        }
    }

}