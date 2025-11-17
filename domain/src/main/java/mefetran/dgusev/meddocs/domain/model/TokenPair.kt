package mefetran.dgusev.meddocs.domain.model

data class TokenPair(
    val accessToken: String = "",
    val refreshToken: String = "",
    /**
     * Access token expiration time in seconds
     */
    val expiresInSec: Int = 0,
)
