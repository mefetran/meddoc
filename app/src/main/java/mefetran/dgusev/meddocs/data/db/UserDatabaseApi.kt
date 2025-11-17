package mefetran.dgusev.meddocs.data.db

import io.realm.Realm
import mefetran.dgusev.meddocs.data.model.UserEntity
import mefetran.dgusev.meddocs.data.db.realm.UserRealmEntity
import mefetran.dgusev.meddocs.data.db.realm.toUserEntity
import mefetran.dgusev.meddocs.data.db.realm.toUserRealmEntity
import javax.inject.Inject

interface UserDatabaseApi {
    suspend fun getUserOrNull(): UserEntity?
    suspend fun saveUser(user: UserEntity)

    suspend fun deleteUser()
}

class UserRealmDatabase @Inject constructor() : UserDatabaseApi {
    override suspend fun getUserOrNull(): UserEntity? {
        Realm.getDefaultInstance().use {  realm ->
            return realm.where(UserRealmEntity::class.java)
                .findFirst()?.let { realm.copyFromRealm(it) }?.toUserEntity()
        }
    }

    override suspend fun saveUser(user: UserEntity) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(user.toUserRealmEntity())
            }
        }
    }

    override suspend fun deleteUser() {
        Realm.getDefaultInstance().use {  realm ->
            realm.executeTransaction {  transactionRealm ->
                transactionRealm.delete(UserRealmEntity::class.java)
            }
        }
    }
}
