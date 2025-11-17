package mefetran.dgusev.meddocs.data.api.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.domain.model.TokenPair

@Serializable
data class TokenPairResponse(
    @SerialName("access_token")
    val accessToken: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
    /**
     * Access token expiration time in seconds
     */
    @SerialName("expires_in")
    val expiresIn: Int = 0,
)

fun TokenPairResponse.toTokenPair(): TokenPair = TokenPair(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken,
    expiresInSec = this.expiresIn,
)
