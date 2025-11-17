package mefetran.dgusev.meddocs.data.db.realm

import io.realm.RealmDictionary
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import mefetran.dgusev.meddocs.app.toRealmDictionary
import mefetran.dgusev.meddocs.domain.model.Category
import mefetran.dgusev.meddocs.data.model.DocumentEntity

@RealmClass
open class DocumentRealmEntity(
    @PrimaryKey
    var id: String = "",
    @Required
    var title: String = "",
    var description: String = "",
    var date: String = "",
    var file: String = "",
    var category: String = Category.Other.name,
    var priority: Int = 0,
    var content: RealmDictionary<String> = RealmDictionary(),
    var createdAt: String = "",
    var updatedAt: String = "",
) : RealmObject()

fun DocumentRealmEntity.toDocumentEntity(): DocumentEntity = DocumentEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    date = this.date,
    file = this.file,
    category = Category.valueOf(this.category),
    priority = this.priority,
    content = this.content.toMap(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)

fun DocumentEntity.toDocumentRealmEntity(): DocumentRealmEntity = DocumentRealmEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    date = this.date,
    file = this.file,
    category = this.category.name,
    priority = this.priority,
    content = this.content.toRealmDictionary(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)
