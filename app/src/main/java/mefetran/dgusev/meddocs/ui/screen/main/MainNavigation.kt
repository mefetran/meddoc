package mefetran.dgusev.meddocs.ui.screen.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.ui.screen.home.Home
import mefetran.dgusev.meddocs.ui.screen.home.homeDestination
import mefetran.dgusev.meddocs.ui.screen.signin.SignIn

@Serializable
internal data object Main

fun NavGraphBuilder.mainDestination(
    onHomeNavigateToSettings: () -> Unit,
    onHomeNavigateToSignIn: () -> Unit,
) {
    navigation<Main>(startDestination = Home) {
        homeDestination(
            onNavigateToSettings = onHomeNavigateToSettings,
            onNavigateToSignIn = onHomeNavigateToSignIn,
        )
    }
}

fun NavController.navigateToMain() {
    navigate(Main) {
        popUpTo<SignIn> { inclusive = true }
    }
}
