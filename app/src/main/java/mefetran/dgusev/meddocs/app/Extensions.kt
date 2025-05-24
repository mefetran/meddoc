package mefetran.dgusev.meddocs.app

import io.realm.RealmDictionary

fun <V: Any> Map<String, V>.toRealmDictionary(): RealmDictionary<V> = RealmDictionary(this)