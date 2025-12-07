package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.DeleteDocumentLocalUseCaseImpl
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteDocumentLocalUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val DOCUMENT_ID = "1"

class DeleteDocumentLocalUseCaseTest {
    private val repository = mockk<DocumentRepository>(relaxed = true)
    private lateinit var useCase: DeleteDocumentLocalUseCase

    @BeforeEach
    fun setup() {
        useCase = DeleteDocumentLocalUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `delete document local delegates`() = runTest {
        val params = DeleteDocumentLocalUseCase.Params(documentId = DOCUMENT_ID)
        useCase.execute(params)
        coVerify { repository.deleteDocumentLocal(DOCUMENT_ID) }
    }
}
