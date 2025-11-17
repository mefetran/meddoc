package mefetran.dgusev.meddocs.app

import androidx.navigation.NavDestination


// An extension function that returns route as it named inside navigation stack
val NavDestination.shortRoute: String?
    get() = route?.substringAfterLast('.')?.substringBefore('?')
