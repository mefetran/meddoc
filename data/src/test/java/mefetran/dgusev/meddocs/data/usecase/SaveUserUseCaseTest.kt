package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.user.SaveUserUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.SaveUserUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val USER_ID = "1"
private const val EMAIL_VALUE = "test@test.ru"
private const val NAME_VALUE = "Denis"

class SaveUserUseCaseTest {
    private val repository = mockk<UserRepository>(relaxed = true)
    private lateinit var useCase: SaveUserUseCase

    @BeforeEach
    fun setup() {
        useCase = SaveUserUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `save user delegates to repo`() = runTest {
        val user = User(USER_ID, EMAIL_VALUE, NAME_VALUE, "", "")
        useCase.execute(SaveUserUseCase.Params(user))
        coVerify { repository.saveUser(user) }
    }
}
