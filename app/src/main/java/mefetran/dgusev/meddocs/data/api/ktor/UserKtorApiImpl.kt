package mefetran.dgusev.meddocs.data.api.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mefetran.dgusev.meddocs.data.api.UserApi
import mefetran.dgusev.meddocs.data.api.request.user.UserRegistrationRequestBody
import mefetran.dgusev.meddocs.data.api.request.user.UserSignInRequestBody
import mefetran.dgusev.meddocs.data.api.response.user.TokenPairResponse
import mefetran.dgusev.meddocs.data.model.UserEntity
import mefetran.dgusev.meddocs.di.AuthClient
import javax.inject.Inject

class UserKtorApiImpl @Inject constructor(
    @AuthClient private val httpClient: HttpClient,
) : UserApi {
    override suspend fun signUpUser(userSignUpCredentials: UserRegistrationRequestBody): Flow<Result<UserEntity>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("register") {
                    setBody(userSignUpCredentials)
                }.bodyOrThrow()
            })
        }

    override suspend fun signInUser(userSignInCredentials: UserSignInRequestBody): Flow<Result<TokenPairResponse>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("login") {
                    setBody(userSignInCredentials)
                }.bodyOrThrow()
            })
        }
}

/*
 * The backend returns only a plain string when signIn/signUp fails.
 * To avoid NoTransformationFoundException, which discards the response's status code,
 * we throw a ClientRequestException with the current HttpResponse.
 * This allows us to properly handle such HTTP client errors.
 */
suspend inline fun <reified T> HttpResponse.bodyOrThrow(): T {
    try {
        return this.body<T>()
    } catch (exception: NoTransformationFoundException) {
        throw ClientRequestException(this, exception.message)
    }
}
