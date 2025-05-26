package mefetran.dgusev.meddocs.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mefetran.dgusev.meddocs.data.api.request.user.UserRegistrationRequestBody
import mefetran.dgusev.meddocs.data.api.request.user.UserSignInRequestBody
import mefetran.dgusev.meddocs.data.api.response.user.TokenPairResponse
import mefetran.dgusev.meddocs.data.model.User
import mefetran.dgusev.meddocs.di.AuthClient
import javax.inject.Inject

interface UserApi {
    suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<User>>

    suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>>
}

class UserApiImpl @Inject constructor(
    @AuthClient private val httpClient: HttpClient,
) : UserApi{
    override suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<User>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("register") {
                    setBody(userSignUpCredentials)
                }.body()
            })
        }

    override suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("login") {
                    setBody(userSignInCredentials)
                }.body()
            })
        }
}