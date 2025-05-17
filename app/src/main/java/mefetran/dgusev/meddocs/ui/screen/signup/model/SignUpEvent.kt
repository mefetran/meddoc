package mefetran.dgusev.meddocs.ui.screen.signup.model

sealed interface SignUpEvent {
    data object SignUp : SignUpEvent
    data object Empty : SignUpEvent
}