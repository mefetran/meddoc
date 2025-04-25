package mefetran.dgusev.meddocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mefetran.dgusev.meddocs.ui.navigation.Routes
import mefetran.dgusev.meddocs.ui.screen.home.HomeScreen
import mefetran.dgusev.meddocs.ui.screen.login.LoginScreen
import mefetran.dgusev.meddocs.ui.screen.login.LoginViewModel
import mefetran.dgusev.meddocs.ui.screen.registration.RegistrationScreen
import mefetran.dgusev.meddocs.ui.screen.settings.SettingsScreen
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeddocsTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.Login) {
                    composable<Routes.Login> {
                        val loginViewModel = hiltViewModel<LoginViewModel>()
                        val state by loginViewModel.state.collectAsStateWithLifecycle()
                        val emailValue by loginViewModel.emailValue.collectAsStateWithLifecycle()
                        val passwordValue by loginViewModel.passwordValue.collectAsStateWithLifecycle()

                        LoginScreen(
                            state = state,
                            emailValue = emailValue,
                            passwordValue = passwordValue,
                            navigateToHomeScreen = {
                                if (loginViewModel.isInputValid()) {
                                    navController.navigate(Routes.Home) {
                                        popUpTo<Routes.Login> { inclusive = true }
                                    }
                                }
                            },
                            navigateToRegistrationScreen = {
                                navController.navigate(Routes.Registration)
                            },
                            onNewEmailValue = loginViewModel::updateEmailValue,
                            onNewPasswordValue = loginViewModel::updatePasswordValue,
                            onShowPasswordClicked = loginViewModel::showPasswordClicked,
                        )
                    }

                    composable<Routes.Home> {
                        HomeScreen(
                            navigateToSettingsScreen = {
                                navController.navigate(Routes.Settings)
                            },
                            navigateToLoginScreen = {
                                navController.navigate(Routes.Login) {
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

                    composable<Routes.Registration> {
                        RegistrationScreen(
                            navigateToHomeScreen = {
                                navController.navigate(Routes.Home) {
                                    popUpTo<Routes.Login> { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}