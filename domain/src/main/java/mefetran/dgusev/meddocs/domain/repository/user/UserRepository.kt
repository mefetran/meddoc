package mefetran.dgusev.meddocs.domain.repository.user

import kotlinx.coroutines.flow.Flow
import mefetran.dgusev.meddocs.domain.model.TokenPair
import mefetran.dgusev.meddocs.domain.model.User

interface UserRepository {
    suspend fun signUpUser(
        email: String,
        password: String,
        name: String? = null,
    ): Flow<Result<User>>
    suspend fun signInUser(
        email: String,
        password: String,
    ): Flow<Result<TokenPair>>
    suspend fun getUserOrNull(): User?
    suspend fun saveUser(user: User)
    suspend fun deleteUser()
}
