package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.user.DeleteUserUseCaseImpl
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.DeleteUserUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteUserUseCaseTest {
    private val repository = mockk<UserRepository>(relaxed = true)
    private lateinit var useCase: DeleteUserUseCase

    @BeforeEach
    fun setup() {
        useCase = DeleteUserUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `delete user delegates`() = runTest {
        useCase.execute(Unit)
        coVerify { repository.deleteUser() }
    }
}
