package mefetran.dgusev.meddocs.app

import androidx.navigation.NavDestination
import io.realm.RealmDictionary

fun <V: Any> Map<String, V>.toRealmDictionary(): RealmDictionary<V> = RealmDictionary(this)

// An extension function that returns route as it named inside navigation stack
val NavDestination.shortRoute: String?
    get() = route?.substringAfterLast('.')?.substringBefore('?')