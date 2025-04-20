package mefetran.dgusev.meddocs.data.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateDocumentRequestBody(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
)
