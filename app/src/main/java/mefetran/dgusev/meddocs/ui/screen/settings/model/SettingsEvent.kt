package mefetran.dgusev.meddocs.ui.screen.settings.model

sealed interface SettingsEvent {
    data object SignIn : SettingsEvent
}