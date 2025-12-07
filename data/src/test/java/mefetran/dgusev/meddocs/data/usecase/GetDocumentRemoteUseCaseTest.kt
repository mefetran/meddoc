package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.GetDocumentRemoteUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentRemoteUseCase
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

class GetDocumentRemoteUseCaseTest {
    private val repository = mockk<DocumentRepository>()
    private lateinit var useCase: GetDocumentRemoteUseCase

    @BeforeEach
    fun setup() {
        useCase = GetDocumentRemoteUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `get document remote returns document flow`() = runTest {
        val document = Document(
            DOCUMENT_ID,
            DOCUMENT_TITLE,
            DOCUMENT_DESCRIPTION,
            DOCUMENT_DATE,
            "",
            "",
            DOCUMENT_CATEGORY,
            DOCUMENT_PRIORITY,
            DOCUMENT_CONTENT,
            "",
            ""
        )
        coEvery { repository.getDocumentById(DOCUMENT_ID) } returns flow {
            emit(
                Result.success(
                    document
                )
            )
        }

        val result = useCase.execute(GetDocumentRemoteUseCase.Params(DOCUMENT_ID)).first()
        assertTrue(result.isSuccess)
        assertEquals(DOCUMENT_TITLE, result.getOrNull()?.title)
        coVerify(exactly = 1) { repository.getDocumentById(DOCUMENT_ID) }
    }

    @Test
    fun `get document remote non existent returns failure`() = runTest {
        coEvery { repository.getDocumentById(DOCUMENT_ID) } returns flow {
            emit(
                Result.failure(
                    NoSuchElementException("not found")
                )
            )
        }

        val result = useCase.execute(GetDocumentRemoteUseCase.Params(DOCUMENT_ID)).first()
        assertTrue(result.isFailure)
        assertEquals("not found", result.exceptionOrNull()?.message)
    }
}
