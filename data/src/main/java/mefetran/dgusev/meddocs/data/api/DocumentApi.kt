package mefetran.dgusev.meddocs.data.api

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.data.api.request.document.CreateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.request.document.UpdateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.response.document.DocumentResponse

interface DocumentApi {
    suspend fun getDocuments(): Flow<Result<List<DocumentResponse>>>

    suspend fun createDocument(requestBody: CreateDocumentRequestBody): Flow<Result<DocumentResponse>>

    suspend fun getDocumentById(id: String): Flow<Result<DocumentResponse>>

    suspend fun updateDocumentById(
        id: String,
        requestBody: UpdateDocumentRequestBody
    ): Flow<Result<DocumentResponse>>

    suspend fun deleteDocumentById(id: String): Result<Int>
}
