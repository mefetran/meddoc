package mefetran.dgusev.meddocs.ui.screen.signin.model

sealed interface SignInEvent {
    data object SignIn : SignInEvent
}