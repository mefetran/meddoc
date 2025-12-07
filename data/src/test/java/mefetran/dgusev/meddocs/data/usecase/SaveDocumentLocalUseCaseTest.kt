package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.SaveDocumentLocalUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.model.Document
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.SaveDocumentLocalUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


private const val DOCUMENT_ID = "1"
private const val DOCUMENT_TITLE = "some title"
private const val DOCUMENT_DESCRIPTION = "some description"
private const val DOCUMENT_DATE = "2024-01-01"
private const val DOCUMENT_PRIORITY = 0
private val DOCUMENT_CATEGORY = Category.Laboratory
private val DOCUMENT_CONTENT = mapOf<String, String>()

class SaveDocumentLocalUseCaseTest {
    private val repository = mockk<DocumentRepository>(relaxed = true)
    private lateinit var useCase: SaveDocumentLocalUseCase

    @BeforeEach
    fun setup() {
        useCase = SaveDocumentLocalUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `save document local delegates to repository`() = runTest {
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
        useCase.execute(SaveDocumentLocalUseCase.Params(document))
        coVerify(exactly = 1) { repository.saveDocumentLocal(document) }
    }
}
