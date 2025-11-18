package mefetran.dgusev.meddocs.domain.usecase.document

import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface GetDocumentLocalUseCase : UseCase<GetDocumentLocalUseCase.Params, Document?> {
    data class Params(
        val documentId: String
    )
}
