package mefetran.dgusev.meddocs.domain.usecase.document

import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface DeleteDocumentLocalUseCase : UseCase<DeleteDocumentLocalUseCase.Params, Unit> {
    data class Params(
        val documentId: String
    )
}
