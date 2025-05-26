package mefetran.dgusev.meddocs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mefetran.dgusev.meddocs.data.api.DocumentApi
import mefetran.dgusev.meddocs.data.api.DocumentApiImpl
import mefetran.dgusev.meddocs.data.api.TokenRefreshApi
import mefetran.dgusev.meddocs.data.api.TokenRefreshApiImpl
import mefetran.dgusev.meddocs.data.api.UserApi
import mefetran.dgusev.meddocs.data.api.UserApiImpl
import mefetran.dgusev.meddocs.data.realm.DocumentRealmApi
import mefetran.dgusev.meddocs.data.realm.DocumentRealmApiImpl
import mefetran.dgusev.meddocs.data.realm.UserRealmApi
import mefetran.dgusev.meddocs.data.realm.UserRealmApiImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {
    @Binds
    @Singleton
    abstract fun bindDocumentApi(impl: DocumentApiImpl): DocumentApi

    @Binds
    @Singleton
    abstract fun bindUserApi(impl: UserApiImpl): UserApi

    @Binds
    @Singleton
    abstract fun bindUserRealmApi(impl: UserRealmApiImpl): UserRealmApi

    @Binds
    @Singleton
    abstract fun bindDocumentRealmApi(impl: DocumentRealmApiImpl): DocumentRealmApi

    @Binds
    @Singleton
    abstract fun bindTokenRefreshApi(impl: TokenRefreshApiImpl): TokenRefreshApi
}