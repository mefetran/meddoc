package mefetran.dgusev.meddocs

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mefetran.dgusev.meddocs.app.datastore.isBlank
import mefetran.dgusev.meddocs.ui.AppEvent
import mefetran.dgusev.meddocs.ui.AppViewModel
import mefetran.dgusev.meddocs.ui.screen.main.Main
import mefetran.dgusev.meddocs.ui.screen.main.mainDestination
import mefetran.dgusev.meddocs.ui.screen.main.navigateToMain
import mefetran.dgusev.meddocs.ui.screen.signin.SignIn
import mefetran.dgusev.meddocs.ui.screen.signin.navigateToSignIn
import mefetran.dgusev.meddocs.ui.screen.signin.signInDestination
import mefetran.dgusev.meddocs.ui.screen.signup.navigateToSignUp
import mefetran.dgusev.meddocs.ui.screen.signup.signUpDestination
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

// An extension function that returns route as it named inside navigation stack
val NavDestination.shortRoute: String?
    get() = route?.substringAfterLast('.')?.substringBefore('?')

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            appViewModel.isLoadingState.value
        }
        setContent {
            val state by appViewModel.state.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                appViewModel.event.collect { event ->
                    when (event) {
                        AppEvent.SignIn -> navController.navigateToSignIn()
                    }
                }
            }

            MeddocsTheme(
                darkTheme = if (state.darkThemeSettings.useSystemSettings) isSystemInDarkTheme() else state.darkThemeSettings.useDarkTheme
            ) {
                NavHost(
                    navController = navController,
                    startDestination = if(state.bearerTokens.isBlank() && state.user == null) SignIn else Main
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
                    mainDestination(
                        onNavigateToSignIn = navController::navigateToSignIn,
                    )
                }
            }
        }
    }
}