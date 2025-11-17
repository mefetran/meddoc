package mefetran.dgusev.meddocs.data.api.request.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestBody(
    @SerialName("refresh_token")
    val refreshToken: String,
)
