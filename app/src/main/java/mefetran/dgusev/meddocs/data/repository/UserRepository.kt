package mefetran.dgusev.meddocs.data.repository

import android.util.Base64
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mefetran.dgusev.meddocs.data.api.UserApi
import mefetran.dgusev.meddocs.data.db.UserDatabaseApi
import mefetran.dgusev.meddocs.data.api.request.user.UserRegistrationRequestBody
import mefetran.dgusev.meddocs.data.api.request.user.UserSignInRequestBody
import mefetran.dgusev.meddocs.data.api.response.user.TokenPairResponse
import mefetran.dgusev.meddocs.data.model.User
import javax.inject.Inject

interface UserRepository {
    suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<User>>

    suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>>

    suspend fun getUserOrNull(): User?
    suspend fun saveUser(user: User)

    suspend fun deleteUser()
}

class RealUserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userDatabaseApi: UserDatabaseApi,
) : UserRepository {
    override suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<User>> =
        userApi.signUpUser(userSignUpCredentials)

    override suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>> =
        userApi.signInUser(userSignInCredentials)

    override suspend fun getUserOrNull(): User? = userDatabaseApi.getUserOrNull()

    override suspend fun saveUser(user: User) = userDatabaseApi.saveUser(user)

    override suspend fun deleteUser() = userDatabaseApi.deleteUser()
}

class FakeUserRepositoryImpl @Inject constructor(
    private val userDatabaseApi: UserDatabaseApi,
) : UserRepository {
    override suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<User>> =
        flow {
            emit(
                runCatching {
                    User(
                        id = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                        email = userSignUpCredentials.email,
                        name = userSignUpCredentials.name ?: "",
                        createdAt = System.currentTimeMillis().toString(),
                    )
                }
            )
        }

    override suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>> =
        flow {
            emit(runCatching {
                TokenPairResponse(
                    accessToken = Base64.encodeToString(
                        userSignInCredentials.email.toByteArray(),
                        Base64.DEFAULT
                    ),
                    refreshToken = Base64.encodeToString(
                        userSignInCredentials.password.toByteArray(),
                        Base64.DEFAULT
                    ),
                    expiresIn = 900,
                )
            })
        }

    override suspend fun getUserOrNull(): User? = userDatabaseApi.getUserOrNull()

    override suspend fun saveUser(user: User) = userDatabaseApi.saveUser(user)

    override suspend fun deleteUser() = userDatabaseApi.deleteUser()
}