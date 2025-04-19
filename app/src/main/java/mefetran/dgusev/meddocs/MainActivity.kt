package mefetran.dgusev.meddocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mefetran.dgusev.meddocs.ui.navigation.Routes
import mefetran.dgusev.meddocs.ui.screen.home.HomeScreen
import mefetran.dgusev.meddocs.ui.screen.login.LoginScreen
import mefetran.dgusev.meddocs.ui.screen.registration.RegistrationScreen
import mefetran.dgusev.meddocs.ui.screen.settings.SettingsScreen
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeddocsTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Routes.Login) {
                    composable<Routes.Login> {
                        LoginScreen(
                            navigateToHomeScreen = {
                                navController.navigate(Routes.Home) {
                                    popUpTo<Routes.Login> { inclusive = true }
                                }
                            },
                            navigateToRegistrationScreen = {
                                navController.navigate(Routes.Registration)
                            }
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