package mefetran.dgusev.meddocs.di

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mefetran.dgusev.meddocs.app.datastore.settingsDataStore
import mefetran.dgusev.meddocs.proto.Settings
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreSettingsModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Settings> = context.settingsDataStore
}