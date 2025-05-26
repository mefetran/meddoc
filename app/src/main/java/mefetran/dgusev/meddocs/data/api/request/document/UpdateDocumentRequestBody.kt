package mefetran.dgusev.meddocs.data.api.request.document

import kotlinx.serialization.Serializable

@Serializable
data class UpdateDocumentRequestBody(
    val title: String? = null,
    val description: String? = null,
    val date: String? = null,
    val file: String? = null,
    val category: String? = null,
    val priority: Int? = null,
    val content: Map<String, String>? = null,
)