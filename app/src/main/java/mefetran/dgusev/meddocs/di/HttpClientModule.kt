package mefetran.dgusev.meddocs.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import mefetran.dgusev.meddocs.data.api.ktor.AuthClient
import mefetran.dgusev.meddocs.data.api.ktor.DefaultClient
import mefetran.dgusev.meddocs.token.TokenManager
import javax.inject.Singleton

const val LOGGER_TAG = "LoggerKtor"
const val HOST_IP = "192.168.0.111"
const val HOST_PORT = 8080

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {
    @Provides
    @Singleton
    @DefaultClient
    fun provideDefaultKtorHttpClient(tokenManager: TokenManager): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d(LOGGER_TAG, message)
                }
            }
            level = LogLevel.ALL
        }

        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTP
                host = HOST_IP
                port = HOST_PORT
                encodedPath = "/api/v1/"
            }
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }

        install(HttpRequestRetry) {
            retryOnExceptionOrServerErrors(maxRetries = 2)
            exponentialDelay(
                baseDelayMs = 200,
                maxDelayMs = 4000,
                randomizationMs = 400,
            )
        }
    }.apply {
        plugin(HttpSend).intercept { request ->
            val accessToken = tokenManager.getValidTokenOrNull()
            request.headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
            execute(request)
        }
    }


    @Provides
    @Singleton
    @AuthClient
    fun provideAuthKtorHttpClient(): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d(LOGGER_TAG, message)
                }
            }
            level = LogLevel.ALL
        }

        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTP
                host = HOST_IP
                port = HOST_PORT
                encodedPath = "/api/v1/auth/"
            }
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }

        install(HttpRequestRetry) {
            retryOnExceptionOrServerErrors(maxRetries = 2)
            exponentialDelay(
                baseDelayMs = 200,
                maxDelayMs = 4000,
                randomizationMs = 400,
            )
        }
    }
}
