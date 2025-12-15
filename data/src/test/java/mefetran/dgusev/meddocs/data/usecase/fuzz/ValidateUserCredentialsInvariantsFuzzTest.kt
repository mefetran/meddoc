package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.model.EMAIL_LENGTH
import mefetran.dgusev.meddocs.data.model.NAME_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MIN_LENGTH
import mefetran.dgusev.meddocs.data.model.isEmail
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase

class ValidateUserCredentialsInvariantsFuzzTest {
    companion object {
        private val useCase = ValidateUserCredentialsUseCaseImpl()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val email = data.consumeString(1000)
            val password = data.consumeString(1000)
            val name = if (data.consumeBoolean()) data.consumeRemainingAsString() else null

            runBlocking {
                val result = useCase.execute(
                    ValidateUserCredentialsUseCase.Params(
                        email = email,
                        password = password,
                        name = name
                    )
                )

                if (result is ValidateResult.Success) {
                    if (!email.isEmail()) {
                        throw AssertionError("Success with invalid email: '$email'")
                    }

                    if (email.length > EMAIL_LENGTH) {
                        throw AssertionError("Success with too long email: ${email.length} > $EMAIL_LENGTH")
                    }

                    if (password.length !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH) {
                        throw AssertionError(
                            "Success with invalid password length: ${password.length} not in $PASSWORD_MIN_LENGTH..$PASSWORD_MAX_LENGTH"
                        )
                    }

                    name?.let {
                        if (it.length > NAME_LENGTH) {
                            throw AssertionError("Success with too long name: ${it.length} > $NAME_LENGTH")
                        }
                    }
                }
            }
        }
    }
}
