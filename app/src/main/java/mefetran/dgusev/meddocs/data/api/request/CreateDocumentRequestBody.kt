package mefetran.dgusev.meddocs.data.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.data.model.Document

@Serializable
data class CreateDocumentRequestBody(
    val id: String = "",
    val title: String = "",
    val content: Map<String, String> = hashMapOf(),
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
) {
    companion object {
        fun fromDocument(document: Document) = CreateDocumentRequestBody(
            id = document.id,
            title = document.title,
            content = document.content,
            createdAt = document.createdAt,
            updatedAt = document.updatedAt,
        )
    }
}