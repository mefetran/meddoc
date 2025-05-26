package mefetran.dgusev.meddocs.data.api.response.document

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.app.toRealmDictionary
import mefetran.dgusev.meddocs.data.model.Document

@Serializable
data class DocumentResponse(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val file: String = "",
    val category: String = "",
    val priority: Int = 0,
    val content: Map<String, String> = hashMapOf(),
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
)

fun DocumentResponse.toDocument() = Document(
    id = this.id,
    title = this.title,
    content = this.content.toRealmDictionary(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    description = this.description,
    date = this.date,
    file = this.file,
    category = this.category,
    priority = this.priority,
)