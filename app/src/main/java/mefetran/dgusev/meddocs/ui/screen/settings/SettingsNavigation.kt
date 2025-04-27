package mefetran.dgusev.meddocs.ui.screen.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
internal data object Settings

fun NavGraphBuilder.settingsDestination(
    onPopToHome: () -> Unit,
) {
    composable<Settings> {
        SettingsScreen(
            navigateToHomeScreen = onPopToHome,
        )
    }
}

fun NavController.navigateToSettings() {
    navigate(Settings)
}