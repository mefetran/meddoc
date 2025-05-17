package mefetran.dgusev.meddocs.data.api

import io.realm.Realm
import mefetran.dgusev.meddocs.data.model.User
import javax.inject.Inject

interface UserRealmApi {
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)
}

class UserRealmApiImpl @Inject constructor() : UserRealmApi {
    override suspend fun getUser(): User? {
        Realm.getDefaultInstance().use {  realm ->
            return realm.where(User::class.java)
                .findFirst()?.let { realm.copyFromRealm(it) }
        }
    }

    override suspend fun saveUser(user: User) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction { currentRealm ->
                currentRealm.insertOrUpdate(user)
            }
        }
    }
}