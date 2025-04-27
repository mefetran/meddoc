package mefetran.dgusev.meddocs.ui.screen.signup.model

data class SignUpState(
    val showPassword: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordShortError: Boolean = false,
)