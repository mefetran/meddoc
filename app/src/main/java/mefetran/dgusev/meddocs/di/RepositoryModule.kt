package mefetran.dgusev.meddocs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mefetran.dgusev.meddocs.data.repository.DocumentRepository
import mefetran.dgusev.meddocs.data.repository.FakeDocumentRepositoryImpl
import mefetran.dgusev.meddocs.data.repository.FakeUserRepositoryImpl
import mefetran.dgusev.meddocs.data.repository.RealDocumentRepositoryImpl
import mefetran.dgusev.meddocs.data.repository.UserRepository
import mefetran.dgusev.meddocs.data.repository.RealUserRepositoryImpl
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealRepository

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FakeRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    @RealRepository
    abstract fun bindRealDocumentRepository(impl: RealDocumentRepositoryImpl): DocumentRepository

    @Binds
    @Singleton
    @RealRepository
    abstract fun bindRealUserRepository(impl: RealUserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    @FakeRepository
    abstract fun bindFakeUserRepository(impl: FakeUserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    @FakeRepository
    abstract fun bindFakeDocumentRepository(impl: FakeDocumentRepositoryImpl): DocumentRepository
}