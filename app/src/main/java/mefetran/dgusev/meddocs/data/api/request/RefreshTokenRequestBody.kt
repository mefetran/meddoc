package mefetran.dgusev.meddocs.data.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.proto.BearerTokens

@Serializable
data class RefreshTokenRequestBody(
    @SerialName("refresh_token")
    val refreshToken: String = "",
) {
    companion object {
        fun fromBearerToken(tokenPair: BearerTokens) = RefreshTokenRequestBody(tokenPair.refreshToken)
    }
}