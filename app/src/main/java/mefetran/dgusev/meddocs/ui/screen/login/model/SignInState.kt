package mefetran.dgusev.meddocs.ui.screen.login.model

data class SignInState(
    val showPassword: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordEmptyError: Boolean = false,
)