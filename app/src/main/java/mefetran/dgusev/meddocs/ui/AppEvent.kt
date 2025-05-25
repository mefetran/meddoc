package mefetran.dgusev.meddocs.ui

sealed interface AppEvent {
    data object SignIn: AppEvent
}