package mefetran.dgusev.meddocs.domain.usecase.document

import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface UpdateDocumentLocalUseCase : UseCase<UpdateDocumentLocalUseCase.Params, Unit> {
    data class Params(
        val id: String,
        val title: String? = null,
        val description: String? = null,
        val date: String? = null,
        val localFilePath: String? = null,
        val file: String? = null,
        val category: Category? = null,
        val priority: Int? = null,
        val content: Map<String, String>? = null,
    )
}