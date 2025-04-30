package mefetran.dgusev.meddocs.app.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import mefetran.dgusev.meddocs.proto.BearerTokens
import mefetran.dgusev.meddocs.proto.DarkThemeSettings
import mefetran.dgusev.meddocs.proto.Settings
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings = Settings
        .newBuilder()
        .setDarkThemeSettings(
            DarkThemeSettings
                .newBuilder()
                .setUseDarkTheme(true)
                .setUseSystemSettings(true)
                .build()
        )
        .setBearerTokens(BearerTokens.newBuilder().setAccessToken("").setRefreshToken("").build())
        .build()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            exception.printStackTrace()
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
}

fun Settings.withTheme(
    useDarkTheme: Boolean,
    useSystemTheme: Boolean,
): Settings = this.toBuilder()
    .setDarkThemeSettings(
        this.darkThemeSettings.toBuilder()
            .setUseDarkTheme(useDarkTheme)
            .setUseSystemSettings(useSystemTheme)
            .build()
    )
    .build()

val Context.settingsDataStore: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer,
)