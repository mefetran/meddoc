package mefetran.dgusev.meddocs.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface SignUpUserUseCase : UseCase<SignUpUserUseCase.Params, Flow<Result<User>>> {
    data class Params(
        val email: String,
        val password: String,
        val name: String? = null,
    )
}
