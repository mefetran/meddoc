package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.model.EMAIL_LENGTH
import mefetran.dgusev.meddocs.data.usecase.user.ERROR_EMAIL_TOO_LONG
import mefetran.dgusev.meddocs.data.usecase.user.ERROR_INVALID_EMAIL
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase

class ValidateUserCredentialsEmailBoundaryFuzzTest {
    companion object {
        private val useCase = ValidateUserCredentialsUseCaseImpl()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val emailLength = data.consumeInt(0, EMAIL_LENGTH + 100)
            val email = data.consumeString(emailLength)

            val password = "ValidPass123"

            runBlocking {
                val result = useCase.execute(
                    ValidateUserCredentialsUseCase.Params(
                        email = email,
                        password = password,
                        name = null
                    )
                )

                if (result is ValidateResult.Error) {
                    when {
                        email.isBlank() -> {
                            if (result.message != ERROR_INVALID_EMAIL) {
                                throw AssertionError(
                                    "Blank email should give ERROR_INVALID_EMAIL, got: ${result.message}"
                                )
                            }
                        }
                        email.length > EMAIL_LENGTH -> {
                            if (result.message != ERROR_EMAIL_TOO_LONG && result.message != ERROR_INVALID_EMAIL) {
                                throw AssertionError(
                                    "Too long email should give appropriate error, got: ${result.message}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
