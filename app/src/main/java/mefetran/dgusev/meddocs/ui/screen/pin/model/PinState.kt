package mefetran.dgusev.meddocs.ui.screen.pin.model

data class PinState(
    val showBiometric: Boolean = false,
    val pin: String = "",
    val isCreatePin: Boolean = false,
)
