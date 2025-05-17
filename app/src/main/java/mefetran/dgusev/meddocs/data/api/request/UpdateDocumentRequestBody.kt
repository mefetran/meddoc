package mefetran.dgusev.meddocs.data.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.data.model.Document

@Serializable
data class UpdateDocumentRequestBody(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
) {
    companion object {
        fun fromDocument(document: Document) = UpdateDocumentRequestBody(
            id = document.id,
            title = document.title,
            content = document.content,
            createdAt = document.createdAt,
            updatedAt = document.updatedAt,
        )
    }
}