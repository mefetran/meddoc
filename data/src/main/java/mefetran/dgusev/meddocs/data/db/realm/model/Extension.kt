package mefetran.dgusev.meddocs.data.db.realm.model

import io.realm.RealmDictionary

fun <V: Any> Map<String, V>.toRealmDictionary(): RealmDictionary<V> = RealmDictionary(this)
