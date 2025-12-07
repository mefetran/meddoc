package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.user.GetUserUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.GetUserUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val USER_ID = "1"
private const val EMAIL_VALUE = "test@test.ru"
private const val NAME_VALUE = "Denis"

class GetUserUseCaseTest {
    private val repository = mockk<UserRepository>()
    private lateinit var useCase: GetUserUseCase

    @BeforeEach
    fun setup() {
        useCase = GetUserUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `get user returns stored user or null`() = runTest {
        val user = User(USER_ID, EMAIL_VALUE, NAME_VALUE, "", "")
        coEvery { repository.getUserOrNull() } returns user
        val result = useCase.execute(Unit)
        assertEquals(user, result)
        coVerify { repository.getUserOrNull() }
    }
}
