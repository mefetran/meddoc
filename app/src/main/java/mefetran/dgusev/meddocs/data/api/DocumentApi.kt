package mefetran.dgusev.meddocs.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mefetran.dgusev.meddocs.data.api.request.CreateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.request.UpdateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.response.DocumentResponse
import javax.inject.Inject

interface DocumentApi {
    fun getDocuments(): Flow<Result<List<DocumentResponse>>>

    fun createDocument(requestBody: CreateDocumentRequestBody): Flow<Result<DocumentResponse>>

    fun getDocumentById(id: String): Flow<Result<DocumentResponse>>

    fun updateDocumentById(
        id: String,
        requestBody: UpdateDocumentRequestBody
    ): Flow<Result<DocumentResponse>>

    fun deleteDocumentById(id: String): Flow<Result<String>>
}

class DocumentApiImpl @Inject constructor(private val httpClient: HttpClient) : DocumentApi {
    override fun getDocuments(): Flow<Result<List<DocumentResponse>>> = flow {
        emit(kotlin.runCatching {
            httpClient.get("documents").body()
        })
    }

    override fun createDocument(requestBody: CreateDocumentRequestBody): Flow<Result<DocumentResponse>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("documents") {
                    setBody(requestBody)
                }.body()
            })
        }

    override fun getDocumentById(id: String): Flow<Result<DocumentResponse>> = flow {
        emit(kotlin.runCatching {
            httpClient.get("documents/$id").body()
        })
    }

    override fun updateDocumentById(
        id: String,
        requestBody: UpdateDocumentRequestBody
    ): Flow<Result<DocumentResponse>> = flow {
        emit(kotlin.runCatching {
            httpClient.put("documents/$id") {
                setBody(requestBody)
            }.body()
        })
    }

    override fun deleteDocumentById(id: String): Flow<Result<String>> = flow {
        emit(kotlin.runCatching {
            httpClient.delete("documents/$id").body()
        })
    }
}