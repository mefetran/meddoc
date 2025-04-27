package mefetran.dgusev.meddocs.ui.screen.signin

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.ui.components.RouteName
import mefetran.dgusev.meddocs.ui.screen.main.Main

@Serializable
internal data object SignIn : RouteName {
    override fun routeName(): String = SignIn::class.simpleName ?: ""
}

fun NavGraphBuilder.signInDestination(
    onNavigateToMain: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    composable<SignIn>(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(700)
            )
        }
    ) {
        val signInViewModel = hiltViewModel<SignInViewModel>()
        val state by signInViewModel.state.collectAsStateWithLifecycle()
        val emailValue by signInViewModel.emailValue.collectAsStateWithLifecycle()
        val passwordValue by signInViewModel.passwordValue.collectAsStateWithLifecycle()

        SignInScreen(
            state = state,
            emailValue = emailValue,
            passwordValue = passwordValue,
            navigateToMain = {
                if (signInViewModel.isInputValid()) {
                    onNavigateToMain()
                }
            },
            navigateToRegistrationScreen = onNavigateToSignUp,
            onNewEmailValue = signInViewModel::updateEmailValue,
            onNewPasswordValue = signInViewModel::updatePasswordValue,
            onShowPasswordClicked = signInViewModel::showPasswordClicked,
        )
    }
}

fun NavController.navigateToSignIn() {
    navigate(SignIn) {
        popUpTo<Main> { inclusive = true }
    }
}