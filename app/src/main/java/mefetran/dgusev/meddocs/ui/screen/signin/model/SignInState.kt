package mefetran.dgusev.meddocs.ui.screen.signin.model

data class SignInState(
    val showPassword: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordEmptyError: Boolean = false,
    val isLoading: Boolean = false,
)