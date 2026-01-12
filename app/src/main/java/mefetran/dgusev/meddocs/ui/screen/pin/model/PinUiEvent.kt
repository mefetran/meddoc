package mefetran.dgusev.meddocs.ui.screen.pin.model

sealed interface PinUiEvent {
    data object SuccessUnlock : PinUiEvent
    data object SuccessCreate : PinUiEvent
}
