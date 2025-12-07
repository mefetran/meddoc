package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.UpdateDocumentLocalUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.UpdateDocumentLocalUseCase
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

class UpdateDocumentLocalUseCaseTest {
    private val repository = mockk<DocumentRepository>(relaxed = true)
    private lateinit var useCase: UpdateDocumentLocalUseCase

    @BeforeEach
    fun setup() {
        useCase = UpdateDocumentLocalUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `update document local delegates repository update`() = runTest {
        val params = UpdateDocumentLocalUseCase.Params(
            id = DOCUMENT_ID,
            title = DOCUMENT_TITLE,
            description = DOCUMENT_DESCRIPTION,
            date = DOCUMENT_DATE,
            localFilePath = null,
            file = null,
            category = DOCUMENT_CATEGORY,
            priority = DOCUMENT_PRIORITY,
            content = DOCUMENT_CONTENT,
        )
        useCase.execute(params)
        coVerify {
            repository.updateDocumentLocal(
                id = DOCUMENT_ID,
                title = DOCUMENT_TITLE,
                description = DOCUMENT_DESCRIPTION,
                date = DOCUMENT_DATE,
                localFilePath = null,
                file = null,
                category = DOCUMENT_CATEGORY,
                priority = DOCUMENT_PRIORITY,
                content = DOCUMENT_CONTENT,
            )
        }
    }
}
