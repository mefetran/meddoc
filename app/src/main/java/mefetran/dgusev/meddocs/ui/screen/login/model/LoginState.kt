package mefetran.dgusev.meddocs.ui.screen.login.model

data class LoginState(
    val showPassword: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
)
