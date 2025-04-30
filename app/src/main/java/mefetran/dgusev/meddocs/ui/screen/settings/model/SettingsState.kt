package mefetran.dgusev.meddocs.ui.screen.settings.model

data class SettingsState(
    val useDarkTheme: Boolean = true,
    val useSystemTheme: Boolean = true,
    val currentLanguageCode: String = "en",
)