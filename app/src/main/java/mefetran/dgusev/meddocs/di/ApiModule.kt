package mefetran.dgusev.meddocs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mefetran.dgusev.meddocs.data.api.DocumentApi
import mefetran.dgusev.meddocs.data.api.DocumentKtorApiImpl
import mefetran.dgusev.meddocs.data.api.TokenRefreshApi
import mefetran.dgusev.meddocs.data.api.TokenRefreshApiImpl
import mefetran.dgusev.meddocs.data.api.UserApi
import mefetran.dgusev.meddocs.data.api.UserKtorApiImpl
import mefetran.dgusev.meddocs.data.db.DocumentDatabaseApi
import mefetran.dgusev.meddocs.data.db.DocumentRealmDatabase
import mefetran.dgusev.meddocs.data.db.UserDatabaseApi
import mefetran.dgusev.meddocs.data.db.UserRealmDatabase
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    @Singleton
    abstract fun bindDocumentApi(impl: DocumentKtorApiImpl): DocumentApi

    @Binds
    @Singleton
    abstract fun bindUserApi(impl: UserKtorApiImpl): UserApi

    @Binds
    @Singleton
    abstract fun bindUserDatabaseApi(impl: UserRealmDatabase): UserDatabaseApi

    @Binds
    @Singleton
    abstract fun bindDocumentDatabaseApi(impl: DocumentRealmDatabase): DocumentDatabaseApi

    @Binds
    @Singleton
    abstract fun bindTokenRefreshApi(impl: TokenRefreshApiImpl): TokenRefreshApi
}