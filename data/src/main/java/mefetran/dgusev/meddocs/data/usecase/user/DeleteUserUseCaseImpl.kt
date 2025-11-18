package mefetran.dgusev.meddocs.data.usecase.user

import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.DeleteUserUseCase
import javax.inject.Inject

class DeleteUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : DeleteUserUseCase {
    override suspend fun execute(input: Unit) {
        userRepository.deleteUser()
    }
}
