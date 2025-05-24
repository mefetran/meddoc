package mefetran.dgusev.meddocs.data.model

import io.realm.RealmDictionary
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class Document(
    @PrimaryKey
    var id: String = "",
    @Required
    var title: String = "",
    var content: RealmDictionary<String> = RealmDictionary(),
    var date: String = "",
    var description: String = "",
    var category: String = Category.Other.toString(),
    var file: String = "",
    var priority: Int = 0,
    var createdAt: String = "",
    var updatedAt: String = "",
) : RealmObject()