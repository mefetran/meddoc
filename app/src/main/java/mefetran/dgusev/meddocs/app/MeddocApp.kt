package mefetran.dgusev.meddocs.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

const val DATABASE_NAME = "REALM_MEDDOCS_DATABASE"

@HiltAndroidApp
class MeddocApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration
            .Builder()
            .name(DATABASE_NAME)
            .schemaVersion(1)
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}