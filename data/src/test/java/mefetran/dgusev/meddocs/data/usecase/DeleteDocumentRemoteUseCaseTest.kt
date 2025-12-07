package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.DeleteDocumentRemoteUseCaseImpl
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteDocumentRemoteUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val DOCUMENT_ID = "1"
private const val RESULT_SUCCESS_CODE = 200

class DeleteDocumentRemoteUseCaseTest {
    private val repository = mockk<DocumentRepository>()
    private lateinit var useCase: DeleteDocumentRemoteUseCase

    @BeforeEach
    fun setup() {
        useCase = DeleteDocumentRemoteUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `delete remote returns result code`() = runTest {
        coEvery { repository.deleteDocumentById(DOCUMENT_ID) } returns Result.success(
            RESULT_SUCCESS_CODE
        )
        val result = useCase.execute(DeleteDocumentRemoteUseCase.Params(DOCUMENT_ID))
        assertTrue(result.isSuccess)
        assertEquals(RESULT_SUCCESS_CODE, result.getOrNull())
        coVerify { repository.deleteDocumentById(DOCUMENT_ID) }
    }
}
