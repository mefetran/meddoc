package mefetran.dgusev.meddocs.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mefetran.dgusev.meddocs.data.api.request.RefreshTokenRequestBody
import mefetran.dgusev.meddocs.data.api.request.UserRegistrationRequestBody
import mefetran.dgusev.meddocs.data.api.request.UserSignInRequestBody
import mefetran.dgusev.meddocs.data.api.response.TokenPairResponse
import mefetran.dgusev.meddocs.data.model.User
import javax.inject.Inject

interface UserApi {
    suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<User>>

    suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>>

    suspend fun refreshToken(refreshTokenBody: RefreshTokenRequestBody): Flow<Result<TokenPairResponse>>
}

class UserApiImpl @Inject constructor(
    private val httpClient: HttpClient,
) : UserApi {
    override suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<User>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("auth/register") {
                    setBody(userSignUpCredentials)
                }.body()
            })
        }

    override suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("auth/login") {
                    setBody(userSignInCredentials)
                }.body()
            })
        }

    override suspend fun refreshToken(refreshTokenBody: RefreshTokenRequestBody): Flow<Result<TokenPairResponse>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("auth/refresh") {
                    setBody(refreshTokenBody)
                }.body()
            })
        }
}