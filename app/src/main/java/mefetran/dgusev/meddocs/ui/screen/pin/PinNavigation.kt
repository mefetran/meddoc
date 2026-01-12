package mefetran.dgusev.meddocs.ui.screen.pin

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import mefetran.dgusev.meddocs.ui.components.ObserveAsEvents
import mefetran.dgusev.meddocs.ui.components.SnackbarController
import mefetran.dgusev.meddocs.ui.components.SnackbarEvent
import mefetran.dgusev.meddocs.ui.screen.pin.model.BiometricAuthenticator
import mefetran.dgusev.meddocs.ui.screen.pin.model.PinUiEvent

@Serializable
internal data class Pin(
    val isCreatePin: Boolean,
)

fun NavGraphBuilder.pinDestination(
    biometricEnabled: Boolean,
    biometricAuthenticator: BiometricAuthenticator,
    onPinSuccessUnlock: () -> Unit,
    onPinSuccessCreate: () -> Unit,
) {
    composable<Pin> { navEntry ->
        val pinViewModel = hiltViewModel<PinViewModel>()
        val state by pinViewModel.state.collectAsStateWithLifecycle()
        val isCreatePin = navEntry.toRoute<Pin>().isCreatePin
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            pinViewModel.init(
                showBiometric = biometricEnabled,
                isCreatePin = isCreatePin,
            )
        }

        ObserveAsEvents(flow = pinViewModel.uiEvent) { event ->
            when (event) {
                PinUiEvent.SuccessUnlock -> {
                    onPinSuccessUnlock()
                }

                PinUiEvent.SuccessCreate -> {
                    onPinSuccessCreate()
                }
            }
        }

        PinScreen(
            state = state,
            onPinButtonClick = pinViewModel::onPinButtonClick,
            onBiometricClick = {
                biometricAuthenticator.authenticate(
                    onSuccess = {
                        onPinSuccessUnlock()
                    },
                    onError = { error ->
                        scope.launch {
                            SnackbarController.sendEvent(SnackbarEvent(message = "Error while biometric:\n$error"))
                        }
                    }
                )
            },
            onClearClick = pinViewModel::onClearClick,
            onDoneClick = pinViewModel::savePin,
        )
    }
}

fun NavController.navigateToCreatePin() {
    navigate(Pin(true)) {
        popUpTo(0) {
            inclusive = true
        }
    }
}

fun NavController.navigateToUnlockPin() {
    navigate(Pin(false))
}
