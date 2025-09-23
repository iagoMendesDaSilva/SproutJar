package com.sproutjar.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sproutjar.data.api.ApiService
import com.sproutjar.data.database.AppDatabase
import com.sproutjar.data.database.PotDao
import com.sproutjar.data.database.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideApi(): ApiService {
        val customGson: Gson = GsonBuilder()
            .create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .baseUrl(ApiService.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile("sproutjar_datastore") }
        )
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sproutjar_db"
        )  .fallbackToDestructiveMigration() //TODO
            .build()

    @Singleton
    @Provides
    fun providePotDao(appDatabase: AppDatabase): PotDao =
        appDatabase.potDao()

    @Singleton
    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao =
        appDatabase.transactionDao()
}



