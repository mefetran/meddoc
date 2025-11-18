package mefetran.dgusev.meddocs.ui.screen.signin.model

import androidx.annotation.StringRes

sealed interface SignInUiEvent {
    data object SignIn : SignInUiEvent

    data class ShowSnackbar(
        @param:StringRes val messageResId: Int,
        val errorDescription: String? = null,
    ) : SignInUiEvent
}
