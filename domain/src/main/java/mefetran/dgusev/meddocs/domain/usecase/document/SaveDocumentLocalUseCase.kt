package mefetran.dgusev.meddocs.domain.usecase.document

import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface SaveDocumentLocalUseCase : UseCase<SaveDocumentLocalUseCase.Params, Unit> {
    data class Params(
        val document: Document,
    )
}
