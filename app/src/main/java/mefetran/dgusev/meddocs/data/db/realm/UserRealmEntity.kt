package mefetran.dgusev.meddocs.data.db.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.data.model.UserEntity

@RealmClass
@Serializable
open class UserRealmEntity(
    @PrimaryKey
    var id: String = "",
    @Required
    var email: String = "",
    var name: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
) : RealmObject()

fun UserRealmEntity.toUserEntity(): UserEntity = UserEntity(
    id = this.id,
    email = this.email,
    name = this.name,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun UserEntity.toUserRealmEntity(): UserRealmEntity = UserRealmEntity(
    id = this.id,
    email = this.email,
    name = this.name,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)
