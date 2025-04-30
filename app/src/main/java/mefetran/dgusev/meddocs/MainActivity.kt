package mefetran.dgusev.meddocs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mefetran.dgusev.meddocs.ui.screen.main.mainDestination
import mefetran.dgusev.meddocs.ui.screen.main.navigateToMain
import mefetran.dgusev.meddocs.ui.screen.signin.SignIn
import mefetran.dgusev.meddocs.ui.screen.signin.signInDestination
import mefetran.dgusev.meddocs.ui.screen.signup.navigateToSignUp
import mefetran.dgusev.meddocs.ui.screen.signup.signUpDestination
import mefetran.dgusev.meddocs.ui.screen.main.MainViewModel
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

// An extension function that returns route as it named inside navigation stack
val NavDestination.shortRoute: String?
    get() = route?.substringAfterLast('.')?.substringBefore('?')

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoadingState.value
        }
        setContent {
            val darkThemeSettings by mainViewModel.darkThemeState.collectAsStateWithLifecycle()
            MeddocsTheme(
                darkTheme = if (darkThemeSettings.useSystemSettings) isSystemInDarkTheme() else darkThemeSettings.useDarkTheme
            ) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = SignIn
                ) {
                    signInDestination(
                        onNavigateToMain = navController::navigateToMain,
                        onNavigateToSignUp = navController::navigateToSignUp,
                    )
                    signUpDestination(
                        onNavigateToMain = navController::navigateToMain,
                        onBackClicked = {
                            navController.popBackStack()
                        },
                    )
                    mainDestination()
                }
            }
        }
    }
}