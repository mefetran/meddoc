package mefetran.dgusev.meddocs.ui.screen.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.ui.components.BackToolbar
import mefetran.dgusev.meddocs.ui.components.ScreenTitle
import mefetran.dgusev.meddocs.ui.screen.signup.model.SignUpState
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

@Composable
internal fun SignUpScreen(
    state: SignUpState,
    emailValue: TextFieldValue,
    passwordValue: TextFieldValue,
    nameValue: TextFieldValue,
    modifier: Modifier = Modifier,
    onSignUp: () -> Unit,
    onNewEmailValue: (TextFieldValue) -> Unit,
    onNewPasswordValue: (TextFieldValue) -> Unit,
    onNewNameValue: (TextFieldValue) -> Unit,
    onShowPasswordClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()

    BackHandler {
        onBackClicked()
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .safeDrawingPadding(),
        ) {
            BackToolbar(onBackClicked = onBackClicked)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                ScreenTitle(stringResource(id = R.string.sign_up_title))
                Spacer(Modifier.height(32.dp))
                OutlinedTextField(
                    value = emailValue,
                    onValueChange = { newValue ->
                        val text = newValue.text.trim().replace(" ", "")
                        val isPasting = newValue.text.length > emailValue.text.length + 1
                        val newTextFieldValue = if (isPasting) {
                            TextFieldValue(
                                text = text,
                                selection = TextRange(text.length)
                            )
                        } else {
                            newValue.copy(text)
                        }

                        onNewEmailValue(newTextFieldValue)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "${stringResource(id = R.string.email_label)}*",
                        )
                    },
                    supportingText = {
                        if (state.isEmailError) {
                            Text(
                                text = stringResource(id = R.string.error_invalid_email)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                    singleLine = true,
                    isError = state.isEmailError,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = passwordValue,
                    onValueChange = { newValue ->
                        val text = newValue.text.trim().replace(" ", "")
                        val isPasting = newValue.text.length > emailValue.text.length + 1
                        val newTextFieldValue = if (isPasting) {
                            TextFieldValue(
                                text = text,
                                selection = TextRange(text.length)
                            )
                        } else {
                            newValue.copy(text)
                        }

                        onNewPasswordValue(newTextFieldValue)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "${stringResource(id = R.string.password_label)}*",
                        )
                    },
                    supportingText = {
                        if (state.isPasswordShortError) {
                            Text(
                                text = stringResource(id = R.string.error_short_password)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Send,
                    ),
                    keyboardActions = KeyboardActions(onSend = {
                        onSignUp()
                    }),
                    visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        if (passwordValue.text.isNotBlank()) {
                            Icon(
                                imageVector = if (state.showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Toggle password visibility",
                                modifier = Modifier.clickable { onShowPasswordClicked() }
                            )
                        }
                    },
                    singleLine = true,
                    isError = state.isPasswordShortError,
                )
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = nameValue,
                    onValueChange = { newValue ->
                        val text =
                            if (newValue.text.all { it == ' ' }) newValue.text.trim() else newValue.text
                        val isPasting = newValue.text.length > emailValue.text.length + 1
                        val newTextFieldValue = if (isPasting) {
                            TextFieldValue(
                                text = text,
                                selection = TextRange(text.length)
                            )
                        } else {
                            newValue.copy(text)
                        }

                        onNewNameValue(newTextFieldValue)
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.name_label)
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onSignUp,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .imePadding()
                    .navigationBarsPadding(),
            ) {
                Text(
                    text = stringResource(id = R.string.create_account_button),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }

    if (state.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.surfaceDim.copy(0.8f))
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 6.dp,
                modifier = Modifier.size(56.dp),
            )
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
internal fun SignUpPreview(modifier: Modifier = Modifier) {
    val emailValue by remember { mutableStateOf(TextFieldValue("")) }
    val passwordValue by remember { mutableStateOf(TextFieldValue("")) }
    val nameValue by remember { mutableStateOf(TextFieldValue("")) }

    MeddocsTheme {
        SignUpScreen(
            state = SignUpState(isEmailError = false, isPasswordShortError = false,
                isLoading = false),
            emailValue = emailValue,
            passwordValue = passwordValue,
            nameValue = nameValue,
            onSignUp = {},
            onNewPasswordValue = {},
            onNewEmailValue = {},
            onShowPasswordClicked = {},
            onNewNameValue = {},
            onBackClicked = {},
        )
    }
}
