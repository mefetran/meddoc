package mefetran.dgusev.meddocs.ui

import mefetran.dgusev.meddocs.domain.model.User
import mefetran.dgusev.meddocs.proto.BearerTokens
import mefetran.dgusev.meddocs.proto.DarkThemeSettings

data class AppState(
    val darkThemeSettings: DarkThemeSettings = DarkThemeSettings.getDefaultInstance(),
    val bearerTokens: BearerTokens = BearerTokens.getDefaultInstance(),
    val user: User? = null,
    val isAppLocked: Boolean = true,
    val isPinEnabled: Boolean = false,
    val isBiometricEnabled: Boolean = false,
)
