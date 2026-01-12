package mefetran.dgusev.meddocs.ui.screen.pin

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mefetran.dgusev.meddocs.app.datastore.withPin
import mefetran.dgusev.meddocs.proto.Settings
import mefetran.dgusev.meddocs.ui.screen.pin.model.PinState
import mefetran.dgusev.meddocs.ui.screen.pin.model.PinUiEvent
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
): ViewModel() {
    private val _state = MutableStateFlow(PinState())
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<PinUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun init(
        showBiometric: Boolean,
        isCreatePin: Boolean,
    ) {
        _state.update {
            PinState(
                showBiometric = showBiometric,
                isCreatePin = isCreatePin,
            )
        }
    }

    fun onPinButtonClick(digit: Int) {
        if (_state.value.pin.length >= PIN_LENGTH) return

        _state.update {
            it.copy(
                pin = it.pin + digit.toString()
            )
        }

        if (!_state.value.isCreatePin && _state.value.pin.length == PIN_LENGTH) {
            viewModelScope.launch {
                val currentSettings = settingsDataStore.data.first()
                val currentPin = currentSettings.securitySettings.pin

                if (currentPin == _state.value.pin) {
                    _uiEvent.emit(PinUiEvent.SuccessUnlock)
                }
            }
        }
    }

    fun savePin() {
        if (state.value.isCreatePin && _state.value.pin.length >= PIN_LENGTH) {
            viewModelScope.launch {
                settingsDataStore.updateData { it.withPin(_state.value.pin) }
                _uiEvent.emit(PinUiEvent.SuccessCreate)
            }
        }
    }

    fun onClearClick() {
        _state.update {
            it.copy(
                pin = "",
            )
        }
    }
}
