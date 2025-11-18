package mefetran.dgusev.meddocs.data.db

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.data.model.DocumentEntity
import mefetran.dgusev.meddocs.domain.model.Category

interface DocumentDatabaseApi {
    suspend fun saveDocumentsList(documentsList: List<DocumentEntity>)
    suspend fun getDocumentsListOrNull(): List<DocumentEntity>?
    suspend fun deleteDocumentsList()
    suspend fun saveDocument(document: DocumentEntity)
    suspend fun getDocumentOrNull(documentId: String): DocumentEntity?
    suspend fun deleteDocument(documentId: String)
    suspend fun observeDocuments(): Flow<List<DocumentEntity>>
    suspend fun updateDocument(
        id: String,
        title: String?,
        description: String?,
        date: String?,
        localFilePath: String?,
        file: String?,
        category: Category?,
        priority: Int?,
        content: Map<String, String>?
    )
}
