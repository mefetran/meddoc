package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.document.DeleteAllDocumentsLocalUseCaseImpl
import mefetran.dgusev.meddocs.domain.repository.document.DocumentRepository
import mefetran.dgusev.meddocs.domain.usecase.document.DeleteAllDocumentsLocalUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteAllDocumentsLocalUseCaseTest {
    private val repository = mockk<DocumentRepository>(relaxed = true)
    private lateinit var useCase: DeleteAllDocumentsLocalUseCase

    @BeforeEach
    fun setup() {
        useCase = DeleteAllDocumentsLocalUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `delete all local calls repository`() = runTest {
        useCase.execute(Unit)
        coVerify { repository.deleteDocumentsListLocal() }
    }
}
