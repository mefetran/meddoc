package mefetran.dgusev.meddocs.domain.usecase.user

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.TokenPair
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface SignInUserUseCase : UseCase<SignInUserUseCase.Params, Flow<Result<TokenPair>>>{
    data class Params(
        val email: String,
        val password: String,
    )
}
