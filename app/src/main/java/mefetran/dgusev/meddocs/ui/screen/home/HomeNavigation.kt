package mefetran.dgusev.meddocs.ui.screen.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.shortRoute
import mefetran.dgusev.meddocs.ui.screen.signin.SignIn

@Serializable
internal data object Home

fun NavGraphBuilder.homeDestination(
    onNavigateToSettings: () -> Unit,
    onNavigateToSignIn: () -> Unit,
) {
    composable<Home>(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(700)
            )
        },
        exitTransition = {
            when (this.targetState.destination.shortRoute) {
                SignIn.routeName() -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(700)
                )

                else -> {
                    fadeOut(tween(700))
                }
            }
        },
        popEnterTransition = {
            fadeIn(tween(700))
        },
        popExitTransition = {
            fadeOut(tween(700))
        }
    ) {
        HomeScreen(
            navigateToSettingsScreen = onNavigateToSettings,
            navigateToSignInScreen = onNavigateToSignIn,
        )
    }
}

fun NavController.popToHome() {
    popBackStack(Home, inclusive = false)
}