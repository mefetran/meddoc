package mefetran.dgusev.meddocs.ui.screen.signup

import android.util.Log
import android.util.Patterns
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.substring
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.app.EMAIL_LENGTH
import mefetran.dgusev.meddocs.app.NAME_LENGTH
import mefetran.dgusev.meddocs.app.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.app.PASSWORD_MIN_LENGTH
import mefetran.dgusev.meddocs.app.datastore.withBearerToken
import mefetran.dgusev.meddocs.data.api.request.user.UserRegistrationRequestBody
import mefetran.dgusev.meddocs.data.api.request.user.UserSignInRequestBody
import mefetran.dgusev.meddocs.data.repository.UserRepository
import mefetran.dgusev.meddocs.di.RealRepository
import mefetran.dgusev.meddocs.proto.Settings
import mefetran.dgusev.meddocs.ui.screen.signup.model.SignUpUiEvent
import mefetran.dgusev.meddocs.ui.screen.signup.model.SignUpState
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val dispatcher: CoroutineDispatcher,
    @RealRepository val userRepository: UserRepository,
) : ViewModel() {
    private val _emailValue = MutableStateFlow(TextFieldValue(""))
    val emailValue = _emailValue.asStateFlow()

    private val _passwordValue = MutableStateFlow(TextFieldValue(""))
    val passwordValue = _passwordValue.asStateFlow()

    private val _nameValue = MutableStateFlow(TextFieldValue(""))
    val nameValue = _nameValue.asStateFlow()

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _uiEvents =
        MutableSharedFlow<SignUpUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    fun updateEmailValue(newValue: TextFieldValue) {
        viewModelScope.launch {
            if (newValue.text.length < EMAIL_LENGTH) {
                _emailValue.update { newValue }
            } else {
                _emailValue.update {
                    newValue.copy(
                        text = newValue.text.substring(
                            TextRange(
                                0,
                                EMAIL_LENGTH
                            )
                        )
                    )
                }
            }
            _state.update { it.copy(isEmailError = false) }
        }
    }

    fun updatePasswordValue(newValue: TextFieldValue) {
        viewModelScope.launch {
            if (newValue.text.length < PASSWORD_MAX_LENGTH) {
                _passwordValue.update { newValue }
            } else {
                _passwordValue.update {
                    newValue.copy(
                        text = newValue.text.substring(
                            TextRange(
                                0,
                                PASSWORD_MAX_LENGTH
                            )
                        )
                    )
                }
            }
            _state.update { it.copy(isPasswordShortError = newValue.text.length < PASSWORD_MIN_LENGTH) }
        }

        if (newValue.text.isBlank()) {
            viewModelScope.launch {
                _state.update { it.copy(showPassword = false) }
            }
        }
    }

    fun updateNameValue(newValue: TextFieldValue) {
        viewModelScope.launch {
            if (newValue.text.length < NAME_LENGTH) {
                _nameValue.update { newValue }
            } else {
                _nameValue.update {
                    newValue.copy(
                        text = newValue.text.substring(
                            TextRange(
                                0,
                                NAME_LENGTH
                            )
                        )
                    )
                }
            }
        }
    }

    fun showPasswordInput() {
        viewModelScope.launch {
            _state.update {
                it.copy(showPassword = !it.showPassword)
            }
        }
    }

    private fun isInputValid(): Boolean {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(_emailValue.value.text).matches()
        val isPasswordValid = _passwordValue.value.text.length >= PASSWORD_MIN_LENGTH

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isEmailError = !isEmailValid,
                    isPasswordShortError = !isPasswordValid,
                )
            }
        }

        return isEmailValid && isPasswordValid
    }

    private fun startLoading() {
        _state.update {
            it.copy(isLoading = true)
        }
    }

    private fun stopLoading() {
        _state.update {
            it.copy(isLoading = false)
        }
    }

    fun signUp() {
        if (!isInputValid()) {
            return
        }
        startLoading()
        viewModelScope.launch {
            val userSignUpCredentials = UserRegistrationRequestBody(
                email = _emailValue.value.text,
                password = _passwordValue.value.text,
                name = _nameValue.value.text.ifBlank { null }
            )

            val signUpDeferred = async {
                userRepository.signUpUser(userSignUpCredentials).flowOn(dispatcher).first()
            }
            val signUpResult = signUpDeferred.await()

            signUpResult
                .onSuccess { user ->
                    userRepository.saveUser(user)
                    val userSignInCredentials = UserSignInRequestBody(
                        email = _emailValue.value.text,
                        password = _passwordValue.value.text
                    )
                    val signInDeferred = async {
                        userRepository.signInUser(userSignInCredentials).flowOn(dispatcher)
                            .first()
                    }
                    val signInResult = signInDeferred.await()
                    signInResult
                        .onSuccess { bearerTokens ->
                            settingsDataStore.updateData { settings ->
                                settings.withBearerToken(tokenPairResponse = bearerTokens)
                            }.also { _uiEvents.tryEmit(SignUpUiEvent.SignUp) }
                            stopLoading()
                        }
                        .onFailure { singInException ->
                            Log.e("SignIn", "${singInException.message}")
                            stopLoading()
                        }
                }
                .onFailure { signUpException ->
                    Log.e("SignUp", "${signUpException.message}")
                    handleSignUpError(signUpException)
                    stopLoading()
                }
        }
    }

    private fun handleSignUpError(singUpException: Throwable) {
        viewModelScope.launch {
            val (messageResId, errorDescription) = when (singUpException) {
                is ClientRequestException -> when (singUpException.response.status.value) {
                    400 -> R.string.error_invalid_input to null
                    409 -> R.string.error_user_exists to null
                    else -> R.string.error_client to singUpException.response.status.description
                }

                is ServerResponseException ->
                    R.string.error_server to singUpException.response.status.description

                is IOException -> R.string.error_network to null

                else -> R.string.error_unknown to singUpException.message
            }

            _uiEvents.emit(SignUpUiEvent.ShowSnackbar(messageResId, errorDescription))
        }
    }
}