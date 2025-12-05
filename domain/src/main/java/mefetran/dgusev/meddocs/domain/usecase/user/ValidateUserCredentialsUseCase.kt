package mefetran.dgusev.meddocs.domain.usecase.user

import mefetran.dgusev.meddocs.domain.model.ValidateResult
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface ValidateUserCredentialsUseCase :
    UseCase<ValidateUserCredentialsUseCase.Params, ValidateResult> {

    data class Params(
        val email: String,
        val password: String,
        val name: String? = null,
    )
}
