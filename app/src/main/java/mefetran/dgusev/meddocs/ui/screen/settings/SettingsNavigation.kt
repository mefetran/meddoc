package mefetran.dgusev.meddocs.ui.screen.settings

import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.R

@Serializable
internal data object Settings

fun NavGraphBuilder.settingsDestination() {
    composable<Settings> {
        val settingsViewModel = hiltViewModel<SettingsViewModel>()
        val state by settingsViewModel.state.collectAsStateWithLifecycle()
        val context = LocalContext.current

        SettingsScreen(
            state = state,
            onThemeOptionClicked = { option ->
                when (option) {
                    context.getString(R.string.theme_system_label) -> settingsViewModel.selectTheme(
                        useSystemTheme = true,
                        useDarkTheme = true
                    )

                    context.getString(R.string.theme_dark_label) -> settingsViewModel.selectTheme(
                        useSystemTheme = false,
                        useDarkTheme = true
                    )

                    context.getString(R.string.theme_light_label) -> settingsViewModel.selectTheme(
                        useSystemTheme = false,
                        useDarkTheme = false
                    )
                }
            }
        )
    }
}

fun NavController.navigateToSettings() {
    navigate(Settings)
}