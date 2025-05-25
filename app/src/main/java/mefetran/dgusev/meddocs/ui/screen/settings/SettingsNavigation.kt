package mefetran.dgusev.meddocs.ui.screen.settings

import androidx.activity.compose.LocalActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.ui.components.model.ThemeOption
import mefetran.dgusev.meddocs.ui.screen.settings.model.SettingsEvent

@Serializable
internal data object Settings

fun NavGraphBuilder.settingsDestination(
    onNavigateToSignIn: () -> Unit,
) {
    composable<Settings> {
        val settingsViewModel = hiltViewModel<SettingsViewModel>()
        val state by settingsViewModel.state.collectAsStateWithLifecycle()
        val activity = LocalActivity.current

        LaunchedEffect(Unit) {
            settingsViewModel.event.collect { event ->
                when (event) {
                    SettingsEvent.SignIn -> onNavigateToSignIn()
                }
            }
        }

        SettingsScreen(
            state = state,
            onThemeOptionClick = { themeOption ->
                when (themeOption) {
                    ThemeOption.Dark -> settingsViewModel.selectTheme(
                        useSystemTheme = false,
                        useDarkTheme = true
                    )

                    ThemeOption.Light -> settingsViewModel.selectTheme(
                        useSystemTheme = false,
                        useDarkTheme = false
                    )

                    ThemeOption.System -> settingsViewModel.selectTheme(
                        useSystemTheme = true,
                        useDarkTheme = true
                    )
                }
            },
            onLanguageOptionClick = { languageOption ->
                activity?.let {
                    activity.runOnUiThread {
                        val appLocale = LocaleListCompat.forLanguageTags(languageOption.languageCode)
                        AppCompatDelegate.setApplicationLocales(appLocale)
                    }
                    settingsViewModel.selectLanguage(languageOption.languageCode)
                }
            },
            onLogoutClick = settingsViewModel::logout
        )
    }
}

fun NavController.navigateToSettings() {
    navigate(Settings)
}