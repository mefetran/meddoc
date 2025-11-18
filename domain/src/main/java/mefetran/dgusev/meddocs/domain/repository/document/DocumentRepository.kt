package mefetran.dgusev.meddocs.domain.repository.document

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document

interface DocumentRepository {
    suspend fun getDocuments(): Flow<Result<List<Document>>>
    suspend fun createDocument(
        title: String,
        description: String? = null,
        date: String? = null,
        localFilePath: String? = null,
        file: String? = null,
        category: Category? = null,
        priority: Int? = null,
        content: Map<String, String>? = null,
    ): Flow<Result<Document>>
    suspend fun getDocumentById(id: String): Flow<Result<Document>>
    suspend fun updateDocumentById(
        id: String,
        title: String? = null,
        description: String? = null,
        date: String? = null,
        file: String? = null,
        category: Category? = null,
        priority: Int? = null,
        content: Map<String, String>? = null,
    ): Flow<Result<Document>>
    suspend fun deleteDocumentById(id: String): Result<Int>
    suspend fun saveDocumentsListLocal(documentsList: List<Document>)
    suspend fun getDocumentsListOrNullLocal(): List<Document>?
    suspend fun deleteDocumentsListLocal()
    suspend fun saveDocumentLocal(document: Document)
    suspend fun getDocumentOrNullLocal(documentId: String): Document?
    suspend fun deleteDocumentLocal(documentId: String)
    suspend fun observeDocuments(): Flow<List<Document>>
    suspend fun updateDocumentLocal(
        id: String,
        title: String? = null,
        description: String? = null,
        date: String? = null,
        localFilePath: String? = null,
        file: String? = null,
        category: Category? = null,
        priority: Int? = null,
        content: Map<String, String>? = null,
    )
}
