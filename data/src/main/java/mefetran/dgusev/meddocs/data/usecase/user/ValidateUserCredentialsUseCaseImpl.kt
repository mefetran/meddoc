package mefetran.dgusev.meddocs.data.usecase.user

import mefetran.dgusev.meddocs.data.model.EMAIL_LENGTH
import mefetran.dgusev.meddocs.data.model.NAME_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MIN_LENGTH
import mefetran.dgusev.meddocs.data.model.isEmail
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase
import javax.inject.Inject

const val ERROR_INVALID_EMAIL = "Invalid email"
const val ERROR_EMAIL_TOO_LONG = "Email too long"
const val ERROR_INVALID_PASSWORD_LENGTH = "Invalid password length"
const val ERROR_NAME_TOO_LONG = "Name too long"

class ValidateUserCredentialsUseCaseImpl @Inject constructor() :
    ValidateUserCredentialsUseCase {

    override suspend fun execute(input: ValidateUserCredentialsUseCase.Params): ValidateResult {

        if (input.email.isBlank() ||
            !input.email.isEmail()
        ) {
            return ValidateResult.Error(ERROR_INVALID_EMAIL)
        }

        if (input.email.length > EMAIL_LENGTH)
            return ValidateResult.Error(ERROR_EMAIL_TOO_LONG)

        if (input.password.length !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH)
            return ValidateResult.Error(ERROR_INVALID_PASSWORD_LENGTH)

        input.name?.let {
            if (it.length > NAME_LENGTH)
                return ValidateResult.Error(ERROR_NAME_TOO_LONG)
        }

        return ValidateResult.Success
    }
}
