package mefetran.dgusev.meddocs.data.usecase

import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.usecase.user.SignInUserUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.TokenPair
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.SignInUserUseCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private const val EMAIL_VALUE = "email"
private const val PASSWORD_VALUE = "password"
private const val ACCESS_TOKEN = "access_token"
private const val REFRESH_TOKEN = "refresh_token"

class SignInUserUseCaseTest {
    private val repository = mockk<UserRepository>()
    private lateinit var useCase: SignInUserUseCase

    @BeforeEach
    fun setup() {
        useCase = SignInUserUseCaseImpl(repository)
    }

    @AfterEach
    fun teardown() = clearAllMocks()

    @Test
    fun `sign in forwards to repository and returns tokens`() = runTest {
        val tokenPair = TokenPair(ACCESS_TOKEN, REFRESH_TOKEN, 3600)
        coEvery {
            repository.signInUser(
                EMAIL_VALUE,
                PASSWORD_VALUE
            )
        } returns flow { emit(Result.success(tokenPair)) }

        val resultFlow = useCase.execute(
            SignInUserUseCase.Params(
                email = EMAIL_VALUE,
                password = PASSWORD_VALUE
            )
        )
        val result = resultFlow.first()
        assertTrue(result.isSuccess)
        assertEquals(ACCESS_TOKEN, result.getOrNull()?.accessToken)
        coVerify { repository.signInUser(EMAIL_VALUE, PASSWORD_VALUE) }
    }
}
