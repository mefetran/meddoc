package mefetran.dgusev.meddocs.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mefetran.dgusev.meddocs.data.api.request.RefreshTokenRequestBody
import mefetran.dgusev.meddocs.data.api.response.TokenPairResponse
import mefetran.dgusev.meddocs.di.AuthClient
import javax.inject.Inject

interface TokenRefresher {
    suspend fun refreshToken(refreshTokenBody: RefreshTokenRequestBody): Flow<Result<TokenPairResponse>>
}

class TokenRefresherImpl @Inject constructor(
    @AuthClient private val httpClient: HttpClient
) : TokenRefresher {
    override suspend fun refreshToken(refreshTokenBody: RefreshTokenRequestBody): Flow<Result<TokenPairResponse>> =
        flow {
            emit(kotlin.runCatching {
                httpClient.post("refresh") {
                    setBody(refreshTokenBody)
                }.body()
            })
        }
}