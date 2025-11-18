package mefetran.dgusev.meddocs.data.usecase.user

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.SignUpUserUseCase
import javax.inject.Inject

class SignUpUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : SignUpUserUseCase {
    override suspend fun execute(input: SignUpUserUseCase.Params): Flow<Result<User>> = userRepository.signUpUser(
        email = input.email,
        password = input.password,
        name = input.name,
    )
}
