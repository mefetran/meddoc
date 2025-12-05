package mefetran.dgusev.meddocs.data.api.response.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.data.model.UserEntity

@Serializable
data class UserResponse(
    val id: String = "",
    val email: String = "",
    val name: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = ""
)

fun UserResponse.toUserEntity() = UserEntity(
    id = this.id,
    email = this.email,
    name = this.name,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)
