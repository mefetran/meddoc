package mefetran.dgusev.meddocs.ui.screen.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.TextFieldValue
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import mefetran.dgusev.meddocs.HiltTestActivity
import mefetran.dgusev.meddocs.ui.screen.signin.model.SignInState
import mefetran.dgusev.meddocs.ui.theme.MeddocsTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

const val EMAIL_VALUE = "test@test.ru"
const val PASSWORD_VALUE = "some_password123"

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SignInScreenTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @Test
    fun shouldShowLoadingScreenWhenClickedSignInButton() {
        // arrange
        var isLoading: Boolean = false
        composeRule.setContent {
            MeddocsTheme {
                var signInState by remember {
                    mutableStateOf(SignInState())
                }

                SignInScreen(
                    state = signInState,
                    emailValue = TextFieldValue(""),
                    passwordValue = TextFieldValue(""),
                    onSignIn = {
                        isLoading = true
                        signInState = signInState.copy(isLoading = true)
                    },
                    navigateToRegistrationScreen = {},
                    onNewEmailValue = {},
                    onNewPasswordValue = {},
                    onShowPasswordClicked = {},
                )
            }
        }

        // act
        composeRule
            .onNodeWithTag("loginButton")
            .assertIsDisplayed()
            .performClick()

        // assert
        composeRule
            .onNodeWithTag("loadingScreenTag")
            .assertExists()
            .assertIsDisplayed()
        assertTrue(isLoading)
    }

    @Test
    fun shouldProperlyEnterEmailAndPasswordValue() {
        // arrange
        var emailInputTestValue = TextFieldValue("")
        var passwordInputTestValue = TextFieldValue("")
        composeRule.setContent {
            MeddocsTheme {
                var emailInputState = remember { mutableStateOf(TextFieldValue("")) }
                var passwordInputState = remember { mutableStateOf(TextFieldValue("")) }

                SignInScreen(
                    state = SignInState(),
                    emailValue = emailInputState.value,
                    passwordValue = passwordInputState.value,
                    onSignIn = {},
                    navigateToRegistrationScreen = {},
                    onNewEmailValue = { newValue ->
                        emailInputState.value = newValue
                        emailInputTestValue = newValue
                    },
                    onNewPasswordValue = { newValue ->
                        passwordInputState.value = newValue
                        passwordInputTestValue = newValue
                    },
                    onShowPasswordClicked = {},
                )
            }
        }

        // act
        composeRule
            .onNodeWithTag("usernameInput")
            .performTextClearance()
        composeRule
            .onNodeWithTag("usernameInput")
            .performTextInput(EMAIL_VALUE)
        composeRule
            .onNodeWithTag("passwordInput")
            .performTextClearance()
        composeRule
            .onNodeWithTag("passwordInput")
            .performTextInput(PASSWORD_VALUE)

        // assert
        assertTrue(emailInputTestValue.text.isNotBlank())
        assertTrue(passwordInputTestValue.text.isNotBlank())
    }

}