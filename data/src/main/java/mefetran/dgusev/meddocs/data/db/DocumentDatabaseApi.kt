package mefetran.dgusev.meddocs.data.db

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.data.model.DocumentEntity

interface DocumentDatabaseApi {
    suspend fun saveDocumentsList(documentsList: List<DocumentEntity>)
    suspend fun getDocumentsListOrNull(): List<DocumentEntity>?
    suspend fun deleteDocumentsList()
    suspend fun saveDocument(document: DocumentEntity)
    suspend fun getDocumentOrNull(documentId: String): DocumentEntity?
    suspend fun deleteDocument(documentId: String)
    suspend fun observeDocuments(): Flow<List<DocumentEntity>>
}
