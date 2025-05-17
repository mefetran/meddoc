package mefetran.dgusev.meddocs.app

import io.realm.RealmSet

fun <T: Any> Set<T>.toRealmSet(): RealmSet<T> = RealmSet<T>(this)