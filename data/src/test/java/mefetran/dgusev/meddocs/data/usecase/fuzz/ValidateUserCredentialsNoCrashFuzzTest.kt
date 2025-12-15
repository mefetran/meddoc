package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase

class ValidateUserCredentialsNoCrashFuzzTest {
    companion object {
        private val useCase = ValidateUserCredentialsUseCaseImpl()

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val email = data.consumeString(1000)
            val password = data.consumeString(1000)
            val name = if (data.consumeBoolean()) data.consumeRemainingAsString() else null

            runBlocking {
                try {
                    useCase.execute(
                        ValidateUserCredentialsUseCase.Params(
                            email = email,
                            password = password,
                            name = name
                        )
                    )
                } catch (e: Throwable) {
                    throw AssertionError(
                        "Validation crashed on: email='$email', password='$password', name='$name'",
                        e
                    )
                }
            }
        }
    }
}
