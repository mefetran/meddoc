package mefetran.dgusev.meddocs.ui.screen.login

import android.util.Patterns
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mefetran.dgusev.meddocs.data.api.DocumentApi
import mefetran.dgusev.meddocs.ui.screen.login.model.LoginState
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val documentApi: DocumentApi,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _emailValue = MutableStateFlow(TextFieldValue(""))
    val emailValue = _emailValue.asStateFlow()

    private val _passwordValue = MutableStateFlow(TextFieldValue(""))
    val passwordValue = _passwordValue.asStateFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun updateEmailValue(newValue: TextFieldValue) {
        val trimmedNewValue = newValue.copy(text = newValue.text.trim().replace(" ", ""))

        viewModelScope.launch {
            _emailValue.update { trimmedNewValue }
            _state.update { it.copy(isEmailError = false) }
        }
    }

    fun updatePasswordValue(newValue: TextFieldValue) {
        val trimmedNewValue = newValue.copy(text = newValue.text.trim().replace(" ", ""))

        viewModelScope.launch {
            _passwordValue.update { trimmedNewValue }
            _state.update { it.copy(isPasswordError = trimmedNewValue.text.isBlank()) }
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
                    isPasswordError = !isPasswordValid
                )
            }
        }

        return isEmailValid && isPasswordValid
    }
}