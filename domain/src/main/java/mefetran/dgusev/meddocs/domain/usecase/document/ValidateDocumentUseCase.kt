package mefetran.dgusev.meddocs.domain.usecase.document

import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface ValidateDocumentUseCase :
    UseCase<ValidateDocumentUseCase.Params, ValidateResult> {

    data class Params(
        val title: String,
        val description: String? = null,
        val date: String? = null,
        val category: Category? = null,
        val priority: Int? = null,
        val content: Map<String, String>? = null,
    )
}
