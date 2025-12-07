package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.CreateDocumentUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.CreateDocumentUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val DOCUMENT_ID = "1"
private const val DOCUMENT_TITLE = "some title"
private const val DOCUMENT_DESCRIPTION = "some description"
private const val DOCUMENT_DATE = "2024-01-01"
private const val DOCUMENT_PRIORITY = 0
private val DOCUMENT_CATEGORY = Category.Laboratory
private val DOCUMENT_CONTENT = mapOf<String, String>()

class CreateDocumentUseCaseTest {

    private val repository = mockk<DocumentRepository>()
    private lateinit var useCase: CreateDocumentUseCase

    @BeforeEach
    fun setup() {
        useCase = CreateDocumentUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `create document calls repository and returns created document`() = runTest {
        val created = Document(
            id = DOCUMENT_ID,
            title = DOCUMENT_TITLE,
            description = DOCUMENT_DESCRIPTION,
            date = DOCUMENT_DATE,
            localFilePath = "",
            file = "",
            category = DOCUMENT_CATEGORY,
            priority = DOCUMENT_PRIORITY,
            content = DOCUMENT_CONTENT,
            createdAt = "",
            updatedAt = "",
        )

        // mockk behavior: repository.createDocument(...) returns Flow<Result<Document>>
        coEvery {
            repository.createDocument(
                any(), any(), any(), any(), any(), any(), any(), any()
            )
        } returns flow { emit(Result.success(created)) }

        val params = CreateDocumentUseCase.Params(
            title = created.title,
            description = created.description,
            date = created.date,
            localFilePath = created.localFilePath.ifBlank { null },
            file = created.file.ifBlank { null },
            category = created.category,
            priority = created.priority,
            content = created.content,
        )

        val resultFlow = useCase.execute(params)
        val result = resultFlow.first()
        assertTrue(result.isSuccess)
        assertEquals(created, result.getOrNull())

        coVerify(exactly = 1) {
            repository.createDocument(any(), any(), any(), any(), any(), any(), any(), any())
        }
    }

    @Test
    fun `mock variations - relaxed mock and slot capture`() = runTest {
        // relaxed mock example
        val relaxedRepo = mockk<DocumentRepository>(relaxed = true)
        val useCase2 = CreateDocumentUseCaseImpl(relaxedRepo)

        // When using relaxed repo, no need to stub; we can spy on calls with coVerify
        val params = CreateDocumentUseCase.Params(
            title = DOCUMENT_TITLE,
            description = null,
            date = null,
            localFilePath = null,
            file = null,
            category = DOCUMENT_CATEGORY,
            priority = DOCUMENT_PRIORITY,
            content = DOCUMENT_CONTENT,
        )

        useCase2.execute(params)
        coVerify {
            relaxedRepo.createDocument(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        }

        val fakeRepo = spyk(object : DocumentRepository {
            override suspend fun getDocuments(): Flow<Result<List<Document>>> =
                flow { emit(Result.success(emptyList())) }

            override suspend fun createDocument(
                title: String,
                description: String?,
                date: String?,
                localFilePath: String?,
                file: String?,
                category: Category?,
                priority: Int?,
                content: Map<String, String>?
            ): Flow<Result<Document>> = flow {
                emit(
                    Result.success(
                        Document(
                            DOCUMENT_ID,
                            title,
                            description ?: "",
                            date ?: "",
                            localFilePath ?: "",
                            file ?: "",
                            category ?: Category.Other,
                            priority ?: 0,
                            content ?: mapOf(),
                            "",
                            ""
                        )
                    )
                )
            }

            override suspend fun getDocumentById(id: String) =
                flow { emit(Result.failure<Document>(RuntimeException("not implemented"))) }

            override suspend fun updateDocumentById(
                id: String,
                title: String?,
                description: String?,
                date: String?,
                file: String?,
                category: Category?,
                priority: Int?,
                content: Map<String, String>?
            ) = flow { emit(Result.failure<Document>(RuntimeException("not implemented"))) }

            override suspend fun deleteDocumentById(id: String) = Result.success(1)
            override suspend fun saveDocumentsListLocal(documentsList: List<Document>) {}
            override suspend fun getDocumentsListOrNullLocal() = null
            override suspend fun deleteDocumentsListLocal() {}
            override suspend fun saveDocumentLocal(document: Document) {}
            override suspend fun getDocumentOrNullLocal(documentId: String) = null
            override suspend fun deleteDocumentLocal(documentId: String) {}
            override suspend fun observeDocuments() = flow { emit(emptyList<Document>()) }
            override suspend fun updateDocumentLocal(
                id: String,
                title: String?,
                description: String?,
                date: String?,
                localFilePath: String?,
                file: String?,
                category: Category?,
                priority: Int?,
                content: Map<String, String>?
            ) {
            }
        })
        val useCase3 = CreateDocumentUseCaseImpl(fakeRepo)
        val resultFlow = useCase3.execute(params)
        val result = resultFlow.first()
        assertTrue(result.isSuccess)
        assertEquals(DOCUMENT_TITLE, result.getOrNull()?.title)
        coVerify {
            fakeRepo.createDocument(
                DOCUMENT_TITLE,
                null,
                null,
                null,
                null,
                DOCUMENT_CATEGORY,
                DOCUMENT_PRIORITY,
                DOCUMENT_CONTENT,
            )
        }
    }
}
