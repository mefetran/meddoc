package mefetran.dgusev.meddocs.data.model

import kotlinx.serialization.SerialName
import mefetran.dgusev.meddocs.domain.model.User

data class UserEntity(
    val id: String,
    val email: String,
    val name: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)

fun UserEntity.toUser(): User = User(
    id = this.id,
    email = this.email,
    name = this.name,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun User.toUserEntity(): UserEntity = UserEntity(
    id = this.id,
    email = this.email,
    name = this.name,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)
