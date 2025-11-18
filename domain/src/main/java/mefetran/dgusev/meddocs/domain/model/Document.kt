package mefetran.dgusev.meddocs.domain.model

data class Document(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val localFilePath: String,
    val file: String,
    val category: Category,
    val priority: Int,
    val content: Map<String, String>,
    val createdAt: String,
    val updatedAt: String,
)
