package mefetran.dgusev.meddocs.data.model

import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document

data class DocumentEntity(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val file: String,
    val category: Category,
    val priority: Int,
    val content: Map<String, String>,
    val createdAt: String,
    val updatedAt: String,
)

fun DocumentEntity.toDocument(): Document = Document(
    id = this.id,
    title = this.title,
    description = this.description,
    date = this.date,
    file = this.file,
    category = this.category,
    priority = this.priority,
    content = this.content.toMap(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun Document.toDocumentEntity(): DocumentEntity = DocumentEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    date = this.date,
    file = this.file,
    category = this.category,
    priority = this.priority,
    content = this.content.toMap(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)
