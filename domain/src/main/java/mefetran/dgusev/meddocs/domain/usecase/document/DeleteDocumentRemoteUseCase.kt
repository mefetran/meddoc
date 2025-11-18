package mefetran.dgusev.meddocs.domain.usecase.document

import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface DeleteDocumentRemoteUseCase : UseCase<DeleteDocumentRemoteUseCase.Params, Result<Int>> {
    data class Params(
        val documentId: String
    )
}
