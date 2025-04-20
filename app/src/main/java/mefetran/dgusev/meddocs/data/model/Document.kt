package mefetran.dgusev.meddocs.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Document(
    @PrimaryKey
    var id: String = "",
    @Required
    var title: String = "",
    @Required
    var content: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",
) : RealmObject()
