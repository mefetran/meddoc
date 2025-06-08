package mefetran.dgusev.meddocs.ui.screen.signup

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.ui.components.SnackbarController
import mefetran.dgusev.meddocs.ui.components.SnackbarEvent
import mefetran.dgusev.meddocs.ui.screen.signup.model.SignUpUiEvent

@Serializable
internal data object SignUp

fun NavGraphBuilder.signUpDestination(
    onNavigateToMain: () -> Unit,
    onBackClicked: () -> Unit,
) {
    composable<SignUp>(
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(700)
            )
        }
    ) {
        val signUpViewModel = hiltViewModel<SignUpViewModel>()
        val state by signUpViewModel.state.collectAsStateWithLifecycle()
        val emailValue by signUpViewModel.emailValue.collectAsStateWithLifecycle()
        val passwordValue by signUpViewModel.passwordValue.collectAsStateWithLifecycle()
        val nameValue by signUpViewModel.nameValue.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            signUpViewModel.uiEvent.collect { event ->
                when (event) {
                    SignUpUiEvent.SignUp -> {
                        onNavigateToMain()
                    }

                    is SignUpUiEvent.ShowSnackbar -> {
                        val message = context.getString(event.messageResId, event.errorDescription)
                        SnackbarController.sendEvent(SnackbarEvent(message = message))
                    }
                }
            }
        }

        SignUpScreen(
            state = state,
            emailValue = emailValue,
            passwordValue = passwordValue,
            nameValue = nameValue,
            onNewEmailValue = signUpViewModel::updateEmailValue,
            onNewPasswordValue = signUpViewModel::updatePasswordValue,
            onNewNameValue = signUpViewModel::updateNameValue,
            onShowPasswordClicked = signUpViewModel::showPasswordInput,
            onSignUp = signUpViewModel::signUp,
            onBackClicked = onBackClicked,
        )
    }
}

fun NavController.navigateToSignUp() {
    navigate(SignUp)
}