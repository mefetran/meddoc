package mefetran.dgusev.meddocs.domain.usecase.document

import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface SaveDocumentsListLocalUseCase : UseCase<SaveDocumentsListLocalUseCase.Params, Unit> {
    data class Params(
        val documents: List<Document>
    )
}
