package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.GetDocumentsUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.GetDocumentsUseCase
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

class GetDocumentsUseCaseTest {
    private val repository = mockk<DocumentRepository>()
    private lateinit var useCase: GetDocumentsUseCase

    @BeforeEach
    fun setup() {
        useCase = GetDocumentsUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `get documents delegates to repository and returns list`() = runTest {
        val documents = listOf(
            Document(
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
        )
        coEvery { repository.getDocuments() } returns flow { emit(Result.success(documents)) }

        val flow = useCase.execute(Unit)
        val result = flow.first()
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals(DOCUMENT_TITLE, result.getOrNull()?.first()?.title)

        coVerify(exactly = 1) { repository.getDocuments() }
    }

    @Test
    fun `repository error is propagated`() = runTest {
        coEvery { repository.getDocuments() } returns flow { emit(Result.failure(RuntimeException("net"))) }

        val result = useCase.execute(Unit).first()
        assertTrue(result.isFailure)
        assertEquals("net", result.exceptionOrNull()?.message)
    }
}
