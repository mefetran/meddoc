package mefetran.dgusev.meddocs

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import mefetran.dgusev.meddocs.app.datastore.isBlank
import mefetran.dgusev.meddocs.ui.StartViewModel
import mefetran.dgusev.meddocs.ui.screen.main.Main
import mefetran.dgusev.meddocs.ui.screen.main.mainDestination
import mefetran.dgusev.meddocs.ui.screen.main.navigateToMain
import mefetran.dgusev.meddocs.ui.screen.signin.SignIn
import mefetran.dgusev.meddocs.ui.screen.signin.signInDestination
import mefetran.dgusev.meddocs.ui.screen.signup.navigateToSignUp
import mefetran.dgusev.meddocs.ui.screen.signup.signUpDestination
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

// An extension function that returns route as it named inside navigation stack
val NavDestination.shortRoute: String?
    get() = route?.substringAfterLast('.')?.substringBefore('?')

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            startViewModel.isLoadingState.value
        }
        setContent {
            val state by startViewModel.state.collectAsStateWithLifecycle()

            MeddocsTheme(
                darkTheme = if (state.darkThemeSettings.useSystemSettings) isSystemInDarkTheme() else state.darkThemeSettings.useDarkTheme
            ) {
                val navController = rememberNavController()
                val user = remember { startViewModel.getUser() }

                NavHost(
                    navController = navController,
                    startDestination = if(state.bearerTokens.isBlank() && user == null) SignIn else Main
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