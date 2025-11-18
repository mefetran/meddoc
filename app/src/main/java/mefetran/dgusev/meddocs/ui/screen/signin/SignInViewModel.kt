package mefetran.dgusev.meddocs.ui.screen.signin

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
import mefetran.dgusev.meddocs.app.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.app.datastore.withBearerToken
import mefetran.dgusev.meddocs.domain.repository.user.UserRepository
import mefetran.dgusev.meddocs.proto.Settings
import mefetran.dgusev.meddocs.ui.screen.signin.model.SignInState
import mefetran.dgusev.meddocs.ui.screen.signin.model.SignInUiEvent
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _emailValue = MutableStateFlow(TextFieldValue(""))
    val emailValue = _emailValue.asStateFlow()

    private val _passwordValue = MutableStateFlow(TextFieldValue(""))
    val passwordValue = _passwordValue.asStateFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _uiEvents =
        MutableSharedFlow<SignInUiEvent>()
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
            _state.update { it.copy(isPasswordEmptyError = newValue.text.isBlank()) }
        }

        if (newValue.text.isBlank()) {
            viewModelScope.launch {
                _state.update { it.copy(showPassword = false) }
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

    private fun isInputValid(): Boolean {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(_emailValue.value.text).matches()
        val isPasswordValid = _passwordValue.value.text.isNotBlank()

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isEmailError = !isEmailValid,
                    isPasswordEmptyError = !isPasswordValid
                )
            }
        }

        return isEmailValid && isPasswordValid
    }

    fun signIn() {
        if (!isInputValid()) {
            return
        }
        startLoading()
        viewModelScope.launch {
            val signInDeferred = async {
                userRepository.signInUser(
                    email = _emailValue.value.text,
                    password = _passwordValue.value.text,
                ).flowOn(dispatcher).first()
            }
            val signInResult = signInDeferred.await()
            signInResult
                .onSuccess { bearerTokens ->
                    settingsDataStore.updateData { settings ->
                        settings.withBearerToken(tokenPair = bearerTokens)
                    }.also { _uiEvents.emit(SignInUiEvent.SignIn) }
                    stopLoading()
                }
                .onFailure { singInException ->
                    Log.e("SignIn", "${singInException.message}")
                    handleSignInError(singInException)
                    stopLoading()
                }
        }
    }

    private fun handleSignInError(singInException: Throwable) {
        viewModelScope.launch {
            val (messageResId, errorDescription) = when (singInException) {
                is ClientRequestException -> when (singInException.response.status.value) {
                    400 -> R.string.error_invalid_input to null
                    401 -> R.string.error_invalid_credentials to null
                    403 -> R.string.error_forbidden to null
                    else -> R.string.error_client to singInException.response.status.description
                }

                is ServerResponseException ->
                    R.string.error_server to singInException.response.status.description

                is IOException -> R.string.error_network to null

                else -> R.string.error_unknown to singInException.message
            }

            _uiEvents.emit(SignInUiEvent.ShowSnackbar(messageResId, errorDescription))
        }
    }
}
