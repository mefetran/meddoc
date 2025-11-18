package mefetran.dgusev.meddocs.data.usecase.user

import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.GetUserUseCase
import javax.inject.Inject

class GetUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : GetUserUseCase {
    override suspend fun execute(input: Unit): User? = userRepository.getUserOrNull()
}
