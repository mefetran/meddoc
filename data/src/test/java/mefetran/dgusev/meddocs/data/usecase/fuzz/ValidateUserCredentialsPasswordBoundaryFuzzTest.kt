package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.model.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MIN_LENGTH
import mefetran.dgusev.meddocs.data.usecase.user.ERROR_INVALID_PASSWORD_LENGTH
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase

class ValidateUserCredentialsPasswordBoundaryFuzzTest {
    companion object {
        private val useCase = ValidateUserCredentialsUseCaseImpl()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val email = "test@example.com"

            val passwordLength = data.consumeInt(0, PASSWORD_MAX_LENGTH + 50)
            val password = data.consumeString(passwordLength)

            runBlocking {
                val result = useCase.execute(
                    ValidateUserCredentialsUseCase.Params(
                        email = email,
                        password = password,
                        name = null
                    )
                )

                when (password.length) {
                    !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH -> {
                        if (result !is ValidateResult.Error || result.message != ERROR_INVALID_PASSWORD_LENGTH) {
                            throw AssertionError(
                                "Invalid password length ${password.length} should give ERROR_INVALID_PASSWORD_LENGTH"
                            )
                        }
                    }
                    in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH -> {
                        if (result !is ValidateResult.Success) {
                            throw AssertionError(
                                "Valid password length ${password.length} should give Success, got: $result"
                            )
                        }
                    }
                }
            }
        }
    }
}
