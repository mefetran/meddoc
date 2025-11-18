package mefetran.dgusev.meddocs.data.usecase.user

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.TokenPair
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.domain.usecase.user.SignInUserUseCase
import javax.inject.Inject

class SignInUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : SignInUserUseCase {
    override suspend fun execute(input: SignInUserUseCase.Params): Flow<Result<TokenPair>> = userRepository.signInUser(
        email = input.email,
        password = input.password
    )
}
