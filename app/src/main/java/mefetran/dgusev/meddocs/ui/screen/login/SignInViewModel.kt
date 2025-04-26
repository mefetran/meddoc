package mefetran.dgusev.meddocs.ui.screen.login

import android.util.Patterns
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.substring
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mefetran.dgusev.meddocs.app.EMAIL_LENGTH
import mefetran.dgusev.meddocs.app.PASSWORD_MAX_LENGTH
import mefetran.dgusev.meddocs.ui.screen.login.model.SignInState
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _emailValue = MutableStateFlow(TextFieldValue(""))
    val emailValue = _emailValue.asStateFlow()

    private val _passwordValue = MutableStateFlow(TextFieldValue(""))
    val passwordValue = _passwordValue.asStateFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun updateEmailValue(newValue: TextFieldValue) {
        viewModelScope.launch {
            if (newValue.text.length < EMAIL_LENGTH) {
                _emailValue.update { newValue }
            } else {
                _emailValue.update { newValue.copy(text = newValue.text.substring(TextRange(0, EMAIL_LENGTH))) }
            }
            _state.update { it.copy(isEmailError = false) }
        }
    }

    fun updatePasswordValue(newValue: TextFieldValue) {
        viewModelScope.launch {
            if (newValue.text.length < PASSWORD_MAX_LENGTH) {
                _passwordValue.update { newValue }
            } else {
                _passwordValue.update { newValue.copy(text = newValue.text.substring(TextRange(0, PASSWORD_MAX_LENGTH))) }
            }
            _state.update { it.copy(isPasswordEmptyError = newValue.text.isBlank()) }
        }

        if (newValue.text.isBlank()) {
            viewModelScope.launch {
                _state.update { it.copy(showPassword = false) }
            }
        }
    }

    fun showPasswordClicked() {
        viewModelScope.launch {
            _state.update {
                it.copy(showPassword = !it.showPassword)
            }
        }
    }

    fun isInputValid(): Boolean {
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
}