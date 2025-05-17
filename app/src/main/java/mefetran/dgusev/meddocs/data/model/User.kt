package mefetran.dgusev.meddocs.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@RealmClass
@Serializable
open class User(
    @PrimaryKey
    var id: String = "",
    @Required
    var email: String = "",
    var name: String = "",
    @SerialName("created_at")
    var createdAt: String = "",
    @SerialName("updated_at")
    var updatedAt: String = "",
) : RealmObject()