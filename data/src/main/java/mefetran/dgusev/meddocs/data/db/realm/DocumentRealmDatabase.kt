package mefetran.dgusev.meddocs.data.db.realm

import io.realm.Realm
import io.realm.kotlin.toFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import mefetran.dgusev.meddocs.data.db.DocumentDatabaseApi
import mefetran.dgusev.meddocs.data.model.DocumentEntity
import mefetran.dgusev.meddocs.data.db.realm.model.DocumentRealmEntity
import mefetran.dgusev.meddocs.data.db.realm.model.toDocumentEntity
import mefetran.dgusev.meddocs.data.db.realm.model.toDocumentRealmEntity
import mefetran.dgusev.meddocs.data.db.realm.model.toRealmDictionary
import mefetran.dgusev.meddocs.domain.model.Category
import javax.inject.Inject

class DocumentRealmDatabase @Inject constructor() : DocumentDatabaseApi {
    override suspend fun saveDocumentsList(documentsList: List<DocumentEntity>) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(documentsList.map { it.toDocumentRealmEntity() })
            }
        }
    }

    override suspend fun getDocumentsListOrNull(): List<DocumentEntity>? {
        Realm.getDefaultInstance().use { realm ->
            val result = realm.where(DocumentRealmEntity::class.java).findAll()
            return realm.copyFromRealm(result)?.map { it.toDocumentEntity() }
        }
    }

    override suspend fun deleteDocumentsList() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.delete(DocumentRealmEntity::class.java)
            }
        }
    }

    override suspend fun saveDocument(document: DocumentEntity) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(document.toDocumentRealmEntity())
            }
        }
    }

    override suspend fun getDocumentOrNull(documentId: String): DocumentEntity? {
        Realm.getDefaultInstance().use { realm ->
            return realm.where(DocumentRealmEntity::class.java)
                .equalTo("id", documentId)
                .findFirst()?.let { realm.copyFromRealm(it) }?.toDocumentEntity()
        }
    }

    override suspend fun deleteDocument(documentId: String) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                val documentToDelete =
                    transactionRealm.where(DocumentRealmEntity::class.java).equalTo("id", documentId)
                        .findFirst()
                documentToDelete?.deleteFromRealm()
            }
        }
    }

    override suspend fun observeDocuments(): Flow<List<DocumentEntity>> {
        val realm = Realm.getDefaultInstance()

        return realm
            .where(DocumentRealmEntity::class.java)
            .findAllAsync()
            .toFlow()
            .map { result ->
                realm
                    .copyFromRealm(result)
                    .map { it.toDocumentEntity() }
                    .sortedByDescending { it.updatedAt }
            }
            .flowOn(Dispatchers.Main)
    }

    override suspend fun updateDocument(
        id: String,
        title: String?,
        description: String?,
        date: String?,
        localFilePath: String?,
        file: String?,
        category: Category?,
        priority: Int?,
        content: Map<String, String>?
    ) {
        Realm.getDefaultInstance().use { realm ->
            val currentDocument = realm.where(DocumentRealmEntity::class.java)
                .equalTo("id", id)
                .findFirst()?.let { realm.copyFromRealm(it) }
            currentDocument?.let {
                title?.let { currentDocument.title = title }
                description?.let{ currentDocument.description = description}
                date?.let { currentDocument.date = date }
                localFilePath?.let { currentDocument.localFilePath = localFilePath }
                file?.let { currentDocument.file = file }
                category?.let { currentDocument.category = category.name }
                priority?.let { currentDocument.priority = priority }
                content?.let { currentDocument.content = content.toRealmDictionary() }

                realm.executeTransaction { transactionRealm ->
                    transactionRealm.insertOrUpdate(currentDocument)
                }
            }
        }
    }
}
