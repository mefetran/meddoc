package mefetran.dgusev.meddocs.data.api

import androidx.datastore.core.DataStore
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.io.IOException
import mefetran.dgusev.meddocs.app.datastore.withBearerToken
import mefetran.dgusev.meddocs.data.api.request.user.RefreshTokenRequestBody
import mefetran.dgusev.meddocs.data.api.response.user.toTokenPair
import mefetran.dgusev.meddocs.proto.Settings
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val tokenRefreshApi: TokenRefreshApi,
    private val dispatcher: CoroutineDispatcher,
    private val tokenInvalidationEmitter: TokenInvalidationEmitter,
) {
    private val mutex = Mutex()

    suspend fun getValidTokenOrNull(): String? {
        mutex.withLock {
            val settings = settingsDataStore.data.first()
            return if (isExpiredToken(settings.bearerTokens.timestampSec)) {
                refreshToken(settings.bearerTokens.refreshToken)
            } else settings.bearerTokens.accessToken
        }
    }

    suspend fun checkAndUpdateToken() {
        mutex.withLock {
            val settings = settingsDataStore.data.first()
            if (isExpiredToken(settings.bearerTokens.timestampSec)) {
                refreshToken(settings.bearerTokens.refreshToken)
            }
        }
    }

    fun getTokenInvalidationEmitterFlow() = tokenInvalidationEmitter.invalidationFlow

    private fun isExpiredToken(timestampSec: Long): Boolean {
        val currentTimestampSec = System.currentTimeMillis() / 1000
        return currentTimestampSec >= timestampSec
    }

    private suspend fun refreshToken(refreshToken: String): String? {
        if (refreshToken.isBlank()) return null

        return try {
            val result = tokenRefreshApi
                .refreshToken(RefreshTokenRequestBody(refreshToken))
                .flowOn(dispatcher)
                .first()

            result.getOrNull()?.also { tokenPairResponse ->
                settingsDataStore.updateData { it.withBearerToken(tokenPairResponse.toTokenPair()) }
            }?.accessToken
        } catch (clientRequestException: ClientRequestException) {
            if (clientRequestException.response.status == HttpStatusCode.Unauthorized
                || clientRequestException.response.status == HttpStatusCode.Forbidden
            ) {
                tokenInvalidationEmitter.emitInvalidation()
            }
            null
        } catch (serverResponseException: ServerResponseException) {
            null
        } catch (ioException: IOException) {
            null
        }
    }
}
