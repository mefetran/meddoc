package mefetran.dgusev.meddocs.data.repository

import android.util.Base64
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import mefetran.dgusev.meddocs.domain.model.TokenPair
import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.data.api.UserApi
import mefetran.dgusev.meddocs.data.db.UserDatabaseApi
import mefetran.dgusev.meddocs.data.api.request.user.UserRegistrationRequestBody
import mefetran.dgusev.meddocs.data.api.request.user.UserSignInRequestBody
import mefetran.dgusev.meddocs.data.api.response.user.toTokenPair
import mefetran.dgusev.meddocs.data.model.toUser
import mefetran.dgusev.meddocs.data.model.toUserEntity
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDatabaseApi: UserDatabaseApi,
) : UserRepository {
    override suspend fun getUserOrNull(): User? = userDatabaseApi.getUserOrNull()?.toUser()

    override suspend fun saveUser(user: User) = userDatabaseApi.saveUser(user.toUserEntity())

    override suspend fun deleteUser() = userDatabaseApi.deleteUser()

    override suspend fun signUpUser(
        email: String,
        password: String,
        name: String?
    ): Flow<Result<User>> {
        val credentials = UserRegistrationRequestBody(
            email = email,
            password = password,
            name = name,
        )

        return userApi
            .signUpUser(credentials)
            .map { result -> result.map { userEntity -> userEntity.toUser() } }
    }

    override suspend fun signInUser(
        email: String,
        password: String
    ): Flow<Result<TokenPair>> {
        val credentials = UserSignInRequestBody(
            email = email,
            password = password,
        )

        return userApi
            .signInUser(credentials)
            .map { result -> result.map { tokenPairResponse -> tokenPairResponse.toTokenPair() } }
    }
}

class FakeUserRepositoryImpl @Inject constructor(
    private val userDatabaseApi: UserDatabaseApi,
) : UserRepository {

    override suspend fun signUpUser(
        email: String,
        password: String,
        name: String?
    ): Flow<Result<User>> =
        flow {
            emit(
                runCatching {
                    User(
                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                        email =email,
                        name =name ?: "",
                        createdAt = System.currentTimeMillis().toString(),
                        updatedAt = System.currentTimeMillis().toString(),
                    )
                }
            )
        }

    override suspend fun signInUser(
        email: String,
        password: String
    ): Flow<Result<TokenPair>> =
        flow {
            emit(runCatching {
                TokenPair(
                    accessToken = Base64.encodeToString(
                        email.toByteArray(),
                        Base64.DEFAULT
                    ),
                    refreshToken = Base64.encodeToString(
                        password.toByteArray(),
                        Base64.DEFAULT
                    ),
                    expiresInSec = 900,
                )
            })
        }

    override suspend fun getUserOrNull(): User? = userDatabaseApi.getUserOrNull()?.toUser()

    override suspend fun saveUser(user: User) = userDatabaseApi.saveUser(user.toUserEntity())

    override suspend fun deleteUser() = userDatabaseApi.deleteUser()
}
