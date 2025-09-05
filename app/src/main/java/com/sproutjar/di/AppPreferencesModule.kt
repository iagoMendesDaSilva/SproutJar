package com.sproutjar.di

import com.sproutjar.utils.AppPreference
import com.sproutjar.utils.DataStoreService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class AppPreferencesModule {

    @Binds
    abstract fun bindAppPreferences(dataStoreService: DataStoreService): AppPreference
}