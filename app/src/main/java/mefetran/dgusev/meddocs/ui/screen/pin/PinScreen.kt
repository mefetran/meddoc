package mefetran.dgusev.meddocs.ui.screen.pin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.ui.components.ScreenTitle
import mefetran.dgusev.meddocs.ui.screen.pin.model.PinState

const val PIN_LENGTH = 4

@Composable
fun PinScreen(
    state: PinState,
    modifier: Modifier = Modifier,
    onPinButtonClick: (Int) -> Unit,
    onBiometricClick: () -> Unit,
    onClearClick: () -> Unit,
    onDoneClick: () -> Unit,
) {
    LaunchedEffect(Unit) { Log.d("my", "Inside PinScreen") }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())
        ) {
            ScreenTitle(title = stringResource(if (state.isCreatePin) R.string.create_pin else R.string.pin))
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(R.string.enter_pin),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            PinEnterDots(
                enteredDigits = state.pin.length.coerceIn(0, PIN_LENGTH),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PinPad(
                showBiometric = state.showBiometric,
                isCreatePin = state.isCreatePin,
                onPinButtonClick = onPinButtonClick,
                onBiometricClick = onBiometricClick,
                onClearClick = onClearClick,
                onDoneClick = onDoneClick,
                modifier = Modifier.padding(all = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, locale = "ru")
@Composable
fun PinScreenPreview() {
    var pin by remember { mutableStateOf("") }
    var state by remember {
        mutableStateOf(
            PinState(
                showBiometric = true,
                pin = pin,
                isCreatePin = true
            )
        )
    }

    PinScreen(
        state = state,
        onBiometricClick = {},
        onPinButtonClick = { digit ->
            state = state.copy(pin = state.pin + digit)
        },
        onClearClick = { state = state.copy(pin = "") },
        onDoneClick = {},
    )
}
