package mefetran.dgusev.meddocs.domain.usecase.document

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface CreateDocumentUseCase : UseCase<CreateDocumentUseCase.Params, Flow<Result<Document>>> {
    data class Params(
        val title: String,
        val description: String? = null,
        val date: String? = null,
        val file: String? = null,
        val category: Category? = null,
        val priority: Int? = null,
        val content: Map<String, String>? = null,
    )
}
