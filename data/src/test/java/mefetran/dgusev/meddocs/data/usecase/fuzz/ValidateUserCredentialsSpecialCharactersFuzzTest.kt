package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase

class ValidateUserCredentialsSpecialCharactersFuzzTest {
    companion object {
        private val useCase = ValidateUserCredentialsUseCaseImpl()

        private val specialChars = charArrayOf(
            '\u0000', '\n', '\r', '\t', '\'', '"', '\\',
            '<', '>', '&', ';', '|', '`', '$',
            '\uFFFD',
            '\u200B',
            '\uD800',
        )

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            val email = buildString {
                repeat(data.consumeInt(0, 100)) {
                    if (data.consumeBoolean()) {
                        append(data.pickValue(specialChars))
                    } else {
                        append(data.consumeChar())
                    }
                }
            }

            val password = buildString {
                repeat(data.consumeInt(0, 100)) {
                    if (data.consumeBoolean()) {
                        append(data.pickValue(specialChars))
                    } else {
                        append(data.consumeChar())
                    }
                }
            }

            val name = if (data.consumeBoolean()) {
                buildString {
                    repeat(data.consumeInt(0, 100)) {
                        if (data.consumeBoolean()) {
                            append(data.pickValue(specialChars))
                        } else {
                            append(data.consumeChar())
                        }
                    }
                }
            } else null

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
                        "Validation crashed on special characters: email='$email', password='$password', name='$name'",
                        e
                    )
                }
            }
        }
    }
}
