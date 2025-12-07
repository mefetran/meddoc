package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.user.SignUpUserUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.SignUpUserUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val USER_ID = "1"
private const val EMAIL_VALUE = "test@test.ru"
private const val PASSWORD_VALUE = "password"
private const val NAME_VALUE = "Denis"

class SignUpUserUseCaseTest {
    private val repository = mockk<UserRepository>()
    private lateinit var useCase: SignUpUserUseCase

    @BeforeEach
    fun setup() {
        useCase = SignUpUserUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `sign up delegates and returns user`() = runTest {
        val user = User(USER_ID, EMAIL_VALUE, NAME_VALUE, "", "")
        coEvery {
            repository.signUpUser(
                EMAIL_VALUE,
                PASSWORD_VALUE,
                NAME_VALUE
            )
        } returns flow { emit(Result.success(user)) }

        val result = useCase.execute(
            SignUpUserUseCase.Params(
                email = EMAIL_VALUE,
                password = PASSWORD_VALUE,
                name = NAME_VALUE
            )
        ).first()
        assertTrue(result.isSuccess)
        assertEquals(EMAIL_VALUE, result.getOrNull()?.email)
        coVerify { repository.signUpUser(EMAIL_VALUE, PASSWORD_VALUE, NAME_VALUE) }
    }
}
