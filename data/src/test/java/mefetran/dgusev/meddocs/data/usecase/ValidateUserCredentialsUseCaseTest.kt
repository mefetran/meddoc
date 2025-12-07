package mefetran.dgusev.meddocs.data.usecase

import kotlinx.coroutines.test.runTest
import mefetran.dgusev.meddocs.data.model.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MIN_LENGTH
import mefetran.dgusev.meddocs.data.usecase.user.ERROR_INVALID_EMAIL
import mefetran.dgusev.meddocs.data.usecase.user.ERROR_INVALID_PASSWORD_LENGTH
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


private const val EMAIL_VALUE = "test@test.ru"
private const val PASSWORD_VALUE = "password"
private const val NAME_VALUE = "Denis"

@DisplayName("Class that tests validation of user credentials")
class ValidateUserCredentialsUseCaseTest {
    private lateinit var useCase: ValidateUserCredentialsUseCase

    @BeforeEach
    fun setup() {
        useCase = ValidateUserCredentialsUseCaseImpl()
    }

    @Test
    fun `valid credentials returns Success`() = runTest {
        val params = ValidateUserCredentialsUseCase.Params(
            email = EMAIL_VALUE,
            password = PASSWORD_VALUE,
            name = NAME_VALUE,
        )

        val result = useCase.execute(params)
        assertTrue(result is ValidateResult.Success, "Want Success for valid data")
    }

    @Test
    fun `empty email returns Invalid email error`() = runTest {
        val params = ValidateUserCredentialsUseCase.Params(
            email = "",
            password = PASSWORD_VALUE
        )
        val result = useCase.execute(params)
        assertTrue(result is ValidateResult.Error)
        assertEquals(ERROR_INVALID_EMAIL, (result as ValidateResult.Error).message)
        assertThat(result.message, containsString("Invalid"))
    }

    @ParameterizedTest(name = "invalid email '{0}' should produce Error")
    @ValueSource(strings = ["plainaddress", "missingatsign.com", "a@b", "user@.com"])
    fun `invalid email formats produce error`(email: String) = runTest {
        // EP: класс эквивалентности "невалидные форматы email"
        val params = ValidateUserCredentialsUseCase.Params(
            email = email,
            password = PASSWORD_VALUE
        )
        val result = useCase.execute(params)
        assertTrue(result is ValidateResult.Error)
        assertEquals(ERROR_INVALID_EMAIL, (result as ValidateResult.Error).message)
    }

    @Test
    fun `password boundary length checks - BVA`() = runTest {
        // BVA: проверка границ PASSWORD_MIN_LENGTH и PASSWORD_MAX_LENGTH
        val short = "p".repeat(PASSWORD_MIN_LENGTH - 1)
        val minOk = "p".repeat(PASSWORD_MIN_LENGTH)
        val maxOk = "p".repeat(PASSWORD_MAX_LENGTH)
        val tooLong = "p".repeat(PASSWORD_MAX_LENGTH + 1)

        run {
            val result = useCase.execute(
                ValidateUserCredentialsUseCase.Params(
                    email = EMAIL_VALUE,
                    password = short
                )
            )
            assertTrue(result is ValidateResult.Error)
            assertEquals(ERROR_INVALID_PASSWORD_LENGTH, (result as ValidateResult.Error).message)
        }

        run {
            val result = useCase.execute(
                ValidateUserCredentialsUseCase.Params(
                    email = EMAIL_VALUE,
                    password = minOk
                )
            )
            assertTrue(result is ValidateResult.Success)
        }

        run {
            val result = useCase.execute(
                ValidateUserCredentialsUseCase.Params(
                    email = EMAIL_VALUE,
                    password = maxOk
                )
            )
            assertTrue(result is ValidateResult.Success)
        }

        run {
            val result = useCase.execute(
                ValidateUserCredentialsUseCase.Params(
                    email = EMAIL_VALUE,
                    password = tooLong
                )
            )
            assertTrue(result is ValidateResult.Error)
        }
    }

    @Test
    fun `MCDC style tests for email blank or bad format`() = runTest {
        // MCDC for (isBlank || !isEmail)
        // Case 1: blank -> error"Invalid password length"
        run {
            val result = useCase.execute(
                ValidateUserCredentialsUseCase.Params(
                    email = "",
                    password = PASSWORD_VALUE
                )
            )
            assertTrue(result is ValidateResult.Error)
        }

        // Case 2: not blank but invalid format -> error
        run {
            val result = useCase.execute(
                ValidateUserCredentialsUseCase.Params(
                    email = "bademail",
                    password = PASSWORD_VALUE
                )
            )
            assertTrue(result is ValidateResult.Error)
        }

        // Case 3: not blank and valid format -> success
        run {
            val result = useCase.execute(
                ValidateUserCredentialsUseCase.Params(
                    email = EMAIL_VALUE,
                    password = PASSWORD_VALUE
                )
            )
            assertTrue(result is ValidateResult.Success)
        }
    }

    @Test
    fun `assumptions example - run only when env variable set`() = runTest {
        assumeTrue(System.getProperty("runUserValidationTests") != "false")
        assumeTrue(::useCase.isInitialized)

        val result = useCase.execute(
            ValidateUserCredentialsUseCase.Params(
                email = EMAIL_VALUE,
                password = PASSWORD_VALUE,
            )
        )
        assertTrue(result is ValidateResult.Success)
    }

    @Test
    fun `assert methods variety demonstration`() = runTest {
        val result = useCase.execute(
            ValidateUserCredentialsUseCase.Params(
                email = EMAIL_VALUE,
                password = PASSWORD_VALUE,
            )
        )
        assertNotNull(result)
        assertFalse(result is ValidateResult.Error, "Result cannot be Error")
    }
}
