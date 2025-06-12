package mefetran.dgusev.meddocs.data.db

import io.realm.Realm
import io.realm.kotlin.toFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import mefetran.dgusev.meddocs.data.model.Document
import javax.inject.Inject

interface DocumentDatabaseApi {
    suspend fun saveDocumentsList(documentsList: List<Document>)

    suspend fun getDocumentsListOrNull(): List<Document>?

    suspend fun deleteDocumentsList()

    suspend fun saveDocument(document: Document)

    suspend fun getDocumentOrNull(documentId: String): Document?

    suspend fun deleteDocument(documentId: String)

    suspend fun observeDocuments(): Flow<List<Document>>
}

class DocumentRealmDatabase @Inject constructor() : DocumentDatabaseApi {
    override suspend fun saveDocumentsList(documentsList: List<Document>) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(documentsList)
            }
        }
    }

    override suspend fun getDocumentsListOrNull(): List<Document>? {
        Realm.getDefaultInstance().use { realm ->
            val result = realm.where(Document::class.java).findAll()
            return realm.copyFromRealm(result)
        }
    }

    override suspend fun deleteDocumentsList() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.delete(Document::class.java)
            }
        }
    }

    override suspend fun saveDocument(document: Document) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(document)
            }
        }
    }

    override suspend fun getDocumentOrNull(documentId: String): Document? {
        Realm.getDefaultInstance().use { realm ->
            return realm.where(Document::class.java)
                .equalTo("id", documentId)
                .findFirst()?.let { realm.copyFromRealm(it) }
        }
    }

    override suspend fun deleteDocument(documentId: String) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                val documentToDelete =
                    transactionRealm.where(Document::class.java).equalTo("id", documentId)
                        .findFirst()
                documentToDelete?.deleteFromRealm()
            }
        }
    }

    override suspend fun observeDocuments(): Flow<List<Document>> {
        val realm = Realm.getDefaultInstance()

        return realm
            .where(Document::class.java)
            .findAllAsync()
            .toFlow()
            .map { result ->
                realm
                    .copyFromRealm(result)
                    .sortedByDescending { it.updatedAt }
            }
            .flowOn(Dispatchers.Main)
    }
}