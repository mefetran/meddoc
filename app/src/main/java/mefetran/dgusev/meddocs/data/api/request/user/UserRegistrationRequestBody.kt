package mefetran.dgusev.meddocs.data.api.request.user

import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationRequestBody(
    val email: String,
    val password: String,
    val name: String? = null,
)