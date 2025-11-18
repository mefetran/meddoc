package mefetran.dgusev.meddocs.ui.screen.signup.model

import androidx.annotation.StringRes

sealed interface SignUpUiEvent {
    data object SignUp : SignUpUiEvent

    data class ShowSnackbar(
        @param:StringRes val messageResId: Int,
        val errorDescription: String? = null,
    ) : SignUpUiEvent
}
