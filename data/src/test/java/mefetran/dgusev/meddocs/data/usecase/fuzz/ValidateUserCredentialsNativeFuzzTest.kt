package mefetran.dgusev.meddocs.data.usecase.fuzz

import com.code_intelligence.jazzer.api.FuzzedDataProvider
import kotlinx.coroutines.runBlocking
import mefetran.dgusev.meddocs.data.usecase.user.ValidateUserCredentialsUseCaseImpl
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase
class ValidateUserCredentialsNativeFuzzTest {
    companion object {
        private val useCase = ValidateUserCredentialsUseCaseImpl()
        private var callCount = 0

        @JvmStatic
        fun fuzzerTestOneInput(data: FuzzedDataProvider) {
            callCount++

            val email = data.consumeString(500)
            val password = data.consumeString(500)
            val name = if (data.consumeBoolean()) data.consumeRemainingAsString() else null

            // Логируем каждую 1000-ю попытку
            if (callCount % 1000 == 0) {
                println("=== Fuzzing iteration $callCount ===")
                println("Email: '$email' (length: ${email.length})")
                println("Password: '$password' (length: ${password.length})")
                println("Name: '$name'")
            }

            runBlocking {
                try {
                    val result = useCase.execute(
                        ValidateUserCredentialsUseCase.Params(
                            email = email,
                            password = password,
                            name = name
                        )
                    )

                    if (callCount % 1000 == 0) {
                        println("Result: $result")
                    }

                } catch (e: StringIndexOutOfBoundsException) {
                    println("!!! FOUND BUG at iteration $callCount !!!")
                    println("Email: '$email'")
                    println("Password: '$password'")
                    println("Name: '$name'")
                    throw AssertionError("FOUND BUG: StringIndexOutOfBoundsException", e)
                } catch (e: Throwable) {
                    // Другие исключения - нормально для валидации
                    if (callCount % 1000 == 0) {
                        println("Expected exception: ${e.javaClass.simpleName}")
                    }
                }
            }
        }
    }
}
