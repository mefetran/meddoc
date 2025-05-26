package mefetran.dgusev.meddocs.data.realm

import io.realm.Realm
import mefetran.dgusev.meddocs.data.model.User
import javax.inject.Inject

interface UserRealmApi {
    suspend fun getUserOrNull(): User?
    suspend fun saveUser(user: User)

    suspend fun deleteUser()
}

class UserRealmApiImpl @Inject constructor() : UserRealmApi {
    override suspend fun getUserOrNull(): User? {
        Realm.getDefaultInstance().use {  realm ->
            return realm.where(User::class.java)
                .findFirst()?.let { realm.copyFromRealm(it) }
        }
    }

    override suspend fun saveUser(user: User) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { transactionRealm ->
                transactionRealm.insertOrUpdate(user)
            }
        }
    }

    override suspend fun deleteUser() {
        Realm.getDefaultInstance().use {  realm ->
            realm.executeTransaction {  transactionRealm ->
                transactionRealm.delete(User::class.java)
            }
        }
    }
}