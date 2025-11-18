package mefetran.dgusev.meddocs.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mefetran.dgusev.meddocs.data.usecase.document.CreateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.DeleteDocumentLocalUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.DeleteDocumentRemoteUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.GetDocumentLocalUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.GetDocumentRemoteUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.GetDocumentsLocalUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.GetDocumentsUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.ObserveDocumentsUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.SaveDocumentLocalUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.SaveDocumentsListLocalUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.document.UpdateDocumentLocalUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.user.DeleteUserUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.user.GetUserUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.user.SaveUserUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.user.SignInUserUseCaseImpl
import mefetran.dgusev.meddocs.data.usecase.user.SignUpUserUseCaseImpl
import mefetran.dgusev.meddocs.domain.usecase.document.CreateDocumentUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteDocumentLocalUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteDocumentRemoteUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentLocalUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentRemoteUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentsLocalUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentsUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.ObserveDocumentsUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.SaveDocumentLocalUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.SaveDocumentsListLocalUseCase
import mefetran.dgusev.meddocs.domain.usecase.document.UpdateDocumentLocalUseCase
import mefetran.dgusev.meddocs.domain.usecase.user.DeleteUserUseCase
import mefetran.dgusev.meddocs.domain.usecase.user.GetUserUseCase
import mefetran.dgusev.meddocs.domain.usecase.user.SaveUserUseCase
import mefetran.dgusev.meddocs.domain.usecase.user.SignInUserUseCase
import mefetran.dgusev.meddocs.domain.usecase.user.SignUpUserUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetUserUseCase(impl: GetUserUseCaseImpl): GetUserUseCase

    @Binds
    abstract fun bindSignInUserUseCase(impl: SignInUserUseCaseImpl): SignInUserUseCase

    @Binds
    abstract fun bindSignUpUserUseCase(impl: SignUpUserUseCaseImpl): SignUpUserUseCase

    @Binds
    abstract fun bindSaveUserUseCase(impl: SaveUserUseCaseImpl): SaveUserUseCase

    @Binds
    abstract fun bindDeleteUserUseCase(impl: DeleteUserUseCaseImpl): DeleteUserUseCase

    @Binds
    abstract fun bindGetDocumentsUseCase(impl: GetDocumentsUseCaseImpl): GetDocumentsUseCase

    @Binds
    abstract fun bindSaveDocumentsListLocalUseCase(impl: SaveDocumentsListLocalUseCaseImpl): SaveDocumentsListLocalUseCase

    @Binds
    abstract fun bindObserveDocumentsUseCase(impl: ObserveDocumentsUseCaseImpl): ObserveDocumentsUseCase

    @Binds
    abstract fun bindGetDocumentLocalUseCase(impl: GetDocumentLocalUseCaseImpl): GetDocumentLocalUseCase

    @Binds
    abstract fun bindGetDocumentRemoteUseCase(impl: GetDocumentRemoteUseCaseImpl): GetDocumentRemoteUseCase

    @Binds
    abstract fun bindDeleteDocumentRemoteUseCase(impl: DeleteDocumentRemoteUseCaseImpl): DeleteDocumentRemoteUseCase

    @Binds
    abstract fun bindDeleteDocumentLocalUseCase(impl: DeleteDocumentLocalUseCaseImpl): DeleteDocumentLocalUseCase

    @Binds
    abstract fun bindCreateDocumentUseCase(impl: CreateDocumentUseCaseImpl): CreateDocumentUseCase

    @Binds
    abstract fun bindSaveDocumentLocalUseCase(impl: SaveDocumentLocalUseCaseImpl): SaveDocumentLocalUseCase

    @Binds
    abstract fun bindUpdateDocumentLocalUseCase(impl: UpdateDocumentLocalUseCaseImpl): UpdateDocumentLocalUseCase

    @Binds
    abstract fun bindGetDocumentsLocalUseCase(impl: GetDocumentsLocalUseCaseImpl): GetDocumentsLocalUseCase
}
