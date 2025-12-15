package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.model.NAME_LENGTH
import mefetran.dgusev.meddocs.data.usecase.user.ERROR_NAME_TOO_LONG
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase

class ValidateUserCredentialsNameBoundaryFuzzTest {
    companion object {
        private val useCase = ValidateUserCredentialsUseCaseImpl()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val email = "test@example.com"
            val password = "ValidPass123"

            val nameLength = data.consumeInt(0, NAME_LENGTH + 100)
            val name = if (data.consumeBoolean()) {
                data.consumeString(nameLength)
            } else {
                null
            }

            runBlocking {
                val result = useCase.execute(
                    ValidateUserCredentialsUseCase.Params(
                        email = email,
                        password = password,
                        name = name
                    )
                )

                if (name != null && name.length > NAME_LENGTH) {
                    if (result !is ValidateResult.Error || result.message != ERROR_NAME_TOO_LONG) {
                        throw AssertionError(
                            "Name length ${name.length} > $NAME_LENGTH should give ERROR_NAME_TOO_LONG"
                        )
                    }
                } else if (name == null || name.length <= NAME_LENGTH) {
                    if (result !is ValidateResult.Success) {
                        throw AssertionError(
                            "Valid name should give Success, got: $result"
                        )
                    }
                }
            }
        }
    }
}
