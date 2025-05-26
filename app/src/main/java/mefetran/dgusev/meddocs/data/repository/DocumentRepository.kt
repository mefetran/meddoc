package mefetran.dgusev.meddocs.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mefetran.dgusev.meddocs.app.toRealmDictionary
import mefetran.dgusev.meddocs.data.api.DocumentApi
import mefetran.dgusev.meddocs.data.api.request.document.CreateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.request.document.UpdateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.response.document.toDocument
import mefetran.dgusev.meddocs.data.model.Category
import mefetran.dgusev.meddocs.data.model.Document
import mefetran.dgusev.meddocs.data.realm.DocumentRealmApi
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

interface DocumentRepository {
    suspend fun getDocuments(): Flow<Result<List<Document>>>

    suspend fun createDocument(requestBody: CreateDocumentRequestBody): Flow<Result<Document>>

    suspend fun getDocumentById(id: String): Flow<Result<Document>>

    suspend fun updateDocumentById(
        id: String,
        requestBody: UpdateDocumentRequestBody
    ): Flow<Result<Document>>

    suspend fun deleteDocumentById(id: String): Flow<Result<String>>

    suspend fun saveDocumentsListLocal(documentsList: List<Document>)

    suspend fun getDocumentsListOrNullLocal(): List<Document>?

    suspend fun deleteDocumentsListLocal()

    suspend fun saveDocumentLocal(document: Document)

    suspend fun getDocumentOrNullLocal(documentId: String): Document?

    suspend fun deleteDocumentLocal(documentId: String)
}

class RealDocumentRepositoryImpl @Inject constructor(
    private val documentApi: DocumentApi,
    private val documentRealmApi: DocumentRealmApi,
) : DocumentRepository {
    override suspend fun getDocuments(): Flow<Result<List<Document>>> =
        documentApi.getDocuments().map { result ->
            result.mapCatching { listResponse ->
                listResponse.map { documentResponse ->
                    documentResponse.toDocument()
                }
            }
        }

    override suspend fun createDocument(requestBody: CreateDocumentRequestBody): Flow<Result<Document>> =
        documentApi.createDocument(requestBody).map { result ->
            result.mapCatching { documentResponse -> documentResponse.toDocument() }
        }

    override suspend fun getDocumentById(id: String): Flow<Result<Document>> =
        documentApi.getDocumentById(id).map { result ->
            result.mapCatching { documentResponse -> documentResponse.toDocument() }
        }

    override suspend fun updateDocumentById(
        id: String,
        requestBody: UpdateDocumentRequestBody
    ): Flow<Result<Document>> = documentApi.updateDocumentById(id, requestBody).map { result ->
        result.mapCatching { documentResponse -> documentResponse.toDocument() }
    }

    override suspend fun deleteDocumentById(id: String): Flow<Result<String>> =
        documentApi.deleteDocumentById(id)

    override suspend fun saveDocumentsListLocal(documentsList: List<Document>) = documentRealmApi.saveDocumentsList(documentsList)

    override suspend fun getDocumentsListOrNullLocal(): List<Document>? = documentRealmApi.getDocumentsListOrNull()

    override suspend fun deleteDocumentsListLocal() = documentRealmApi.deleteDocumentsList()

    override suspend fun saveDocumentLocal(document: Document) = documentRealmApi.saveDocument(document)

    override suspend fun getDocumentOrNullLocal(documentId: String): Document? = documentRealmApi.getDocumentOrNull(documentId)

    override suspend fun deleteDocumentLocal(documentId: String) = documentRealmApi.deleteDocument(documentId)
}

class FakeDocumentRepositoryImpl @Inject constructor(
    private val documentRealmApi: DocumentRealmApi,
) : DocumentRepository {
    override suspend fun getDocuments(): Flow<Result<List<Document>>> = flow {
        runCatching {
            listOf(
                Document(
                    title = "Общий анализ крови",
                    date = LocalDate.now().minusDays(Random.nextLong(1, 5)).toString(),
                    description = "Показатели общего анализа крови в норме, без признаков воспаления или инфекции.",
                    category = Category.Laboratory.toString(),
                ),
                Document(
                    title = "Электрокардиограмма (ЭКГ)",
                    date = LocalDate.now().minusDays(Random.nextLong(6, 15)).toString(),
                    description = "Результаты ЭКГ в пределах нормы, нарушений ритма не выявлено.",
                    category = Category.ECG.toString(),
                ),
                Document(
                    title = "УЗИ органов брюшной полости",
                    date = LocalDate.now().minusDays(Random.nextLong(16, 30)).toString(),
                    description = "Печень, поджелудочная и селезёнка без патологии, размеры в пределах нормы.",
                    category = Category.Ultrasound.toString(),
                ),
                Document(
                    title = "МРТ головного мозга",
                    date = LocalDate.now().minusDays(Random.nextLong(31, 60)).toString(),
                    description = "Отсутствие признаков опухолевых образований или очаговых изменений.",
                    category = Category.MRI.toString(),
                ),
                Document(
                    title = "Эндоскопия желудка (ФГДС)",
                    date = LocalDate.now().minusDays(Random.nextLong(61, 100)).toString(),
                    description = "Выявлен поверхностный гастрит, биопсия не проводилась.",
                    category = Category.Endoscopy.toString(),
                )
            )
        }
    }

    override suspend fun createDocument(requestBody: CreateDocumentRequestBody): Flow<Result<Document>> = flow {
        runCatching {
            Document(
                title = requestBody.title,
            )
        }
    }

    override suspend fun getDocumentById(id: String): Flow<Result<Document>> = flow {
        runCatching {
            Document(
                id = id,
                title = "КТ органов грудной клетки",
                date = LocalDate.now().minusDays(Random.nextLong(30, 90)).toString(),
                description = "КТ-картина соответствует норме, патологических изменений не выявлено.",
                category = Category.CT.toString(),
            )
        }
    }

    override suspend fun updateDocumentById(
        id: String,
        requestBody: UpdateDocumentRequestBody
    ): Flow<Result<Document>> = flow {
        Document(
            id = id,
            content = requestBody.content?.toRealmDictionary() ?: emptyMap<String, String>().toRealmDictionary(),
        )
    }

    override suspend fun deleteDocumentById(id: String): Flow<Result<String>> = flow {
        runCatching { "204" }
    }

    override suspend fun saveDocumentsListLocal(documentsList: List<Document>) = documentRealmApi.saveDocumentsList(documentsList)

    override suspend fun getDocumentsListOrNullLocal(): List<Document>? = documentRealmApi.getDocumentsListOrNull()

    override suspend fun deleteDocumentsListLocal() = documentRealmApi.deleteDocumentsList()

    override suspend fun saveDocumentLocal(document: Document) = documentRealmApi.saveDocument(document)

    override suspend fun getDocumentOrNullLocal(documentId: String): Document? = documentRealmApi.getDocumentOrNull(documentId)

    override suspend fun deleteDocumentLocal(documentId: String) = documentRealmApi.deleteDocument(documentId)
}