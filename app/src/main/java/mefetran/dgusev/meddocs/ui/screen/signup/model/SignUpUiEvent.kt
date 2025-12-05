package mefetran.dgusev.meddocs.ui.screen.signup.model

import androidx.annotation.StringRes

sealed interface SignUpUiEvent {
    data object SignUp : SignUpUiEvent

    data class ShowSnackbarRes(
        @param:StringRes val messageResId: Int,
        val errorDescription: String? = null,
    ) : SignUpUiEvent

    data class ShowSnackbar(
        val message: String,
    ) : SignUpUiEvent
}
