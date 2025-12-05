package mefetran.dgusev.meddocs.data.usecase.user

import mefetran.dgusev.meddocs.data.model.EMAIL_LENGTH
import mefetran.dgusev.meddocs.data.model.NAME_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.data.model.PASSWORD_MIN_LENGTH
import mefetran.dgusev.meddocs.data.model.isEmail
import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.user.ValidateUserCredentialsUseCase
import javax.inject.Inject

class ValidateUserCredentialsUseCaseImpl @Inject constructor() :
    ValidateUserCredentialsUseCase {

    override suspend fun execute(input: ValidateUserCredentialsUseCase.Params): ValidateResult {

        if (input.email.isBlank() ||
            !input.email.isEmail()
        ) {
            return ValidateResult.Error("Invalid email")
        }

        if (input.email.length > EMAIL_LENGTH)
            return ValidateResult.Error("Email too long")

        if (input.password.length !in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH)
            return ValidateResult.Error("Invalid password length")

        input.name?.let {
            if (it.length > NAME_LENGTH)
                return ValidateResult.Error("Name too long")
        }

        return ValidateResult.Success
    }
}
