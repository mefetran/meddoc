package mefetran.dgusev.meddocs.domain.usecase.user

import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.domain.usecase.UseCase

interface SaveUserUseCase : UseCase<SaveUserUseCase.Params, Unit> {
    data class Params(
        val user: User
    )
}
