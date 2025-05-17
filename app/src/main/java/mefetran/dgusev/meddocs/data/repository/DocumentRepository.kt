package mefetran.dgusev.meddocs.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mefetran.dgusev.meddocs.app.toRealmSet
import mefetran.dgusev.meddocs.data.api.DocumentApi
import mefetran.dgusev.meddocs.data.api.request.CreateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.request.UpdateDocumentRequestBody
import mefetran.dgusev.meddocs.data.api.response.toDocument
import mefetran.dgusev.meddocs.data.model.Category
import mefetran.dgusev.meddocs.data.model.Document
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
}

class RealDocumentRepositoryImpl @Inject constructor(
    private val documentApi: DocumentApi,
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
}

class FakeDocumentRepositoryImpl : DocumentRepository {
    override suspend fun getDocuments(): Flow<Result<List<Document>>> = flow {
        runCatching {
            listOf(
                Document(
                    title = "Общий анализ крови",
                    date = LocalDate.now().minusDays(Random.nextLong(1, 5)).toString(),
                    description = "Показатели общего анализа крови в норме, без признаков воспаления или инфекции.",
                    tags = setOf("Обследование", "Анализы").toRealmSet(),
                    category = Category.Laboratory.toString(),
                ),
                Document(
                    title = "Электрокардиограмма (ЭКГ)",
                    date = LocalDate.now().minusDays(Random.nextLong(6, 15)).toString(),
                    description = "Результаты ЭКГ в пределах нормы, нарушений ритма не выявлено.",
                    tags = setOf("Сердце", "Диагностика").toRealmSet(),
                    category = Category.ECG.toString(),
                ),
                Document(
                    title = "УЗИ органов брюшной полости",
                    date = LocalDate.now().minusDays(Random.nextLong(16, 30)).toString(),
                    description = "Печень, поджелудочная и селезёнка без патологии, размеры в пределах нормы.",
                    tags = setOf("УЗИ", "ЖКТ").toRealmSet(),
                    category = Category.Ultrasound.toString(),
                ),
                Document(
                    title = "МРТ головного мозга",
                    date = LocalDate.now().minusDays(Random.nextLong(31, 60)).toString(),
                    description = "Отсутствие признаков опухолевых образований или очаговых изменений.",
                    tags = setOf("МРТ", "Голова").toRealmSet(),
                    category = Category.MRI.toString(),
                ),
                Document(
                    title = "Эндоскопия желудка (ФГДС)",
                    date = LocalDate.now().minusDays(Random.nextLong(61, 100)).toString(),
                    description = "Выявлен поверхностный гастрит, биопсия не проводилась.",
                    tags = setOf("Желудок", "Гастроэнтерология").toRealmSet(),
                    category = Category.Endoscopy.toString(),
                )
            )
        }
    }

    override suspend fun createDocument(requestBody: CreateDocumentRequestBody): Flow<Result<Document>> = flow {
        runCatching {
            Document(
                id = requestBody.id,
                title = requestBody.title,
                content = requestBody.content,
                createdAt = requestBody.createdAt,
                updatedAt = requestBody.updatedAt,
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
                tags = setOf("КТ", "Лёгкие").toRealmSet(),
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
            title = requestBody.title,
            content = requestBody.content,
            createdAt = requestBody.createdAt,
            updatedAt = requestBody.updatedAt,
        )
    }

    override suspend fun deleteDocumentById(id: String): Flow<Result<String>> = flow {
        runCatching { "204" }
    }
}