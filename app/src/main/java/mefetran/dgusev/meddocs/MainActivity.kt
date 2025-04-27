package mefetran.dgusev.meddocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mefetran.dgusev.meddocs.ui.screen.home.popToHome
import mefetran.dgusev.meddocs.ui.screen.main.mainDestination
import mefetran.dgusev.meddocs.ui.screen.main.navigateToMain
import mefetran.dgusev.meddocs.ui.screen.settings.navigateToSettings
import mefetran.dgusev.meddocs.ui.screen.settings.settingsDestination
import mefetran.dgusev.meddocs.ui.screen.signin.SignIn
import mefetran.dgusev.meddocs.ui.screen.signin.navigateToSignIn
import mefetran.dgusev.meddocs.ui.screen.signin.signInDestination
import mefetran.dgusev.meddocs.ui.screen.signup.navigateToSignUp
import mefetran.dgusev.meddocs.ui.screen.signup.signUpDestination
import mefetran.dgusev.meddocs.ui.screen.splash.SplashViewModel
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

// An extension function that returns route as it named inside navigation stack
val NavDestination.shortRoute: String?
    get() = route?.substringAfterLast('.')?.substringBefore('?')

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

                NavHost(navController = navController, startDestination = SignIn) {
                    signInDestination(
                        onNavigateToMain = navController::navigateToMain,
                        onNavigateToSignUp = navController::navigateToSignUp,
                    )
                    settingsDestination(
                        onPopToHome = navController::popToHome,
                    )
                    signUpDestination(
                        onNavigateToMain = navController::navigateToMain,
                        onBackClicked = {
                            navController.popBackStack()
                        },
                    )
                    mainDestination(
                        onHomeNavigateToSettings = navController::navigateToSettings,
                        onHomeNavigateToSignIn = navController::navigateToSignIn,
                    )
                }
            }
        }
    }
}