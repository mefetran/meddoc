package mefetran.dgusev.meddocs.di

import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import mefetran.dgusev.meddocs.data.api.TokenInvalidationEmitter
import mefetran.dgusev.meddocs.data.api.TokenManager
import mefetran.dgusev.meddocs.data.api.TokenRefresher
import mefetran.dgusev.meddocs.proto.Settings
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenManagerModule {
    @Provides
    @Singleton
    fun provideTokenInvalidationEmitter(): TokenInvalidationEmitter = TokenInvalidationEmitter()

    @Provides
    @Singleton
    fun provideTokenManagerModule(
        settingsDataStore: DataStore<Settings>,
        tokenRefresher: TokenRefresher,
        dispatcher: CoroutineDispatcher,
        tokenInvalidationEmitter: TokenInvalidationEmitter,
    ): TokenManager = TokenManager(
        settingsDataStore = settingsDataStore,
        tokenRefresher = tokenRefresher,
        dispatcher = dispatcher,
        tokenInvalidationEmitter = tokenInvalidationEmitter,
    )
}