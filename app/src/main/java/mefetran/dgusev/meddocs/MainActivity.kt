package mefetran.dgusev.meddocs

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mefetran.dgusev.meddocs.app.datastore.isBlank
import mefetran.dgusev.meddocs.ui.AppEvent
import mefetran.dgusev.meddocs.ui.AppViewModel
import mefetran.dgusev.meddocs.ui.components.ObserveAsEvents
import mefetran.dgusev.meddocs.ui.components.SnackbarController
import mefetran.dgusev.meddocs.ui.screen.documentcreate.createDocumentDestination
import mefetran.dgusev.meddocs.ui.screen.documentcreate.navigateToCreateDocument
import mefetran.dgusev.meddocs.ui.screen.documentopen.navigateToOpenDocument
import mefetran.dgusev.meddocs.ui.screen.documentopen.openDocumentDestination
import mefetran.dgusev.meddocs.ui.screen.main.Main
import mefetran.dgusev.meddocs.ui.screen.main.mainDestination
import mefetran.dgusev.meddocs.ui.screen.main.navigateToMain
import mefetran.dgusev.meddocs.ui.screen.signin.SignIn
import mefetran.dgusev.meddocs.ui.screen.signin.navigateToSignIn
import mefetran.dgusev.meddocs.ui.screen.signin.signInDestination
import mefetran.dgusev.meddocs.ui.screen.signup.navigateToSignUp
import mefetran.dgusev.meddocs.ui.screen.signup.signUpDestination
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme


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
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                appViewModel.event.collect { event ->
                    when (event) {
                        AppEvent.SignIn -> navController.navigateToSignIn()
                    }
                }
            }

            ObserveAsEvents(
                flow = SnackbarController.events,
                key1 = snackbarHostState,
            ) { event ->
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()

                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action?.name,
                        duration = event.duration,
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        event.action?.action?.invoke()
                    }
                }
            }

            MeddocsTheme(
                darkTheme = if (state.darkThemeSettings.useSystemSettings) isSystemInDarkTheme() else state.darkThemeSettings.useDarkTheme
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = if (state.bearerTokens.isBlank() && state.user == null) SignIn else Main
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
                            onNavigateToCreateDocument = navController::navigateToCreateDocument,
                            onNavigateToOpenDocument = navController::navigateToOpenDocument,
                        )
                        createDocumentDestination(
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                        openDocumentDestination(
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }
                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            .padding(
                                bottom = WindowInsets.safeDrawing.asPaddingValues()
                                    .calculateBottomPadding() + 16.dp
                            )
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }
}