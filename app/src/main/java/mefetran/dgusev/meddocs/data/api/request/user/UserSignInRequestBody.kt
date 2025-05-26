package mefetran.dgusev.meddocs.data.api.request.user

import kotlinx.serialization.Serializable

@Serializable
data class UserSignInRequestBody(
    val email: String,
    val password: String,
)