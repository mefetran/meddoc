package mefetran.dgusev.meddocs.ui.screen.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.ui.screen.signin.SignIn

@Serializable
internal data object Main

fun NavGraphBuilder.mainDestination(
    onNavigateToSignIn: () -> Unit,
) {
    composable<Main>(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(700)
                )
        },
    ) {
        MainNavHost(
            onNavigateToSignIn = onNavigateToSignIn,
        )
    }
}

fun NavController.navigateToMain() {
    navigate(Main) {
        popUpTo<SignIn> { inclusive = true }
    }
}