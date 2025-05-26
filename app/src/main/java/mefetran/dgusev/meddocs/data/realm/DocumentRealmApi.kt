package mefetran.dgusev.meddocs.data.realm

import io.realm.Realm
import mefetran.dgusev.meddocs.data.model.Document
import javax.inject.Inject

interface DocumentRealmApi {
    suspend fun saveDocumentsList(documentsList: List<Document>)

    suspend fun getDocumentsListOrNull(): List<Document>?

    suspend fun deleteDocumentsList()

    suspend fun saveDocument(document: Document)

    suspend fun getDocumentOrNull(documentId: String): Document?

    suspend fun deleteDocument(documentId: String)
}

class DocumentRealmApiImpl @Inject constructor(): DocumentRealmApi {
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
            realm.executeTransaction { transactionRealm->
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
                val documentToDelete = transactionRealm.where(Document::class.java).equalTo("id", documentId).findFirst()
                documentToDelete?.deleteFromRealm()
            }
        }
    }
}