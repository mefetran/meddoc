package mefetran.dgusev.meddocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mefetran.dgusev.meddocs.ui.navigation.Routes
import mefetran.dgusev.meddocs.ui.screen.home.HomeScreen
import mefetran.dgusev.meddocs.ui.screen.login.SignInScreen
import mefetran.dgusev.meddocs.ui.screen.login.SignInViewModel
import mefetran.dgusev.meddocs.ui.screen.registration.RegistrationScreen
import mefetran.dgusev.meddocs.ui.screen.registration.SignUpViewModel
import mefetran.dgusev.meddocs.ui.screen.settings.SettingsScreen
import mefetran.dgusev.meddocs.ui.screen.splash.SplashViewModel
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            splashViewModel.isLoadingState.value
        }
        setContent {
            MeddocsTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.SignIn) {
                    composable<Routes.SignIn>(
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
                            navigateToHomeScreen = {
                                if (signInViewModel.isInputValid()) {
                                    navController.navigate(Routes.Home) {
                                        popUpTo<Routes.SignIn> { inclusive = true }
                                    }
                                }
                            },
                            navigateToRegistrationScreen = {
                                navController.navigate(Routes.SignUp)
                            },
                            onNewEmailValue = signInViewModel::updateEmailValue,
                            onNewPasswordValue = signInViewModel::updatePasswordValue,
                            onShowPasswordClicked = signInViewModel::showPasswordClicked,
                        )
                    }

                    composable<Routes.Home>(
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
                        popEnterTransition = {
                            fadeIn(tween(700))
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                tween(700)
                            )
                        }
                    ) {
                        HomeScreen(
                            navigateToSettingsScreen = {
                                navController.navigate(Routes.Settings)
                            },
                            navigateToLoginScreen = {
                                navController.navigate(Routes.SignIn) {
                                    popUpTo<Routes.Home> { inclusive = true }
                                }
                            }
                        )
                    }

                    composable<Routes.Settings> {
                        SettingsScreen(
                            navigateToHomeScreen = {
                                navController.popBackStack(Routes.Home, inclusive = false)
                            }
                        )
                    }

                    composable<Routes.SignUp>(
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

                        RegistrationScreen(
                            state = state,
                            emailValue = emailValue,
                            passwordValue = passwordValue,
                            nameValue = nameValue,
                            onNewEmailValue = signUpViewModel::updateEmailValue,
                            onNewPasswordValue = signUpViewModel::updatePasswordValue,
                            onNewNameValue = signUpViewModel::updateNameValue,
                            onShowPasswordClicked = signUpViewModel::showPasswordClicked,
                            navigateToHomeScreen = {
                                if (signUpViewModel.isInputValid()) {
                                    navController.navigate(Routes.Home) {
                                        popUpTo<Routes.SignIn> { inclusive = true }
                                    }
                                }
                            },
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}