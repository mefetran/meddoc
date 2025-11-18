package mefetran.dgusev.meddocs.data.usecase.user

import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.SaveUserUseCase
import javax.inject.Inject

class SaveUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : SaveUserUseCase {
    override suspend fun execute(input: SaveUserUseCase.Params) {
        userRepository.saveUser(input.user)
    }
}
