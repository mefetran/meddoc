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
import mefetran.dgusev.meddocs.app.EMAIL_LENGTH
import mefetran.dgusev.meddocs.app.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.app.datastore.withBearerToken
import mefetran.dgusev.meddocs.data.api.request.UserSignInRequestBody
import mefetran.dgusev.meddocs.data.repository.UserRepository
import mefetran.dgusev.meddocs.di.FakeRepository
import mefetran.dgusev.meddocs.proto.Settings
import mefetran.dgusev.meddocs.ui.screen.signin.model.SignInEvent
import mefetran.dgusev.meddocs.ui.screen.signin.model.SignInState
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val settingsDataStore: DataStore<Settings>,
    private val dispatcher: CoroutineDispatcher,
    @FakeRepository val userRepository: UserRepository,
) : ViewModel() {
    private val _emailValue = MutableStateFlow(TextFieldValue(""))
    val emailValue = _emailValue.asStateFlow()

    private val _passwordValue = MutableStateFlow(TextFieldValue(""))
    val passwordValue = _passwordValue.asStateFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _event =
        MutableSharedFlow<SignInEvent>()
    val event = _event.asSharedFlow()

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
        if (isInputValid()) {
            startLoading()
            viewModelScope.launch {
                val userSignInCredentials = UserSignInRequestBody(
                    email = _emailValue.value.text,
                    password = _passwordValue.value.text
                )

                val signInDeferred = async {
                    userRepository.signInUser(userSignInCredentials).flowOn(dispatcher).first()
                }
                val signInResult = signInDeferred.await()
                signInResult
                    .onSuccess { bearerTokens ->
                        settingsDataStore.updateData { settings ->
                            settings.withBearerToken(bearerTokens)
                        }.also { _event.tryEmit(SignInEvent.SignIn) }
                        stopLoading()
                    }
                    .onFailure { singInException ->
                        Log.e("SignIn", "${singInException.message}")
                        stopLoading()
                    }
            }
        }
    }
}