package mefetran.dgusev.meddocs.data.db

import mefetran.dgusev.meddocs.data.model.UserEntity

interface UserDatabaseApi {
    suspend fun getUserOrNull(): UserEntity?
    suspend fun saveUser(user: UserEntity)

    suspend fun deleteUser()
}
