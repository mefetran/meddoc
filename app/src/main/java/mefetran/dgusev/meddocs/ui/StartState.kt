package mefetran.dgusev.meddocs.ui

import mefetran.dgusev.meddocs.proto.BearerTokens
import mefetran.dgusev.meddocs.proto.DarkThemeSettings

data class StartState(
    val darkThemeSettings: DarkThemeSettings = DarkThemeSettings.getDefaultInstance(),
    val bearerTokens: BearerTokens = BearerTokens.getDefaultInstance(),
)