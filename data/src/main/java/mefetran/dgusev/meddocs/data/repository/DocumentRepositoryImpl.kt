package mefetran.dgusev.meddocs.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mefetran.dgusev.meddocs.data.api.DocumentApi
import mefetran.dgusev.meddocs.data.api.request.document.CreateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.request.document.UpdateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.response.document.toDocumentEntity
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.data.db.DocumentDatabaseApi
import mefetran.dgusev.meddocs.data.model.toDocument
import mefetran.dgusev.meddocs.data.model.toDocumentEntity
import java.time.LocalDate
import java.util.HashMap
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

class DocumentRepositoryImpl @Inject constructor(
    private val documentApi: DocumentApi,
    private val documentDatabaseApi: DocumentDatabaseApi,
) : DocumentRepository {
    override suspend fun getDocuments(): Flow<Result<List<Document>>> =
        documentApi.getDocuments().map { result ->
            result.mapCatching { listResponse ->
                listResponse.map { documentResponse ->
                    documentResponse.toDocumentEntity().toDocument()
                }
            }
        }

    override suspend fun createDocument(
        title: String,
        description: String?,
        date: String?,
        file: String?,
        category: Category?,
        priority: Int?,
        content: Map<String, String>?
    ): Flow<Result<Document>> = documentApi.createDocument(
        CreateDocumentRequestBody(
            title = title,
            description = description,
            date = date,
            file = file,
            category = category?.name,
            priority = priority,
            content = content,
        )
    ).map { result ->
        result.mapCatching { documentResponse ->
            documentResponse.toDocumentEntity().toDocument()
        }
    }

    override suspend fun getDocumentById(id: String): Flow<Result<Document>> =
        documentApi.getDocumentById(id).map { result ->
            result.mapCatching { documentResponse ->
                documentResponse.toDocumentEntity().toDocument()
            }
        }

    override suspend fun updateDocumentById(
        id: String,
        title: String?,
        description: String?,
        date: String?,
        file: String?,
        category: Category?,
        priority: Int?,
        content: Map<String, String>?
    ): Flow<Result<Document>> =
        documentApi.updateDocumentById(
            id, UpdateDocumentRequestBody(
                title = title,
                description = description,
                date = date,
                file = file,
                category = category?.name,
                priority = priority,
                content = content,
            )
        ).map { result ->
            result.mapCatching { documentResponse ->
                documentResponse.toDocumentEntity().toDocument()
            }
        }

    override suspend fun deleteDocumentById(id: String): Result<Int> =
        documentApi.deleteDocumentById(id)

    override suspend fun saveDocumentsListLocal(documentsList: List<Document>) =
        documentDatabaseApi.saveDocumentsList(documentsList.map { it.toDocumentEntity() })

    override suspend fun getDocumentsListOrNullLocal(): List<Document>? =
        documentDatabaseApi.getDocumentsListOrNull()?.map { it.toDocument() }

    override suspend fun deleteDocumentsListLocal() = documentDatabaseApi.deleteDocumentsList()

    override suspend fun saveDocumentLocal(document: Document) =
        documentDatabaseApi.saveDocument(document.toDocumentEntity())

    override suspend fun getDocumentOrNullLocal(documentId: String): Document? =
        documentDatabaseApi.getDocumentOrNull(documentId)?.toDocument()

    override suspend fun deleteDocumentLocal(documentId: String) =
        documentDatabaseApi.deleteDocument(documentId)

    override suspend fun observeDocuments(): Flow<List<Document>> =
        documentDatabaseApi
            .observeDocuments()
            .map { documentEntities -> documentEntities.map { documentEntity -> documentEntity.toDocument() } }
}

class FakeDocumentRepositoryImpl @Inject constructor(
    private val documentDatabaseApi: DocumentDatabaseApi,
) : DocumentRepository {
    override suspend fun getDocuments(): Flow<Result<List<Document>>> = flow {
        runCatching {
            listOf(
                Document(
                    title = "Общий анализ крови",
                    date = LocalDate.now().minusDays(Random.nextLong(1, 5)).toString(),
                    description = "Показатели общего анализа крови в норме, без признаков воспаления или инфекции.",
                    category = Category.Laboratory,
                    id = UUID.randomUUID().toString(),
                    file = "",
                    priority = 0,
                    content = HashMap<String, String>(),
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString(),
                ),
                Document(
                    title = "Электрокардиограмма (ЭКГ)",
                    date = LocalDate.now().minusDays(Random.nextLong(6, 15)).toString(),
                    description = "Результаты ЭКГ в пределах нормы, нарушений ритма не выявлено.",
                    category = Category.ECG,
                    id = UUID.randomUUID().toString(),
                    file = "",
                    priority = 0,
                    content = HashMap<String, String>(),
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString(),
                ),
                Document(
                    title = "УЗИ органов брюшной полости",
                    date = LocalDate.now().minusDays(Random.nextLong(16, 30)).toString(),
                    description = "Печень, поджелудочная и селезёнка без патологии, размеры в пределах нормы.",
                    category = Category.Ultrasound,
                    id = UUID.randomUUID().toString(),
                    file = "",
                    priority = 0,
                    content = HashMap<String, String>(),
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString(),
                ),
                Document(
                    title = "МРТ головного мозга",
                    date = LocalDate.now().minusDays(Random.nextLong(31, 60)).toString(),
                    description = "Отсутствие признаков опухолевых образований или очаговых изменений.",
                    category = Category.MRI,
                    id = UUID.randomUUID().toString(),
                    file = "",
                    priority = 0,
                    content = HashMap<String, String>(),
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString(),
                ),
                Document(
                    title = "Эндоскопия желудка (ФГДС)",
                    date = LocalDate.now().minusDays(Random.nextLong(61, 100)).toString(),
                    description = "Выявлен поверхностный гастрит, биопсия не проводилась.",
                    category = Category.Endoscopy,
                    id = UUID.randomUUID().toString(),
                    file = "",
                    priority = 0,
                    content = HashMap<String, String>(),
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString(),
                )
            )
        }
    }

    override suspend fun createDocument(
        title: String,
        description: String?,
        date: String?,
        file: String?,
        category: Category?,
        priority: Int?,
        content: Map<String, String>?
    ): Flow<Result<Document>> =
        flow {
            runCatching {
                Document(
                    title = title,
                    id = UUID.randomUUID().toString(),
                    file = file ?: "",
                    priority = priority ?: 0,
                    content = content ?: HashMap<String, String>(),
                    createdAt = System.currentTimeMillis().toString(),
                    updatedAt = System.currentTimeMillis().toString(),
                    date = date ?: "",
                    description = description ?: "",
                    category = category ?: Category.Other,
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
                category = Category.CT,
                file = "",
                priority = 0,
                content = HashMap<String, String>(),
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString(),
            )
        }
    }

    override suspend fun updateDocumentById(
        id: String,
        title: String?,
        description: String?,
        date: String?,
        file: String?,
        category: Category?,
        priority: Int?,
        content: Map<String, String>?
    ): Flow<Result<Document>> = flow {
        Document(
            id = id,
            title = title ?: "",
            description = description ?: "",
            date = date ?: "",
            file = file ?: "",
            category = category ?: Category.Other,
            priority = priority ?: 0,
            content = content ?: HashMap<String, String>(),
            createdAt = System.currentTimeMillis().toString(),
            updatedAt = System.currentTimeMillis().toString(),
        )
    }

    override suspend fun deleteDocumentById(id: String): Result<Int> = runCatching { 204 }

    override suspend fun saveDocumentsListLocal(documentsList: List<Document>) =
        documentDatabaseApi.saveDocumentsList(documentsList.map { it.toDocumentEntity() })

    override suspend fun getDocumentsListOrNullLocal(): List<Document>? =
        documentDatabaseApi.getDocumentsListOrNull()
            ?.map { documentEntity -> documentEntity.toDocument() }

    override suspend fun deleteDocumentsListLocal() = documentDatabaseApi.deleteDocumentsList()

    override suspend fun saveDocumentLocal(document: Document) =
        documentDatabaseApi.saveDocument(document.toDocumentEntity())

    override suspend fun getDocumentOrNullLocal(documentId: String): Document? =
        documentDatabaseApi.getDocumentOrNull(documentId)?.toDocument()

    override suspend fun deleteDocumentLocal(documentId: String) =
        documentDatabaseApi.deleteDocument(documentId)

    override suspend fun observeDocuments(): Flow<List<Document>> =
        documentDatabaseApi
            .observeDocuments()
            .map { documentEntities -> documentEntities.map { documentEntity -> documentEntity.toDocument() } }
}
