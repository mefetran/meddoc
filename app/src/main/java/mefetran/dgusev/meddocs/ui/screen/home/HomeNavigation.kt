package mefetran.dgusev.meddocs.ui.screen.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
internal data object Home

fun NavGraphBuilder.homeDestination() {
    composable<Home> {
        HomeScreen()
    }
}

fun NavController.popToHome() {
    popBackStack(Home, inclusive = false)
}

fun NavController.navigateToHome() {
    navigate(Home)
}