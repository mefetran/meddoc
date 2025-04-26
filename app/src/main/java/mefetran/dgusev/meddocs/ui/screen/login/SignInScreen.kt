package mefetran.dgusev.meddocs.ui.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mefetran.dgusev.meddocs.R
import mefetran.dgusev.meddocs.ui.screen.login.model.SignInState
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme

@Composable
fun SignInScreen(
    state: SignInState,
    emailValue: TextFieldValue,
    passwordValue: TextFieldValue,
    modifier: Modifier = Modifier,
    navigateToHomeScreen: () -> Unit,
    navigateToRegistrationScreen: () -> Unit,
    onNewEmailValue: (TextFieldValue) -> Unit,
    onNewPasswordValue: (TextFieldValue) -> Unit,
    onShowPasswordClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .safeDrawingPadding()
                .padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.welcome_title),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.sign_in_hint),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(16.dp))
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
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.email_label),
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
                modifier = Modifier
                    .align(Alignment.Start)
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(id = R.string.password_label),
                    )
                },
                supportingText = {
                    if (state.isPasswordEmptyError) {
                        Text(
                            text = stringResource(id = R.string.error_empty_password)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions(onSend = {
                    navigateToHomeScreen()
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
                isError = state.isPasswordEmptyError,
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = navigateToHomeScreen,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.sign_in_button),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.sign_up_hint),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Button(
                onClick = navigateToRegistrationScreen,
                modifier = Modifier.height(48.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.sign_up_button),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ru")
@Composable
fun SignInScreenPreview(modifier: Modifier = Modifier) {
    val emailValue by remember { mutableStateOf(TextFieldValue("")) }
    val passwordValue by remember { mutableStateOf(TextFieldValue("")) }

    MeddocsTheme {
        SignInScreen(
            state = SignInState(isEmailError = true, isPasswordEmptyError = true),
            emailValue = emailValue,
            passwordValue = passwordValue,
            navigateToHomeScreen = {},
            navigateToRegistrationScreen = {},
            onNewPasswordValue = {},
            onNewEmailValue = {},
            onShowPasswordClicked = {},
        )
    }
}